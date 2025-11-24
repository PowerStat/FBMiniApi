/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Energy;
import de.powerstat.fb.mini.Power;
import de.powerstat.fb.mini.Powermeter;
import de.powerstat.fb.mini.Voltage;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Powermeter tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class PowermeterTests
 {
  /**
   * Null pointer exception expected.
   */
  private static final String NULL_POINTER_EXCEPTION_EXPECTED = "Null pointer exception expected"; //$NON-NLS-1$


  /**
   * Default constructor.
   */
  /* default */ PowermeterTests()
   {
    super();
   }


  /**
   * Test correct Powermeter.
   */
  @Test
  /* default */ void testPowermeterCorrect()
   {
    final Powermeter cleanPowermeter = Powermeter.of(Voltage.of(0), Power.of(0), Energy.of(0));
    assertAll("testPowermeterCorrect", //$NON-NLS-1$
      () -> assertEquals(0, cleanPowermeter.getVoltage().longValue(), "Voltage not as expected"), //$NON-NLS-1$
      () -> assertEquals(0, cleanPowermeter.getPower().longValue(), "Power not as expected"), //$NON-NLS-1$
      () -> assertEquals(0, cleanPowermeter.getEnergy().longValue(), "Energy not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Powermeter.
   */
  @Test
  /* default */ void testPowermeterWrong1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Powermeter cleanPowermeter = */ Powermeter.of(null, Power.of(0), Energy.of(0));
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong Powermeter.
   */
  @Test
  /* default */ void testPowermeterWrong2()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Powermeter cleanPowermeter = */ Powermeter.of(Voltage.of(0), null, Energy.of(0));
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong Powermeter.
   */
  @Test
  /* default */ void testPowermeterWrong3()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Powermeter cleanPowermeter = */ Powermeter.of(Voltage.of(0), Power.of(0), null);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Powermeter powermeter = Powermeter.of(Voltage.of(0), Power.of(1), Energy.of(2));
    assertEquals("1", powermeter.stringValue(), "Powermeter not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Powermeter.class).withNonnullFields("voltage", "power", "energy").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Powermeter powermeter = Powermeter.of(Voltage.of(0), Power.of(1), Energy.of(2));
    assertEquals("Powermeter[voltage=Voltage[voltage=0], power=Power[power=1], energy=Energy[energy=2]]", powermeter.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Powermeter powermeter1 = Powermeter.of(Voltage.of(0), Power.of(0), Energy.of(0));
    final Powermeter powermeter2 = Powermeter.of(Voltage.of(0), Power.of(0), Energy.of(0));
    final Powermeter powermeter3 = Powermeter.of(Voltage.of(1), Power.of(1), Energy.of(1));
    final Powermeter powermeter4 = Powermeter.of(Voltage.of(2), Power.of(2), Energy.of(2));
    final Powermeter powermeter5 = Powermeter.of(Voltage.of(0), Power.of(0), Energy.of(0));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(powermeter1.compareTo(powermeter2) == -powermeter2.compareTo(powermeter1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(powermeter1.compareTo(powermeter3) == -powermeter3.compareTo(powermeter1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((powermeter4.compareTo(powermeter3) > 0) && (powermeter3.compareTo(powermeter1) > 0) && (powermeter4.compareTo(powermeter1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((powermeter1.compareTo(powermeter2) == 0) && (Math.abs(powermeter1.compareTo(powermeter5)) == Math.abs(powermeter2.compareTo(powermeter5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((powermeter1.compareTo(powermeter2) == 0) && powermeter1.equals(powermeter2), "equals") //$NON-NLS-1$
    );
   }

 }
