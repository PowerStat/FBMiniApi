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

import de.powerstat.fb.mini.DurationMS100;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * DurationMS100 tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class DurationMS100Tests
 {
  /**
   * Default constructor.
   */
  /* default */ DurationMS100Tests()
   {
    super();
   }


  /**
   * Is a duration value.
   *
   * @param duration DurationMS100
   */
  @ParameterizedTest
  @ValueSource(ints = {0, Integer.MAX_VALUE})
  /* default */ void testIsDurationMS100Value(final int duration)
   {
    assertEquals(duration, DurationMS100.of(duration).intValue(), "Not a duration value!"); //$NON-NLS-1$
   }


  /**
   * Is not a duration value.
   *
   * @param duration DurationMS100 (0-Integer.MaxValue)
   */
  @ParameterizedTest
  @ValueSource(ints = {-1})
  /* default */ void testIsNotAdurationValue(final int duration)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final DurationMS100 duration = */ DurationMS100.of(duration);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a duration string value.
   */
  @Test
  /* default */ void testIsDurationMS100String()
   {
    assertEquals(127, DurationMS100.of("127").intValue(), "Not a duration value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final DurationMS100 duration1 = DurationMS100.of(1);
    final DurationMS100 duration2 = DurationMS100.of(1);
    final DurationMS100 duration3 = DurationMS100.of(2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(duration1.hashCode(), duration2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(duration1.hashCode(), duration3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final DurationMS100 duration1 = DurationMS100.of(1);
    final DurationMS100 duration2 = DurationMS100.of(1);
    final DurationMS100 duration3 = DurationMS100.of(2);
    final DurationMS100 duration4 = DurationMS100.of(1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(duration1.equals(duration1), "duration11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(duration1.equals(duration2), "duration12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(duration2.equals(duration1), "duration21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(duration2.equals(duration4), "duration24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(duration1.equals(duration4), "duration14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(duration1.equals(duration3), "duration13 are equal"), //$NON-NLS-1$
      () -> assertFalse(duration3.equals(duration1), "duration31 are equal"), //$NON-NLS-1$
      () -> assertFalse(duration1.equals(null), "duration10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final DurationMS100 duration = DurationMS100.of(1);
    assertEquals("DurationMS100[duration=1]", duration.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final DurationMS100 duration1 = DurationMS100.of(1);
    final DurationMS100 duration2 = DurationMS100.of(1);
    final DurationMS100 duration3 = DurationMS100.of(2);
    final DurationMS100 duration4 = DurationMS100.of(3);
    final DurationMS100 duration5 = DurationMS100.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(duration1.compareTo(duration2) == -duration2.compareTo(duration1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(duration1.compareTo(duration3) == -duration3.compareTo(duration1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((duration4.compareTo(duration3) > 0) && (duration3.compareTo(duration1) > 0) && (duration4.compareTo(duration1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((duration1.compareTo(duration2) == 0) && (Math.abs(duration1.compareTo(duration5)) == Math.abs(duration2.compareTo(duration5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((duration1.compareTo(duration2) == 0) && duration1.equals(duration2), "equals") //$NON-NLS-1$
    );
   }

 }
