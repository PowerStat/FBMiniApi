/*
 * Copyright (C) 2019-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.SortedMap;
import java.util.stream.Stream;

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
import org.javatuples.Pair;
import org.javatuples.Quintet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentMatcher;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import nl.jqno.equalsverifier.Mode;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import de.powerstat.fb.mini.AHASessionMini;
import de.powerstat.fb.mini.AHASessionMini.HandleBlind;
import de.powerstat.fb.mini.AHASessionMini.HandleOnOff;
import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.Device;
// import de.powerstat.fb.mini.Action;
import de.powerstat.fb.mini.DurationMS100;
import de.powerstat.fb.mini.EndTimestamp;
import de.powerstat.fb.mini.Energy;
import de.powerstat.fb.mini.Functions;
import de.powerstat.fb.mini.Hs;
import de.powerstat.fb.mini.Hue;
import de.powerstat.fb.mini.Level;
import de.powerstat.fb.mini.Power;
import de.powerstat.fb.mini.SID;
import de.powerstat.fb.mini.Saturation;
import de.powerstat.fb.mini.SubscriptionState;
import de.powerstat.fb.mini.TR64SessionMini;
import de.powerstat.fb.mini.TemperatureCelsius;
import de.powerstat.fb.mini.TemperatureKelvin;
import de.powerstat.fb.mini.Template;
import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.fb.mini.Version;
import de.powerstat.fb.mini.Voltage;
import de.powerstat.validation.values.Hostname;
import de.powerstat.validation.values.Password;
import de.powerstat.validation.values.Percent;
import de.powerstat.validation.values.Port;
import de.powerstat.validation.values.Seconds;
import de.powerstat.validation.values.Username;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * AHASessionMini tests.
 */
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.CouplingBetweenObjects"})
@SuppressFBWarnings({"EC_NULL_ARG", "NAB_NEEDLESS_BOOLEAN_CONSTANT_CONVERSION", "RV_NEGATING_RESULT_OF_COMPARETO", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class AHASessionMiniTests
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
   * fritz.box host name.
   */
  private static final String FRITZ_BOX = "fritz.box"; //$NON-NLS-1$

  /**
   * Minimum session xml document.
   */
  private static final String MIN_SESSION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000000000</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$

  /**
   * Line break.
   */
  private static final String LINEBREAK = "\n";

  /**
   * Logon failed message.
   */
  private static final String LOGON_FAILED = "Logon failed";

  /**
   * Logoff failed message.
   */
  private static final String LOGOFF_FAILED = "Logoff failed";

  /**
   * SID 4711.
   */
  private static final String SID4711 = "&sid=0000000000004711";

  /**
   * AIN 000000000001.
   */
  private static final String AIN1 = "000000000001"; //$NON-NLS-1$

  /**
   * Test content 0.
   */
  private static final String TEST_CONTENT_0 = "0\n"; //$NON-NLS-1$

  /**
   * Test content 1.
   */
  private static final String TEST_CONTENT_1 = "1\n"; //$NON-NLS-1$

  /**
   * Home auto switch test url.
   */
  private static final String HOMEAUTOSWITCH1 = "/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchon&sid=0000000000004711"; //$NON-NLS-1$

  /**
   * Home auto switch test url.
   */
  private static final String HOMEAUTOSWITCH2 = "/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchoff&sid=0000000000004711"; //$NON-NLS-1$

  /**
   * Home auto switch test url.
   */
  private static final String HOMEAUTOSWITCH3 = "/webservices/homeautoswitch.lua?switchcmd=getswitchlist&sid=0000000000004711";

  /**
   * Home auto switch test url.
   */
  private static final String HOMEAUTOSWITCH4 = "/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchstate&sid=0000000000004711";

  /**
   * Switch not off.
   */
  private static final String SWITCH_NOT_OFF = "Switch not off";

  /**
   * Duration url parameter.
   */
  private static final String DURATION = "&duration="; //$NON-NLS-1$

  /**
   * FB password.
   */
  private static final String PASSWORD = "TopSecret"; //$NON-NLS-1$

  /**
   * Device info list not as expected message.
   */
  private static final String DEVICE_INFO_LIST_NOT_AS_EXPECTED = "device info list not as expected";

  /**
   * Temperature not as expected message.
   */
  private static final String TEMPERATURE_NOT_AS_EXPECTED = "temperature not as expected";

  /**
   * Illegal argument exception expected.
   */
  private static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED = "Illegal argument exception expected"; //$NON-NLS-1$

  /**
   * Switches are not as expected.
   */
  private static final String SWITCHES_ARE_NOT_AS_EXPECTED = "Switches are not as expected";

  /**
   * Provider not found exception expected.
   */
  private static final String PROVIDER_NOT_FOUND_EXPECTED = "Provider not found exception expected"; //$NON-NLS-1$

  /**
   * Index out of bounds exception expected.
   */
  private static final String INDEX_OUT_OF_BOUNDS_EXPECTED = "Index out of bounds exception expected"; //$NON-NLS-1$

  /**
   * Switch not on.
   */
  private static final String SWITCH_NOT_ON = "Switch not on";

  /**
   * Switch stte not off.
   */
  private static final String SWITCH_STATE_NOT_OFF = "Switch state not off";

  /**
   * Wrong endtime.
   */
  private static final String WRONG_ENDTIME = "Wrong endtime";


  /**
   * Default constructor.
   */
  /* default */ AHASessionMiniTests()
   {
    super();
   }


  /**
   * Level provider.
   *
   * @return Stream of Level objects
   */
  private static Stream<Level> levelProvider()
   {
    return Stream.of(Level.of(0), Level.of(127), Level.of(255));
   }


  /**
   * Percent provider.
   *
   * @return Stream of Percent objects
   */
  private static Stream<Percent> percentProvider()
   {
    return Stream.of(Percent.of(0), Percent.of(50), Percent.of(100));
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
  private void createLogonMocks(final CloseableHttpClient mockHttpclient, final StatusLine mockStatusLineOk, final String resultDoc, final boolean successful) throws IOException, NoSuchAlgorithmException
   {
    final HttpEntity mockHttpEntity1 = mock(HttpEntity.class);
    when(mockHttpEntity1.isStreaming()).thenReturn(false);
    when(mockHttpEntity1.getContentType()).thenReturn(null);
    when(mockHttpEntity1.getContent()).thenReturn(new ByteArrayInputStream(resultDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity1.getContentLength()).thenReturn((long)resultDoc.length());

    final CloseableHttpResponse mockCloseableHttpResponse1 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse1.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse1.getEntity()).thenReturn(mockHttpEntity1);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?version=2")))).thenReturn(mockCloseableHttpResponse1); //$NON-NLS-1$

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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?response=deadbeef-" + new String(Hex.encodeHex(MessageDigest.getInstance("MD5").digest(("deadbeef-" + AHASessionMiniTests.FBPASSWORD).getBytes(Charset.forName("utf-16le"))))) + "&username=&version=2")))).thenReturn(mockCloseableHttpResponse2); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?sid=0000000000004711&version=2")))).thenReturn(mockCloseableHttpResponse3); //$NON-NLS-1$
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
  private void createLogoffMocks(final CloseableHttpClient mockHttpclient, final StatusLine mockStatusLineOk, final String resultDoc) throws IOException
   {
    final HttpEntity mockHttpEntity4 = mock(HttpEntity.class);
    when(mockHttpEntity4.isStreaming()).thenReturn(false);
    when(mockHttpEntity4.getContentType()).thenReturn(null);
    when(mockHttpEntity4.getContent()).thenReturn(new ByteArrayInputStream(resultDoc.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity4.getContentLength()).thenReturn((long)resultDoc.length());

    final CloseableHttpResponse mockCloseableHttpResponse4 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse4.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse4.getEntity()).thenReturn(mockHttpEntity4);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/login_sid.lua?logout=1&sid=0000000000004711&version=2")))).thenReturn(mockCloseableHttpResponse4); //$NON-NLS-1$
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
  /* default */ void testNewInstance1() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, ParserConfigurationException
   {
    final var ahasession = AHASessionMini.newInstance(AHASessionMiniTests.FBPASSWORD);
    assertNotNull(ahasession, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test newInstance.
   */
  @Test
  /* default */ void testNewInstance2()
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final var ahasession = AHASessionMini.newInstance(mockHttpclient, mockDocBuilder, AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    assertNotNull(ahasession, "newInstance failed!"); //$NON-NLS-1$
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final DocumentBuilder mockDocBuilder = mock(DocumentBuilder.class);
    final var ahasession = AHASessionMini.newInstance(mockHttpclient, mockDocBuilder, AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final String representation = ahasession.toString();
    assertEquals("AHASessionMini[hostname=" + AHASessionMiniTests.FRITZ_BOX + ", username=, sid=0000000000000000]", representation, "toString with unexpected result"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Logon/Logoff test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  @Disabled("TODO")
  /* default */ void testLogonLogoff() throws NoSuchAlgorithmException, ParserConfigurationException, IOException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$

    final boolean successLogon = ahasession.logon();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Logon failure test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testLogonFailure() throws NoSuchAlgorithmException, ParserConfigurationException, IOException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, false);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$

    final boolean successLogon = ahasession.logon();
    assertAll(
      () -> assertFalse(successLogon, "Logon success") //$NON-NLS-1$
    );
   }


  // TODO testLogon 2$ challenge


  /**
   * Logoff failure test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testLogoffFailure() throws NoSuchAlgorithmException, ParserConfigurationException, IOException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    final String testDoc2 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SessionInfo><SID>0000000000004711</SID><Challenge>deadbeef</Challenge><BlockTime>0</BlockTime><Rights></Rights></SessionInfo>"; //$NON-NLS-1$
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc2);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$

    final boolean successLogon = ahasession.logon();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertFalse(successLogoff, "Logoff success") //$NON-NLS-1$
    );
   }


  /**
   * Test for invalid session.
   *
   * @throws ParserConfigurationException Parser configuration exception
   *
   */
  @Test
  /* default */ void testHasValidSessionFalse() throws ParserConfigurationException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean valid = ahasession.hasValidSession();
    assertFalse(valid, "No valid session"); //$NON-NLS-1$
   }


  /**
   * Test for valid session.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException  IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testHasValidSessionTrue() throws ParserConfigurationException, NoSuchAlgorithmException, IOException, InvalidKeyException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final boolean valid = ahasession.hasValidSession();
    assertTrue(valid, "No valid session"); //$NON-NLS-1$
   }


  /**
   * Get switch list test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser cofiguration exception
   * @throws SAXException  SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchList1() throws IOException, NoSuchAlgorithmException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(HOMEAUTOSWITCH3)))).thenReturn(mockCloseableHttpResponse5);

    final List<AIN> results = new ArrayList<>();
    results.add(AIN.of(AHASessionMiniTests.AIN1));
    results.add(AIN.of("000000000002")); //$NON-NLS-1$
    results.add(AIN.of("000000000003")); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final List<AIN> switches = ahasession.getSwitchList();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(results, switches, SWITCHES_ARE_NOT_AS_EXPECTED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Get switch list test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser cofiguration exception
   * @throws SAXException  SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchList2() throws IOException, NoSuchAlgorithmException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(HOMEAUTOSWITCH3)))).thenReturn(mockCloseableHttpResponse5);

    final List<AIN> results = new ArrayList<>();
    results.add(AIN.of(AHASessionMiniTests.AIN1));

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final List<AIN> switches = ahasession.getSwitchList();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(results, switches, SWITCHES_ARE_NOT_AS_EXPECTED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Get switch list empty test.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ParserConfigurationException Parser cofiguration exception
   * @throws SAXException  SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchListEmpty() throws IOException, NoSuchAlgorithmException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(HOMEAUTOSWITCH3)))).thenReturn(mockCloseableHttpResponse5);

    final List<AIN> results = new ArrayList<>();

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final List<AIN> switches = ahasession.getSwitchList();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(results, switches, SWITCHES_ARE_NOT_AS_EXPECTED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set switch on.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchOn() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = AHASessionMiniTests.TEST_CONTENT_1;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH1)))).thenReturn(mockCloseableHttpResponse5);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOn(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(result, SWITCH_NOT_ON),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set switch on with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchOnFailure1() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final StatusLine mockStatusLineFailure = mock(StatusLine.class);
    when(mockStatusLineFailure.getStatusCode()).thenReturn(HttpURLConnection.HTTP_FORBIDDEN);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = AHASessionMiniTests.TEST_CONTENT_1;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineFailure);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH1)))).thenReturn(mockCloseableHttpResponse5);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    assertThrows(IOException.class, () ->
     {
      /* final boolean result = */ ahasession.setSwitchOn(AIN.of(AHASessionMiniTests.AIN1));
     }, "IO exception expected" //$NON-NLS-1$
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchOnFailure2() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH1)))).thenReturn(mockCloseableHttpResponse5);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOn(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertFalse(result, "Switch on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set switch off.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchOff() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH2)))).thenReturn(mockCloseableHttpResponse6);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(result, AHASessionMiniTests.SWITCH_NOT_OFF),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set switch off with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchOffFailure() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = LINEBREAK;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH2)))).thenReturn(mockCloseableHttpResponse6);

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean result = ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertFalse(result, "Switch off"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set switch toggle.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchToggle() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH2)))).thenReturn(mockCloseableHttpResponse6);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = AHASessionMiniTests.TEST_CONTENT_1;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchtoggle&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean resultOff1 = ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean resultToggle1 = ahasession.setSwitchToggle(AIN.of(AHASessionMiniTests.AIN1));
    /* final boolean resultOff2 = */ ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(resultOff1, AHASessionMiniTests.SWITCH_NOT_OFF),
      () -> assertTrue(resultToggle1, SWITCH_NOT_ON),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set switch toggle with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetSwitchToggleFailure() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH2)))).thenReturn(mockCloseableHttpResponse6);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setswitchtoggle&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean resultOff1 = ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean resultToggle1 = ahasession.setSwitchToggle(AIN.of(AHASessionMiniTests.AIN1));
    /* final boolean resultOff2 = */ ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(resultOff1, AHASessionMiniTests.SWITCH_NOT_OFF),
      () -> assertFalse(resultToggle1, "Switch on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test get switch state.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchState() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = AHASessionMiniTests.TEST_CONTENT_1;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH1)))).thenReturn(mockCloseableHttpResponse6);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = AHASessionMiniTests.TEST_CONTENT_1;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(HOMEAUTOSWITCH4)))).thenReturn(mockCloseableHttpResponse7);

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean resultOn = ahasession.setSwitchOn(AIN.of(AHASessionMiniTests.AIN1));
    final boolean resultState = ahasession.getSwitchState(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(resultOn, SWITCH_NOT_ON),
      () -> assertTrue(resultState, "Switch state not on"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"inval\n", "inval"})
  /* default */ void testGetSwitchStateFailure1(final String inval) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH2)))).thenReturn(mockCloseableHttpResponse6);

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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(HOMEAUTOSWITCH4)))).thenReturn(mockCloseableHttpResponse7);

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    /* final boolean resultOff = */ ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(ProviderNotFoundException.class, () ->
     {
      /* final boolean resultState = */ ahasession.getSwitchState(ain);
     }, PROVIDER_NOT_FOUND_EXPECTED
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchStateFailure2() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity6 = mock(HttpEntity.class);
    when(mockHttpEntity6.isStreaming()).thenReturn(false);

    final String testDoc6 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity6.getContentType()).thenReturn(null);
    when(mockHttpEntity6.getContent()).thenReturn(new ByteArrayInputStream(testDoc6.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity6.getContentLength()).thenReturn((long)testDoc6.length());

    final CloseableHttpResponse mockCloseableHttpResponse6 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse6.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse6.getEntity()).thenReturn(mockHttpEntity6);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(AHASessionMiniTests.HOMEAUTOSWITCH2)))).thenReturn(mockCloseableHttpResponse6);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher(HOMEAUTOSWITCH4)))).thenReturn(mockCloseableHttpResponse7);

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean resultOff = ahasession.setSwitchOff(AIN.of(AHASessionMiniTests.AIN1));
    final boolean resultState = ahasession.getSwitchState(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(resultOff, AHASessionMiniTests.SWITCH_NOT_OFF),
      () -> assertFalse(resultState, SWITCH_STATE_NOT_OFF),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test is switch present.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testIsSwitchPresent() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = AHASessionMiniTests.TEST_CONTENT_1;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchpresent&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean present = ahasession.isSwitchPresent(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(present, SWITCH_STATE_NOT_OFF),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test is switch present with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testIsSwitchPresentFailure() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=getswitchpresent&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final boolean present = ahasession.isSwitchPresent(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertFalse(present, SWITCH_STATE_NOT_OFF),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"10150\n", "1"})
  /* default */ void testGetSwitchPower(final String powerValue) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final Power power = ahasession.getSwitchPower(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(Long.parseLong(powerValue.replace(LINEBREAK, "")), power.longValue(), "Switch power not as expected"), //$NON-NLS-1$ //$NON-NLS-2$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test get switch power with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchPowerFailure() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(ProviderNotFoundException.class, () ->
     {
      /* final Power power = */ ahasession.getSwitchPower(ain);
     }, PROVIDER_NOT_FOUND_EXPECTED
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"75519\n", "1"})
  /* default */ void testGetSwitchEnergy(final String energyValue) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final Energy energy = ahasession.getSwitchEnergy(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(Long.parseLong(energyValue.replace(LINEBREAK, "")), energy.longValue(), "Switch power not as expected"), //$NON-NLS-1$ //$NON-NLS-2$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test get switch energy with failure.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetSwitchEnergyFailure() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(ProviderNotFoundException.class, () ->
     {
      /* final Energy energy = */ ahasession.getSwitchEnergy(ain);
     }, PROVIDER_NOT_FOUND_EXPECTED
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"01 office\n", LINEBREAK, ""})
  /* default */ void testGetSwitchName(final String name) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final String switchName = ahasession.getSwitchName(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(name.replace(LINEBREAK, ""), switchName, "Switch name not as expected"), //$NON-NLS-1$ //$NON-NLS-2$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test get device list infos.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetDeviceListInfos() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<devicelist version=\"1\" fwversion=\"8.00\"><device identifier=\"00000 0000001\" id=\"1\" functionbitmask=\"0\" fwversion=\"1.0\" manufacturer=\"TEST\" productname=\"Test product one\"></device></devicelist>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?sid=0000000000004711&switchcmd=getdevicelistinfos")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    List<Device> expected = new ArrayList<>();
    Device device = Device.of(AIN.of("000000000001"), 1, EnumSet.noneOf(Functions.class), Version.of("1.0"), "TEST", "Test product one", false, false, null, false, null, null, null, null, null, null, null, new ArrayList<>(), null, null, null, null, null, null);
    expected.add(device);

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final List<Device> devices = ahasession.getDeviceListInfos();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(expected, devices, AHASessionMiniTests.DEVICE_INFO_LIST_NOT_AS_EXPECTED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test get temperature.
   *
   * @param temperatureValue Temperature test value
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"182\n"})
  /* default */ void testGetTemperature(final String temperatureValue) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final TemperatureCelsius temperature = ahasession.getTemperature(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(Long.parseLong(temperatureValue.replace(LINEBREAK, "")), temperature.longValue(), AHASessionMiniTests.TEMPERATURE_NOT_AS_EXPECTED), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {LINEBREAK, ""})
  /* default */ void testGetTemperatureFailure(final String temperatureValue) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(NumberFormatException.class, () ->
     {
      /* final Temperature temperature = */ ahasession.getTemperature(ain);
     }, "Number format exception expected" //$NON-NLS-1$
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"40\n", "253\n", "254\n"})
  /* default */ void testGetHkrtSoll(final String soll) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final TemperatureCelsius temperature = ahasession.getHkrtSoll(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> {
        long expected = (Long.parseLong(soll.replace(LINEBREAK, "")) * 10) / 2; //$NON-NLS-1$
        if (expected == ((253 * 10) / 2))
         {
          expected = 0;
         }
        else if (expected == ((254 * 10) / 2))
         {
          expected = 300;
         }
        assertEquals(expected, temperature.longValue(), AHASessionMiniTests.TEMPERATURE_NOT_AS_EXPECTED);
       },
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {LINEBREAK, ""})
  /* default */ void testGetHkrtSollFailure(final String soll) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final var ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(NumberFormatException.class, () ->
     {
      /* final Temperature temperature = */ ahasession.getHkrtSoll(ain);
     }, "Number format exception expected" //$NON-NLS-1$
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"40\n"})
  /* default */ void testGetHkrKomfort(final String komfort) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final TemperatureCelsius temperature = ahasession.getHkrKomfort(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals((Long.parseLong(komfort.replace(LINEBREAK, "")) * 10) / 2, temperature.longValue(), AHASessionMiniTests.TEMPERATURE_NOT_AS_EXPECTED), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"34\n"})
  /* default */ void testGetHkrAbsenk(final String absenk) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final TemperatureCelsius temperature = ahasession.getHkrAbsenk(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals((Long.parseLong(absenk.replace(LINEBREAK, "")) * 10) / 2, temperature.longValue(), AHASessionMiniTests.TEMPERATURE_NOT_AS_EXPECTED), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 300, 80, 280})
  /* default */ void testSetHkrtSoll(final long temperature) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&param=" + ((temperature == 0) ? "253" : (temperature == 300 ? "254" : ((temperature * 2) / 10))) + "&switchcmd=sethkrtsoll&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$ //$NON-NLS-2$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setHkrtSoll(AIN.of(AHASessionMiniTests.AIN1), TemperatureCelsius.of(temperature));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(strings = {"75", "285"})
  /* default */ void testSetHkrtSollFailure(final String temperature) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=sethkrtsoll&param=" + temperature + SID4711)))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    final var temp = TemperatureCelsius.of(temperature);
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      ahasession.setHkrtSoll(ain, temp);
     }, INDEX_OUT_OF_BOUNDS_EXPECTED
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
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetBasicDeviceStats() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&sid=0000000000004711&switchcmd=getbasicdevicestats")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final Quintet<SortedMap<UnixTimestamp, TemperatureCelsius>, SortedMap<UnixTimestamp, Percent>, SortedMap<UnixTimestamp, Voltage>, SortedMap<UnixTimestamp, Power>, SortedMap<UnixTimestamp, Energy>> devicestats = ahasession.getBasicDeviceStats(AIN.of(AHASessionMiniTests.AIN1));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertNotNull(devicestats, AHASessionMiniTests.DEVICE_INFO_LIST_NOT_AS_EXPECTED), // TODO details
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test get template list infos.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetTemplateListInfos() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
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

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?sid=0000000000004711&switchcmd=gettemplatelistinfos")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final List<Template> templates = ahasession.getTemplateListInfos();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertNotNull(templates, AHASessionMiniTests.DEVICE_INFO_LIST_NOT_AS_EXPECTED), // TODO in deep
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test apply template.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testApplyTemplate() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=tmp000000-000000000&switchcmd=applytemplate&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.applyTemplate(AIN.of("tmp000000-000000000")); //$NON-NLS-1$
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set simple on off.
   *
   * @param onoff 0: off; 1: on; 2: toggle
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @EnumSource(HandleOnOff.class)
  /* default */ void testSetSimpleOnOff(final HandleOnOff onoff) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&onoff=" + onoff.ordinal() + "&switchcmd=setsimpleonoff&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setSimpleOnOff(AIN.of(AHASessionMiniTests.AIN1), onoff);
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set level.
   *
   * @param level 0-255
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @MethodSource("levelProvider")
  /* default */ void testSetLevel(final Level level) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&level=" + level.intValue() + "&switchcmd=setlevel&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setLevel(AIN.of(AHASessionMiniTests.AIN1), level);
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set level percentage.
   *
   * @param level 0-100
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @MethodSource("percentProvider")
  /* default */ void testSetLevelPercentage(final Percent level) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&level=" + level.intValue() + "&switchcmd=setlevelpercentage&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setLevelPercentage(AIN.of(AHASessionMiniTests.AIN1), level);
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set color.
   *
   * @param hue 0-359 degrees
   * @param saturation 0-255 (0% - 100%)
   * @param duration Duration of change in 100ms, 0 means immediately
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @CsvSource({"0, 0, 0", "359, 255, 10"})
  /* default */ void testSetColor(final int hue, final int saturation, final int duration) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001" + AHASessionMiniTests.DURATION + duration + "&hue=" + hue + "&saturation=" + saturation + "&switchcmd=setcolor&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setColor(AIN.of(AHASessionMiniTests.AIN1), Hue.of(hue), Saturation.of(saturation), DurationMS100.of(duration));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set color with failure.
   *
   * @param hue 0-359 degrees
   * @param saturation 0-255 (0% - 100%)
   * @param duration Duration of change in 100ms, 0 means immediately
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @CsvSource({"-1, 0, 0", "360, 0, 0", "0, -1, 0", "0, 256, 0", "0, 0, -1"})
  /* default */ void testSetColorFailure(final int hue, final int saturation, final int duration) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setcolor&hue=" + hue + "&saturation=" + saturation + AHASessionMiniTests.DURATION + duration + SID4711)))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(RuntimeException.class, () -> // IllegalArgumentException, IndexOutOfBoundsException
     {
      ahasession.setColor(ain, Hue.of(hue), Saturation.of(saturation), DurationMS100.of(duration));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test set color temperature.
   *
   * @param temperature In kelvin 2700 - 6500
   * @param duration Duration of change in 100ms, 0 means immediately
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @CsvSource({"2700, 0", "6500, 10"})
  /* default */ void testSetColorTemperature(final int temperature, final int duration) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001" + AHASessionMiniTests.DURATION + duration + "&switchcmd=setcolortemperature&temperature=" + temperature + SID4711)))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setColorTemperature(AIN.of(AHASessionMiniTests.AIN1), TemperatureKelvin.of(temperature), DurationMS100.of(duration));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set color temperature with failure.
   *
   * @param temperature In kelvin 2700 - 6500
   * @param duration Duration of change in 100ms, 0 means immediately
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @CsvSource({"2699, 0", "6501, 0", "2700, -1"})
  /* default */ void testSetColorTemperatureFailure(final int temperature, final int duration) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    final HttpEntity mockHttpEntity5 = mock(HttpEntity.class);
    when(mockHttpEntity5.isStreaming()).thenReturn(false);

    final String testDoc5 = LINEBREAK;
    when(mockHttpEntity5.getContentType()).thenReturn(null);
    when(mockHttpEntity5.getContent()).thenReturn(new ByteArrayInputStream(testDoc5.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity5.getContentLength()).thenReturn((long)testDoc5.length());

    final CloseableHttpResponse mockCloseableHttpResponse5 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse5.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse5.getEntity()).thenReturn(mockHttpEntity5);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setcolortemperature&temperature=" + temperature + AHASessionMiniTests.DURATION + duration + SID4711)))).thenReturn(mockCloseableHttpResponse5); //$NON-NLS-1$

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(RuntimeException.class, () -> // IllegalArgumentException IndexOutOfBoundsException
     {
      ahasession.setColorTemperature(ain, TemperatureKelvin.of(temperature), DurationMS100.of(duration));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test get color defaults.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testGetColorDefaults() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<colordefaults></colordefaults>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?sid=0000000000004711&switchcmd=getcolordefaults")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final Pair<List<Hs>, List<TemperatureKelvin>> result = ahasession.getColorDefaults();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertNotNull(result, AHASessionMiniTests.DEVICE_INFO_LIST_NOT_AS_EXPECTED), // TODO deeper
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set hkr boost with 0 as endtime.
   *
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @Test
  /* default */ void testSetHkrBoost0() throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = AHASessionMiniTests.TEST_CONTENT_0;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&endtimestamp=0&switchcmd=sethkrboost&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final long endtime = ahasession.setHkrBoost(AIN.of(AHASessionMiniTests.AIN1), EndTimestamp.of(Seconds.of(0)));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(0, endtime, WRONG_ENDTIME),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set hkr boost with new plus seconds as endtime.
   *
   * @param seconds Seconds to add to now
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 3600, 86400})
  /* default */ void testSetHkrBoostNowPlusSeconds(final long seconds) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final long boostend = (seconds == 0) ? 0 : (System.currentTimeMillis() / 1000L) + seconds;

    final String testDoc7 = String.valueOf(boostend) + LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&endtimestamp=" + boostend + "&switchcmd=sethkrboost&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final long endtime = ahasession.setHkrBoost(AIN.of(AHASessionMiniTests.AIN1), EndTimestamp.of(Seconds.of(boostend)));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(boostend, endtime, WRONG_ENDTIME),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set hkr boost with new plus seconds as endtime with failure.
   *
   * @param seconds Seconds to add to now
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(longs = {-1, 87000})
  /* default */ void testSetHkrBoostNowPlusSecondsFailure(final long seconds) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final long boostend = (System.currentTimeMillis() / 1000L) + seconds;

    final String testDoc7 = String.valueOf(boostend) + LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=sethkrboost&endtimestamp=" + boostend + SID4711)))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final long endtime = */ ahasession.setHkrBoost(ain, EndTimestamp.of(Seconds.of(boostend)));
     }, INDEX_OUT_OF_BOUNDS_EXPECTED
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test set hkr window open with new plus seconds as endtime.
   *
   * @param seconds Seconds to add to now
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 3600, 86400})
  /* default */ void testSetHkrWindowOpenNowPlusSeconds(final long seconds) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final long openend = (seconds == 0) ? 0 : (System.currentTimeMillis() / 1000L) + seconds;

    final String testDoc7 = String.valueOf(openend) + LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&endtimestamp=" + openend + "&switchcmd=sethkrwindowopen&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final long endtime = ahasession.setHkrWindowOpen(AIN.of(AHASessionMiniTests.AIN1), EndTimestamp.of(Seconds.of(openend)));
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals(openend, endtime, WRONG_ENDTIME),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Test set hkr window open with new plus seconds as endtime with failure.
   *
   * @param seconds Seconds to add to now
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @ValueSource(longs = {-1, 87000})
  /* default */ void testSetHkrWindowOpenNowPlusSecondsFailure(final long seconds) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final long openend = (System.currentTimeMillis() / 1000L) + seconds;

    final String testDoc7 = String.valueOf(openend) + LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=sethkrwindowopen&endtimestamp=" + openend + SID4711)))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    /* final boolean successLogon = */ ahasession.logon();
    final var ain = AIN.of(AHASessionMiniTests.AIN1);
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final long endtime = */ ahasession.setHkrWindowOpen(ain, EndTimestamp.of(Seconds.of(openend)));
     }, INDEX_OUT_OF_BOUNDS_EXPECTED
    );
    /* final boolean successLogoff = */ ahasession.logoff();
   }


  /**
   * Test set blind.
   *
   * @param target HandleBlind: CLOSE, OPEN, STOP
   * @throws IOException IO exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws ClientProtocolException Client protocol exception
   * @throws ParserConfigurationException Parser configuration exception
   * @throws SAXException SAX exception
   * @throws InvalidKeyException Invalid key exception
   */
  @ParameterizedTest
  @EnumSource(HandleBlind.class)
  /* default */ void testSetBlind(final HandleBlind target) throws NoSuchAlgorithmException, IOException, ParserConfigurationException, SAXException, InvalidKeyException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&switchcmd=setblind&target=" + target.name().toLowerCase(Locale.getDefault()) + SID4711)))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setBlind(AIN.of(AHASessionMiniTests.AIN1), target);
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals("CLOSE".equals(target.name()) ? 0 : ("OPEN".equals(target.name()) ? 1 : 2), target.getAction(), "Wrong action"), //$NON-NLS-1$
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Set name test.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws InvalidKeyException Invalid key exception
   * @throws NoSuchAlgorithmException No such algorithmE exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   */
  @Test
  /* default */ void testSetName() throws ParserConfigurationException, InvalidKeyException, NoSuchAlgorithmException, IOException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?ain=000000000001&name=" + "&switchcmd=setname&sid=0000000000004711")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.setName(AIN.of(AIN1), ""); // name = 40, > 40
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      // TODO
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Start ule subscription test.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws InvalidKeyException Invalid key exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   */
  @Test
  /* default */ void testStartUleSubscription() throws ParserConfigurationException, InvalidKeyException, NoSuchAlgorithmException, IOException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = LINEBREAK;
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?switchcmd=startulesubscription" + SID4711)))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    ahasession.startUleSubscription();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      // TODO
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  /**
   * Get subscription state test.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws InvalidKeyException Invalid key exception
   * @throws NoSuchAlgorithmException No such algorithm exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   */
  @Test
  /* default */ void testGetSubscriptionState() throws ParserConfigurationException, InvalidKeyException, NoSuchAlgorithmException, IOException, SAXException
   {
    final CloseableHttpClient mockHttpclient = mock(CloseableHttpClient.class);
    final StatusLine mockStatusLineOk = mock(StatusLine.class);
    when(mockStatusLineOk.getStatusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    final String testDoc1 = AHASessionMiniTests.MIN_SESSION;
    createLogonMocks(mockHttpclient, mockStatusLineOk, testDoc1, true);
    createLogoffMocks(mockHttpclient, mockStatusLineOk, testDoc1);

    // ----------

    final HttpEntity mockHttpEntity7 = mock(HttpEntity.class);
    when(mockHttpEntity7.isStreaming()).thenReturn(false);

    final String testDoc7 = "<state code=\"3\"><latestain>000000000001</latestain></state>\n"; //$NON-NLS-1$
    when(mockHttpEntity7.getContentType()).thenReturn(null);
    when(mockHttpEntity7.getContent()).thenReturn(new ByteArrayInputStream(testDoc7.getBytes(StandardCharsets.UTF_8)));
    when(mockHttpEntity7.getContentLength()).thenReturn((long)testDoc7.length());

    final CloseableHttpResponse mockCloseableHttpResponse7 = mock(CloseableHttpResponse.class);
    when(mockCloseableHttpResponse7.getStatusLine()).thenReturn(mockStatusLineOk);
    when(mockCloseableHttpResponse7.getEntity()).thenReturn(mockHttpEntity7);

    when(mockHttpclient.execute(argThat(new HttpGetMatcher("/webservices/homeautoswitch.lua?sid=0000000000004711&switchcmd=getsubscriptionstate")))).thenReturn(mockCloseableHttpResponse7); //$NON-NLS-1$

    // ----------

    final AHASessionMini ahasession = AHASessionMini.newInstance(mockHttpclient, getDocBuilder(), AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.FBPASSWORD); //$NON-NLS-1$
    final boolean successLogon = ahasession.logon();
    final SubscriptionState state = ahasession.getSubscriptionState();
    final boolean successLogoff = ahasession.logoff();
    assertAll(
      () -> assertTrue(successLogon, AHASessionMiniTests.LOGON_FAILED),
      () -> assertEquals("OTHER_ERROR", state.subscriptionCodeValue().stringValue(), "SubscriptionCode not as expected"),
      () -> assertEquals(AIN1, state.ainValue().stringValue(), "AIN not as expected"),
      () -> assertTrue(successLogoff, AHASessionMiniTests.LOGOFF_FAILED)
    );
   }


  // TODO Document getTriggerListInfos()

  // TODO Document getDeviceInfos(final AIN ain)
  // TODO void setUnmappedColor(final AIN ain, final int hue, final int saturation, final int duration)
  // TODO void setMetaData(final AIN ain, final String metadata)
  // TODO setTriggerActive(final AIN ain, final boolean active)
  // TODO void addColorLevelTemplate(final String name, final int levelPercentage, final int hue, final int saturation, final int temperatureKelvin, final boolean colorpreset, final AIN ... ains)


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    final SID sid1 = SID.of("0000000000000000");
    final SID sid2 = SID.of("0000000000000001");
    final Hostname hostname1 = Hostname.of("fritz.box");
    final Hostname hostname2 = Hostname.of("fritz2.box");
    final Port port1 = Port.of(443);
    final Port port2 = Port.of(80);
    final Username username1 = Username.of("user1");
    final Username username2 = Username.of("user2");
    final Password password1 = Password.of("password1");
    final Password password2 = Password.of("password2");
    EqualsVerifier.forClass(AHASessionMini.class).set(Mode.skipMockito()).withNonnullFields("docBuilder", "httpclient", "hostname", "port", "username", "password", "sid", "timeoutThread", "LOGGER", "TIMEOUT", "INVAL", "INVALID", "SESSIONID", "AIN_STR", "SHA256").withIgnoredFields("docBuilder", "httpclient", "lastAccess", "timeoutThread").withPrefabValues(SID.class, sid1, sid2).withPrefabValues(Hostname.class, hostname1, hostname2).withPrefabValues(Port.class, port1, port2).withPrefabValues(Username.class, username1, username2).withPrefabValues(Password.class, password1, password2).suppress(Warning.NONFINAL_FIELDS).verify();
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
    final AHASessionMini session1 = AHASessionMini.newInstance(AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.PASSWORD); //$NON-NLS-1$
    final AHASessionMini session2 = AHASessionMini.newInstance(AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.PASSWORD); //$NON-NLS-1$
    final AHASessionMini session3 = AHASessionMini.newInstance("fritz2.box", 443, "", "TopSecret2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session4 = AHASessionMini.newInstance("fritz3.box", 443, "", "TopSecret3"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    final AHASessionMini session5 = AHASessionMini.newInstance(AHASessionMiniTests.FRITZ_BOX, 443, "", AHASessionMiniTests.PASSWORD); //$NON-NLS-1$
    final AHASessionMini session6 = AHASessionMini.newInstance(AHASessionMiniTests.FRITZ_BOX, 443, "admin", AHASessionMiniTests.PASSWORD); //$NON-NLS-1$
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
   * Security test.
   *
   * @throws ParserConfigurationException Parser configuration exception
   * @throws IOException IO exception
   * @throws SAXException SAX exception
   * @throws TransformerException Transformer exception
   */
  @Test
  @Disabled("TODO")
  /* default */ void testSecurity() throws ParserConfigurationException, SAXException, IOException, TransformerException
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
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true); //$NON-NLS-1$
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false);
    // factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true); //$NON-NLS-1$
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
    // factory.setFeature(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
    // factory.setFeature("http://xml.org/sax/features/external-general-entities", true); //$NON-NLS-1$
    // factory.setFeature("http://xml.org/sax/features/external-parameter-entities", true); //$NON-NLS-1$
    // factory.setXIncludeAware(false);
    // factory.setExpandEntityReferences(false);
    // factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    final DocumentBuilder docBuilder = factory.newDocumentBuilder();
    final Document doc = docBuilder.parse(stream);
    if (AHASessionMiniTests.LOGGER.isDebugEnabled())
     {
      AHASessionMiniTests.LOGGER.debug("XMLString: {}", TR64SessionMini.docToString(doc)); //$NON-NLS-1$
     }
    assertNotNull(doc, "Dummy");
   }


  /**
   * HttpGet matcher.
   */
  private static class HttpGetMatcher implements ArgumentMatcher<HttpGet>
   {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(HttpGetMatcher.class);

    /**
     * URI path to match.
     */
    private final String path;


    /**
     * Constructor.
     *
     * @param path URI path to match
     */
    /* default */ HttpGetMatcher(final String path)
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
      if (HttpGetMatcher.LOGGER.isDebugEnabled())
       {
        HttpGetMatcher.LOGGER.debug("rpath: {}", rpath); //$NON-NLS-1$
       }
      return path.equals(rpath);
     }
   }

 }
