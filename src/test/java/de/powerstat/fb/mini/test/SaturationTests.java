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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.fb.mini.Saturation;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Saturation tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class SaturationTests
 {
  /**
   * Default constructor.
   */
  /* default */ SaturationTests()
   {
    super();
   }


  /**
   * Is a saturation value.
   *
   * @param saturation Saturation
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 127, 255})
  /* default */ void testIsSaturationValue(final int saturation)
   {
    assertEquals(saturation, Saturation.of(saturation).intValue(), "Not a saturation value!"); //$NON-NLS-1$
   }


  /**
   * Is not a saturation value.
   *
   * @param saturation Saturation (0-255)
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 256})
  /* default */ void testIsNotAsaturationValue(final int saturation)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Saturation saturation = */ Saturation.of(saturation);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a saturation string value.
   */
  @Test
  /* default */ void testIsSaturationString()
   {
    assertEquals(127, Saturation.of("127").intValue(), "Not a saturation value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Saturation saturation1 = Saturation.of(1);
    final Saturation saturation2 = Saturation.of(1);
    final Saturation saturation3 = Saturation.of(2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(saturation1.hashCode(), saturation2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(saturation1.hashCode(), saturation3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final Saturation saturation1 = Saturation.of(1);
    final Saturation saturation2 = Saturation.of(1);
    final Saturation saturation3 = Saturation.of(2);
    final Saturation saturation4 = Saturation.of(1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(saturation1.equals(saturation1), "saturation11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(saturation1.equals(saturation2), "saturation12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(saturation2.equals(saturation1), "saturation21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(saturation2.equals(saturation4), "saturation24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(saturation1.equals(saturation4), "saturation14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(saturation1.equals(saturation3), "saturation13 are equal"), //$NON-NLS-1$
      () -> assertFalse(saturation3.equals(saturation1), "saturation31 are equal"), //$NON-NLS-1$
      () -> assertFalse(saturation1.equals(null), "saturation10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Saturation saturation = Saturation.of(1);
    assertEquals("Saturation[saturation=1]", saturation.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Saturation saturation1 = Saturation.of(1);
    final Saturation saturation2 = Saturation.of(1);
    final Saturation saturation3 = Saturation.of(2);
    final Saturation saturation4 = Saturation.of(3);
    final Saturation saturation5 = Saturation.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(saturation1.compareTo(saturation2) == -saturation2.compareTo(saturation1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(saturation1.compareTo(saturation3) == -saturation3.compareTo(saturation1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((saturation4.compareTo(saturation3) > 0) && (saturation3.compareTo(saturation1) > 0) && (saturation4.compareTo(saturation1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((saturation1.compareTo(saturation2) == 0) && (Math.abs(saturation1.compareTo(saturation5)) == Math.abs(saturation2.compareTo(saturation5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((saturation1.compareTo(saturation2) == 0) && saturation1.equals(saturation2), "equals") //$NON-NLS-1$
    );
   }

 }
