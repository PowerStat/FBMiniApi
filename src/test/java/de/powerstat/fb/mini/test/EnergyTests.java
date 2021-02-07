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

import de.powerstat.fb.mini.Energy;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Energy tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
public class EnergyTests
 {
  /**
   * Default constructor.
   */
  public EnergyTests()
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
  public void isEnergyValue(final long energy)
   {
    assertEquals(energy, Energy.of(energy).getEnergyWattHours(), "Not an energy value!"); //$NON-NLS-1$
   }


  /**
   * Is not an energy value.
   *
   * @param energy Energy in Wh
   */
  @ParameterizedTest
  @ValueSource(longs = {-1})
  public void isNotAnEnergyValue(final long energy)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Energy energy = */ Energy.of(energy);
     }
    );
   }


  /**
   * Is an energy string value.
   */
  @Test
  public void isEnergyString()
   {
    assertEquals(75, Energy.of("75519").getEnergyKiloWattHours(), "Not an energy value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  public void testHashCode()
   {
    final Energy energy1 = new Energy(1);
    final Energy energy2 = new Energy(1);
    final Energy energy3 = new Energy(2);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(energy1.hashCode(), energy2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(energy1.hashCode(), energy3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  public void testEquals()
   {
    final Energy energy1 = new Energy(1);
    final Energy energy2 = new Energy(1);
    final Energy energy3 = new Energy(2);
    final Energy energy4 = new Energy(1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(energy1.equals(energy1), "energy11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(energy1.equals(energy2), "energy12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(energy2.equals(energy1), "energy21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(energy2.equals(energy4), "energy24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(energy1.equals(energy4), "energy14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(energy1.equals(energy3), "energy13 are equal"), //$NON-NLS-1$
      () -> assertFalse(energy3.equals(energy1), "energy31 are equal"), //$NON-NLS-1$
      () -> assertFalse(energy1.equals(null), "energy10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  public void testToString()
   {
    final Energy energy = new Energy(1);
    assertEquals("Energy[energy=1]", energy.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  public void testCompareTo()
   {
    final Energy energy1 = new Energy(1);
    final Energy energy2 = new Energy(1);
    final Energy energy3 = new Energy(2);
    final Energy energy4 = new Energy(3);
    final Energy energy5 = new Energy(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(energy1.compareTo(energy2) == -energy2.compareTo(energy1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(energy1.compareTo(energy3) == -energy3.compareTo(energy1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((energy4.compareTo(energy3) > 0) && (energy3.compareTo(energy1) > 0) && (energy4.compareTo(energy1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((energy1.compareTo(energy2) == 0) && (Math.abs(energy1.compareTo(energy5)) == Math.abs(energy2.compareTo(energy5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((energy1.compareTo(energy2) == 0) && energy1.equals(energy2), "equals") //$NON-NLS-1$
    );
   }

 }
