/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.HkrNextChange;
import de.powerstat.fb.mini.TemperatureCelsius;
import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Hkr net change tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class HkrNextChangeTests
 {
  /**
   * Default constructor.
   */
  /* default */ HkrNextChangeTests()
   {
    super();
   }


  /**
   * Test correct HkrNextChange.
   */
  @Test
  /* default */ void testHkrNextChangeCorrect()
   {
    final HkrNextChange cleanHkrNextChange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(200));
    assertEquals("0", cleanHkrNextChange.stringValue(), "HkrNextChange not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test wrong HkrNextChange.
   */
  @Test
  /* default */ void testHkrNextChangeWrong1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final HkrNextChange cleanHkrNextChange = */ HkrNextChange.of(null, TemperatureCelsius.of(200));
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong HkrNextChange.
   */
  @Test
  /* default */ void testHkrNextChangeWrong2()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final HkrNextChange cleanHkrNextChange = */ HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final HkrNextChange cleanHkrNextChange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(200));
    assertEquals("0", cleanHkrNextChange.stringValue(), "HkrNextChange not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(HkrNextChange.class).withNonnullFields("endperiod", "tchange").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final HkrNextChange hkrNextChange = HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(200));
    assertEquals("HkrNextChange[endperiod=UnixTimestamp[seconds=Seconds[seconds=0]], tchange=TemperatureCelsius[temperature=200]]", hkrNextChange.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final HkrNextChange hkrNextChange1 = HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(200));
    final HkrNextChange hkrNextChange2 = HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(200));
    final HkrNextChange hkrNextChange3 = HkrNextChange.of(UnixTimestamp.of(Seconds.of(1)), TemperatureCelsius.of(210));
    final HkrNextChange hkrNextChange4 = HkrNextChange.of(UnixTimestamp.of(Seconds.of(1)), TemperatureCelsius.of(220));
    final HkrNextChange hkrNextChange5 = HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(200));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(hkrNextChange1.compareTo(hkrNextChange2) == -hkrNextChange2.compareTo(hkrNextChange1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(hkrNextChange1.compareTo(hkrNextChange3) == -hkrNextChange3.compareTo(hkrNextChange1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((hkrNextChange4.compareTo(hkrNextChange3) > 0) && (hkrNextChange3.compareTo(hkrNextChange1) > 0) && (hkrNextChange4.compareTo(hkrNextChange1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((hkrNextChange1.compareTo(hkrNextChange2) == 0) && (Math.abs(hkrNextChange1.compareTo(hkrNextChange5)) == Math.abs(hkrNextChange2.compareTo(hkrNextChange5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((hkrNextChange1.compareTo(hkrNextChange2) == 0) && hkrNextChange1.equals(hkrNextChange2), "equals") //$NON-NLS-1$
    );
   }

 }
