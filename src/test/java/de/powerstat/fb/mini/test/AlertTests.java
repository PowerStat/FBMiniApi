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

import de.powerstat.fb.mini.Alert;
import de.powerstat.fb.mini.Alert.AlertState;
import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Alert tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class AlertTests
 {
  /**
   * Alert not as expected.
   */
  private static final String ALERT_NOT_AS_EXPECTED = "Alert not as expected";

  /**
   * No error.
   */
  private static final String NO_ERROR = "NO_ERROR";


  /**
   * Default constructor.
   */
  /* default */ AlertTests()
   {
    super();
   }


  /**
   * Test correct Alert.
   */
  @Test
  /* default */ void testAlertCorrect1()
   {
    final Alert cleanAlert = Alert.of(Alert.AlertState.NO_ERROR, UnixTimestamp.of(Seconds.of(0)));
    assertEquals(NO_ERROR, cleanAlert.stringValue(), ALERT_NOT_AS_EXPECTED);
   }


  /**
   * Test correct Alert.
   */
  @Test
  /* default */ void testAlertCorrect2()
   {
    final Alert cleanAlert = Alert.of(AlertState.of(NO_ERROR), UnixTimestamp.of(Seconds.of(0)));
    assertEquals(NO_ERROR, cleanAlert.stringValue(), ALERT_NOT_AS_EXPECTED);
   }


  /**
   * Test correct Alert.
   */
  @Test
  /* default */ void testAlertCorrect3()
   {
    final Alert cleanAlert = Alert.of(AlertState.of(1), UnixTimestamp.of(Seconds.of(0)));
    assertEquals("BARRIER", cleanAlert.stringValue(), ALERT_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test AlertState.
   */
  @Test
  /* default */ void testAlertState1()
   {
    final AlertState state = AlertState.of(1);
    assertEquals(1, state.getAction(), "AlertState action not as expected"); //$NON-NLS-1$
   }


  /**
   * Test AlertState.
   */
  @Test
  /* default */ void testAlertState2()
   {
    final AlertState state = AlertState.of(NO_ERROR);
    assertEquals(NO_ERROR, state.stringValue(), "AlertState action not as expected"); //$NON-NLS-1$
   }


  /**
   * Test failure Alert.
   */
  @Test
  /* default */ void testAlertFailure1()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Alert cleanAlert = */ Alert.of(AlertState.of(99), UnixTimestamp.of(Seconds.of(0)));
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Alert alert = Alert.of(Alert.AlertState.NO_ERROR, UnixTimestamp.of(Seconds.of(0)));
    assertEquals(NO_ERROR, alert.stringValue(), ALERT_NOT_AS_EXPECTED);
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Alert.class).withNonnullFields("state", "lastalertchgtimestamp").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Alert alert = Alert.of(Alert.AlertState.NO_ERROR, UnixTimestamp.of(Seconds.of(0)));
    assertEquals("Alert[state=NO_ERROR, lastalertchgtimestamp=UnixTimestamp[seconds=Seconds[seconds=0]]]", alert.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Alert alert1 = Alert.of(Alert.AlertState.NO_ERROR, UnixTimestamp.of(Seconds.of(0)));
    final Alert alert2 = Alert.of(Alert.AlertState.NO_ERROR, UnixTimestamp.of(Seconds.of(0)));
    final Alert alert3 = Alert.of(Alert.AlertState.BARRIER, UnixTimestamp.of(Seconds.of(1)));
    final Alert alert4 = Alert.of(Alert.AlertState.OVERHEAT, UnixTimestamp.of(Seconds.of(2)));
    final Alert alert5 = Alert.of(Alert.AlertState.NO_ERROR, UnixTimestamp.of(Seconds.of(0)));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(alert1.compareTo(alert2) == -alert2.compareTo(alert1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(alert1.compareTo(alert3) == -alert3.compareTo(alert1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((alert4.compareTo(alert3) > 0) && (alert3.compareTo(alert1) > 0) && (alert4.compareTo(alert1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((alert1.compareTo(alert2) == 0) && (Math.abs(alert1.compareTo(alert5)) == Math.abs(alert2.compareTo(alert5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((alert1.compareTo(alert2) == 0) && alert1.equals(alert2), "equals") //$NON-NLS-1$
    );
   }

 }
