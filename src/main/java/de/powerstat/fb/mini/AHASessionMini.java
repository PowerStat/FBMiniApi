/*
 * Copyright (C) 2015-2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

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
import org.javatuples.Pair;
import org.javatuples.Quintet;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.gson.Gson;

import de.powerstat.fb.mini.json.FBMetadata;
import de.powerstat.validation.values.Hostname;
import de.powerstat.validation.values.Milliseconds;
import de.powerstat.validation.values.Password;
import de.powerstat.validation.values.Percent;
import de.powerstat.validation.values.Port;
import de.powerstat.validation.values.Seconds;
import de.powerstat.validation.values.Username;
import de.powerstat.validation.values.strategies.UsernameConfigurableStrategy;
import de.powerstat.validation.values.strategies.UsernameConfigurableStrategy.HandleEMail;


/**
 * FB AHA session 1.35.
 *
 * This class is not serializable because of session management!
 *
 * @author Kai Hofmann
 * @see <a href="https://avm.de/fileadmin/user_upload/Global/Service/Schnittstellen/AHA-HTTP-Interface.pdf">AHA-HTTP-Interface</a>
 *
 * TODO group identifer 5:3A:18-900
 * TODO Version number handling? (ask fritzbox for it's version)
 * TODO Rigths handling
 * TODO 1.35 -> 1.61
 * TODO XML results -> value objects
 */
