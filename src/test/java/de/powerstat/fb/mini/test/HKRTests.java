/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import java.time.Instant;

import de.powerstat.fb.mini.HKR;
import de.powerstat.fb.mini.HkrErrorCodes;
import de.powerstat.fb.mini.HkrNextChange;
import de.powerstat.fb.mini.TemperatureCelsius;
import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.validation.values.Percent;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * HKR tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class HKRTests
 {
  /**
   * Null pointer exception expected.
   */
  private static final String NULL_POINTER_EXCEPTION_EXPECTED = "Null pointer exception expected"; //$NON-NLS-1$


  /**
   * Default constructor.
   */
  /* default */ HKRTests()
   {
    super();
   }


  /**
   * Test correct HKR.
   */
  @Test
  /* default */ void testHkrCorrect()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    final HKR cleanHkr = HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    assertEquals("", cleanHkr.stringValue(), "HKR not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test wrong HKR.
   */
  @Test
  /* default */ void testHkrWrong1()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = null;
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    assertThrows(NullPointerException.class, () ->
     {
      /* final HKR cleanHkr = */ HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong HKR.
   */
  @Test
  /* default */ void testHkrWrong2()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = null;
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    assertThrows(NullPointerException.class, () ->
     {
      /* final HKR cleanHkr = */ HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong HKR.
   */
  @Test
  /* default */ void testHkrWrong3()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = null;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    assertThrows(NullPointerException.class, () ->
     {
      /* final HKR cleanHkr = */ HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong HKR.
   */
  @Test
  /* default */ void testHkrWrong4()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    assertThrows(NullPointerException.class, () ->
     {
      /* final HKR cleanHkr = */ HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong HKR.
   */
  @Test
  /* default */ void testHkrWrong5()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = null;
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    assertThrows(NullPointerException.class, () ->
     {
      /* final HKR cleanHkr = */ HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final TemperatureCelsius tist = TemperatureCelsius.of(180);
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    final HKR cleanHkr = HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    assertEquals("180", cleanHkr.stringValue(), "HKR not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(HKR.class).withNonnullFields("absenk", "komfort", "errorcode", "battery", "nextchange").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final UnixTimestamp tstamp = UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600));
    final HkrNextChange nextchange = HkrNextChange.of(tstamp, TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    final HKR hkr = HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    assertEquals("HKR[tist=null, tsoll=null, absenk=TemperatureCelsius[temperature=180], komfort=TemperatureCelsius[temperature=200], lock=null, devicelock=null, errorcode=NO_ERROR, windowsopenactive=false, windowopenactiveendtime=null, boostactive=false, boostactiveendtime=null, batterylow=false, battery=Percent[percent=100], nextchange=HkrNextChange[endperiod=UnixTimestamp[seconds=Seconds[seconds=" + tstamp.longValue() + "]], tchange=TemperatureCelsius[temperature=200]], summeractive=false, holidayactive=false, adaptiveHeatingActive=false, adaptiveHeatingRunning=false]", hkr.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final TemperatureCelsius tist = null;
    final TemperatureCelsius tsoll = null;
    final TemperatureCelsius absenk = TemperatureCelsius.of(180);
    final TemperatureCelsius komfort = TemperatureCelsius.of(200);
    final Boolean lock = null;
    final Boolean devicelock = null;
    final HkrErrorCodes errorcode = HkrErrorCodes.NO_ERROR;
    final boolean windowsopenactive = false;
    final UnixTimestamp windowopenactiveendtime = null;
    final boolean boostactive = false;
    final UnixTimestamp boostactiveendtime = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(100);
    final HkrNextChange nextchange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(Instant.now().getEpochSecond() + 3600)), TemperatureCelsius.of(200));
    final boolean summeractive = false;
    final boolean holidayactive = false;
    final boolean adaptiveHeatingActive = false;
    final boolean adaptiveHeatingRunning = false;
    final HKR hkr1 = HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    final HKR hkr2 = HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    final HKR hkr3 = HKR.of(TemperatureCelsius.of(180), tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    final HKR hkr4 = HKR.of(TemperatureCelsius.of(200), tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    final HKR hkr5 = HKR.of(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(hkr1.compareTo(hkr2) == -hkr2.compareTo(hkr1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(hkr1.compareTo(hkr3) == -hkr3.compareTo(hkr1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((hkr4.compareTo(hkr3) > 0) && (hkr3.compareTo(hkr1) > 0) && (hkr4.compareTo(hkr1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((hkr1.compareTo(hkr2) == 0) && (Math.abs(hkr1.compareTo(hkr5)) == Math.abs(hkr2.compareTo(hkr5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((hkr1.compareTo(hkr2) == 0) && hkr1.equals(hkr2), "equals") //$NON-NLS-1$
    );
   }

 }
