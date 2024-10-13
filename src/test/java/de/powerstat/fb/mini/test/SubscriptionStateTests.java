/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.SubscriptionCode;
import de.powerstat.fb.mini.SubscriptionState;


/**
 * SubscriptionState tests.
 */
final class SubscriptionStateTests
 {
  /**
   * Default constructor.
   */
  /* default */ SubscriptionStateTests()
   {
    super();
   }


  /**
   * SubscriptionState factory test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertNotNull(SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000")), "SubscriptionState not as expected");
   }


  /**
   * SubscriptionState factory test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertNotNull(SubscriptionState.of(SubscriptionCode.of(0), null), "SubscriptionState not as expected");
   }


  /**
   * Test factory failed.
   */
  @Test
  /* default */ void testFactoryFailed()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final SubscriptionState cleanState = */ SubscriptionState.of(null, AIN.of("000000000000"));
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * stringValue test.
   */
  @Test
  /* default */ void testStringValue()
   {
    assertEquals("NO_PROGRESS", SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000")).stringValue(), "SubscriptionState not as expected");
   }


  /**
   * codeValue test.
   */
  @Test
  /* default */ void testCodeValue()
   {
    final SubscriptionCode code = SubscriptionCode.of(0);
    assertEquals(code, SubscriptionState.of(code, AIN.of("000000000000")).subscriptionCodeValue(), "SubscriptionState not as expected");
   }


  /**
   * ainValue test.
   */
  @Test
  /* default */ void testAINValue()
   {
    final AIN ain = AIN.of("000000000000");
    assertEquals(ain, SubscriptionState.of(SubscriptionCode.of(0), ain).ainValue(), "SubscriptionState not as expected");
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final SubscriptionState state1 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000"));
    final SubscriptionState state2 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000"));
    final SubscriptionState state3 = SubscriptionState.of(SubscriptionCode.of(1), AIN.of("000000000001")); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(state1.hashCode(), state2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(state1.hashCode(), state3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final SubscriptionState state1 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000"));
    final SubscriptionState state2 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000"));
    final SubscriptionState state3 = SubscriptionState.of(SubscriptionCode.of(1), AIN.of("000000000001")); //$NON-NLS-1$
    final SubscriptionState state4 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000"));
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(state1.equals(state1), "state11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(state1.equals(state2), "state12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(state2.equals(state1), "state21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(state2.equals(state4), "state24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(state1.equals(state4), "state14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(state1.equals(state3), "state13 are equal"), //$NON-NLS-1$
      () -> assertFalse(state3.equals(state1), "state31 are equal"), //$NON-NLS-1$
      () -> assertFalse(state1.equals(null), "state10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final SubscriptionState state = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000"));
    assertEquals("SubscriptionState[code=NO_PROGRESS, AIN[ain=000000000000]]", state.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final SubscriptionState state1 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000")); //$NON-NLS-1$
    final SubscriptionState state2 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000")); //$NON-NLS-1$
    final SubscriptionState state3 = SubscriptionState.of(SubscriptionCode.of(1), AIN.of("000000000001")); //$NON-NLS-1$
    final SubscriptionState state4 = SubscriptionState.of(SubscriptionCode.of(2), AIN.of("000000000002")); //$NON-NLS-1$
    final SubscriptionState state5 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of("000000000000")); //$NON-NLS-1$
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(state1.compareTo(state2) == -state2.compareTo(state1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(state1.compareTo(state3) == -state3.compareTo(state1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((state4.compareTo(state3) > 0) && (state3.compareTo(state1) > 0) && (state4.compareTo(state1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((state1.compareTo(state2) == 0) && (Math.abs(state1.compareTo(state5)) == Math.abs(state2.compareTo(state5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((state1.compareTo(state2) == 0) && state1.equals(state2), "equals") //$NON-NLS-1$
    );
   }

 }
