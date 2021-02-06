/*
 * Copyright (C) 2015-2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
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
import de.powerstat.validation.values.Password;
import de.powerstat.validation.values.Port;
import de.powerstat.validation.values.Username;


/**
 * FB TR64 session.
 *
 * This class is not serializable because of session management!
 *
 * @author Kai Hofmann
 */
public class TR64SessionMini implements Comparable<TR64SessionMini>
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(TR64SessionMini.class);

  /**
   * HTTP client.
   */
  private final CloseableHttpClient httpclient;

  /**
   * XML document builder.
   */
  private final DocumentBuilder docBuilder;

  /**
   * Hostname.
   */
  private final Hostname hostname;

  /**
   * Port.
   */
  private final Port port;


  /**
   * Constructor.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname FB hostname
   * @param port FB port number
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyManagementException Key management exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NullPointerException If hostname, username or password is null
   */
  protected TR64SessionMini(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final Hostname hostname, final Port port) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, ParserConfigurationException
   {
    super();
    this.httpclient = Objects.requireNonNull(httpclient, "httpclient"); //$NON-NLS-1$
    this.docBuilder = Objects.requireNonNull(docBuilder, "docBuilder"); //$NON-NLS-1$
    this.hostname = Objects.requireNonNull(hostname, "hostname"); //$NON-NLS-1$
    this.port = Objects.requireNonNull(port, "port"); //$NON-NLS-1$
   }


  /**
   * Get new instance for a TR64SessionMini.
   *
   * @param httpclient CloseableHttpClient
   * @param docBuilder DocumentBuilder
   * @param hostname FB hostname
   * @param port FB port number
   * @return TR64SessionMini
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyManagementException Key management exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NullPointerException If hostname, username or password is null
   */
  public static TR64SessionMini newInstance(final CloseableHttpClient httpclient, final DocumentBuilder docBuilder, final Hostname hostname, final Port port) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return new TR64SessionMini(httpclient, docBuilder, hostname, port);
   }


  /**
   * Get new instance for a TR64SessionMini.
   *
   * @param hostname FB hostname
   * @param port FB port number
   * @param username FB username
   * @param password FB password
   * @return TR64SessionMini
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyManagementException Key management exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NullPointerException If hostname, username or password is null
   */
  public static TR64SessionMini newInstance(final Hostname hostname, final Port port, final Username username, final Password password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    Objects.requireNonNull(hostname, "hostname"); //$NON-NLS-1$
    Objects.requireNonNull(port, "port"); //$NON-NLS-1$
    Objects.requireNonNull(username, "username"); //$NON-NLS-1$
    Objects.requireNonNull(password, "password"); //$NON-NLS-1$
    final CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(new AuthScope(hostname.getHostname(), port.getPort()), new UsernamePasswordCredentials(username.getUsername(), password.getPassword()));
    final CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build())).setDefaultCredentialsProvider(credsProvider).build();

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();

    return newInstance(httpclient, docBuilder, hostname, port);
   }


  /**
   * Get new instance for a TR64SessionMini.
   *
   * @param hostname FB hostname
   * @param port FB port number
   * @param username FB username
   * @param password FB password
   * @return TR64SessionMini
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyManagementException Key management exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NullPointerException If hostname, username or password is null
   */
  public static TR64SessionMini newInstance(final String hostname, final int port, final String username, final String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance(Hostname.of(hostname), Port.of(port), Username.of(username), Password.of(password));
   }


  /**
   * Get new instance for default hostname fritz.box and default port 49443.
   *
   * @param username FB username
   * @param password FB password
   * @return TR64SessionMini
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyManagementException Key management exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws NullPointerException If hostname, username or password is null
   */
  public static TR64SessionMini newInstance(final String username, final String password) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    return newInstance("fritz.box", 49443, username, password); //$NON-NLS-1$
   }


  /**
   * XML doc to string.
   *
   * @param doc XML document
   * @return XML string
   * @throws TransformerException Transformer exception
   * @throws NullPointerException If doc is null
   */
  public static String docToString(final Document doc) throws TransformerException
   {
    Objects.requireNonNull(doc, "doc"); //$NON-NLS-1$
    final StringWriter strWriter = new StringWriter();
    final TransformerFactory factory = TransformerFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); //$NON-NLS-1$
    factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); //$NON-NLS-1$
    // factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); //$NON-NLS-1$
    // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    // factory.setFeature("http://xml.org/sax/features/external-general-entities", false); //$NON-NLS-1$ //$NON-NLS-2$
    // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false); //$NON-NLS-1$
    // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$
    // factory.setXIncludeAware(false);
    // factory.setExpandEntityReferences(false);
    final Transformer transformer = factory.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no"); //$NON-NLS-1$
    transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
    transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //$NON-NLS-1$
    transformer.transform(new DOMSource(doc), new StreamResult(strWriter));
    return strWriter.toString();
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
   * @throws NullPointerException If urlPath is null
   *
   * TODO urlPath value object
   */
  public final Document getDoc(final String urlPath) throws IOException, SAXException
   {
    Objects.requireNonNull(urlPath, "urlPath"); //$NON-NLS-1$
    try (CloseableHttpResponse response = this.httpclient.execute(new HttpGet("https://" + this.hostname.getHostname() + ":" + this.port.getPort() + ValidationUtils.sanitizeUrlPath(urlPath)))) //$NON-NLS-1$ //$NON-NLS-2$
     {
      LOGGER.debug(response.getStatusLine());
      final HttpEntity entity = response.getEntity();
      LOGGER.debug(entity.getContentType());
      final Document doc = this.docBuilder.parse(new InputSource(new ByteArrayInputStream(EntityUtils.toString(entity).getBytes(StandardCharsets.UTF_8))));
      EntityUtils.consume(entity);
      return doc;
     }
   }


  /**
   * Do soap request.
   *
   * @param controlURL Control url
   * @param serviceType Service type
   * @param action Action
   * @param parameters Parameters or null
   * @return XML response Document
   * @throws UnsupportedEncodingException Unsupported encoding exceptoin
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws SAXException SAX exception
   * @throws ParseException Parse exception
   * @throws NullPointerException If controlUrl, serviceType or action is null
   *
   * TODO controlURL value object
   * TODO serviceType value object
   * TODO action value object
   */
  public final Document doSOAPRequest(final String controlURL, final String serviceType, final String action, final Map<String, String> parameters) throws IOException, SAXException
   {
    Objects.requireNonNull(controlURL, "controlURL"); //$NON-NLS-1$
    Objects.requireNonNull(serviceType, "serviceType"); //$NON-NLS-1$
    Objects.requireNonNull(action, "action"); //$NON-NLS-1$
    final StringBuilder requestBuffer = new StringBuilder(243);
    requestBuffer.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><s:Envelope xmlns:s=\"http://schemas.xmlsoap.org/soap/envelope/\" s:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">  <s:Body>    <u:" + action + " xmlns:u=\"" + serviceType + "\">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    if (parameters != null)
     {
      for (final Entry<String, String> parameter : parameters.entrySet())
       {
        requestBuffer.append("    <" + parameter.getKey() + ">" + parameter.getValue() + "</" + parameter.getKey() + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
       }
     }
    requestBuffer.append("    </u:" + action + ">  </s:Body></s:Envelope>"); //$NON-NLS-1$ //$NON-NLS-2$
    // LOGGER.info(sb.toString());

    final HttpPost httpPost = new HttpPost("https://" + this.hostname.getHostname() + ":" + this.port.getPort() + controlURL); //$NON-NLS-1$ //$NON-NLS-2$
    httpPost.setHeader("SoapAction", "\"" + serviceType + "#" + action + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    httpPost.setHeader("USER-AGENT", "PowerStats FB TR64 mini client"); //$NON-NLS-1$ //$NON-NLS-2$
    httpPost.setHeader("Content-Type", "text/xml; charset=utf-8"); //$NON-NLS-1$ //$NON-NLS-2$

    httpPost.setEntity(new StringEntity(requestBuffer.toString()));
    try (CloseableHttpResponse response = this.httpclient.execute(httpPost))
     {
      // LOGGER.info(response.getStatusLine());
      final HttpEntity entity = response.getEntity();
      // LOGGER.info(entity.getContentType());
      final Document doc = this.docBuilder.parse(new InputSource(new ByteArrayInputStream(EntityUtils.toString(entity).getBytes(StandardCharsets.UTF_8))));
      EntityUtils.consume(entity);
      return doc;
     }
   }


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   *
   * TODO username
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(this.hostname, this.port);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   *
   * TODO username
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof TR64SessionMini))
     {
      return false;
     }
    final TR64SessionMini other = (TR64SessionMini)obj;
    return (this.hostname.equals(other.hostname) && this.port.equals(other.port));
   }


  /**
   * Returns the string representation of this TR64SessionMini.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "TR64SessionMini[hostname=fritz.box, port=49443, ...]"
   *
   * @return String representation of this TR64SessionMini.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    return new StringBuilder().append("TR64SessionMini[hostname=").append(this.hostname.getHostname()).append(", port=").append(this.port.getPort()).append(']').toString(); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   *
   * TODO username
   */
  @Override
  public int compareTo(final TR64SessionMini obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = this.hostname.compareTo(obj.hostname);
    if (result == 0)
     {
      result = this.port.compareTo(obj.port);
     }
    return result;
   }

 }
