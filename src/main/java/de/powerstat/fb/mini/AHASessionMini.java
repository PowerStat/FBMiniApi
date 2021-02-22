/*
 * Copyright (C) 2015-2021 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.ProviderNotFoundException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.powerstat.validation.ValidationUtils;
import de.powerstat.validation.values.Hostname;
import de.powerstat.validation.values.Milliseconds;
import de.powerstat.validation.values.Password;
import de.powerstat.validation.values.Port;
import de.powerstat.validation.values.Username;
import de.powerstat.validation.values.strategies.UsernameConfigurableStrategy;
import de.powerstat.validation.values.strategies.UsernameConfigurableStrategy.HandleEMail;


/**
 * FB AHA session.
 *
 * This class is not serializable because of session management!
 *
 * @author Kai Hofmann
 * @see <a href="https://avm.de/fileadmin/user_upload/Global/Service/Schnittstellen/AHA-HTTP-Interface.pdf">AHA-HTTP-Interface</a>
 *
 * TODO Template identifier value object tmp653A18-38AE7FDE
 * TODO group identifer 5:3A:18-900
 * TODO urlPath, urlParameters value objects
 * TODO Version number handling? (ask fritzbox for it's version)
 * TODO Rigths handling
 */
public class AHASessionMini implements Runnable, Comparable<AHASessionMini>
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(AHASessionMini.class);

  /**
   * Timeout: 10 minutes - 15 seconds.
   */
  private static final Milliseconds TIMEOUT = new Milliseconds((long)(10 * 60 * 1000) - (15 * 1000));

  /**
   * Url constant.
   */
  private static final String HOMEAUTOSWITCH = "/webservices/homeautoswitch.lua?ain="; //$NON-NLS-1$

  /**
   * FB invalid string constant.
   */
  private static final String INVAL = "inval"; //$NON-NLS-1$

  /**
   * Invalid string constant.
   */
  private static final String INVALID = "invalid"; //$NON-NLS-1$

  /**
   * SID.
   */
  private static final String SESSIONID = "SID"; //$NON-NLS-1$

  /**
   * AIN.
   */
  private static final String AIN_STR = "ain"; //$NON-NLS-1$

  /**
   * Hmac SHA256 algorithm.
   */
  private static final String SHA256 = "HmacSHA256"; //$NON-NLS-1$

  /**
   * Document builder.
   */
  private final DocumentBuilder docBuilder;

  /**
   * HTTP client.
   */
  private final CloseableHttpClient httpclient;

  /**
   * FB hostname.
   */
  private final Hostname hostname;

  /**
   * FB port.
   */
  private final Port port;

  /**
   * FB username.
   */
  private final Username username;

  /**
   * FB password.
   */
  private final Password password;

  /**
   * Session id.
   */
  private SID sid = SID.ofInvalid();

  /**
   * Last session access to fb timestamp.
   */
  private long lastAccess;

  /**
   * Timeout thread.
   */
  private Thread timeoutThread = new Thread();

  /**
   * Enum for handling of blinds.
   */
  public enum HandleBlind
   {
    /**
     * Close blind.
     */
    CLOSE(0),

    /**
     * Open blind.
     */
    OPEN(1),

    /**
     * Stop blind.
     */
    STOP(2);


    /**
     * Action number.
     */
    private final int action;


    /**
     * Ordinal constructor.
     *
     * @param action Action number
     */
    HandleBlind(final int action)
     {
      this.action = action;
     }


    /**
     * Get action number.
     *
     * @return Action number
     */
    public int getAction()
     {
      return this.action;
     }

   }


  /**
   * Constructor.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname FB hostname
   * @param port FB port
   * @param username FB Username
   * @param password FB Password
   * @throws NullPointerException If a parameter is null
   *
   * docBuilder must be secure:
   * final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   * factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
   * factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
   * final DocumentBuilder docBuilder = factory.newDocumentBuilder();
   */
  protected AHASessionMini(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final Hostname hostname, final Port port, final Username username, final Password password)
   {
    super();
    this.hostname = Objects.requireNonNull(hostname, "hostname"); //$NON-NLS-1$
    this.port = Objects.requireNonNull(port, "port"); //$NON-NLS-1$
    this.username = Objects.requireNonNull(username, "username"); //$NON-NLS-1$
    this.password = Objects.requireNonNull(password, "password"); //$NON-NLS-1$
    this.httpclient = Objects.requireNonNull(httpclient, "httpclient"); //$NON-NLS-1$
    this.docBuilder = Objects.requireNonNull(docBuilder, "docBuilder"); //$NON-NLS-1$
   }


  /**
   * Get new instance for a FB hostname with password.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname FB hostname
   * @param port FB port
   * @param username FB username
   * @param password FB password
   * @return A new AHASessionMini instance.
   * @throws NullPointerException If hostname or password is null
   *
   * docBuilder must be secure:
   * final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   * factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
   * factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
   * final DocumentBuilder docBuilder = factory.newDocumentBuilder();
   */
  public static AHASessionMini newInstance(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final Hostname hostname, final Port port, final Username username, final Password password)
   {
    return new AHASessionMini(httpclient, docBuilder, hostname, port, username, password);
   }


  /**
   * Get new instance for a FB hostname with password.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname FB hostname
   * @param port FB port
   * @param username FB username
   * @param password FB password
   * @return A new AHASessionMini instance.
   * @throws NullPointerException If a parameter is null
   *
   * docBuilder must be secure:
   * final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   * factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
   * factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
   * final DocumentBuilder docBuilder = factory.newDocumentBuilder();
   */
  public static AHASessionMini newInstance(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final String hostname, final int port, final String username, final String password)
   {
    return AHASessionMini.newInstance(httpclient, docBuilder, Hostname.of(hostname), Port.of(port), Username.of(UsernameConfigurableStrategy.of(0, 32, "^[@./_0-9a-zA-Z-]*$", HandleEMail.EMAIL_POSSIBLE), username), Password.of(password)); //$NON-NLS-1$
   }


  /**
   * Get new instance for a FB hostname with password.
   *
   * @param hostname FB hostname
   * @param port FB port
   * @param username FB username
   * @param password FB password
   * @return A new AHASessionMini instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static AHASessionMini newInstance(final String hostname, final int port, final String username, final String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build())).build();

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false);
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
    // factory.setFeature("http://xml.org/sax/features/external-general-entities", true); //$NON-NLS-1$
    // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true); //$NON-NLS-1$
    // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true); //$NON-NLS-1$
    // factory.setXIncludeAware(false);
    // factory.setExpandEntityReferences(false);
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();

    return newInstance(httpclient, docBuilder, hostname, port, username, password);
   }


  /**
   * Get new instance for a FB default hostname fritz.box, default port 443 with password.
   *
   * @param username FB username
   * @param password FB password
   * @return A new AHASessionMini instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static AHASessionMini newInstance(final String username, final String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance("fritz.box", 443, username, password); //$NON-NLS-1$
   }


  /**
   * Get new instance for a FB default hostname fritz.box with password.
   *
   * @param password FB password
   * @return A new AHASessionMini instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static AHASessionMini newInstance(final String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance("", password); //$NON-NLS-1$
   }


  /**
   * Prevent from timeout.
   *
   * TODO https://stackoverflow.com/questions/4909655/java-privately-calling-run-in-runnable-class
   */
  @Override
  public final void run()
   {
    while (true)
     {
      try
       {
        if (LOGGER.isDebugEnabled())
         {
          LOGGER.debug("Checking timeout"); //$NON-NLS-1$
         }
        if ((this.lastAccess + AHASessionMini.TIMEOUT.getMilliseconds()) >= System.currentTimeMillis())
         {
          if (LOGGER.isDebugEnabled())
           {
            LOGGER.debug("Preventing from timeout!"); //$NON-NLS-1$
           }
          /* final Document doc = */ getDeviceListInfos();
          // TODO check disconnect and login again
          // TODO update device infos
         }
        Thread.sleep((this.lastAccess + AHASessionMini.TIMEOUT.getMilliseconds()) - System.currentTimeMillis());
       }
      catch (final InterruptedException e)
       {
        LOGGER.debug("Timeout thread interupted", e); //$NON-NLS-1$
        Thread.currentThread().interrupt();
        return;
       }
      catch (final IOException | SAXException e)
       {
        LOGGER.debug("", e); //$NON-NLS-1$
       }
     }
   }


  /**
   * Get XML document.
   *
   * @param urlPath URL path
   * @return Document
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws SAXException SAX exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws UnsupportedOperationException When a bad request appears, could happen when using commands from a newer api version
   *
   * TODO urlPath value object
   */
  private Document getDoc(final String urlPath) throws IOException, SAXException
   {
    assert urlPath != null;
    try (CloseableHttpResponse response = this.httpclient.execute(new HttpGet("https://" + this.hostname.getHostname() + ":" + this.port.getPort() + ValidationUtils.sanitizeUrlPath(urlPath)))) //$NON-NLS-1$ //$NON-NLS-2$
     {
      final int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpURLConnection.HTTP_OK)
       {
        this.lastAccess = System.currentTimeMillis(); // TODO Will the session timeout be updated on bad requests?
        if (LOGGER.isDebugEnabled())
         {
          LOGGER.debug("StatusLine: " + response.getStatusLine()); //$NON-NLS-1$
         }
        if (LOGGER.isInfoEnabled())
         {
          LOGGER.info("HttpStatus: " + response.getStatusLine().getStatusCode() + ":");  //$NON-NLS-1$//$NON-NLS-2$
         }
        if (responseCode == HttpURLConnection.HTTP_FORBIDDEN)
         {
          try
           {
            if (!logon())
             {
              throw new IOException("Connection lost and no reconnection possible!"); //$NON-NLS-1$
             }
           }
          catch (final NoSuchAlgorithmException | SAXException | InvalidKeyException e)
           {
            throw new IOException("Can't logon for reconnect!", e); //$NON-NLS-1$
           }
          return getDoc(urlPath); // TODO fix endless loop
         }
        else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
         {
          throw new UnsupportedOperationException("Possibly you used a command from a newer api version?"); //$NON-NLS-1$
         }
       }
      final HttpEntity entity = response.getEntity();
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("ContentType: " + entity.getContentType()); //$NON-NLS-1$
       }
      final String string = EntityUtils.toString(entity);
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("string: " + string); //$NON-NLS-1$
       }
      final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("bytes: " +  new String(bytes, StandardCharsets.UTF_8)); //$NON-NLS-1$
       }
      final ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
      final InputSource stream = new InputSource(byteStream);
      final Document doc = this.docBuilder.parse(stream);
      EntityUtils.consume(entity);
      return doc;
     }
   }


  /**
   * Get string.
   *
   * @param urlPath URL path
   * @return String
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedOperationException When a bad request appears, could happen when using commands from a newer api version
   *
   * TODO urlPath value object
   */
  private String getString(final String urlPath) throws IOException
   {
    assert urlPath != null;
    try (CloseableHttpResponse response = this.httpclient.execute(new HttpGet("https://" + this.hostname.getHostname() + ":" + this.port.getPort() + ValidationUtils.sanitizeUrlPath(urlPath) + "&sid=" + this.sid.getSID())))//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     {
      final int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpURLConnection.HTTP_OK)
       {
        this.lastAccess = System.currentTimeMillis(); // TODO Will the session timeout be updated on bad requests?
        if (LOGGER.isDebugEnabled())
         {
          LOGGER.debug(response.getStatusLine());
         }
        if (LOGGER.isInfoEnabled())
         {
          LOGGER.info("HttpStatus: " + response.getStatusLine().getStatusCode() + ":");  //$NON-NLS-1$//$NON-NLS-2$
         }
        if (responseCode == HttpURLConnection.HTTP_FORBIDDEN)
         {
          try
           {
            if (!logon())
             {
              throw new IOException("Connection lost and no reconnection possible!"); //$NON-NLS-1$
             }
           }
          catch (final NoSuchAlgorithmException | SAXException | InvalidKeyException e)
           {
            throw new IOException("Can't logon for reconnect!", e); //$NON-NLS-1$
           }
          return getString(urlPath); // TODO fix endless loop
         }
        else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
         {
          throw new UnsupportedOperationException("Possibly you used a command from a newer api version?"); //$NON-NLS-1$
         }
       }
      final HttpEntity entity = response.getEntity();
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("ContentType: " + entity.getContentType()); //$NON-NLS-1$
       }
      final String result = EntityUtils.toString(entity);
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("getString: " + result); //$NON-NLS-1$
       }
      return result;
     }
   }


  /**
   * Convert hex string to byte array.
   *
   * @param hexStr Hex string ([0-9a-f]{2,4,6,8,10})
   * @return Byte array
   */
  private static byte[] fromHex(final String hexStr)
   {
    Objects.requireNonNull(hexStr);
    if ((hexStr.length() % 2) != 0)
     {
      throw new IllegalArgumentException("Length must be even");
     }
    final int len = hexStr.length() / 2;
    final byte[] ret = new byte[len];
    for (int i = 0; i < len; ++i)
     {
      final int pos = i * 2;
      ret[i] = (byte)Integer.parseInt(hexStr.substring(pos, pos + 2), 16);
     }
    return ret;
   }


  /**
   * Convert byte array to hex string.
   *
   * @param bytes Byte array
   * @return String Hex string
   */
  private static String toHex(final byte[] bytes)
   {
    final StringBuilder hexStr = new StringBuilder(bytes.length * 2);
    for (final byte byt : bytes)
     {
      hexStr.append(String.format("%02x", byt)); //$NON-NLS-1$
     }
    return hexStr.toString();
   }


  /**
   * Create a pbkdf2 HMAC by appling the Hmac iter times as specified.
   *
   * @param password Password
   * @param salt Salt
   * @param iters Iterator
   * @return Byte array
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws InvalidKeyException Invalid key exception
   */
  private static byte[] pbkdf2HmacSha256(final byte[] password, final byte[] salt, final int iters) throws NoSuchAlgorithmException, InvalidKeyException
   {
    Objects.requireNonNull(password);
    Objects.requireNonNull(salt);
    if (iters < 0)
     {
      throw new IllegalArgumentException("iters < 0"); //$NON-NLS-1$
     }
    final Mac sha256mac = Mac.getInstance(SHA256);
    sha256mac.init(new SecretKeySpec(password, SHA256));
    final byte[] ret = new byte[sha256mac.getMacLength()];
    byte[] tmp = new byte[salt.length + 4];
    System.arraycopy(salt, 0, tmp, 0, salt.length);
    tmp[salt.length + 3] = 1;
    for (int i = 0; i < iters; ++i)
     {
      tmp = sha256mac.doFinal(tmp);
      for (int j = 0; j < ret.length; ++j)
       {
        ret[j] ^= tmp[j];
       }
     }
    return ret;
   }






  /**
   * Logon to FB.
   *
   * @return true on success; otherwise false
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws SAXException SAX exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws InvalidKeyException Invalid key exception
   */
  public final boolean logon() throws IOException, SAXException, NoSuchAlgorithmException, InvalidKeyException
   {
    /*
    synchronized(AHASessionMini.class)
     {
      if (this.timeoutThread.isAlive())
       {
        return true;
       }
    */
    // get first challenge
    Document doc = getDoc("/login_sid.lua?version=2"); //$NON-NLS-1$
    this.sid = SID.of(doc.getElementsByTagName(SESSIONID).item(0).getTextContent());
    if (LOGGER.isDebugEnabled())
     {
      LOGGER.debug("sid: " + this.sid.getSID()); //$NON-NLS-1$
     }
    if (LOGGER.isDebugEnabled())
     {
      final String blocktime = doc.getElementsByTagName("BlockTime").item(0).getTextContent(); //$NON-NLS-1$
      LOGGER.debug("blocktime: " + blocktime); //$NON-NLS-1$
     }
    // Users?
    // Rigths ?

    // login
    if (!this.sid.isValidSession())
     {
      String challenge = doc.getElementsByTagName("Challenge").item(0).getTextContent(); //$NON-NLS-1$
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("challenge: " + challenge); //$NON-NLS-1$
       }
      String response;
      if (challenge.startsWith("2$")) // PBKDF2 //$NON-NLS-1$
       {
        // challenge = 2$<iter1>$<salt1>$<iter2>$<salt2>
        final String[] challengeParts = challenge.split("\\$"); //$NON-NLS-1$
        final int iter1 = Integer.parseInt(challengeParts[1]);
        final byte[] salt1 = fromHex(challengeParts[2]);
        final int iter2 = Integer.parseInt(challengeParts[3]);
        final byte[] salt2 = fromHex(challengeParts[4]);
        // <hash1> = pbdkf2_hmac_sha256(<password>, <salt1>, <iter1>)
        final byte[] hash1 = pbkdf2HmacSha256(this.password.getPassword().getBytes(StandardCharsets.UTF_8), salt1, iter1);
        // <response> = <salt2>$ + pbdkf2_hmac_sha256(<hash1>, <salt2>, <iter2>)
        final byte[] hash2 = pbkdf2HmacSha256(hash1, salt2, iter2);
        response = challengeParts[4] + '$' + toHex(hash2);
       }
      else // MD5
       {
        // TODO
        // Aus Kompatibilitätsgründen muss für jedes Zeichen, dessen Unicode Codepoint > 255 ist, die Codierung des "."-Zeichens benutzt werden (0x2e 0x00 in UTF-16LE).
        // Dies betrifft also alle Zeichen, die nicht in ISO-8859-1 dargestellt werden können, z.B. das Euro-Zeichen
        response = challenge + '-' + new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest((challenge + '-' + this.password.getPassword()).getBytes(Charset.forName("utf-16le"))))); //$NON-NLS-1$ //$NON-NLS-2$
       }
      doc = getDoc("/login_sid.lua?version=2&username=" + this.username.getUsername() + "&response=" + response); //$NON-NLS-1$ //$NON-NLS-2$
      this.sid = SID.of(doc.getElementsByTagName(SESSIONID).item(0).getTextContent());
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("sid: " + this.sid.getSID()); //$NON-NLS-1$
       }
      if (LOGGER.isDebugEnabled())
       {
        challenge = doc.getElementsByTagName("Challenge").item(0).getTextContent(); //$NON-NLS-1$
        LOGGER.debug("challenge: " + challenge); //$NON-NLS-1$
       }
      if (LOGGER.isDebugEnabled())
       {
        final String blocktime = doc.getElementsByTagName("BlockTime").item(0).getTextContent(); //$NON-NLS-1$
        LOGGER.debug("blocktime: " + blocktime); //$NON-NLS-1$
       }
      // Rigths ?
     }

    // check sid validity
    doc = getDoc("/login_sid.lua?version=2&sid=" + this.sid.getSID()); //$NON-NLS-1$
    this.sid = SID.of(doc.getElementsByTagName(SESSIONID).item(0).getTextContent());
    if (!this.sid.isValidSession())
     {
      LOGGER.error("login invalid"); //$NON-NLS-1$
      return false;
     }
    this.timeoutThread = new Thread();
    this.timeoutThread.start();
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("login valid"); //$NON-NLS-1$
     }
    return true;
    // }
   }


  /**
   * Logoff.
   *
   * @return True on success otherwise false
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParseException Parse exception
   * @throws SAXException SAX exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   */
  public final boolean logoff() throws IOException, SAXException
   {
    /*
    synchronized(AHASessionMini.class)
     {
      if (!this.timeoutThread.isAlive())
       {
        return true;
       }
    */
    final Document doc = getDoc("/login_sid.lua?version=2&logout=1&sid=" + this.sid.getSID()); //$NON-NLS-1$
    this.sid = SID.of(doc.getElementsByTagName(SESSIONID).item(0).getTextContent());
    if (!this.sid.isValidSession())
     {
      // TODO check for existing thread
      this.timeoutThread.interrupt();
      if (LOGGER.isInfoEnabled())
       {
        LOGGER.info("successfully logged out."); //$NON-NLS-1$
       }
      return true;
     }
    LOGGER.warn("logout error"); //$NON-NLS-1$
    return false;
    // }
   }


  /**
   * Get switch list.
   *
   * @return AIN/MAC list
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   */
  public final List<AIN> getSwitchList() throws IOException
   {
    final String list = getString("/webservices/homeautoswitch.lua?switchcmd=getswitchlist"); //$NON-NLS-1$
    if (list.length() < 13)
     {
      return Collections.emptyList();
     }
    final String[] ainStrList = list.substring(0, list.length() - 1).split(","); //$NON-NLS-1$
    final List<AIN> ains = new ArrayList<>(ainStrList.length);
    for (final String ain : ainStrList)
     {
      ains.add(AIN.of(ain));
     }
    return Collections.unmodifiableList(ains);
   }


  /**
   * Set switch on.
   *
   * @param ain AIN
   * @return true when on; otherwise false
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   */
  public final boolean setSwitchOn(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String result = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setswitchon"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("setSwitchOn()->" + result + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    return "1".equals((result.length() == 0) ? "" : result.substring(0, 1)); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Set switch off.
   *
   * @param ain AIN
   * @return true when off; otherwise false
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   */
  public final boolean setSwitchOff(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String result = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setswitchoff"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("setSwitchOff()->" + result + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    return "0".equals((result.length() == 0) ? "" : result.substring(0, 1)); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Set switch toggle.
   *
   * @param ain AIN
   * @return true when on; false when off
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   */
  public final boolean setSwitchToggle(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String state = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setswitchtoggle"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("setSwitchToggle()->" + state + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    return "1".equals((state.length() == 0) ? "" : state.substring(0, 1)); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get switch state.
   *
   * @param ain AIN
   * @return true when on; false when off
   * @throws IOException             IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException    If ain is null
   */
  public final boolean getSwitchState(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String state = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=getswitchstate"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("getSwitchState()->" + state + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    if ((state.length() >= 5) && INVAL.equals(state.substring(0, 5)))
     {
      throw new ProviderNotFoundException(INVALID);
     }
    return "1".equals((state.length() == 0) ? "" : state.substring(0, 1)); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Get switch present.
   *
   * @param ain AIN
   * @return true when present, false otherwise
   * @throws IOException             IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException    If ain is null
   */
  public final boolean isSwitchPresent(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String present = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=getswitchpresent"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("getSwitchPresent()->" + present + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    return "1".equals((present.length() == 0) ? "" : present.substring(0, 1)); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * get switch power.
   *
   * @param ain AIN
   * @return Power in mW
   * @throws ProviderNotFoundException when the AIN is in an invalid state (i.e. is not available)
   * @throws IOException IO exeption
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   */
  public final Power getSwitchPower(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    String power = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=getswitchpower"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("getSwitchPower()->" + power + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    if (power.length() > 1)
     {
      power = power.substring(0, power.length() - 1);
     }
    if (INVAL.equals(power))
     {
      throw new ProviderNotFoundException(INVALID);
     }
    return Power.of(power);
   }


  /**
   * Get switch energy.
   *
   * @param ain AIN
   * @return Energy in Wh
   * @throws ProviderNotFoundException When the AIN is in an invalid state (i.e. is not available)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   */
  public final Energy getSwitchEnergy(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    String energy = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=getswitchenergy"); //$NON-NLS-1$
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("getSwitchEnergy()->" + energy + "<"); //$NON-NLS-1$ //$NON-NLS-2$
     }
    if (energy.length() > 1)
     {
      energy = energy.substring(0, energy.length() - 1);
     }
    if (INVAL.equals(energy))
     {
      throw new ProviderNotFoundException(INVALID);
     }
    return Energy.of(energy);
   }


  /**
   * Get switch name.
   *
   * @param ain AIN
   * @return Switch name
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   */
  public final String getSwitchName(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String name = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=getswitchname"); //$NON-NLS-1$
    return (name.length() > 0) ? name.substring(0, name.length() - 1) : ""; //$NON-NLS-1$
   }


  /**
   * Get device infos.
   *
   * @return Device infos in XML format
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   */
  public final Document getDeviceListInfos() throws IOException, SAXException
   {
    return getDoc("/webservices/homeautoswitch.lua?switchcmd=getdevicelistinfos&sid=" + this.sid.getSID()); //$NON-NLS-1$
   }


  /**
   * Get temperature.
   *
   * @param ain AIN
   * @return Temperature in deci celsius
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IndexOutOfBoundsException If temperature is out of range or undefined
   */
  public final Temperature getTemperature(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String temperature = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=gettemperature"); //$NON-NLS-1$
    return Temperature.of((temperature.length() > 0) ? temperature.substring(0, temperature.length() - 1) : ""); //$NON-NLS-1$
   }


  /**
   * Temperature conversion FB to deci celsius.
   *
   * @param fbTemperatureString Temperature string
   * @return Temperature 0: off; 300: on; 80-280 deci celsius; step by 5
   * @throws IndexOutOfBoundsException If temperature is out of range or undefined
   * @throws NumberFormatException If fbTemerature is of wrong format
   */
  private Temperature temperatureConversion(final String fbTemperatureString)
   {
    assert fbTemperatureString != null;
    final int fbTemperature = Integer.parseInt((fbTemperatureString.length() > 0) ? fbTemperatureString.substring(0, fbTemperatureString.length() - 1) : "");
    long temperature;
    if (fbTemperature == 253)
     {
      temperature = 0;
     }
    else if (fbTemperature == 254)
     {
      temperature = 300;
     }
    else
     {
      temperature = (fbTemperature * 10) / 2;
     }
    return Temperature.of(temperature);
   }


  /**
   * Get hkr temperature soll.
   *
   * @param ain AIN
   * @return Temperature 0: off; 300: on; 80-280 deci celsius; step by 5
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IndexOutOfBoundsException If temperature is out of range or undefined
   */
  public final Temperature getHkrtSoll(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String resultStr = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=gethkrtsoll"); //$NON-NLS-1$
    return temperatureConversion(resultStr);
   }


  /**
   * Get hkr temperature komfort.
   *
   * @param ain AIN
   * @return Temperature 0: off; 300: on; 80-280 deci celsius; step by 5
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IndexOutOfBoundsException If temperature is out of range or undefined
   */
  public final Temperature getHkrKomfort(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String resultStr = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=gethkrkomfort"); //$NON-NLS-1$
    return temperatureConversion(resultStr);
   }


  /**
   * Get hkr temperature absenk.
   *
   * @param ain AIN
   * @return Temperature 0: off; 300: on; 80-280 deci celsius; step by 5
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IndexOutOfBoundsException If temperature is out of range or undefined
   */
  public final Temperature getHkrAbsenk(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    final String resultStr = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=gethkrabsenk"); //$NON-NLS-1$
    return temperatureConversion(resultStr);
   }


  /**
   * Set hkr temperature soll.
   *
   * @param ain AIN
   * @param temperature Soll temperature 0: off; 300: on; 80-280 deci celsius; step by 5
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws IndexOutOfBoundsException If temperature is out of range or undefined
   * @throws NullPointerException If ain or temperature is null
   * @since 6.35
   */
  public final void setHkrtSoll(final AIN ain, final Temperature temperature) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    Objects.requireNonNull(temperature, "temperature"); //$NON-NLS-1$
    long fbTemperature;
    if (temperature.getTemperatureDeciCelsius() == 0)
     {
      fbTemperature = 253;
     }
    else if (temperature.getTemperatureDeciCelsius() == 300)
     {
      fbTemperature = 254;
     }
    else if ((temperature.getTemperatureDeciCelsius() < 80) || (temperature.getTemperatureDeciCelsius() > 280))
     {
      throw new IndexOutOfBoundsException("Illegal temperature value!"); //$NON-NLS-1$
     }
    else
     {
      fbTemperature = (temperature.getTemperatureDeciCelsius() * 2) / 10;
     }
    /* String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=sethkrtsoll&param=" + fbTemperature); //$NON-NLS-1$
   }


  /**
   * Get basic device statistics.
   *
   * @param ain AIN
   * @return XML document
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 6.98
   */
  public final Document getBasicDeviceStats(final AIN ain) throws IOException, SAXException
   {
    Objects.requireNonNull(ain, AIN_STR);
    return getDoc(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=getbasicdevicestats&sid=" + this.sid.getSID()); //$NON-NLS-1$
   }


  /**
   * Get template list infos.
   *
   * @return XML document
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 6.98
   */
  public final Document getTemplateListInfos() throws IOException, SAXException
   {
    return getDoc("/webservices/homeautoswitch.lua?switchcmd=gettemplatelistinfos&sid=" + this.sid.getSID()); //$NON-NLS-1$
   }


  /**
   * Apply template.
   *
   * @param templateIdentifier Template identifier
   * @throws IOException IO exception
   * @since 6.98
   *
   * TODO Template identifier value object
   */
  public final void applyTemplate(final String templateIdentifier) throws IOException
   {
    /* final String id = */ getString(HOMEAUTOSWITCH + templateIdentifier + "&switchcmd=applytemplate"); //$NON-NLS-1$
   }


  /**
   * Set simple on off.
   *
   * @param ain AIN
   * @param onoff 0: off; 1: on; 2: toggle
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if onoff is not 0, 1 or 2
   * @since 7.15
   */
  public final void setSimpleOnOff(final AIN ain, final int onoff) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((onoff < 0) || (onoff > 2))
     {
      throw new IllegalArgumentException("onoff must be 0, 1 or 2"); //$NON-NLS-1$
     }
    /* final String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setsimpleonoff&onoff=" + onoff); //$NON-NLS-1$
   }


  /**
   * Set level.
   *
   * @param ain AIN
   * @param level 0-255 (0%-100%)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setLevel(final AIN ain, final int level) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((level < 0) || (level > 255))
     {
      throw new IllegalArgumentException("level must be 0-255"); //$NON-NLS-1$
     }
    /* final String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setlevel&level=" + level); //$NON-NLS-1$
   }


  /**
   * Set level percentage.
   *
   * @param ain AIN
   * @param level 0-100
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setLevelPercentage(final AIN ain, final int level) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((level < 0) || (level > 100))
     {
      throw new IllegalArgumentException("level must be 0-100"); //$NON-NLS-1$
     }
    /* final String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setlevelpercentage&level=" + level); //$NON-NLS-1$
   }


  /**
   * Set color.
   *
   * @param ain AIN
   * @param hue 0-359 degrees
   * @param saturation 0-255 (0% - 100%)
   * @param duration Duration of change in 100ms, 0 means immediately
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setColor(final AIN ain, final int hue, final int saturation, final int duration) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((hue < 0) || (hue > 359))
     {
      throw new IllegalArgumentException("hue must be 0-359 degrees"); //$NON-NLS-1$
     }
    if ((saturation < 0) || (saturation > 255))
     {
      throw new IllegalArgumentException("saturation must be 0-255"); //$NON-NLS-1$
     }
    if (duration < 0)
     {
      throw new IllegalArgumentException("duration must be >= 0"); //$NON-NLS-1$
     }
    /* final String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setcolor&hue=" + hue + "&saturation=" + saturation + "&duration=" + duration); //$NON-NLS-1$
   }


  /**
   * Set color temperature.
   *
   * @param ain AIN
   * @param temperature In kelvin 2700 - 6500
   * @param duration Duration of change in 100ms, 0 means immediately
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setColorTemperature(final AIN ain, final int temperature, final int duration) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((temperature < 2700) || (temperature > 6500))
     {
      throw new IllegalArgumentException("temperature must be 2700-6500 kelvin"); //$NON-NLS-1$
     }
    if (duration < 0)
     {
      throw new IllegalArgumentException("duration must be >= 0"); //$NON-NLS-1$
     }
    /* final String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setcolortemperature&temperature=" + temperature + "&duration=" + duration); //$NON-NLS-1$
   }


  /**
   * Get color defaults.
   *
   * @return Color defaults in XML format
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 7.15
   */
  public final Document getColorDefaults() throws IOException, SAXException
   {
    return getDoc("/webservices/homeautoswitch.lua?switchcmd=getcolordefaults&sid=" + this.sid.getSID()); //$NON-NLS-1$
   }


  /**
   * Set hkr boost.
   *
   * @param ain AIN
   * @param endtimestamp 0: deactivate; seconds since 1970-01-01T00:00:00; maximum of 24 hour in the future
   * @return Endtimestamp if successful
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws IllegalArgumentException If endtimestamp is out of range
   * @throws NullPointerException If ain or temperature is null
   * @since 7.15
   */
  public final long setHkrBoost(final AIN ain, final long endtimestamp) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((endtimestamp != 0) && ((endtimestamp < (System.currentTimeMillis() / 1000L)) || (endtimestamp > ((System.currentTimeMillis() / 1000L) + 86400))))
     {
      throw new IllegalArgumentException("endtimestamp must be 0 or between now and in 24 hours"); //$NON-NLS-1$
     }
    final String result = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=sethkrboost&endtimestamp=" + endtimestamp); //$NON-NLS-1$
    return Long.parseLong(result.substring(0, result.length() - 1));
   }


  /**
   * Set hkr window open.
   *
   * @param ain AIN
   * @param endtimestamp 0: deactivate; seconds since 1970-01-01T00:00:00; maximum of 24 hour in the future
   * @return Endtimestamp if successful
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws IllegalArgumentException If endtimestamp is out of range
   * @throws NullPointerException If ain or temperature is null
   * @since 7.15
   */
  public final long setHkrWindowOpen(final AIN ain, final long endtimestamp) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    if ((endtimestamp != 0) && ((endtimestamp < (System.currentTimeMillis() / 1000L)) || (endtimestamp > ((System.currentTimeMillis() / 1000L) + 86400))))
     {
      throw new IllegalArgumentException("endtimestamp must be 0 or between now and in 24 hours"); //$NON-NLS-1$
     }
    final String result = getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=sethkrwindowopen&endtimestamp=" + endtimestamp); //$NON-NLS-1$
    return Long.parseLong(result.substring(0, result.length() - 1));
   }


  /**
   * Set blind.
   *
   * @param ain AIN
   * @param target HandleBlind CLOSE, OPEN, STOP
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws IllegalArgumentException If endtimestamp is out of range
   * @throws NullPointerException If ain or temperature is null
   * @since 7.15
   */
  public final void setBlind(final AIN ain, final HandleBlind target) throws IOException
   {
    Objects.requireNonNull(ain, AIN_STR);
    /* final String result = */ getString(HOMEAUTOSWITCH + ain.getAIN() + "&switchcmd=setblind&target=" + target.name().toLowerCase(Locale.getDefault())); //$NON-NLS-1$
   }


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(this.hostname, this.username, this.password, this.sid.getSID());
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof AHASessionMini))
     {
      return false;
     }
    final AHASessionMini other = (AHASessionMini)obj;
    return (this.hostname.equals(other.hostname)) && (this.username.equals(other.username)) && (this.password.equals(other.password)) && (this.sid.equals(other.sid));
   }


  /**
   * Returns the string representation of this AHASessionMini.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "AHASessionMini[hostname=fritz.box, username=, sid=000000000000, ...]"
   *
   * @return String representation of this AHASessionMini.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("AHASessionMini[hostname=").append(this.hostname.getHostname()).append(", username=").append(this.username.getUsername()).append(", sid=").append(this.sid.getSID()).append(']').toString(); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final AHASessionMini obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = this.hostname.compareTo(obj.hostname);
    if (result == 0)
     {
      result = this.username.compareTo(obj.username);
      if (result == 0)
       {
        result = this.sid.compareTo(obj.sid);
       }
     }
    return result;
   }

 }