@SuppressWarnings("java:S1160")
public class AHASessionMini implements Runnable, Comparable<AHASessionMini>
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(AHASessionMini.class);

  /**
   * Timeout: 10 minutes - 15 seconds.
   */
  private static final Milliseconds TIMEOUT = Milliseconds.of((long)(10 * 60 * 1000) - (15 * 1000));

  /**
   * Url constant.
   */
  private static final String HOMEAUTOSWITCH = "/webservices/homeautoswitch.lua?"; //$NON-NLS-1$

  /**
   * Url constant.
   */
  private static final String HOMEAUTOSWITCH_WITH_AIN = "/webservices/homeautoswitch.lua?ain="; //$NON-NLS-1$

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


    /**
     * Returns the string representation of this enum.
     *
     * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
     *
     * "close|open|stop"
     *
     * @return String representation of this enum (close, open, stop).
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
     {
      return switch (this.action)
       {
       case 0 -> "close"; //$NON-NLS-1$
       case 1 -> "open"; //$NON-NLS-1$
       case 2 -> "stop"; //$NON-NLS-1$
       default -> "close"; //$NON-NLS-1$
       };
     }

   }


  /**
   * Enum for handling of onoff.
   */
  public enum HandleOnOff
   {
    /**
     * Turn off.
     */
    OFF(0),

    /**
     * Turn non.
     */
    ON(1),

    /**
     * Toggle.
     */
    TOGGLE(2);


    /**
     * Action number.
     */
    private final int action;


    /**
     * Ordinal constructor.
     *
     * @param action Action number
     */
    HandleOnOff(final int action)
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


    /**
     * Returns the string representation of this enum.
     *
     * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
     *
     * "off|on|toggle"
     *
     * @return String representation of this enum (off, on, toggle).
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
     {
      return switch (this.action)
       {
       case 0 -> "off"; //$NON-NLS-1$
       case 1 -> "on"; //$NON-NLS-1$
       case 2 -> "toggle"; //$NON-NLS-1$
       default -> "off"; //$NON-NLS-1$
       };
     }

   }


/*
functionbitmask: Bitmaske der Geräte-Funktionsklassen, beginnen mit Bit 0, es können mehrere Bits gesetzt sein
Bit 0: HAN-FUN Gerät
Bit 2: Licht/Lampe
Bit 4: Alarm-Sensor
Bit 5: AVM Button
Bit 6: AVM Heizkörperregler
Bit 7: AVM Energie Messgerät
Bit 8: Temperatursensor
Bit 9: AVM Schaltsteckdose
Bit 10: AVM DECT Repeater
Bit 11: AVM Mikrofon
Bit 13: HAN-FUN-Unit
Bit 15: an-/ausschaltbares Gerät/Steckdose/Lampe/Aktor
Bit 16: Gerät mit einstellbarem Dimm-, Höhen- bzw. Niveau-Level
Bit 17: Lampe mit einstellbarer Farbe/Farbtemperatur
Bit 18: Rollladen(Blind) - hoch, runter, stop und level 0% bis 100 %
Bit 20: Luftfeuchtigkeitssensor
Die Bits 5,6,7,9,10 und 11 werden nur von FRITZ!-Geräten verwendet und nicht von HANFUN- oder Zigbee-Geräten.

HAN-FUN Unit Typ
273 = SIMPLE_BUTTON
256 = SIMPLE_ON_OFF_SWITCHABLE
257 = SIMPLE_ON_OFF_SWITCH
262 = AC_OUTLET
263 = AC_OUTLET_SIMPLE_POWER_METERING
264 = SIMPLE_LIGHT
265 = DIMMABLE_LIGHT
266 = DIMMER_SWITCH
277 = COLOR_BULB
278 = DIMMABLE_COLOR_BULB
281 = BLIND
282 = LAMELLAR
512 = SIMPLE_DETECTOR
513 = DOOR_OPEN_CLOSE_DETECTOR
514 = WINDOW_OPEN_CLOSE_DETECTOR
515 = MOTION_DETECTOR
518 = FLOOD_DETECTOR
519 = GLAS_BREAK_DETECTOR
520 = VIBRATION_DETECTOR
640 = SIREN

HAN-FUN Interfaces
277 = KEEP_ALIVE
256 = ALERT
512 = ON_OFF
513 = LEVEL_CTRL
514 = COLOR_CTRL
516 = OPEN_CLOSE
517 = OPEN_CLOSE_CONFIG
772 = SIMPLE_BUTTON
1024 = SUOTA-Update
*/

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
   * @param hostName FB hostname
   * @param portNr FB port
   * @param userName FB username
   * @param passWord FB password
   * @return A new AHASessionMini instance.
   * @throws NullPointerException If a parameter is null
   *
   * docBuilder must be secure:
   * final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
   * factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
   * factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
   * final DocumentBuilder docBuilder = factory.newDocumentBuilder();
   */
  public static AHASessionMini newInstance(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final String hostName, final int portNr, final String userName, final String passWord)
   {
    return AHASessionMini.newInstance(httpclient, docBuilder, Hostname.of(hostName), Port.of(portNr), Username.of(UsernameConfigurableStrategy.of(0, 32, "^[@./_0-9a-zA-Z-]*$", HandleEMail.EMAIL_POSSIBLE), userName), Password.of(passWord)); //$NON-NLS-1$
   }


  /**
   * Get new instance for a FB hostname with password.
   *
   * @param hostName FB hostname
   * @param portNr FB port
   * @param userName FB username
   * @param passWord FB password
   * @return A new AHASessionMini instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  @SuppressWarnings("PMD.CloseResource")
  public static AHASessionMini newInstance(final String hostName, final int portNr, final String userName, final String passWord) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build())).build();

    final var factory = DocumentBuilderFactory.newInstance();
    try
     {
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
      final var docBuilder = factory.newDocumentBuilder();
      return newInstance(httpclient, docBuilder, hostName, portNr, userName, passWord);
     }
    catch (final ParserConfigurationException e)
     {
      try
       {
        httpclient.close();
       }
      catch (final IOException e1)
       {
        // ignore
       }
      throw e;
     }
   }


  /**
   * Get new instance for a FB default hostname fritz.box, default port 443 with password.
   *
   * @param userName FB username
   * @param passWord FB password
   * @return A new AHASessionMini instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static AHASessionMini newInstance(final String userName, final String passWord) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance("fritz.box", 443, userName, passWord); //$NON-NLS-1$
   }


  /**
   * Get new instance for a FB default hostname fritz.box with password.
   *
   * @param passWord FB password
   * @return A new AHASessionMini instance.
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws NullPointerException If hostname or password is null
   */
  public static AHASessionMini newInstance(final String passWord) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance("", passWord); //$NON-NLS-1$
   }


  /**
   * Prevent from timeout.
   *
   * TODO https://stackoverflow.com/questions/4909655/java-privately-calling-run-in-runnable-class
   */
  @SuppressWarnings("java:S2189")
  @Override
  public final void run()
   {
    while (true)
     {
      try
       {
        if (AHASessionMini.LOGGER.isDebugEnabled())
         {
          AHASessionMini.LOGGER.debug("Checking timeout"); //$NON-NLS-1$
         }
        if ((this.lastAccess + AHASessionMini.TIMEOUT.longValue()) >= System.currentTimeMillis())
         {
          if (AHASessionMini.LOGGER.isDebugEnabled())
           {
            AHASessionMini.LOGGER.debug("Preventing from timeout!"); //$NON-NLS-1$
           }
          /* final Document doc = */ getDeviceListInfos();
          // TODO check disconnect and login again
          // TODO update device infos
         }
        Thread.sleep((this.lastAccess + AHASessionMini.TIMEOUT.longValue()) - System.currentTimeMillis());
       }
      catch (final InterruptedException e)
       {
        AHASessionMini.LOGGER.debug("Timeout thread interupted", e); //$NON-NLS-1$
        Thread.currentThread().interrupt();
        return;
       }
      catch (final IOException | SAXException e)
       {
        AHASessionMini.LOGGER.debug("", e); //$NON-NLS-1$
       }
     }
   }


  /**
   * Get XML document.
   *
   * @param urlPath URL path
   * @param urlQuery URL query
   * @return Document
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws SAXException SAX exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws UnsupportedOperationException When a bad request appears, could happen when using commands from a newer api version
   */
  private Document getDoc(final URIPath urlPath, final URIQuery<URIQueryParameter> urlQuery) throws IOException, SAXException
   {
    assert (urlPath != null) && (urlQuery != null);
    if (AHASessionMini.LOGGER.isDebugEnabled())
     {
      AHASessionMini.LOGGER.debug("url: https://{}:{}{}{}", this.hostname.stringValue(), this.port.intValue(), urlPath.stringValue(), urlQuery.stringValue()); //$NON-NLS-1$
     }
    try (CloseableHttpResponse response = this.httpclient.execute(new HttpGet("https://" + this.hostname.stringValue() + ":" + this.port.intValue() + urlPath.stringValue() + urlQuery.stringValue()))) //$NON-NLS-1$ //$NON-NLS-2$
     {
      final int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpURLConnection.HTTP_OK)
       {
        this.lastAccess = System.currentTimeMillis(); // TODO Will the session timeout be updated on bad requests?
        if (AHASessionMini.LOGGER.isDebugEnabled())
         {
          AHASessionMini.LOGGER.debug("StatusLine: {}", response.getStatusLine()); //$NON-NLS-1$
         }
        if (AHASessionMini.LOGGER.isInfoEnabled())
         {
          AHASessionMini.LOGGER.info("HttpStatus: {}", response.getStatusLine().getStatusCode() + ":");  //$NON-NLS-1$//$NON-NLS-2$
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
          return getDoc(urlPath, urlQuery); // TODO fix endless loop
         }
        if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
         {
          throw new UnsupportedOperationException("Possibly you used a command from a newer api version?"); //$NON-NLS-1$
         }
       }
      final HttpEntity entity = response.getEntity();
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("ContentType: {}", entity.getContentType()); //$NON-NLS-1$
       }
      final var string = EntityUtils.toString(entity);
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("string: {}", string); //$NON-NLS-1$
       }
      final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("bytes: {}", new String(bytes, StandardCharsets.UTF_8)); //$NON-NLS-1$
       }
      final var byteStream = new ByteArrayInputStream(bytes);
      final var stream = new InputSource(byteStream);
      final Document doc = this.docBuilder.parse(stream);
      EntityUtils.consume(entity);
      return doc;
     }
   }


  /**
   * Get string.
   *
   * @param urlPath URL path
   * @param urlQuery URL query
   * @return String
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedOperationException When a bad request appears, could happen when using commands from a newer api version
   */
  private String getString(final URIPath urlPath, final URIQuery<URIQueryParameter> urlQuery) throws IOException
   {
    assert (urlPath != null) && (urlQuery != null);
    try (CloseableHttpResponse response = this.httpclient.execute(new HttpGet("https://" + this.hostname.stringValue() + ":" + this.port.intValue() + urlPath.stringValue() + urlQuery.stringValue() + "&sid=" + this.sid.stringValue())))//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     {
      final int responseCode = response.getStatusLine().getStatusCode();
      if (responseCode != HttpURLConnection.HTTP_OK)
       {
        this.lastAccess = System.currentTimeMillis(); // TODO Will the session timeout be updated on bad requests?
        if (AHASessionMini.LOGGER.isDebugEnabled())
         {
          AHASessionMini.LOGGER.debug(response.getStatusLine());
         }
        if (AHASessionMini.LOGGER.isInfoEnabled())
         {
          AHASessionMini.LOGGER.info("HttpStatus: {}", response.getStatusLine().getStatusCode());  //$NON-NLS-1$
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
          return getString(urlPath, urlQuery); // TODO fix endless loop
         }
        if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST)
         {
          throw new UnsupportedOperationException("Possibly you used a command from a newer api version?"); //$NON-NLS-1$
         }
       }
      final HttpEntity entity = response.getEntity();
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("ContentType: {}", entity.getContentType()); //$NON-NLS-1$
       }
      final var result = EntityUtils.toString(entity);
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("getString: {}", result); //$NON-NLS-1$
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
      throw new IllegalArgumentException("Length must be even"); //$NON-NLS-1$
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
    final var hexStr = new StringBuilder(bytes.length * 2);
    for (final byte byt : bytes)
     {
      hexStr.append(String.format("%02x", byt)); //$NON-NLS-1$
     }
    return hexStr.toString();
   }


  /**
   * Create a pbkdf2 HMAC by appling the Hmac iter times as specified.
   *
   * @param bPassWord Password
   * @param salt Salt
   * @param iters Iterator
   * @return Byte array
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws InvalidKeyException Invalid key exception
   */
  private static byte[] pbkdf2HmacSha256(final byte[] bPassWord, final byte[] salt, final int iters) throws NoSuchAlgorithmException, InvalidKeyException
   {
    Objects.requireNonNull(bPassWord);
    Objects.requireNonNull(salt);
    if (iters < 0)
     {
      throw new IllegalArgumentException("iters < 0"); //$NON-NLS-1$
     }
    final var sha256mac = Mac.getInstance(AHASessionMini.SHA256);
    sha256mac.init(new SecretKeySpec(bPassWord, AHASessionMini.SHA256));
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
   * Has valid session.
   *
   * @return true: session is valid; false: session is invalid
   */
  public final boolean hasValidSession()
   {
    return this.sid.isValidSession();
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
  @SuppressWarnings("java:S4790")
  // @SuppressFBWarnings("WEAK_MESSAGE_DIGEST_MD5")
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
    final URIPath path = URIPath.of("/login_sid.lua");
    final URIQuery<URIQueryParameter> query1 = new URIQuery<>();
    query1.addEntry(URIQueryParameter.of("version", "2"));
    Document doc = getDoc(path, query1);
    this.sid = SID.of(doc.getElementsByTagName(AHASessionMini.SESSIONID).item(0).getTextContent());
    if (AHASessionMini.LOGGER.isDebugEnabled())
     {
      AHASessionMini.LOGGER.debug("sid: {}", this.sid.stringValue()); //$NON-NLS-1$
     }
    if (AHASessionMini.LOGGER.isDebugEnabled())
     {
      final String blocktime = doc.getElementsByTagName("BlockTime").item(0).getTextContent(); //$NON-NLS-1$
      AHASessionMini.LOGGER.debug("blocktime: {}", blocktime); //$NON-NLS-1$
     }
    // Users?
    // Rigths?

    // login
    if (!this.sid.isValidSession())
     {
      String challenge = doc.getElementsByTagName("Challenge").item(0).getTextContent(); //$NON-NLS-1$
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("challenge: {}", challenge); //$NON-NLS-1$
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
        final byte[] hash1 = pbkdf2HmacSha256(this.password.stringValue().getBytes(StandardCharsets.UTF_8), salt1, iter1);
        // <response> = <salt2>$ + pbdkf2_hmac_sha256(<hash1>, <salt2>, <iter2>)
        final byte[] hash2 = pbkdf2HmacSha256(hash1, salt2, iter2);
        response = challengeParts[4] + '$' + toHex(hash2);
       }
      else // MD5
       {
        // TODO
        // Aus Kompatibilitätsgründen muss für jedes Zeichen, dessen Unicode Codepoint > 255 ist, die Codierung des "."-Zeichens benutzt werden (0x2e 0x00 in UTF-16LE).
        // Dies betrifft also alle Zeichen, die nicht in ISO-8859-1 dargestellt werden können, z.B. das Euro-Zeichen
        response = challenge + '-' + new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest((challenge + '-' + this.password.stringValue()).getBytes(StandardCharsets.UTF_16LE)))); //$NON-NLS-1$
       }
      final URIQuery<URIQueryParameter> query2 = new URIQuery<>();
      query2.addEntry(URIQueryParameter.of("version", "2"));
      query2.addEntry(URIQueryParameter.of("username", this.username.stringValue()));
      query2.addEntry(URIQueryParameter.of("response", response));
      doc = getDoc(path, query2);
      this.sid = SID.of(doc.getElementsByTagName(AHASessionMini.SESSIONID).item(0).getTextContent());
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        AHASessionMini.LOGGER.debug("sid: {}", this.sid.stringValue()); //$NON-NLS-1$
       }
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        challenge = doc.getElementsByTagName("Challenge").item(0).getTextContent(); //$NON-NLS-1$
        AHASessionMini.LOGGER.debug("challenge: {}", challenge); //$NON-NLS-1$
       }
      if (AHASessionMini.LOGGER.isDebugEnabled())
       {
        final String blocktime = doc.getElementsByTagName("BlockTime").item(0).getTextContent(); //$NON-NLS-1$
        AHASessionMini.LOGGER.debug("blocktime: {}", blocktime); //$NON-NLS-1$
       }
      // Rigths ?
     }

    // check sid validity
    final URIQuery<URIQueryParameter> query3 = new URIQuery<>();
    query3.addEntry(URIQueryParameter.of("version", "2"));
    query3.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    doc = getDoc(path, query3);
    this.sid = SID.of(doc.getElementsByTagName(AHASessionMini.SESSIONID).item(0).getTextContent());
    if (!this.sid.isValidSession())
     {
      AHASessionMini.LOGGER.error("login invalid"); //$NON-NLS-1$
      // blocktime
      return false;
     }
    this.timeoutThread = new Thread();
    this.timeoutThread.start();
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("login valid"); //$NON-NLS-1$
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
    final URIPath path = URIPath.of("/login_sid.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("version", "2"));
    query.addEntry(URIQueryParameter.of("logout", "1"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);
    this.sid = SID.of(doc.getElementsByTagName(AHASessionMini.SESSIONID).item(0).getTextContent());
    if (!this.sid.isValidSession())
     {
      // TODO check for existing thread
      this.timeoutThread.interrupt();
      if (AHASessionMini.LOGGER.isInfoEnabled())
       {
        AHASessionMini.LOGGER.info("successfully logged out."); //$NON-NLS-1$
       }
      return true;
     }
    AHASessionMini.LOGGER.warn("logout error"); //$NON-NLS-1$
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
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "getswitchlist"));
    final var list = getString(path, query);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setswitchon"));
    final var result = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("setSwitchOn()->{}<", result); //$NON-NLS-1$
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setswitchoff"));
    final var result = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("setSwitchOff()->{}<", result); //$NON-NLS-1$
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setswitchtoggle"));
    final var state = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("setSwitchToggle()->{}<", state); //$NON-NLS-1$
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
  @SuppressWarnings("java:S2047")
  public final boolean getSwitchState(final AIN ain) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain",ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getswitchstate"));
    final var state = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("getSwitchState()->{}<", state); //$NON-NLS-1$
     }
    if ((state.length() >= 5) && AHASessionMini.INVAL.equals(state.substring(0, 5)))
     {
      throw new ProviderNotFoundException(AHASessionMini.INVALID);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getswitchpresent"));
    final var present = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("getSwitchPresent()->{}<", present); //$NON-NLS-1$
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getswitchpower"));
    var power = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("getSwitchPower()->{}<", power); //$NON-NLS-1$
     }
    if (power.length() > 1)
     {
      power = power.substring(0, power.length() - 1);
     }
    if (AHASessionMini.INVAL.equals(power))
     {
      throw new ProviderNotFoundException(AHASessionMini.INVALID);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getswitchenergy"));
    var energy = getString(path, query);
    if (AHASessionMini.LOGGER.isInfoEnabled())
     {
      AHASessionMini.LOGGER.info("getSwitchEnergy()->{}<", energy); //$NON-NLS-1$
     }
    if (energy.length() > 1)
     {
      energy = energy.substring(0, energy.length() - 1);
     }
    if (AHASessionMini.INVAL.equals(energy))
     {
      throw new ProviderNotFoundException(AHASessionMini.INVALID);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getswitchname"));
    final var name = getString(path, query);
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
   *
   * TODO Change Document result to value object
   */
  public final Document getDeviceListInfos() throws IOException, SAXException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "getdevicelistinfos"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    return getDoc(path, query);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "gettemperature"));
    final var temperature = getString(path, query);
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
  private static Temperature temperatureConversion(final String fbTemperatureString)
   {
    assert fbTemperatureString != null;
    final int fbTemperature = Integer.parseInt((fbTemperatureString.length() > 0) ? fbTemperatureString.substring(0, fbTemperatureString.length() - 1) : ""); //$NON-NLS-1$
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "gethkrtsoll"));
    final var resultStr = getString(path, query);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "gethkrkomfort"));
    final var resultStr = getString(path, query);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "gethkrabsenk"));
    final var resultStr = getString(path, query);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    Objects.requireNonNull(temperature, "temperature"); //$NON-NLS-1$
    long fbTemperature;
    if (temperature.longValue() == 0)
     {
      fbTemperature = 253;
     }
    else if (temperature.longValue() == 300)
     {
      fbTemperature = 254;
     }
    else if ((temperature.longValue() < 80) || (temperature.longValue() > 280))
     {
      throw new IndexOutOfBoundsException("Illegal temperature value!"); //$NON-NLS-1$
     }
    else
     {
      fbTemperature = (temperature.longValue() * 2) / 10;
     }
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "sethkrtsoll"));
    query.addEntry(URIQueryParameter.of("param", String.valueOf(fbTemperature)));
    /* String result = */ getString(path, query);
   }


  /* *
   * Parse stats elements.
   *
   * @param <T> Temperature, Percent, Voltage, Power, Energy
   * @param node XML start node: temperature, humidity, voltage, power, energy
   * @param list List of type SortedMap<UnixTimestamp, T>
   * @return SortedMap
   */
  /*
  private static <T> void parseStats(final Node node, final SortedMap<UnixTimestamp, T> list)
   {
    if (node != null)
     {
      final NodeList childs = node.getChildNodes();
      if (childs.getLength() > 0)
       {
        for (int index = 0; index < childs.getLength(); ++index)
         {
          final Node child = childs.item(index);
          final NamedNodeMap attributes = child.getAttributes();
          final Node countAttr = attributes.getNamedItem("count");
          final long count = Long.parseLong(countAttr.getNodeValue());
          final Node gridAttr = attributes.getNamedItem("grid");
          final long grid = Long.parseLong(gridAttr.getNodeValue());
          final Node datetimeAttr = attributes.getNamedItem("datetime");
          final long datetime = Long.parseLong(datetimeAttr.getNodeValue());
          final String contentStr = child.getTextContent();
          final String[] content = contentStr.split(",");
          for (int i = 0; i < content.length; ++i)
           {
            final long datetimepos = datetime - (grid * (count - (i + 1)));
            list.put(UnixTimestamp.of(Seconds.of(datetimepos)), ("-".equals(content[i])) ? null : T.of(Long.parseLong(content[i])));
           }
         }
       }
     }
   }
  */


  /**
   * Get basic device statistics.
   *
   * @param ain AIN
   * @return Quintet&lt;SortedMap&lt;UnixTimestamp, Temperature&gt;, SortedMap&lt;UnixTimestamp, Percent&gt;, SortedMap&lt;UnixTimestamp, Voltage&gt;, SortedMap&lt;UnixTimestamp, Power&gt;, SortedMap&lt;UnixTimestamp, Energy&gt;&gt;
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 6.98
   */
  public final Quintet<SortedMap<UnixTimestamp, Temperature>, SortedMap<UnixTimestamp, Percent>, SortedMap<UnixTimestamp, Voltage>, SortedMap<UnixTimestamp, Power>, SortedMap<UnixTimestamp, Energy>> getBasicDeviceStats(final AIN ain) throws IOException, SAXException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getbasicdevicestats"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);

    final Node temperatureNode = doc.getElementsByTagName("temperature").item(0);
    final SortedMap<UnixTimestamp, Temperature> temperatures = new TreeMap<>();
    // AHASessionMini.<Temperature>parseStats(temperatureNode, temperatures);
    if (temperatureNode != null)
     {
      final NodeList childs = temperatureNode.getChildNodes();
      if (childs.getLength() > 0)
       {
        for (int index = 0; index < childs.getLength(); ++index)
         {
          final Node child = childs.item(index);
          final NamedNodeMap attributes = child.getAttributes();
          final Node countAttr = attributes.getNamedItem("count");
          final long count = Long.parseLong(countAttr.getNodeValue());
          final Node gridAttr = attributes.getNamedItem("grid");
          final long grid = Long.parseLong(gridAttr.getNodeValue());
          final Node datetimeAttr = attributes.getNamedItem("datetime");
          final long datetime = Long.parseLong(datetimeAttr.getNodeValue());
          final String contentStr = child.getTextContent();
          final String[] content = contentStr.split(",");
          for (int i = 0; i < content.length; ++i)
           {
            final long datetimepos = datetime - (grid * (count - (i + 1)));
            temperatures.put(UnixTimestamp.of(Seconds.of(datetimepos)), ("-".equals(content[i])) ? null : Temperature.of(Long.parseLong(content[i])));
           }
         }
       }
     }

    final Node humidityNode = doc.getElementsByTagName("humidity").item(0);
    final SortedMap<UnixTimestamp, Percent> humidities = new TreeMap<>();
    // AHASessionMini.<Percent>parseStats(humidityNode, humidities);
    if (humidityNode != null)
     {
      final NodeList childs = humidityNode.getChildNodes();
      if (childs.getLength() > 0)
       {
        for (int index = 0; index < childs.getLength(); ++index)
         {
          final Node child = childs.item(index);
          final NamedNodeMap attributes = child.getAttributes();
          final Node countAttr = attributes.getNamedItem("count");
          final long count = Long.parseLong(countAttr.getNodeValue());
          final Node gridAttr = attributes.getNamedItem("grid");
          final long grid = Long.parseLong(gridAttr.getNodeValue());
          final Node datetimeAttr = attributes.getNamedItem("datetime");
          final long datetime = Long.parseLong(datetimeAttr.getNodeValue());
          final String contentStr = child.getTextContent();
          final String[] content = contentStr.split(",");
          for (int i = 0; i < content.length; ++i)
           {
            final long datetimepos = datetime - (grid * (count - (i + 1)));
            humidities.put(UnixTimestamp.of(Seconds.of(datetimepos)), ("-".equals(content[i])) ? null : Percent.of(Integer.parseInt(content[i])));
           }
         }
       }
     }

    final Node voltageNode = doc.getElementsByTagName("voltage").item(0);
    final SortedMap<UnixTimestamp, Voltage> voltages = new TreeMap<>();
    // AHASessionMini.<Voltage>parseStats(voltageNode, voltages);
    if (voltageNode != null)
     {
      final NodeList childs = voltageNode.getChildNodes();
      if (childs.getLength() > 0)
       {
        for (int index = 0; index < childs.getLength(); ++index)
         {
          final Node child = childs.item(index);
          final NamedNodeMap attributes = child.getAttributes();
          final Node countAttr = attributes.getNamedItem("count");
          final long count = Long.parseLong(countAttr.getNodeValue());
          final Node gridAttr = attributes.getNamedItem("grid");
          final long grid = Long.parseLong(gridAttr.getNodeValue());
          final Node datetimeAttr = attributes.getNamedItem("datetime");
          final long datetime = Long.parseLong(datetimeAttr.getNodeValue());
          final String contentStr = child.getTextContent();
          final String[] content = contentStr.split(",");
          for (int i = 0; i < content.length; ++i)
           {
            final long datetimepos = datetime - (grid * (count - (i + 1)));
            voltages.put(UnixTimestamp.of(Seconds.of(datetimepos)), ("-".equals(content[i])) ? null : Voltage.of(Long.parseLong(content[i])));
           }
         }
       }
     }

    final Node powerNode = doc.getElementsByTagName("power").item(0);
    final SortedMap<UnixTimestamp, Power> powers = new TreeMap<>();
    // AHASessionMini.<Power>parseStats(powerNode, powers);
    if (powerNode != null)
     {
      final NodeList childs = powerNode.getChildNodes();
      if (childs.getLength() > 0)
       {
        for (int index = 0; index < childs.getLength(); ++index)
         {
          final Node child = childs.item(index);
          final NamedNodeMap attributes = child.getAttributes();
          final Node countAttr = attributes.getNamedItem("count");
          final long count = Long.parseLong(countAttr.getNodeValue());
          final Node gridAttr = attributes.getNamedItem("grid");
          final long grid = Long.parseLong(gridAttr.getNodeValue());
          final Node datetimeAttr = attributes.getNamedItem("datetime");
          final long datetime = Long.parseLong(datetimeAttr.getNodeValue());
          final String contentStr = child.getTextContent();
          final String[] content = contentStr.split(",");
          for (int i = 0; i < content.length; ++i)
           {
            final long datetimepos = datetime - (grid * (count - (i + 1)));
            powers.put(UnixTimestamp.of(Seconds.of(datetimepos)), ("-".equals(content[i])) ? null : Power.of(Long.parseLong(content[i])));
           }
         }
       }
     }

    final Node energyNode = doc.getElementsByTagName("energy").item(0);
    final SortedMap<UnixTimestamp, Energy> energies = new TreeMap<>();
    // AHASessionMini.<Energy>parseStats(energyNode, energies);
    if (energyNode != null)
     {
      final NodeList childs = energyNode.getChildNodes();
      if (childs.getLength() > 0)
       {
        for (int index = 0; index < childs.getLength(); ++index)
         {
          final Node child = childs.item(index);
          final NamedNodeMap attributes = child.getAttributes();
          final Node countAttr = attributes.getNamedItem("count");
          final long count = Long.parseLong(countAttr.getNodeValue());
          final Node gridAttr = attributes.getNamedItem("grid");
          final long grid = Long.parseLong(gridAttr.getNodeValue());
          final Node datetimeAttr = attributes.getNamedItem("datetime");
          final long datetime = Long.parseLong(datetimeAttr.getNodeValue());
          final String contentStr = child.getTextContent();
          final String[] content = contentStr.split(",");
          for (int i = 0; i < content.length; ++i)
           {
            final long datetimepos = datetime - (grid * (count - (i + 1)));
            energies.put(UnixTimestamp.of(Seconds.of(datetimepos)), ("-".equals(content[i])) ? null : Energy.of(Long.parseLong(content[i])));
           }
         }
       }
     }

    final Quintet<SortedMap<UnixTimestamp, Temperature>, SortedMap<UnixTimestamp, Percent>, SortedMap<UnixTimestamp, Voltage>, SortedMap<UnixTimestamp, Power>, SortedMap<UnixTimestamp, Energy>> devicestats = Quintet.with(temperatures, humidities, voltages, powers, energies);
    return devicestats;
   }


  /**
   * Get template list infos.
   *
   * @return List of Template
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 6.98
   */
  public final List<Template> getTemplateListInfos() throws IOException, SAXException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "gettemplatelistinfos"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);
    final List<Template> templates = new ArrayList<>();

    final NodeList templateNodes = doc.getElementsByTagName("template");
    for (int pos = 0; pos < templateNodes.getLength(); ++pos)
     {
      final Node templateNode = templateNodes.item(pos);
      final NamedNodeMap attrs = templateNode.getAttributes();

      final long fbm = Long.parseLong(attrs.getNamedItem("functionbitmask").getNodeValue());
      final List<Functions> functionsList = new ArrayList<>();
      for (int i = 0; i <= Functions.HUMIDITY_SENSOR.getAction(); ++i) // TODO Change HUMIDITY_SENSOR in case that Functions will be extended
       {
        final long mask = 0x00000001 << i;
        if ((fbm & mask) != 0)
         {
          functionsList.add(Functions.of(i));
         }
       }

      final AIN identifier = AIN.of(attrs.getNamedItem("identifier").getNodeValue());
      final long id = Long.parseLong(attrs.getNamedItem("id").getNodeValue());
      final EnumSet<Functions> functionbitmask = EnumSet.copyOf(functionsList);
      final boolean autocreate = "1".equals(attrs.getNamedItem("autocreate").getNodeValue());
      final long applymaskField = Long.parseLong(attrs.getNamedItem("applymask").getNodeValue());

      String name = "";
      Metadata metadata = null;
      final List<AIN> devices = new ArrayList<>();
      final List<AIN> triggers = new ArrayList<>();
      final List<AIN> subtemplates = new ArrayList<>();
      final List<ApplyMask> applyMaskList = new ArrayList<>();
      final NodeList childs = templateNode.getChildNodes();
      for (int cpos = 0; cpos < childs.getLength(); ++cpos)
       {
        final Node childNode = childs.item(pos);
        switch(childNode.getNodeName())
         {
          case "name":
            name = childNode.getTextContent();
            break;
          case "metadata":
           {
            final String json = childNode.getTextContent();
            final Gson gson = new Gson();
            final FBMetadata md = gson.fromJson(json, FBMetadata.class);
            if (md != null)
             {
              metadata = Metadata.of((md.getIcon() == 0) ? -1 : md.getIcon(), ScenarioType.of(md.getType().toUpperCase(Locale.getDefault())));
             }
            break;
           }
          case "devices":
           {
            final NodeList deviceNodes = childNode.getChildNodes();
            for (int devpos = 0; devpos < childs.getLength(); ++devpos)
             {
              final Node deviceNode = deviceNodes.item(devpos);
              devices.add(AIN.of(deviceNode.getAttributes().getNamedItem("identifier").getNodeValue()));
             }
            break;
           }
          case "triggers":
           {
            final NodeList triggerNodes = childNode.getChildNodes();
            for (int trpos = 0; trpos < triggerNodes.getLength(); ++trpos)
             {
              final Node triggerNode = triggerNodes.item(trpos);
              triggers.add(AIN.of(triggerNode.getAttributes().getNamedItem("identifier").getNodeValue()));
             }
            break;
           }
          case "subtemplates":
           {
            final NodeList subtemplateNodes = childNode.getChildNodes();
            for (int stpos = 0; stpos < subtemplateNodes.getLength(); ++stpos)
             {
              final Node subtemplateNode = subtemplateNodes.item(stpos);
              subtemplates.add(AIN.of(subtemplateNode.getAttributes().getNamedItem("identifier").getNodeValue()));
             }
            break;
           }
          case "applymask":
           {
            final NodeList applymaskNodes = childNode.getChildNodes();
            for (int ampos = 0; ampos < applymaskNodes.getLength(); ++ampos)
             {
              final Node applymaskNode = applymaskNodes.item(ampos);
              final String nodeName = applymaskNode.getNodeName().toUpperCase(Locale.getDefault());
              applyMaskList.add(ApplyMask.of(nodeName));
             }
            break;
           }
         }
       }
      final EnumSet<ApplyMask> applymask = EnumSet.copyOf(applyMaskList);
      final Template template = Template.of(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
      templates.add(template);
     }
    return templates;
   }


  /**
   * Apply template.
   *
   * @param templateAIN Template AIN
   * @throws IOException IO exception
   * @since 6.98
   */
  public final void applyTemplate(final AIN templateAIN) throws IOException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", templateAIN.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "applytemplate"));
    /* final String id = */ getString(path, query);
   }


  /**
   * Set simple on off.
   *
   * @param ain AIN
   * @param onoff OFF, ON, TOGGLE
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if onoff is not 0, 1 or 2
   * @since 7.15
   */
  public final void setSimpleOnOff(final AIN ain, final HandleOnOff onoff) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setsimpleonoff"));
    query.addEntry(URIQueryParameter.of("onoff", String.valueOf(onoff.getAction())));
    /* final String result = */ getString(path, query);
   }


  /**
   * Set level.
   *
   * @param ain AIN
   * @param level Level 0-255 (0%-100%)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setLevel(final AIN ain, final Level level) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setlevel"));
    query.addEntry(URIQueryParameter.of("level", level.stringValue()));
    /* final String result = */ getString(path, query);
   }


  /**
   * Set level percentage.
   *
   * @param ain AIN
   * @param level 0-100
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-100
   * @since 7.15
   */
  public final void setLevelPercentage(final AIN ain, final Percent level) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setlevelpercentage"));
    query.addEntry(URIQueryParameter.of("level", level.stringValue()));
    /* final String result = */ getString(path, query);
   }


  /**
   * Set color.
   *
   * @param ain AIN
   * @param hue Hue 0-359 degrees
   * @param saturation Saturation 0-255 (0% - 100%)
   * @param duration Duration of change in 100ms, 0 means immediately (not supported yet)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setColor(final AIN ain, final Hue hue, final Saturation saturation, final DurationMS100 duration) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setcolor"));
    query.addEntry(URIQueryParameter.of("hue", hue.stringValue()));
    query.addEntry(URIQueryParameter.of("saturation", saturation.stringValue()));
    query.addEntry(URIQueryParameter.of("duration", duration.stringValue()));
    /* final String result = */ getString(path, query);
   }


  /**
   * Set color temperature.
   *
   * @param ain AIN
   * @param temperatureKelvin Temperature in kelvin 2700-6500
   * @param duration Duration of change in 100ms, 0 means immediately (not supported yet)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.15
   */
  public final void setColorTemperature(final AIN ain, final TemperatureKelvin temperatureKelvin, final DurationMS100 duration) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setcolortemperature"));
    query.addEntry(URIQueryParameter.of("temperature", temperatureKelvin.stringValue()));
    query.addEntry(URIQueryParameter.of("duration", duration.stringValue()));
    /* final String result = */ getString(path, query);
   }


  /**
   * Get color defaults.
   *
   * @return Color defaults
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 7.15
   */
  public final Pair<List<Hs>, List<TemperatureKelvin>> getColorDefaults() throws IOException, SAXException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "getcolordefaults"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);

    final List<Hs> hsdefaults = new ArrayList<>();
    final NodeList hsNodes = doc.getElementsByTagName("hs");
    for (int pos = 0; pos < hsNodes.getLength(); ++pos)
     {
      final Node hsNode = hsNodes.item(pos);
      final String index = hsNode.getAttributes().getNamedItem("hue_index").getNodeValue();
      final NodeList childs = hsNode.getChildNodes();
      String name = "";
      int nameEnum = 0;
      final List<Color> colors = new ArrayList<>();
      for (int childPos = 0; childPos < childs.getLength(); ++childPos)
       {
        final Node child = childs.item(childPos);
        if ("name".equals(child.getNodeName()))
         {
          name = child.getTextContent();
          nameEnum = Integer.parseInt(child.getAttributes().getNamedItem("enum").getNodeValue());
         }
        else if ("color".equals(child.getNodeName()))
         {
          final NamedNodeMap attributes = child.getAttributes();
          final int idx = Integer.parseInt(attributes.getNamedItem("sat_index").getNodeValue());
          final Hue hue = Hue.of(attributes.getNamedItem("hue").getNodeValue());
          final Saturation sat = Saturation.of(attributes.getNamedItem("sat").getNodeValue());
          final Value val = Value.of(attributes.getNamedItem("val").getNodeValue());
          final Color color = Color.of(idx, hue, sat, val);
          colors.add(color);
         }
       }
      final Hs hs = Hs.of(Integer.parseInt(index), nameEnum, name, colors);
      hsdefaults.add(hs);
     }
    final List<TemperatureKelvin> temperatureDefaults = new ArrayList<>();
    final NodeList tempNodes = doc.getElementsByTagName("temp");
    for (int pos = 0; pos < tempNodes.getLength(); ++pos)
     {
      final Node tempNode = tempNodes.item(pos);
      final String value = tempNode.getAttributes().getNamedItem("value").getNodeValue();
      final TemperatureKelvin tk = TemperatureKelvin.of(value);
      temperatureDefaults.add(tk);
     }
    final Pair<List<Hs>, List<TemperatureKelvin>> colorDefaults = Pair.with(hsdefaults, temperatureDefaults);
    return colorDefaults;
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
  public final long setHkrBoost(final AIN ain, final EndTimestamp endtimestamp) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "sethkrboost"));
    query.addEntry(URIQueryParameter.of("endtimestamp", endtimestamp.stringValue()));
    final var result = getString(path, query);
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
  public final long setHkrWindowOpen(final AIN ain, final EndTimestamp endtimestamp) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "sethkrwindowopen"));
    query.addEntry(URIQueryParameter.of("endtimestamp", endtimestamp.stringValue()));
    final var result = getString(path, query);
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
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setblind"));
    query.addEntry(URIQueryParameter.of("target", target.name().toLowerCase(Locale.getDefault())));
    /* final var result = */ getString(path, query);
   }


  /**
   * Set name.
   *
   * Change device or group name (UTF-8).
   * Requires smarthome and restricted app rights.
   *
   * @param ain AIN
   * @param name Device or group name in UTF-8
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws IllegalArgumentException If name is &gt; 40 characters
   * @throws NullPointerException If ain or temperature is null
   * @since 7.24
   *
   * TODO Name -> value object, encoding?
   */
  public final void setName(final AIN ain, final String name) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    Objects.requireNonNull(name, "name"); //$NON-NLS-1$
    if (name.length() > 40)
     {
      throw new IllegalArgumentException("Name longer than 40 characters!"); //$NON-NLS-1$
     }
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setname"));
    query.addEntry(URIQueryParameter.of("name", name));
    /* final var result = */ getString(path, query);
   }


  /**
   * Start dect-ule device subscription.
   *
   * Requires smarthome and restricted app rights.
   *
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws IllegalArgumentException If name is &gt; 40 characters
   * @throws NullPointerException If ain or temperature is null
   * @since 7.24
   */
  public final void startUleSubscription() throws IOException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "startulesubscription"));
    /* final var result = */ getString(path, query);
   }


  /**
   * Get dect-ule device subscription state.
   *
   * Requires smarthome and restricted app rights.
   *
   * @return SubscriptionState
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 7.24
   */
  public final SubscriptionState getSubscriptionState() throws IOException, SAXException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "getsubscriptionstate"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);
    final SubscriptionCode code = SubscriptionCode.of(Integer.valueOf(doc.getElementsByTagName("state").item(0).getAttributes().getNamedItem("code").getNodeValue()));
    final String ain = doc.getElementsByTagName("latestain").item(0).getTextContent();
    final AIN latestain = ain.isBlank() ? null : AIN.of(ain);
    final SubscriptionState state = SubscriptionState.of(code, latestain);
    return state;
   }


  /**
   * Set HueSaturation color.
   *
   * The HSV color space is supported with the HueSaturation mode. The brightness value can be configured via setlevel/setlevelpercentage; the hue and saturation values ​​can be configured here.
   *
   * @param ain AIN
   * @param hue 0-359 degrees
   * @param saturation 0-255 (0% - 100%)
   * @param duration Duration of change in 100ms, 0 means immediately (not supported yet)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @throws IllegalArgumentException if level is not 0-255
   * @since 7.24
   */
  public final void setUnmappedColor(final AIN ain, final Hue hue, final Saturation saturation, final DurationMS100 duration) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setunmappedcolor"));
    query.addEntry(URIQueryParameter.of("hue", hue.stringValue()));
    query.addEntry(URIQueryParameter.of("saturation", saturation.stringValue()));
    query.addEntry(URIQueryParameter.of("duration", duration.stringValue()));
    /* final var result = */ getString(path, query);
   }


  /**
   * Change/set json metadata of the template.
   *
   * @param ain AIN
   * @param metadata json-metadaten of the template or empty string, maximum 200 byte
   *                 2 defined json-keys:
   *                 "icon" - Integer, Icon id of the icon-font
   *                 "type" - Type of szenario, if defined. A string of "coming", "leaving" or "generic".
   *                 One of these key has to be set, no other keys are allowed
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain or metadata is null
   * @throws IllegalArgumentException if metadata has more than 200 bytes
   * @since 7.39
   *
   * TODO metadata -> value object
   */
  public final void setMetaData(final AIN ain, final String metadata) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    Objects.requireNonNull(metadata, "metadata");
    if (metadata.getBytes(Charset.forName("UTF-8")).length > 200)
     {
      throw new IllegalArgumentException("metatdata should not have more than 200 bytes"); //$NON-NLS-1$
     }
    // TODO Check ain for template
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "setmetadata"));
    query.addEntry(URIQueryParameter.of("level", metadata));
    /* final var result = */ getString(path, query);
   }


  /**
   * Provides the basic information all routines/triggers.
   *
   * @return Information on all routines/triggers
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 7.39
   */
  public final List<Trigger> getTriggerListInfos() throws IOException, SAXException
   {
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "gettriggerlistinfos"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);
    final NodeList nodes = doc.getElementsByTagName("trigger");
    final List<Trigger> triggers = new ArrayList<>();
    for (int index = 0; index < nodes.getLength(); ++index)
     {
      final Node node = nodes.item(index);
      final AIN ain = AIN.of(node.getAttributes().getNamedItem("identifier").getNodeValue());
      final String name = node.getChildNodes().item(0).getTextContent();
      final boolean active = "1".equals(node.getAttributes().getNamedItem("active").getNodeValue());
      final Trigger trigger = Trigger.of(ain, name, active);
      triggers.add(trigger);
     }
    return triggers;
   }


  /**
   * Activate or deactivate trigger.
   *
   * @param ain AIN
   * @param active true: activate trigger; false: deactivate trigger
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain is null
   * @since 7.39
   */
  public final void setTriggerActive(final AIN ain, final boolean active) throws IOException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "settriggeractive"));
    query.addEntry(URIQueryParameter.of("active", (active ? "1" : "0")));
    /* final var result = */ getString(path, query);
   }


  /**
   * Creates a color template for lamps.
   *
   * Name, levelPercentage, ains and hue+saturation or temperature must be passed.
   *
   * @param name Template name
   * @param levelPercentage Level 0-100
   * @param hue 0-359 degrees or null if not used
   * @param saturation 0-255 (0% - 100%) or null if not used
   * @param temperatureKelvin Temperature in kelvin 2700-6500 or null if not used
   * @param colorpreset true: use Colordefaults; false: do not use defaults (default)
   * @param ains AINs (maximum: 9)
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NullPointerException If ain or metadata is null
   * @throws IllegalArgumentException if metadata has more than 200 bytes
   * @since 7.39
   */
  public final void addColorLevelTemplate(final String name, final Percent levelPercentage, final OptionalOf<Hue> hue, final OptionalOf<Saturation> saturation, final OptionalOf<TemperatureKelvin> temperatureKelvin, final boolean colorpreset, final AIN ... ains) throws IOException
   {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(hue, "hue");
    Objects.requireNonNull(saturation, "saturation");
    Objects.requireNonNull(temperatureKelvin, "temperatureKelvin");
    // TODO name regexp
    if (((hue.intValue() == -1) || (saturation.intValue() == -1)) && (temperatureKelvin.intValue() == -1))
     {
      throw new IllegalArgumentException("hue/saturation or temperatureKelvin must be set"); //$NON-NLS-1$
     }
    if (ains.length > 9) // TODO Maximum?
     {
      throw new IllegalArgumentException("More than 9 ains are not allowed"); //$NON-NLS-1$
     }

    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("switchcmd", "addcolorleveltemplate"));
    query.addEntry(URIQueryParameter.of("name", name));
    query.addEntry(URIQueryParameter.of("levelPercentage", levelPercentage.stringValue()));
    query.addEntry(URIQueryParameter.of("colorpreset", String.valueOf(colorpreset)));

    if ((hue.intValue() != -1) && (saturation.intValue() != -1))
     {
      query.addEntry(URIQueryParameter.of("hue", hue.stringValue()));
      query.addEntry(URIQueryParameter.of("saturation", saturation.stringValue()));
     }
    else if (temperatureKelvin.intValue() != -1)
     {
      query.addEntry(URIQueryParameter.of("temperature", temperatureKelvin.stringValue()));
     }
    int n = 1;
    for (final AIN ain : ains)
     {
      query.addEntry(URIQueryParameter.of("child_" + n, ain.stringValue()));
      ++n;
     }
    /* final var result = */ getString(path, query);
   }


  /**
   * Provides the basic information of the SmartHome device.
   *
   * @param ain AIN
   * @return Device infos in XML format see getdevicelistinfos
   * @throws ClientProtocolException Client protocol exception
   * @throws UnsupportedEncodingException Unsupported encoding exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @since 7.24
   *
   * TODO Change Document result to value object
   */
  public final Document getDeviceInfos(final AIN ain) throws IOException, SAXException
   {
    Objects.requireNonNull(ain, AHASessionMini.AIN_STR);
    final URIPath path = URIPath.of("/webservices/homeautoswitch.lua");
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("ain", ain.stringValue()));
    query.addEntry(URIQueryParameter.of("switchcmd", "getdeviceinfos"));
    query.addEntry(URIQueryParameter.of("sid", this.sid.stringValue()));
    final Document doc = getDoc(path, query);
    return doc;
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
    return Objects.hash(this.hostname, this.username, this.password, this.sid.stringValue());
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
    if ((obj == null) || (this.getClass() != obj.getClass()))
    // if (!(obj instanceof AHASessionMini))
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
    return new StringBuilder().append("AHASessionMini[hostname=").append(this.hostname.stringValue()).append(", username=").append(this.username.stringValue()).append(", sid=").append(this.sid.stringValue()).append(']').toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Compare with another object.
   *
   * @param ahaSessionMiniObj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final AHASessionMini ahaSessionMiniObj)
   {
    Objects.requireNonNull(ahaSessionMiniObj, "obj"); //$NON-NLS-1$
    int result = this.hostname.compareTo(ahaSessionMiniObj.hostname);
    if (result == 0)
     {
      result = this.username.compareTo(ahaSessionMiniObj.username);
      if (result == 0)
       {
        result = this.sid.compareTo(ahaSessionMiniObj.sid);
       }
     }
    return result;
   }

 }

