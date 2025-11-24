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

import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.Button;
import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Button tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class ButtonTests
 {
  /**
   * Test.
   */
  private static final String TEST = "Test";

  /**
   * AIN zero.
   */
  private static final String AIN_ZERO = "000000000000";


  /**
   * Default constructor.
   */
  /* default */ ButtonTests()
   {
    super();
   }


  /**
   * Test correct Button.
   */
  @Test
  /* default */ void testButtonCorrect1()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 0L, "1234567890123456789012345678901234567890", UnixTimestamp.of(Seconds.of(0L)));
    assertEquals("1234567890123456789012345678901234567890", cleanButton.stringValue(), "Button not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test correct Button.
   */
  @Test
  /* default */ void testButtonCorrect2()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 0L, null, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals(null, cleanButton.stringValue(), "Button not as expected"); //$NON-NLS-1$
   }


  /**
   * Test correct Button.
   */
  @Test
  /* default */ void testButtonFailure1()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Button cleanButton = */ Button.of(AIN.of(AIN_ZERO), -1L, TEST, UnixTimestamp.of(Seconds.of(0L)));
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test correct Button.
   */
  @Test
  /* default */ void testButtonFailure2()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Button cleanButton = */ Button.of(AIN.of(AIN_ZERO), 0L, "12345678901234567890123456789012345678901", UnixTimestamp.of(Seconds.of(0L)));
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getIdentifier.
   */
  @Test
  /* default */ void testGetIdentifier()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals(AIN_ZERO, cleanButton.getIdentifier().stringValue(), "getIdentifier not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getId.
   */
  @Test
  /* default */ void testGetId()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 1L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals(1L, cleanButton.getId(), "getId not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getName.
   */
  @Test
  /* default */ void testGetName()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals(TEST, cleanButton.getName(), "getName not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getLastPressedTimestamp.
   */
  @Test
  /* default */ void testGetLastPressedTimestamp()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals(0L, cleanButton.getLastPressedTimestamp().longValue(), "getLastPressedTimestamp not as expected"); //$NON-NLS-1$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Button cleanButton = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals(TEST, cleanButton.stringValue(), "sringValue not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Button.class).withNonnullFields("identifier", "name", "lastpressedtimestamp").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Button button = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertEquals("Button[identifier=AIN[ain=000000000000], id=0, name=Test ,lastpressedtimestamp=UnixTimestamp[seconds=Seconds[seconds=0]]]", button.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Button button1 = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    final Button button2 = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    final Button button3 = Button.of(AIN.of("000000000001"), 1L, "Test2", UnixTimestamp.of(Seconds.of(1L))); //$NON-NLS-1$
    final Button button4 = Button.of(AIN.of("000000000002"), 2L, "Test3", UnixTimestamp.of(Seconds.of(2L))); //$NON-NLS-1$
    final Button button5 = Button.of(AIN.of(AIN_ZERO), 0L, TEST, UnixTimestamp.of(Seconds.of(0L)));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(button1.compareTo(button2) == -button2.compareTo(button1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(button1.compareTo(button3) == -button3.compareTo(button1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((button4.compareTo(button3) > 0) && (button3.compareTo(button1) > 0) && (button4.compareTo(button1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((button1.compareTo(button2) == 0) && (Math.abs(button1.compareTo(button5)) == Math.abs(button2.compareTo(button5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((button1.compareTo(button2) == 0) && button1.equals(button2), "equals") //$NON-NLS-1$
    );
   }

 }
