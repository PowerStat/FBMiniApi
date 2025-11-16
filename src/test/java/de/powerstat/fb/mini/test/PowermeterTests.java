/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
      () -> assertEquals(0, cleanPowermeter.getVoltage().longValue(), "Powermeter not as expected"), //$NON-NLS-1$
      () -> assertEquals(0, cleanPowermeter.getPower().longValue(), "Powermeter not as expected"), //$NON-NLS-1$
      () -> assertEquals(0, cleanPowermeter.getEnergy().longValue(), "Powermeter not as expected") //$NON-NLS-1$
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
     }, "Null pointer exception expected" //$NON-NLS-1$
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
     }, "Null pointer exception expected" //$NON-NLS-1$
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
     }, "Null pointer exception expected" //$NON-NLS-1$
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
  public void equalsContract()
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
