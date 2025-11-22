/*
 * Copyright (C) 2020-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Power;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Power tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class PowerTests
 {
  /**
   * Default constructor.
   */
  /* default */ PowerTests()
   {
    super();
   }


  /**
   * Is a power value.
   *
   * @param power Power in mW
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 1, 10150, -1})
  /* default */ void testIsPowerValue(final long power)
   {
    assertEquals(power, Power.of(power).longValue(), "Not a power value!"); //$NON-NLS-1$
   }


  /**
   * Is a power string value.
   */
  @Test
  /* default */ void testIsPowerString()
   {
    assertEquals(10, Power.of("10150").getPowerWatt(), "Not a power value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Power.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Power power = Power.of(1);
    assertEquals("Power[power=1]", power.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Power power1 = Power.of(1);
    final Power power2 = Power.of(1);
    final Power power3 = Power.of(2);
    final Power power4 = Power.of(3);
    final Power power5 = Power.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(power1.compareTo(power2) == -power2.compareTo(power1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(power1.compareTo(power3) == -power3.compareTo(power1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((power4.compareTo(power3) > 0) && (power3.compareTo(power1) > 0) && (power4.compareTo(power1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((power1.compareTo(power2) == 0) && (Math.abs(power1.compareTo(power5)) == Math.abs(power2.compareTo(power5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((power1.compareTo(power2) == 0) && power1.equals(power2), "equals") //$NON-NLS-1$
    );
   }

 }
