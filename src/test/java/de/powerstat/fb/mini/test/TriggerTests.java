/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.Trigger;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Trigger tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class TriggerTests
 {
  /**
   * Default constructor.
   */
  /* default */ TriggerTests()
   {
    super();
   }


  /**
   * Test correct Trigger.
   */
  @Test
  /* default */ void testTriggerFactory1()
   {
    final Trigger cleanTrigger = Trigger.of(AIN.of("000000000000"), "abc", true);
    assertAll("testFactory", //$NON-NLS-1$
      () -> assertEquals(AIN.of("000000000000"), cleanTrigger.ainValue(), "ain not as expected"), //$NON-NLS-1$
      () -> assertEquals("abc", cleanTrigger.stringValue(), "name not as expected"), //$NON-NLS-1$
      () -> assertTrue(cleanTrigger.isActive(), "active state not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test AIN with wrong lengths.
   *
   * @param ain AIN
   */
  @Test
  /* default */ void testTriggerFactoryFail1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Trigger cleanTrigger = */ Trigger.of(null, "abc", true);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test AIN with wrong lengths.
   *
   * @param ain AIN
   */
  @Test
  /* default */ void testTriggerFactoryFail2()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Trigger cleanTrigger = */ Trigger.of(AIN.of("000000000000"), null, true);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test AIN with wrong lengths.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {"", "12345678901234567890123456789012345678901"})
  /* default */ void testTriggerFactoryFail2(final String name)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Trigger cleanTrigger = */ Trigger.of(AIN.of("000000000000"), name, true);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Trigger trigger = Trigger.of(AIN.of("000000000000"), "abc", true); //$NON-NLS-1$
    assertEquals("abc", trigger.stringValue(), "Trigger not as expected"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Trigger trigger1 = Trigger.of(AIN.of("000000000000"), "abc", true); //$NON-NLS-1$
    final Trigger trigger2 = Trigger.of(AIN.of("000000000000"), "abc", true); //$NON-NLS-1$
    final Trigger trigger3 = Trigger.of(AIN.of("000000000001"), "def", false); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(trigger1.hashCode(), trigger2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(trigger1.hashCode(), trigger3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final Trigger trigger1 = Trigger.of(AIN.of("000000000000"), "abc", true);
    final Trigger trigger2 = Trigger.of(AIN.of("000000000000"), "abc", true);
    final Trigger trigger3 = Trigger.of(AIN.of("000000000001"), "def", false); //$NON-NLS-1$
    final Trigger trigger4 = Trigger.of(AIN.of("000000000000"), "abc", true);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(trigger1.equals(trigger1), "trigger11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(trigger1.equals(trigger2), "trigger12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(trigger2.equals(trigger1), "trigger21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(trigger2.equals(trigger4), "trigger24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(trigger1.equals(trigger4), "trigger14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(trigger1.equals(trigger3), "trigger13 are equal"), //$NON-NLS-1$
      () -> assertFalse(trigger3.equals(trigger1), "trigger31 are equal"), //$NON-NLS-1$
      () -> assertFalse(trigger1.equals(null), "trigger10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Trigger trigger = Trigger.of(AIN.of("000000000000"), "abc", true);
    assertEquals("Trigger[ain=AIN[ain=000000000000], name=abc, active=true]", trigger.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Trigger trigger1 = Trigger.of(AIN.of("000000000000"), "abc", true);
    final Trigger trigger2 = Trigger.of(AIN.of("000000000000"), "abc", true);
    final Trigger trigger3 = Trigger.of(AIN.of("000000000001"), "def", false); //$NON-NLS-1$
    final Trigger trigger4 = Trigger.of(AIN.of("000000000002"), "ghi", true); //$NON-NLS-1$
    final Trigger trigger5 = Trigger.of(AIN.of("000000000000"), "abc", true);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(trigger1.compareTo(trigger2) == -trigger2.compareTo(trigger1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(trigger1.compareTo(trigger3) == -trigger3.compareTo(trigger1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((trigger4.compareTo(trigger3) > 0) && (trigger3.compareTo(trigger1) > 0) && (trigger4.compareTo(trigger1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((trigger1.compareTo(trigger2) == 0) && (Math.abs(trigger1.compareTo(trigger5)) == Math.abs(trigger2.compareTo(trigger5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((trigger1.compareTo(trigger2) == 0) && trigger1.equals(trigger2), "equals") //$NON-NLS-1$
    );
   }

 }
