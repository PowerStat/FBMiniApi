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
import de.powerstat.fb.mini.Energy;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Energy tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class EnergyTests
 {
  /**
   * Default constructor.
   */
  /* default */ EnergyTests()
   {
    super();
   }


  /**
   * Is an energy value.
   *
   * @param energy Energy in Wh
   */
  @ParameterizedTest
  @ValueSource(longs = {0, 1, 75519})
  /* default */ void testIsEnergyValue(final long energy)
   {
    assertEquals(energy, Energy.of(energy).longValue(), "Not an energy value!"); //$NON-NLS-1$
   }


  /**
   * Is not an energy value.
   *
   * @param energy Energy in Wh
   */
  @ParameterizedTest
  @ValueSource(longs = {-1})
  /* default */ void testIsNotAnEnergyValue(final long energy)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Energy energy = */ Energy.of(energy);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Get energy kilowatt hours.
   */
  @Test
  /* default */ void testGetKiloWattHours()
   {
    assertEquals(75, Energy.of("75519").getEnergyKiloWattHours(), "Not an energy value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Is an energy string value.
   */
  @Test
  /* default */ void teststringValue()
   {
    assertEquals("75519", Energy.of("75519").stringValue(), "Not an energy value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(Energy.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Energy energy = Energy.of(1);
    assertEquals("Energy[energy=1]", energy.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Energy energy1 = Energy.of(1);
    final Energy energy2 = Energy.of(1);
    final Energy energy3 = Energy.of(2);
    final Energy energy4 = Energy.of(3);
    final Energy energy5 = Energy.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(energy1.compareTo(energy2) == -energy2.compareTo(energy1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(energy1.compareTo(energy3) == -energy3.compareTo(energy1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((energy4.compareTo(energy3) > 0) && (energy3.compareTo(energy1) > 0) && (energy4.compareTo(energy1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((energy1.compareTo(energy2) == 0) && (Math.abs(energy1.compareTo(energy5)) == Math.abs(energy2.compareTo(energy5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((energy1.compareTo(energy2) == 0) && energy1.equals(energy2), "equals") //$NON-NLS-1$
    );
   }

 }