/* TODO
1.36: HANFUN IF OPEN_CLOSE, OPEN_CLOSE_CONFIG
      Vorlagen Applymask erweitert(level, color, dialhelper)
      <humidity> für FD440, getbasicdevicestats mit <humidity> Statistik
      Switchcmd getdeviceinfos hinzugefügt
      Info zu Berechtigungen hinzugefügt

1.39: Vorlage mit autocreate-Flag und metadata-String,
      <sub_templates>-Info für zugeordnete Unter-Vorlagen
      Statistik mit datatime-Unix-Timestamp Attribute

1.42: Vorlage applymask aktualisiert(timer_control, switch_master, http_request, tam_control, guest_wifi, main_wifi, sub_templates, sun_simulation)

1.43: Vorlagen Beispiel mit sub_templates erweitert

1.44: Neuer Vorlagen Typ: custom_notification

1.45: Colorcontrol: fullcolorsupport, mapped, unmapped_hue und unmapped_saturation hinzugefügt
      ausserdem: rel_humidity, blind mit mode und endpositionsset

1.46: windowopenactiveendtime bei externem Fenstersensor: -1

1.47: adaptiveHeatingActive und adaptiveHeatingActive Info

1.49: Metadata-Info erweitert

1.51: windowopenactiveendtime Zusatzinfo
      Vorlagen und Szenarien Info

1.52: Setname cmd unterstützt Vorlagen und Szenarien

1.53: HKR <tist> und <tsoll> können leer sein, wenn die Temperatur unbekannt ist

1.54: setname cmd unterstützt nicht vordefinierte Szenarien und Routinen
      Info zu <windowopenactiv> und <windowopenactiveendtime> erweitert

1.55: Hinweis auf proprietäre 0xf7.. HAN-FUN Interfaces, Name mit Länge 40 Bytes

1.59: functionbitmask für AVM bits konkretisiert

1.60: level und levelpercentage für Rollo konkretisiert

*/
/*
https://fritz.box/webservices/homeautoswitch.lua?ain=<ain>&switchcmd=<cmd>&sid=<sid>

Der HTTPS-Port ist auf der FRITZ!Box konfigurierbar. Er kann über den X_AVM-DE_RemoteAccess TR-064-Service und der
dazugehörigen GetInfo-Action mit der "NewPort"-Variable abgefragt werden. Siehe TR064-Specifikation in [1].


Die AVM Home Automation Session benötigt immer die Smart-Home-Berechtigung. Ausserdem wird für einige
Kommandos die "Eingeschränkte FRITZ!Box Einstellungen für Apps"-Berechtigung benötigt.
Zu Berechtigungen siehe "Actions and User Rights" in der TR064-Specifikation in [1].

Die HTTP Response enthält den zum Kommando zugehörigen Status als Text. Der Content-Type ist "text/plain;
charset=utf-8".
Ausnahme bei getdevicelistinfos, getsubscriptionstate und getbasicdevicestats: Die HTTP Response enthält den Inhalt als
XML. Der Content-Type ist "text/xml; charset=utf-8".
*/
