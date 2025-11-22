/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Blind;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Blind tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class BlindTests
 {
  /**
   * Default constructor.
   */
  /* default */ BlindTests()
   {
    super();
   }


  /**
   * Test correct Blind.
   */
  @Test
  /* default */ void testBlindCorrect()
   {
    final Blind cleanBlind = Blind.of(false, false);
    assertEquals("false", cleanBlind.stringValue(), "Blind not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Blind cleanBlind = Blind.of(false, false);
    assertEquals("false", cleanBlind.stringValue(), "Blind not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Blind.class).withNonnullFields("mode", "endpositionsset").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Blind blind = Blind.of(false, false);
    assertEquals("Blind[mode=false, endpositionsset=false]", blind.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Blind blind1 = Blind.of(false, false);
    final Blind blind2 = Blind.of(false, false);
    final Blind blind3 = Blind.of(true, false);
    final Blind blind4 = Blind.of(true, true);
    final Blind blind5 = Blind.of(false, false);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(blind1.compareTo(blind2) == -blind2.compareTo(blind1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(blind1.compareTo(blind3) == -blind3.compareTo(blind1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((blind4.compareTo(blind3) > 0) && (blind3.compareTo(blind1) > 0) && (blind4.compareTo(blind1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((blind1.compareTo(blind2) == 0) && (Math.abs(blind1.compareTo(blind5)) == Math.abs(blind2.compareTo(blind5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((blind1.compareTo(blind2) == 0) && blind1.equals(blind2), "equals") //$NON-NLS-1$
    );
   }

 }
