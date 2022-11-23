/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.fb.mini.Power;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Power tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
public class PowerTests
 {
  /**
   * Default constructor.
   */
  public PowerTests()
   {
    super();
   }


  /**
   * Is a power value.
   *
   * @param power Power in mW
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 1, 10150})
  public void isPowerValue(final long power)
   {
    assertEquals(power, Power.of(power).longValue(), "Not a power value!"); //$NON-NLS-1$
   }


  /**
   * Is not a power value.
   *
   * @param power Power in mW
   */
  @ParameterizedTest
  @ValueSource(longs = {-1})
  public void isNotAPowerValue(final long power)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Power power = */ Power.of(power);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a power string value.
   */
  @Test
  public void isPowerString()
   {
    assertEquals(10, Power.of("10150").getPowerWatt(), "Not a power value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  public void testHashCode()
   {
    final Power power1 = new Power(1);
    final Power power2 = new Power(1);
    final Power power3 = new Power(2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(power1.hashCode(), power2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(power1.hashCode(), power3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  public void testEquals()
   {
    final Power power1 = new Power(1);
    final Power power2 = new Power(1);
    final Power power3 = new Power(2);
    final Power power4 = new Power(1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(power1.equals(power1), "power11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(power1.equals(power2), "power12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(power2.equals(power1), "power21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(power2.equals(power4), "power24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(power1.equals(power4), "power14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(power1.equals(power3), "power13 are equal"), //$NON-NLS-1$
      () -> assertFalse(power3.equals(power1), "power31 are equal"), //$NON-NLS-1$
      () -> assertFalse(power1.equals(null), "power10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  public void testToString()
   {
    final Power power = new Power(1);
    assertEquals("Power[power=1]", power.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  public void testCompareTo()
   {
    final Power power1 = new Power(1);
    final Power power2 = new Power(1);
    final Power power3 = new Power(2);
    final Power power4 = new Power(3);
    final Power power5 = new Power(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(power1.compareTo(power2) == -power2.compareTo(power1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(power1.compareTo(power3) == -power3.compareTo(power1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((power4.compareTo(power3) > 0) && (power3.compareTo(power1) > 0) && (power4.compareTo(power1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((power1.compareTo(power2) == 0) && (Math.abs(power1.compareTo(power5)) == Math.abs(power2.compareTo(power5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((power1.compareTo(power2) == 0) && power1.equals(power2), "equals") //$NON-NLS-1$
    );
   }

 }
