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

import de.powerstat.fb.mini.TemperatureKelvin;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * TemperatureKelvin tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class TemperatureKelvinTests
 {
  /**
   * Default constructor.
   */
  /* default */ TemperatureKelvinTests()
   {
    super();
   }


  /**
   * Is a temperature value.
   *
   * @param temperature TemperatureKelvin
   */
  @ParameterizedTest
  @ValueSource(ints = {2700, 6500})
  /* default */ void testIsTemperatureKelvinValue(final int temperature)
   {
    assertEquals(temperature, TemperatureKelvin.of(temperature).intValue(), "Not a temperature value!"); //$NON-NLS-1$
   }


  /**
   * Is not a temperature value.
   *
   * @param temperature TemperatureKelvin (2700-6500)
   */
  @ParameterizedTest
  @ValueSource(ints = {2699, 6501})
  /* default */ void testIsNotAtemperatureValue(final int temperature)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final TemperatureKelvin temperature = */ TemperatureKelvin.of(temperature);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a temperature string value.
   */
  @Test
  /* default */ void testIsTemperatureKelvinString()
   {
    assertEquals(3000, TemperatureKelvin.of("3000").intValue(), "Not a temperature value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final TemperatureKelvin temperature1 = TemperatureKelvin.of(2700);
    final TemperatureKelvin temperature2 = TemperatureKelvin.of(2700);
    final TemperatureKelvin temperature3 = TemperatureKelvin.of(3000);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(temperature1.hashCode(), temperature2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(temperature1.hashCode(), temperature3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final TemperatureKelvin temperature1 = TemperatureKelvin.of(2700);
    final TemperatureKelvin temperature2 = TemperatureKelvin.of(2700);
    final TemperatureKelvin temperature3 = TemperatureKelvin.of(3000);
    final TemperatureKelvin temperature4 = TemperatureKelvin.of(2700);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(temperature1.equals(temperature1), "temperature11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(temperature1.equals(temperature2), "temperature12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(temperature2.equals(temperature1), "temperature21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(temperature2.equals(temperature4), "temperature24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(temperature1.equals(temperature4), "temperature14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(temperature1.equals(temperature3), "temperature13 are equal"), //$NON-NLS-1$
      () -> assertFalse(temperature3.equals(temperature1), "temperature31 are equal"), //$NON-NLS-1$
      () -> assertFalse(temperature1.equals(null), "temperature10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final TemperatureKelvin temperature = TemperatureKelvin.of(2700);
    assertEquals("TemperatureKelvin[temperature=2700]", temperature.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final TemperatureKelvin temperature1 = TemperatureKelvin.of(2700);
    final TemperatureKelvin temperature2 = TemperatureKelvin.of(2700);
    final TemperatureKelvin temperature3 = TemperatureKelvin.of(3000);
    final TemperatureKelvin temperature4 = TemperatureKelvin.of(4000);
    final TemperatureKelvin temperature5 = TemperatureKelvin.of(2700);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature2) == -temperature2.compareTo(temperature1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature3) == -temperature3.compareTo(temperature1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((temperature4.compareTo(temperature3) > 0) && (temperature3.compareTo(temperature1) > 0) && (temperature4.compareTo(temperature1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && (Math.abs(temperature1.compareTo(temperature5)) == Math.abs(temperature2.compareTo(temperature5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && temperature1.equals(temperature2), "equals") //$NON-NLS-1$
    );
   }

 }
