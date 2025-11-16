/*
 * Copyright (C) 2019-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Mode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.powerstat.fb.mini.TR64SessionMini;
import de.powerstat.fb.mini.Action;
import de.powerstat.fb.mini.ServiceType;
import de.powerstat.fb.mini.URIPath;
import de.powerstat.validation.values.Hostname;
import de.powerstat.validation.values.Port;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * TR64SessionMini tests.
 *
 * @author PowerStat
 */
@SuppressFBWarnings({"EC_NULL_ARG", "NAB_NEEDLESS_BOOLEAN_CONSTANT_CONVERSION", "RV_NEGATING_RESULT_OF_COMPARETO", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class TR64SessionMiniTests
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(TR64SessionMiniTests.class);

  /**
   * FB default hostname.
   */
  private static final String FBHOSTNAME = "fritz.box"; //$NON-NLS-1$

  /**
   * FB default username.
   */
  private static final String FBUSERNAME = "admin"; //$NON-NLS-1$

  /**
   * FB test password.
   */
  private static final String FBPASSWORD = "topSecret"; //$NON-NLS-1$

  /**
   * Password.
   */
  private static final String FBPASSWORD2 = "TopSecret"; //$NON-NLS-1$

  /**
   * %072x constant.
   */
  private static final String CONST_072X = "%072x"; //$NON-NLS-1$


  /**
   * Default constructor.
   */
  /* default */ TR64SessionMiniTests()
   {
    super();
   }


  /**
   * Test TR64SessionMini new instance creation.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   */
  @Test
  /* default */ void testNewInstance1() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final TR64SessionMini tr64session = TR64SessionMini.newInstance(mockHttpclient, mockDocBuilder, Hostname.of(FBHOSTNAME), Port.of(49443));
    assertNotNull(tr64session, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test TR64SessionMini new instance creation.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   */
  @Test
  /* default */ void testNewInstance2() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final TR64SessionMini tr64session = TR64SessionMini.newInstance(FBUSERNAME, FBPASSWORD);

    assertNotNull(tr64session, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test toString.
   *
   * @throws KeyManagementException Key management exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   */
  @Test
  /* default */ void testToString() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final TR64SessionMini tr64session = TR64SessionMini.newInstance(mockHttpclient, mockDocBuilder, Hostname.of(FBHOSTNAME), Port.of(49443));
    final String representation = tr64session.toString();
    assertEquals("TR64SessionMini[hostname=fritz.box, port=49443]", representation, "toString result not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test getDoc.
   *
   * @throws KeyManagementException Key management exception
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws SAXException SAX exception
   * @throws KeyStoreException Key store exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws TransformerException Transformer exception
   */
  @Test
  /* default */ void testGetDoc() throws KeyManagementException, IOException, NoSuchAlgorithmException, SAXException, KeyStoreException, ParserConfigurationException, TransformerException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final CloseableHttpResponse mockCloseableHttpResponse = mock(CloseableHttpResponse.class);
    final HttpEntity mockHttpEntity = mock(HttpEntity.class);

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();
    when(mockHttpclient.execute(ArgumentMatchers.any(HttpGet.class))).thenReturn(mockCloseableHttpResponse);
    when(mockCloseableHttpResponse.getStatusLine()).thenReturn(null);
    when(mockCloseableHttpResponse.getEntity()).thenReturn(mockHttpEntity);
    when(mockHttpEntity.isStreaming()).thenReturn(false);

    final String testDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<test>abc</test>\n"; //$NON-NLS-1$
    when(mockHttpEntity.getContentType()).thenReturn(null);
    when(mockHttpEntity.getContent()).thenReturn(new ByteArrayInputStream(testDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity.getContentLength()).thenReturn((long)testDoc.length());

    // verify();
    final TR64SessionMini tr64session = TR64SessionMini.newInstance(mockHttpclient, docBuilder, Hostname.of(FBHOSTNAME), Port.of(49443));
    final URIPath urlPath = URIPath.of("/tr64desc.xml");
    final Document doc = tr64session.getDoc(urlPath);
    final String xml = TR64SessionMini.docToString(doc);
    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("doc: " + xml); //$NON-NLS-1$

      LOGGER.info(String.format(CONST_072X, new BigInteger(1, testDoc.getBytes(StandardCharsets.UTF_8))));
      LOGGER.info(String.format(CONST_072X, new BigInteger(1, xml.getBytes(StandardCharsets.UTF_8))));
     }
    assertAll(
      () -> assertNotNull(doc, "getDoc failed!"), //$NON-NLS-1$
      () -> assertEquals(testDoc, xml.replace("\r", ""), "docToString result not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test do SOAP request.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyManagementException Key management exception
   * @throws IOException IO exception
   * @throws TransformerException Transformer exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws SAXException SAX exception
   * @throws KeyStoreException Key store exception
   */
  @Test
  /* default */ void testDoSOAPRequest() throws ParserConfigurationException, KeyManagementException, IOException, TransformerException, NoSuchAlgorithmException, SAXException, KeyStoreException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final CloseableHttpResponse mockCloseableHttpResponse = mock(CloseableHttpResponse.class);
    final HttpEntity mockHttpEntity = mock(HttpEntity.class);

    when(mockHttpclient.execute(ArgumentMatchers.argThat(new HttpPostMatcher("/upnp/control/deviceinfo")))).thenReturn(mockCloseableHttpResponse); //$NON-NLS-1$
    when(mockCloseableHttpResponse.getEntity()).thenReturn(mockHttpEntity);

    final String testDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<test>abc</test>\n"; //$NON-NLS-1$
    when(mockHttpEntity.isStreaming()).thenReturn(false);
    when(mockHttpEntity.getContentType()).thenReturn(null);
    when(mockHttpEntity.getContent()).thenReturn(new ByteArrayInputStream(testDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity.getContentLength()).thenReturn((long)testDoc.length());

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();

    final TR64SessionMini tr64session = TR64SessionMini.newInstance(mockHttpclient, docBuilder, Hostname.of(FBHOSTNAME), Port.of(49443));
    final Map<String, String> parameters = new ConcurrentHashMap<>();
    parameters.put("test", "value"); //$NON-NLS-1$ //$NON-NLS-2$
    final Document doc = tr64session.doSOAPRequest(URIPath.of("/upnp/control/deviceinfo"), ServiceType.of("urn:dslforum-org:service:DeviceInfo:1"), Action.of("GetInfo"), parameters); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final String xml = TR64SessionMini.docToString(doc);

    // verify(mockHttpclient).

    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("doc: " + xml); //$NON-NLS-1$

      LOGGER.info(String.format(CONST_072X, new BigInteger(1, testDoc.getBytes(StandardCharsets.UTF_8))));
      LOGGER.info(String.format(CONST_072X, new BigInteger(1, xml.getBytes(StandardCharsets.UTF_8))));
     }
    assertAll(
      () -> assertNotNull(doc, "doSOAPRequest failed!"), //$NON-NLS-1$
      () -> assertEquals(testDoc, xml.replace("\r", ""), "docToString result not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test do SOAP request with null parameters.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyManagementException Key management exception
   * @throws IOException IO exception
   * @throws TransformerException Transformer exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws SAXException SAX exception
   * @throws KeyStoreException Key store exception
   */
  @Test
  /* default */ void testDoSOAPRequestNull() throws ParserConfigurationException, KeyManagementException, IOException, TransformerException, NoSuchAlgorithmException, SAXException, KeyStoreException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final CloseableHttpResponse mockCloseableHttpResponse = mock(CloseableHttpResponse.class);
    final HttpEntity mockHttpEntity = mock(HttpEntity.class);

    when(mockHttpclient.execute(ArgumentMatchers.any(HttpPost.class))).thenReturn(mockCloseableHttpResponse);
    when(mockCloseableHttpResponse.getEntity()).thenReturn(mockHttpEntity);

    final String testDoc = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<test>abc</test>\n"; //$NON-NLS-1$
    when(mockHttpEntity.isStreaming()).thenReturn(false);
    when(mockHttpEntity.getContentType()).thenReturn(null);
    when(mockHttpEntity.getContent()).thenReturn(new ByteArrayInputStream(testDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity.getContentLength()).thenReturn((long)testDoc.length());

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();

    final TR64SessionMini tr64session = TR64SessionMini.newInstance(mockHttpclient, docBuilder, Hostname.of(FBHOSTNAME), Port.of(49443));
    final Document doc = tr64session.doSOAPRequest(URIPath.of("/upnp/control/deviceinfo"), ServiceType.of("urn:dslforum-org:service:DeviceInfo:1"), Action.of("GetInfo"), null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final String xml = TR64SessionMini.docToString(doc);

    // verify(mockHttpclient).

    if (LOGGER.isInfoEnabled())
     {
      LOGGER.info("doc: " + xml); //$NON-NLS-1$

      LOGGER.info(String.format(CONST_072X, new BigInteger(1, testDoc.getBytes(StandardCharsets.UTF_8))));
      LOGGER.info(String.format(CONST_072X, new BigInteger(1, xml.getBytes(StandardCharsets.UTF_8))));
     }
    assertAll(
      () -> assertNotNull(doc, "doSOAPRequest failed!"), //$NON-NLS-1$
      () -> assertEquals(testDoc, xml.replace("\r", ""), "docToString result not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    Hostname hostname1 = Hostname.of("fritz.box");
    Hostname hostname2 = Hostname.of("fritz2.box");
    Port port1 = Port.of(443);
    Port port2 = Port.of(80);
    EqualsVerifier.simple().forClass(TR64SessionMini.class).set(Mode.skipMockito()).withNonnullFields("hostname", "port").withIgnoredFields("httpclient", "docBuilder").withPrefabValues(Hostname.class, hostname1, hostname2).withPrefabValues(Port.class, port1, port2).verify();
   }


  /**
   * Test compareTo.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final TR64SessionMini session1 = TR64SessionMini.newInstance(FBHOSTNAME, 49443, FBUSERNAME, FBPASSWORD2);
    final TR64SessionMini session2 = TR64SessionMini.newInstance(FBHOSTNAME, 49443, FBUSERNAME, FBPASSWORD2);
    final TR64SessionMini session3 = TR64SessionMini.newInstance("fritz2.box", 49443, "admin2", "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final TR64SessionMini session4 = TR64SessionMini.newInstance("fritz3.box", 49443, "admin3", "TopSecret3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final TR64SessionMini session5 = TR64SessionMini.newInstance(FBHOSTNAME, 49443, FBUSERNAME, FBPASSWORD2);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(session1.compareTo(session2) == -session2.compareTo(session1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(session1.compareTo(session3) == -session3.compareTo(session1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((session4.compareTo(session3) > 0) && (session3.compareTo(session1) > 0) && (session4.compareTo(session1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((session1.compareTo(session2) == 0) && (Math.abs(session1.compareTo(session5)) == Math.abs(session2.compareTo(session5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((session1.compareTo(session2) == 0) && session1.equals(session2), "equals") //$NON-NLS-1$
    );
   }


  /**
   * HttpPost matcher.
   */
  private static class HttpPostMatcher implements ArgumentMatcher<HttpPost>
   {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(HttpPostMatcher.class);

    /**
     * URI path to match.
     */
    private final String path;


    /**
     * Constructor.
     *
     * @param path URI path to match
     */
    /* default */ HttpPostMatcher(final String path)
     {
      super();
      Objects.requireNonNull(path, "path"); //$NON-NLS-1$
      this.path = path;
     }


    /**
     * Matches uri paths.
     *
     * @param right Right HttpGet object.
     * @return true when match, false otherwise
     * @see org.mockito.ArgumentMatcher#matches(java.lang.Object)
     */
    @Override
    public boolean matches(final HttpPost right)
     {
      if (right == null)
       {
        return false;
       }
      String rpath = right.getURI().getPath();
      if (right.getURI().getQuery() != null)
       {
        rpath += "?" + right.getURI().getQuery(); //$NON-NLS-1$
       }
      if (LOGGER.isDebugEnabled())
       {
        LOGGER.debug("rpath: " + rpath); //$NON-NLS-1$
       }
      return path.equals(rpath) && right.containsHeader("SoapAction") && right.containsHeader("USER-AGENT") && right.containsHeader("Content-Type") && (right.getEntity() != null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     }
   }

 }
