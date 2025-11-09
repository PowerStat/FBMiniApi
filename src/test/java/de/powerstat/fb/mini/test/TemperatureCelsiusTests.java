/*
 * Copyright (C) 2020-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
import nl.jqno.equalsverifier.*;
import de.powerstat.fb.mini.Alert;
import de.powerstat.fb.mini.TemperatureCelsius;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Temperature tests.
 */
@SuppressFBWarnings({"EC_NULL_ARG", "RV_NEGATING_RESULT_OF_COMPARETO", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class TemperatureCelsiusTests
 {
  /**
   * Default constructor.
   */
  TemperatureCelsiusTests()
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
    assertEquals(temperature, TemperatureCelsius.of(temperature).longValue(), "Not a temperature value!"); //$NON-NLS-1$
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
      /* final Temperature temperature = */ TemperatureCelsius.of(temperature);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a temperature string value.
   */
  @Test
  /* default */ void testIsTemperatureString()
   {
    assertEquals(20, TemperatureCelsius.of("200").getTemperatureCelsius(), "Not a temperature value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(TemperatureCelsius.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final TemperatureCelsius temperature = TemperatureCelsius.of(200);
    assertEquals("TemperatureCelsius[temperature=200]", temperature.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final TemperatureCelsius temperature1 = TemperatureCelsius.of(1);
    final TemperatureCelsius temperature2 = TemperatureCelsius.of(1);
    final TemperatureCelsius temperature3 = TemperatureCelsius.of(2);
    final TemperatureCelsius temperature4 = TemperatureCelsius.of(3);
    final TemperatureCelsius temperature5 = TemperatureCelsius.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature2) == -temperature2.compareTo(temperature1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature3) == -temperature3.compareTo(temperature1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((temperature4.compareTo(temperature3) > 0) && (temperature3.compareTo(temperature1) > 0) && (temperature4.compareTo(temperature1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && (Math.abs(temperature1.compareTo(temperature5)) == Math.abs(temperature2.compareTo(temperature5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && temperature1.equals(temperature2), "equals") //$NON-NLS-1$
    );
   }


  /**
   * Test add.
   */
  @Test
  /* default */ void testAdd()
   {
    final TemperatureCelsius temperature1 = TemperatureCelsius.of(200);
    final TemperatureCelsius temperature2 = TemperatureCelsius.of(100);
    TemperatureCelsius newTemperature = temperature1.add(temperature2);
    assertEquals(300, newTemperature.longValue(), "add not equal"); //$NON-NLS-1$
   }


  /**
   * Test subtract.
   */
  @Test
  /* default */ void testSubtract()
   {
    final TemperatureCelsius temperature1 = TemperatureCelsius.of(200);
    final TemperatureCelsius temperature2 = TemperatureCelsius.of(100);
    TemperatureCelsius newTemperature = temperature1.subtract(temperature2);
    assertEquals(100, newTemperature.longValue(), "subtract not equal"); //$NON-NLS-1$
   }

 }
