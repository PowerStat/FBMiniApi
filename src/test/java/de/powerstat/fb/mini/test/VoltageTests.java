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

import de.powerstat.fb.mini.Voltage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Voltage tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class VoltageTests
 {
  /**
   * Default constructor.
   */
  /* default */ VoltageTests()
   {
    super();
   }


  /**
   * Is an voltage value.
   *
   * @param voltage Volt in 0,001V
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 1, 228201})
  /* default */ void testIsVoltageValue(final long voltage)
   {
    assertEquals(voltage, Voltage.of(voltage).longValue(), "Not an voltage value!"); //$NON-NLS-1$
   }


  /**
   * Is not an voltage value.
   *
   * @param voltage Voltage in 0,001V
   */
  @ParameterizedTest
  @ValueSource(longs = {-1})
  /* default */ void testIsNotAnVoltageValue(final long voltage)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Voltage voltage = */ Voltage.of(voltage);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is an voltage string value.
   */
  @Test
  /* default */ void testIsVoltageString()
   {
    assertEquals(228, Voltage.of("228201").getVoltageVolt(), "Not an voltage value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Voltage voltage1 = Voltage.of(1);
    final Voltage voltage2 = Voltage.of(1);
    final Voltage voltage3 = Voltage.of(2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(voltage1.hashCode(), voltage2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(voltage1.hashCode(), voltage3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final Voltage voltage1 = Voltage.of(1);
    final Voltage voltage2 = Voltage.of(1);
    final Voltage voltage3 = Voltage.of(2);
    final Voltage voltage4 = Voltage.of(1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(voltage1.equals(voltage1), "voltage11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(voltage1.equals(voltage2), "voltage12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(voltage2.equals(voltage1), "voltage21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(voltage2.equals(voltage4), "voltage24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(voltage1.equals(voltage4), "voltage14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(voltage1.equals(voltage3), "voltage13 are equal"), //$NON-NLS-1$
      () -> assertFalse(voltage3.equals(voltage1), "voltage31 are equal"), //$NON-NLS-1$
      () -> assertFalse(voltage1.equals(null), "voltage10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Voltage voltage = Voltage.of(1);
    assertEquals("Voltage[voltage=1]", voltage.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Voltage voltage1 = Voltage.of(1);
    final Voltage voltage2 = Voltage.of(1);
    final Voltage voltage3 = Voltage.of(2);
    final Voltage voltage4 = Voltage.of(3);
    final Voltage voltage5 = Voltage.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(voltage1.compareTo(voltage2) == -voltage2.compareTo(voltage1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(voltage1.compareTo(voltage3) == -voltage3.compareTo(voltage1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((voltage4.compareTo(voltage3) > 0) && (voltage3.compareTo(voltage1) > 0) && (voltage4.compareTo(voltage1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((voltage1.compareTo(voltage2) == 0) && (Math.abs(voltage1.compareTo(voltage5)) == Math.abs(voltage2.compareTo(voltage5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((voltage1.compareTo(voltage2) == 0) && voltage1.equals(voltage2), "equals") //$NON-NLS-1$
    );
   }

 }
