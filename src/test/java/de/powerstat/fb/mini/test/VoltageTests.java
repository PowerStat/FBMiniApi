/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import nl.jqno.equalsverifier.EqualsVerifier;

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
   * Voltage string value.
   */
  @Test
  /* default */ void testVoltageStringValue()
   {
    assertEquals("228201", Voltage.of("228201").stringValue(), "Not an voltage value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(Voltage.class).verify();
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
