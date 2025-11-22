/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.SubscriptionCode;
import de.powerstat.fb.mini.SubscriptionState;


/**
 * SubscriptionState tests.
 */
final class SubscriptionStateTests
 {
  /**
   * Subcription state not as expected.
   */
  private static final String SUBSCRIPTION_STATE_NOT_AS_EXPECTED = "SubscriptionState not as expected";

  /**
   * AIN zero.
   */
  private static final String AIN_ZERO = "000000000000";


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
    assertNotNull(SubscriptionState.of(SubscriptionCode.of(0), AIN.of(AIN_ZERO)), SUBSCRIPTION_STATE_NOT_AS_EXPECTED);
   }


  /**
   * SubscriptionState factory test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertNotNull(SubscriptionState.of(SubscriptionCode.of(0), null), SUBSCRIPTION_STATE_NOT_AS_EXPECTED);
   }


  /**
   * Test factory failed.
   */
  @Test
  /* default */ void testFactoryFailed()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final SubscriptionState cleanState = */ SubscriptionState.of(null, AIN.of(AIN_ZERO));
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * stringValue test.
   */
  @Test
  /* default */ void testStringValue()
   {
    assertEquals("NO_PROGRESS", SubscriptionState.of(SubscriptionCode.of(0), AIN.of(AIN_ZERO)).stringValue(), SUBSCRIPTION_STATE_NOT_AS_EXPECTED);
   }


  /**
   * codeValue test.
   */
  @Test
  /* default */ void testCodeValue()
   {
    final SubscriptionCode code = SubscriptionCode.of(0);
    assertEquals(code, SubscriptionState.of(code, AIN.of(AIN_ZERO)).subscriptionCodeValue(), SUBSCRIPTION_STATE_NOT_AS_EXPECTED);
   }


  /**
   * ainValue test.
   */
  @Test
  /* default */ void testAINValue()
   {
    final AIN ain = AIN.of(AIN_ZERO);
    assertEquals(ain, SubscriptionState.of(SubscriptionCode.of(0), ain).ainValue(), SUBSCRIPTION_STATE_NOT_AS_EXPECTED);
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(SubscriptionState.class).withNonnullFields("code", "latestain").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final SubscriptionState state = SubscriptionState.of(SubscriptionCode.of(0), AIN.of(AIN_ZERO));
    assertEquals("SubscriptionState[code=NO_PROGRESS, AIN[ain=000000000000]]", state.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final SubscriptionState state1 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of(AIN_ZERO));
    final SubscriptionState state2 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of(AIN_ZERO));
    final SubscriptionState state3 = SubscriptionState.of(SubscriptionCode.of(1), AIN.of("000000000001")); //$NON-NLS-1$
    final SubscriptionState state4 = SubscriptionState.of(SubscriptionCode.of(2), AIN.of("000000000002")); //$NON-NLS-1$
    final SubscriptionState state5 = SubscriptionState.of(SubscriptionCode.of(0), AIN.of(AIN_ZERO));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(state1.compareTo(state2) == -state2.compareTo(state1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(state1.compareTo(state3) == -state3.compareTo(state1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((state4.compareTo(state3) > 0) && (state3.compareTo(state1) > 0) && (state4.compareTo(state1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((state1.compareTo(state2) == 0) && (Math.abs(state1.compareTo(state5)) == Math.abs(state2.compareTo(state5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((state1.compareTo(state2) == 0) && state1.equals(state2), "equals") //$NON-NLS-1$
    );
   }

 }
