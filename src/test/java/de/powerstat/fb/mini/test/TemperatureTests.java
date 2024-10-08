/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.fb.mini.Temperature;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Temperature tests.
 */
@SuppressFBWarnings({"EC_NULL_ARG", "RV_NEGATING_RESULT_OF_COMPARETO", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class TemperatureTests
 {
  /**
   * Default constructor.
   */
  /* default */ TemperatureTests()
   {
    super();
   }


  /**
   * Is a temperature value.
   *
   * @param temperature Temperature in deci celsius
   */
  @ParameterizedTest
  @ValueSource(longs = {-2732, 0, 200})
  /* default */ void testIsTemperatureValue(final long temperature)
   {
    assertEquals(temperature, Temperature.of(temperature).longValue(), "Not a temperature value!"); //$NON-NLS-1$
   }


  /**
   * Is not a temperature value.
   *
   * @param temperature Temperature in deci celsius
   */
  @ParameterizedTest
  @ValueSource(longs = {-2733})
  /* default */ void testIsNotATemperatureValue(final long temperature)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Temperature temperature = */ Temperature.of(temperature);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a temperature string value.
   */
  @Test
  /* default */ void testIsTemperatureString()
   {
    assertEquals(20, Temperature.of("200").getTemperatureCelsius(), "Not a temperature value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Temperature temperature1 = Temperature.of(1);
    final Temperature temperature2 = Temperature.of(1);
    final Temperature temperature3 = Temperature.of(2);
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
    final Temperature temperature1 = Temperature.of(1);
    final Temperature temperature2 = Temperature.of(1);
    final Temperature temperature3 = Temperature.of(2);
    final Temperature temperature4 = Temperature.of(1);
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
    final Temperature temperature = Temperature.of(200);
    assertEquals("Temperature[temperature=200]", temperature.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Temperature temperature1 = Temperature.of(1);
    final Temperature temperature2 = Temperature.of(1);
    final Temperature temperature3 = Temperature.of(2);
    final Temperature temperature4 = Temperature.of(3);
    final Temperature temperature5 = Temperature.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature2) == -temperature2.compareTo(temperature1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature3) == -temperature3.compareTo(temperature1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((temperature4.compareTo(temperature3) > 0) && (temperature3.compareTo(temperature1) > 0) && (temperature4.compareTo(temperature1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && (Math.abs(temperature1.compareTo(temperature5)) == Math.abs(temperature2.compareTo(temperature5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && temperature1.equals(temperature2), "equals") //$NON-NLS-1$
    );
   }

 }
