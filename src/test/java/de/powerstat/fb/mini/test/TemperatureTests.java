/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
import de.powerstat.fb.mini.Temperature;
import de.powerstat.fb.mini.TemperatureCelsius;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Temperature tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
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
   * Test correct Temperature.
   */
  @Test
  /* default */ void testTemperatureCorrect1()
   {
    final Temperature cleanTemperature = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    assertEquals(200, cleanTemperature.temperatureValue().longValue(), "Temperature not as expected"); //$NON-NLS-1$
   }


  /**
   * Test correct Temperature.
   */
  @Test
  /* default */ void testTemperatureCorrect2()
   {
    final Temperature cleanTemperature = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(10));
    assertEquals(210, cleanTemperature.temperatureValue().longValue(), "Temperature not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getTemperature.
   */
  @Test
  /* default */ void testGetTemperature()
   {
    final Temperature cleanTemperature = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    assertEquals(200, cleanTemperature.getTemperature().longValue(), "getTemperature not as expected"); //$NON-NLS-1$
   }


  /**
   * Test getOffset.
   */
  @Test
  /* default */ void testGetOffset()
   {
    final Temperature cleanTemperature = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    assertEquals(0, cleanTemperature.getOffset().longValue(), "getOffset not as expected"); //$NON-NLS-1$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Temperature cleanTemperature = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    assertEquals("200", cleanTemperature.stringValue(), "Temperature not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(Temperature.class).withNonnullFields("temperature", "offset").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Temperature temperature = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    assertEquals("Temperature[temperature=TemperatureCelsius[temperature=200], offset=TemperatureCelsius[temperature=0]]", temperature.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Temperature temperature1 = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    final Temperature temperature2 = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    final Temperature temperature3 = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(10));
    final Temperature temperature4 = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(20));
    final Temperature temperature5 = Temperature.of(TemperatureCelsius.of(200), TemperatureCelsius.of(0));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature2) == -temperature2.compareTo(temperature1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(temperature1.compareTo(temperature3) == -temperature3.compareTo(temperature1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((temperature4.compareTo(temperature3) > 0) && (temperature3.compareTo(temperature1) > 0) && (temperature4.compareTo(temperature1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && (Math.abs(temperature1.compareTo(temperature5)) == Math.abs(temperature2.compareTo(temperature5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((temperature1.compareTo(temperature2) == 0) && temperature1.equals(temperature2), "equals") //$NON-NLS-1$
    );
   }

 }
