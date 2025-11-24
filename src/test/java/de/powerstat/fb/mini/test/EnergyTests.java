/*
 * Copyright (C) 2020-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
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

import de.powerstat.fb.mini.Energy;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Energy tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class EnergyTests
 {
  /**
   * Energy.
   */
  private static final String ENERGY_75519 = "75519";

  /**
   * Not an energy value.
   */
  private static final String NOT_AN_ENERGY_VALUE = "Not an energy value!";


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
    assertEquals(energy, Energy.of(energy).longValue(), NOT_AN_ENERGY_VALUE);
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
    assertEquals(75, Energy.of(ENERGY_75519).getEnergyKiloWattHours(), NOT_AN_ENERGY_VALUE);
   }


  /**
   * Is an energy string value.
   */
  @Test
  /* default */ void testStringValue()
   {
    assertEquals(ENERGY_75519, Energy.of(ENERGY_75519).stringValue(), NOT_AN_ENERGY_VALUE);
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
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
