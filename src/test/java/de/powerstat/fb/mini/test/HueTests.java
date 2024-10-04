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

import de.powerstat.fb.mini.Hue;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Hue tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
public class HueTests
 {
  /**
   * Default constructor.
   */
  /* default */ HueTests()
   {
    super();
   }


  /**
   * Is a hue value.
   *
   * @param hue Hue
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 180, 359})
  /* default */ void testIsHueValue(final int hue)
   {
    assertEquals(hue, Hue.of(hue).intValue(), "Not a hue value!"); //$NON-NLS-1$
   }


  /**
   * Is not a hue value.
   *
   * @param hue Hue (0-359)
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 360})
  /* default */ void testIsNotAHueValue(final int hue)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Hue hue = */ Hue.of(hue);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a hue string value.
   */
  @Test
  /* default */ void testIsHueString()
   {
    assertEquals(180, Hue.of("180").intValue(), "Not a hue value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Hue hue1 = Hue.of(1);
    final Hue hue2 = Hue.of(1);
    final Hue hue3 = Hue.of(2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(hue1.hashCode(), hue2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(hue1.hashCode(), hue3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final Hue hue1 = Hue.of(1);
    final Hue hue2 = Hue.of(1);
    final Hue hue3 = Hue.of(2);
    final Hue hue4 = Hue.of(1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(hue1.equals(hue1), "hue11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(hue1.equals(hue2), "hue12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(hue2.equals(hue1), "hue21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(hue2.equals(hue4), "hue24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(hue1.equals(hue4), "hue14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(hue1.equals(hue3), "hue13 are equal"), //$NON-NLS-1$
      () -> assertFalse(hue3.equals(hue1), "hue31 are equal"), //$NON-NLS-1$
      () -> assertFalse(hue1.equals(null), "hue10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Hue hue = Hue.of(1);
    assertEquals("Hue[hue=1]", hue.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Hue hue1 = Hue.of(1);
    final Hue hue2 = Hue.of(1);
    final Hue hue3 = Hue.of(2);
    final Hue hue4 = Hue.of(3);
    final Hue hue5 = Hue.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(hue1.compareTo(hue2) == -hue2.compareTo(hue1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(hue1.compareTo(hue3) == -hue3.compareTo(hue1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((hue4.compareTo(hue3) > 0) && (hue3.compareTo(hue1) > 0) && (hue4.compareTo(hue1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((hue1.compareTo(hue2) == 0) && (Math.abs(hue1.compareTo(hue5)) == Math.abs(hue2.compareTo(hue5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((hue1.compareTo(hue2) == 0) && hue1.equals(hue2), "equals") //$NON-NLS-1$
    );
   }

 }
