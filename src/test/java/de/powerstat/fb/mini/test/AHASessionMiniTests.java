/*
 * Copyright (C) 2019-2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.ProviderNotFoundException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatcher;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.powerstat.fb.mini.AHASessionMini;
import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.Energy;
import de.powerstat.fb.mini.Power;
import de.powerstat.fb.mini.TR64SessionMini;
import de.powerstat.fb.mini.Temperature;


/**
 * AHASessionMini tests.
 */
public class AHASessionMiniTests
 {
  /**
   * Logger.
   */
  private static final Logger LOGGER = LogManager.getLogger(AHASessionMiniTests.class);

  /**
   * FB test password.
   */
  private static final String FBPASSWORD = "topSecret"; //$NON-NLS-1$


  /**
   * Default constructor.
   */
  public AHASessionMiniTests()
   {
    super();
   }


  /**
   * Get document builder.
   *
   * @return DocumentBuilder
   * @throws ParserConfigurationException Parser configuration exception
   */
  private DocumentBuilder getDocBuilder() throws ParserConfigurationException
   {
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    return factory.newDocumentBuilder();
   }


  /**
   * Create logon mocks.
   *
   * @param mockHttpclient Httpclient mock
   * @param mockStatusLineOk Statusline mock
   * @param resultDoc XML result document
   * @param successful Successful login, otherwise login with failure
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   */
  private void createLogonMocks(final CloseableHttpClient mockHttpclient, final StatusLine mockStatusLineOk, final String resultDoc, final boolean successful) throws IOException, ClientProtocolException, NoSuchAlgorithmException
   {
    final HttpEntity mockHttpEntity1 = mock(HttpEntity.class);
    when(mockHttpEntity1.isStreaming()).thenReturn(false);
    when(mockHttpEntity1.getContentType()).thenReturn(null);
    when(mockHttpEntity1.getContent()).thenReturn(new ByteArrayInputStream(resultDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity1.getContentLength()).thenReturn((long)resultDoc.length());

    final CloseableHttpResponse mockCloseableHttpResponse1 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse1.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse1.getEntity()).thenReturn(mockHttpEntity1);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua")))).thenReturn(mockCloseableHttpResponse1); //$NON-NLS-1$

    // ----------

    final HttpEntity mockHttpEntity2 = mock(HttpEntity.class);
    when(mockHttpEntity2.isStreaming()).thenReturn(false);

    final String testDoc2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000004711</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights><Name>Dial</Name><Access>2</Access><Name>NAS</Name><Access>2</Access><Name>App</Name><Access>2</Access><Name>HomeAuto</Name><Access>2</Access><Name>BoxAdmin</Name><Access>2</Access><Name>Phone</Name><Access>2</Access></Rights></SessionInfo>"; //$NON-NLS-1$
    when(mockHttpEntity2.getContentType()).thenReturn(null);
    when(mockHttpEntity2.getContent()).thenReturn(new ByteArrayInputStream(testDoc2.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity2.getContentLength()).thenReturn((long)testDoc2.length());

    final CloseableHttpResponse mockCloseableHttpResponse2 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse2.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse2.getEntity()).thenReturn(mockHttpEntity2);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?username=&response=deadbeef-" + new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest(("deadbeef-" + FBPASSWORD).getBytes(Charset.forName("utf-16le"))))))))).thenReturn(mockCloseableHttpResponse2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    // ----------

    final HttpEntity mockHttpEntity3 = mock(HttpEntity.class);
    when(mockHttpEntity3.isStreaming()).thenReturn(false);
    when(mockHttpEntity3.getContentType()).thenReturn(null);
    if (successful)
     {
      when(mockHttpEntity3.getContent()).thenReturn(new ByteArrayInputStream(testDoc2.getBytes(StandardCharsets.UTF_8)));
      when(mockHttpEntity3.getContentLength()).thenReturn((long)testDoc2.length());
     }
    else
     {
      when(mockHttpEntity3.getContent()).thenReturn(new ByteArrayInputStream(resultDoc.getBytes(StandardCharsets.UTF_8)));
      when(mockHttpEntity3.getContentLength()).thenReturn((long)resultDoc.length());
     }

    final CloseableHttpResponse mockCloseableHttpResponse3 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse3.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse3.getEntity()).thenReturn(mockHttpEntity3);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse3); //$NON-NLS-1$
   }


  /**
   * Create logoff mocks.
   *
   * @param mockHttpclient Httpclient mock
   * @param mockStatusLineOk StatusLine mock
   * @param resultDoc Result xml document
   * @throws IOException IO exception
   * @throws ClientProtocolException Client protocol exception
   */
  private void createLogoffMocks(final CloseableHttpClient mockHttpclient, final StatusLine mockStatusLineOk, final String resultDoc) throws IOException, ClientProtocolException
   {
    final HttpEntity mockHttpEntity4 = mock(HttpEntity.class);
    when(mockHttpEntity4.isStreaming()).thenReturn(false);
    when(mockHttpEntity4.getContentType()).thenReturn(null);
    when(mockHttpEntity4.getContent()).thenReturn(new ByteArrayInputStream(resultDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity4.getContentLength()).thenReturn((long)resultDoc.length());

    final CloseableHttpResponse mockCloseableHttpResponse4 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse4.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse4.getEntity()).thenReturn(mockHttpEntity4);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?logout=1&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse4); //$NON-NLS-1$
   }


  /**
   * Test newInstance.
   *
   * @throws KeyManagementException KeyManagementException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws KeyStoreException KeyStoreException
   * @throws ParserConfigurationException ParserConfigurationException
   */
  @Test
  public void newInstance1() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final AHASessionMini ahasession = AHASessionMini.newInstance(FBPASSWORD);
    assertNotNull(ahasession, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test newInstance.
   *
   * @throws KeyManagementException KeyManagementException
   * @throws NoSuchAlgorithmException NoSuchAlgorithmException
   * @throws KeyStoreException KeyStoreException
   * @throws ParserConfigurationException ParserConfigurationException
   */
  @Test
  public void newInstance2() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, mockDocBuilder, "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    assertNotNull(ahasession, "newInstance failed!"); //$NON-NLS-1$
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
  public void testToString() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, mockDocBuilder, "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final String representation = ahasession.toString();
    assertEquals("AHASessionMini[hostname=fritz.box, username=, sid=]", representation, "toString with unexpected result"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Logon/Logoff test.
   *
   * @throws IOException IO exception
   * @throws KeyManagementException Key management exception
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   */
  // @Test
  public void logonLogoff() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$

    final boolean successLogon = ahasession.logon();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Logon failure test.
   *
   * @throws IOException IO exception
   * @throws KeyManagementException Key management exception
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   */
  @Test
  public void logonFailure() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, false);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$

    final boolean successLogon = ahasession.logon();
    assertAll(
      () -> assertFalse(successLogon, "Logon success") //$NON-NLS-1$
    );
   }


  /**
   * Logoff failure test.
   *
   * @throws IOException IO exception
   * @throws KeyManagementException Key management exception
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   */
  @Test
  public void logoffFailure() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    final String testDoc2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000004711</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc2);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$

    final boolean successLogon = ahasession.logon();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertFalse(successLogoff, "Logoff success") //$NON-NLS-1$
    );
   }


  /**
   * Get switch list test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser cofiguration exception
   * @throws KeyStoreException  Key store exception
   * @throws KeyManagementException  Key management exception
   * @throws SAXException  SAX exception
   */
  @Test
  public void getSwitchList1() throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc3 = "000000000001,000000000002,000000000003\n"; //$NON-NLS-1$
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc3.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc3.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=getswitchlist&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final List<AIN> results = new ArrayList<>();
    results.add(AIN.of("000000000001")); //$NON-NLS-1$
    results.add(AIN.of("000000000002")); //$NON-NLS-1$
    results.add(AIN.of("000000000003")); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final List<AIN> switches = ahasession.getSwitchList();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(results, switches, "Switches are not as expected"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Get switch list test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser cofiguration exception
   * @throws KeyStoreException  Key store exception
   * @throws KeyManagementException  Key management exception
   * @throws SAXException  SAX exception
   */
  @Test
  public void getSwitchList2() throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc3 = "000000000001\n"; //$NON-NLS-1$
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc3.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc3.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=getswitchlist&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final List<AIN> results = new ArrayList<>();
    results.add(AIN.of("000000000001")); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final List<AIN> switches = ahasession.getSwitchList();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(results, switches, "Switches are not as expected"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Get switch list empty test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser cofiguration exception
   * @throws KeyStoreException  Key store exception
   * @throws KeyManagementException  Key management exception
   * @throws SAXException  SAX exception
   */
  @Test
  public void getSwitchListEmpty() throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc3 = ""; //$NON-NLS-1$
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc3.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc3.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=getswitchlist&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final List<AIN> results = new ArrayList<>();

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final List<AIN> switches = ahasession.getSwitchList();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(results, switches, "Switches are not as expected"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set switch on.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchOn() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = "1\n"; //$NON-NLS-1$
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchon&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOn(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(result, "Switch not on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set switch on with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchOnFailure1() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final StatusLine mockStatusLineFailure = mock(StatusLine.class);
    when(mockStatusLineFailure.getStatusCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = "1\n"; //$NON-NLS-1$
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineFailure);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchon&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(IOException.class, () ->
     {
      /* final boolean result = */ ahasession.setSwitchOn(AIN.of("000000000001")); //$NON-NLS-1$
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test set switch on with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchOnFailure2() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchon&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOn(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertFalse(result, "Switch on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set switch off.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchOff() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "0\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(result, "Switch not off"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set switch off with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchOffFailure() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertFalse(result, "Switch off"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set switch toggle.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchToggle() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "0\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "1\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchtoggle&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean resultOff1 = ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean resultToggle1 = ahasession.setSwitchToggle(AIN.of("000000000001")); //$NON-NLS-1$
    /* final boolean resultOff2 = */ ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(resultOff1, "Switch not off"), //$NON-NLS-1$
      () -> assertTrue(resultToggle1, "Switch not on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set switch toggle with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void setSwitchToggleFailure() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "0\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchtoggle&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean resultOff1 = ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean resultToggle1 = ahasession.setSwitchToggle(AIN.of("000000000001")); //$NON-NLS-1$
    /* final boolean resultOff2 = */ ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(resultOff1, "Switch not off"), //$NON-NLS-1$
      () -> assertFalse(resultToggle1, "Switch on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get switch state.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getSwitchState() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "1\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchon&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "1\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchstate&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean resultOn = ahasession.setSwitchOn(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean resultState = ahasession.getSwitchState(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(resultOn, "Switch not on"), //$NON-NLS-1$
      () -> assertTrue(resultState, "Switch state not on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get switch state with failure.
   *
   * @param inval Invalid
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"inval\n", "inval"})
  public void getSwitchStateFailure1(final String inval) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "0\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = inval;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchstate&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    /* final boolean resultOff = */ ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    assertThrows(ProviderNotFoundException.class, () ->
     {
      /* final boolean resultState = */ ahasession.getSwitchState(AIN.of("000000000001")); //$NON-NLS-1$
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get switch state with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getSwitchStateFailure2() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = "0\n"; //$NON-NLS-1$
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse6); //$NON-NLS-1$

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchstate&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean resultOff = ahasession.setSwitchOff(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean resultState = ahasession.getSwitchState(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(resultOff, "Switch not off"), //$NON-NLS-1$
      () -> assertFalse(resultState, "Switch state not off"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test is switch present.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void isSwitchPresent() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "1\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchpresent&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean present = ahasession.isSwitchPresent(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(present, "Switch state not off"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test is switch present with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void isSwitchPresentFailure() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchpresent&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final boolean present = ahasession.isSwitchPresent(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertFalse(present, "Switch state not off"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get switch power.
   *
   * @param powerValue Power value for testing
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"10150\n", "1"})
  public void getSwitchPower(final String powerValue) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = powerValue;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchpower&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Power power = ahasession.getSwitchPower(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(Long.parseLong(powerValue.replaceAll("\n", "")), power.getPowerMilliWatt(), "Switch power not as expected"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get switch power with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getSwitchPowerFailure() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "inval\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchpower&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(ProviderNotFoundException.class, () ->
     {
      /* final Power power = */ ahasession.getSwitchPower(AIN.of("000000000001")); //$NON-NLS-1$
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get switch energy.
   *
   * @param energyValue Energy test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"75519\n", "1"})
  public void getSwitchEnergy(final String energyValue) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = energyValue;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchenergy&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Energy energy = ahasession.getSwitchEnergy(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(Long.parseLong(energyValue.replaceAll("\n", "")), energy.getEnergyWattHours(), "Switch power not as expected"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get switch energy with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getSwitchEnergyFailure() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "inval\n";
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchenergy&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(ProviderNotFoundException.class, () ->
     {
      /* final Energy energy = */ ahasession.getSwitchEnergy(AIN.of("000000000001")); //$NON-NLS-1$
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get switch name.
   *
   * @param name Switch name for test
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"01 office\n", "\n", ""})
  public void getSwitchName(final String name) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = name;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchname&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final String switchName = ahasession.getSwitchName(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(name.replaceAll("\n", ""), switchName, "Switch name not as expected"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get device ist infos.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getDeviceListInfos() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<devicelist version=\"1\"></devicelist>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=getdevicelistinfos&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Document doc = ahasession.getDeviceListInfos();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<devicelist version=\"1\"/>\n", TR64SessionMini.docToString(doc).replaceAll("\r", ""), "device info list not as expected"), //$NON-NLS-1$ //$NON-NLS-2$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get device ist infos with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getDeviceListInfosFailure() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final StatusLine mockStatusLineForbidden = mock(StatusLine.class);
    when(mockStatusLineForbidden.getStatusCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<devicelist version=\"1\"></devicelist>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineForbidden);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=getdevicelistinfos&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(IOException.class, () ->
     {
      /* final Document doc = */ ahasession.getDeviceListInfos();
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get temperature.
   *
   * @param temperatureValue Temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"182\n"})
  public void getTemperature(final String temperatureValue) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = temperatureValue;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=gettemperature&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Temperature temperature = ahasession.getTemperature(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals(Long.parseLong(temperatureValue.replaceAll("\n", "")), temperature.getTemperatureDeciCelsius(), "temperature not as expected"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get temperature with failure.
   *
   * @param temperatureValue Temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"\n", ""})
  public void getTemperatureFailure(final String temperatureValue) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = temperatureValue;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=gettemperature&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(NumberFormatException.class, () ->
     {
      /* final Temperature temperature = */ ahasession.getTemperature(AIN.of("000000000001")); //$NON-NLS-1$
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get hkrt soll.
   *
   * @param soll Soll temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"40\n", "253\n", "254\n"})
  public void getHkrtSoll(final String soll) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = soll;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=gethkrtsoll&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Temperature temperature = ahasession.getHkrtSoll(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> {
        long expected = (Long.parseLong(soll.replaceAll("\n", "")) * 10) / 2; //$NON-NLS-1$ //$NON-NLS-2$
        if (expected == ((253 * 10) / 2))
         {
          expected = 0;
         }
        else if (expected == ((254 * 10) / 2))
         {
          expected = 300;
         }
        assertEquals(expected, temperature.getTemperatureDeciCelsius(), "temperature not as expected"); //$NON-NLS-1$
       },
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get hkrt soll with failure.
   *
   * @param soll Soll temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"\n", ""})
  public void getHkrtSollFailure(final String soll) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = soll;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=gethkrtsoll&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(NumberFormatException.class, () ->
     {
      /* final Temperature temperature = */ ahasession.getHkrtSoll(AIN.of("000000000001")); //$NON-NLS-1$
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get hkr komfort.
   *
   * @param komfort Komfort temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"40\n"})
  public void getHkrKomfort(final String komfort) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = komfort;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=gethkrkomfort&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Temperature temperature = ahasession.getHkrKomfort(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals((Long.parseLong(komfort.replaceAll("\n", "")) * 10) / 2, temperature.getTemperatureDeciCelsius(), "temperature not as expected"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get hkrt absenk.
   *
   * @param absenk Absenk temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"34\n"})
  public void getHkrAbsenk(final String absenk) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = absenk;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=gethkrabsenk&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Temperature temperature = ahasession.getHkrAbsenk(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals((Long.parseLong(absenk.replaceAll("\n", "")) * 10) / 2, temperature.getTemperatureDeciCelsius(), "temperature not as expected"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set hkrt soll.
   *
   * @param temperature Temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 300, 80, 280})
  public void setHkrtSoll(final long temperature) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=sethkrtsoll&param=" + ((temperature == 0) ? "253" : (temperature == 300 ? "254" : ((temperature * 2) / 10))) + "&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$ //$NON-NLS-2$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    ahasession.setHkrtSoll(AIN.of("000000000001"), Temperature.of(temperature)); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test set hkrt soll with failure.
   *
   * @param temperature Temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"75", "285"})
  public void setHkrtSollFailure(final String temperature) throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=sethkrtsoll&param=" + temperature + "&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$ //$NON-NLS-2$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      ahasession.setHkrtSoll(AIN.of("000000000001"), Temperature.of(temperature));
     }
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get basic device statistics.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getBasicDeviceStats() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<devicestats></devicestats>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getbasicdevicestats&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Document doc = ahasession.getBasicDeviceStats(AIN.of("000000000001")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<devicestats/>\n", TR64SessionMini.docToString(doc).replaceAll("\r", ""), "device info list not as expected"), //$NON-NLS-1$ //$NON-NLS-2$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test get template list infos.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void getTemplateListInfos() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<templatelist version=\"1\"></templatelist>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=gettemplatelistinfos&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    final Document doc = ahasession.getTemplateListInfos();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<templatelist version=\"1\"/>\n", TR64SessionMini.docToString(doc).replaceAll("\r", ""), "device info list not as expected"), //$NON-NLS-1$ //$NON-NLS-2$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test apply template.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   */
  @Test
  public void applyTemplate() throws ClientProtocolException, NoSuchAlgorithmException, IOException, KeyManagementException, KeyStoreException, ParserConfigurationException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "0\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=tmp000000-000000000&switchcmd=applytemplate&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), "fritz.box", 443, "", FBPASSWORD); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean successLogon = ahasession.logon();
    ahasession.applyTemplate("tmp000000-000000000"); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, "Logon failed"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, "Logoff failed") //$NON-NLS-1$
    );
   }


  /**
   * Test against real fritz box.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException Key store exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws KeyManagementException Key management exception
   * @throws SAXException SAX exception
   * @throws IOException IO exception
   * @throws TransformerException Transformer exception
   */
  // @Test
  public void real() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException, IOException, SAXException, TransformerException
   {
    LOGGER.debug("---------- real start ----------"); //$NON-NLS-1$
    final AHASessionMini ahasession = AHASessionMini.newInstance(""); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    // final Document result = ahasession.getTemplateListInfos();
    // LOGGER.debug("Stats: " + TR64SessionMini.docToString(result));
    ahasession.applyTemplate("tmp000000-000000000"); //$NON-NLS-1$
    /* final boolean successLogoff = */ ahasession.logoff();
    LOGGER.debug("---------- real end ----------"); //$NON-NLS-1$
   }


  /**
   * Security test.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @throws TransformerException Transformer exception
   */
  // @Test
  public void security() throws ParserConfigurationException, SAXException, IOException, TransformerException
   {
    // final String string = "<test>&lt;script&gt;alert();&lt;/script&gt;</test>";
    // final String string = "<test>%3Cscript%3Ealert()%3C/script%3E</test>";
    // final String string = "<element>\n<![CDATA[<script>alert();</script>]]>\n</element>";

    // final String string = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!DOCTYPE updateProfile [<!ENTITY file SYSTEM \"file:///etc/passwd\"> ]>\n<updateProfile>\n<lastname>&file;</lastname>\n</updateProfile>\n";
    // final String string = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<!DOCTYPE lolz[\n<!ENTITY lol \"lol\"><!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n<!ENTITY lol2 \"&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;\">\n<!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n<!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n<!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n<!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n<!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\">\n<!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\">\n<!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\">]>\n<lolz>&lol9;</lolz>";
    // final String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE root [\n<!ENTITY % xxe SYSTEM \"https://tei-c.org/Vault/P4/Lite/DTD/teilite.dtd\">\n%xxe;\n]>";
    final String string = ""; //$NON-NLS-1$

    final byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
    final ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
    final InputSource stream = new InputSource(byteStream);

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false);
    // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true); //$NON-NLS-1$
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
    // factory.setFeature("http://xml.org/sax/features/external-general-entities", true); //$NON-NLS-1$
    // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true); //$NON-NLS-1$
    // factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    // factory.setXIncludeAware(false);
    // factory.setExpandEntityReferences(false);
    // factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();
    final Document doc = docBuilder.parse(stream);
    if (LOGGER.isDebugEnabled())
     {
      LOGGER.debug("XMLString: " + TR64SessionMini.docToString(doc)); //$NON-NLS-1$
     }
   }


  /**
   * Test hash code.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @Test
  public void testHashCode() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final AHASessionMini session1 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session2 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session3 = AHASessionMini.newInstance("fritz2.box", 443, "", "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(session1.hashCode(), session2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(session1.hashCode(), session3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws KeyStoreException  Key store exception
   * @throws NoSuchAlgorithmException  No such algorithm exception
   * @throws KeyManagementException  Key management exception
   */
  @Test
  public void testEquals() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final AHASessionMini session1 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session2 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session3 = AHASessionMini.newInstance("fritz2.box", 443, "", "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session4 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(session1.equals(session1), "session11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(session1.equals(session2), "session12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(session2.equals(session1), "session21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(session2.equals(session4), "session24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(session1.equals(session4), "session14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(session1.equals(session3), "session13 are equal"), //$NON-NLS-1$
      () -> assertFalse(session3.equals(session1), "session31 are equal"), //$NON-NLS-1$
      () -> assertFalse(session1.equals(null), "session10 is equal") //$NON-NLS-1$
    );
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
  public void testCompareTo() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final AHASessionMini session1 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session2 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session3 = AHASessionMini.newInstance("fritz2.box", 443, "", "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session4 = AHASessionMini.newInstance("fritz3.box", 443, "", "TopSecret3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session5 = AHASessionMini.newInstance("fritz.box", 443, "", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session6 = AHASessionMini.newInstance("fritz.box", 443, "admin", "TopSecret"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(session1.compareTo(session2) == -session2.compareTo(session1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(session1.compareTo(session3) == -session3.compareTo(session1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((session4.compareTo(session3) > 0) && (session3.compareTo(session1) > 0) && (session4.compareTo(session1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((session1.compareTo(session2) == 0) && (Math.abs(session1.compareTo(session5)) == Math.abs(session2.compareTo(session5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((session1.compareTo(session2) == 0) && session1.equals(session2), "equals"), //$NON-NLS-1$
      () -> assertTrue((session1.compareTo(session6) < 0), "not as expected") //$NON-NLS-1$
    );
   }


  /**
   * HttpGet matcher.
   */
  public static class HttpGetMatcher implements ArgumentMatcher<HttpGet>
   {
    /**
     * URI path to match.
     */
    private final String path;


    /**
     * Constructor.
     *
     * @param path URI path to match
     */
    public HttpGetMatcher(final String path)
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
    public boolean matches(final HttpGet right)
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
      return this.path.equals(rpath);
     }
   }

 }
