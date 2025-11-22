/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Switch;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Switch tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class SwitchTests
 {
  /**
   * Default constructor.
   */
  /* default */ SwitchTests()
   {
    super();
   }


  /**
   * Test correct Switch.
   */
  @Test
  /* default */ void testSwitchCorrect1()
   {
    final Switch cleanSwitch = Switch.of(false, false, false, false);
    assertAll("testSwitchCorrect1", //$NON-NLS-1$
      () -> assertFalse(cleanSwitch.getState(), "State not as expected"), //$NON-NLS-1$
      () -> assertFalse(cleanSwitch.getMode(), "Mode not as expected"), //$NON-NLS-1$
      () -> assertFalse(cleanSwitch.getLock(), "Lock not as expected"), //$NON-NLS-1$
      () -> assertFalse(cleanSwitch.getDevicelock(), "Devicelock not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test correct Switch.
   */
  @Test
  /* default */ void testSwitchCorrect2()
   {
    final Switch cleanSwitch = Switch.of(true, true, true, true);
    assertAll("testSwitchCorrect2", //$NON-NLS-1$
      () -> assertTrue(cleanSwitch.getState(), "State not as expected"), //$NON-NLS-1$
      () -> assertTrue(cleanSwitch.getMode(), "Mode not as expected"), //$NON-NLS-1$
      () -> assertTrue(cleanSwitch.getLock(), "Lock not as expected"), //$NON-NLS-1$
      () -> assertTrue(cleanSwitch.getDevicelock(), "Devicelock not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Switch cleanSwitch = Switch.of(false, false, false, false);
    assertEquals("false", cleanSwitch.stringValue(), "Switch not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Switch.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Switch switchState = Switch.of(false, false, false, false);
    assertEquals("Switch[state=false, mode=false, lock=false, devicelock=false]", switchState.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Switch switchState1 = Switch.of(false, false, false, false);
    final Switch switchState2 = Switch.of(false, false, false, false);
    final Switch switchState3 = Switch.of(true, true, false, false);
    final Switch switchState4 = Switch.of(true, true, true, true);
    final Switch switchState5 = Switch.of(false, false, false, false);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(switchState1.compareTo(switchState2) == -switchState2.compareTo(switchState1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(switchState1.compareTo(switchState3) == -switchState3.compareTo(switchState1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((switchState4.compareTo(switchState3) > 0) && (switchState3.compareTo(switchState1) > 0) && (switchState4.compareTo(switchState1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((switchState1.compareTo(switchState2) == 0) && (Math.abs(switchState1.compareTo(switchState5)) == Math.abs(switchState2.compareTo(switchState5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((switchState1.compareTo(switchState2) == 0) && switchState1.equals(switchState2), "equals") //$NON-NLS-1$
    );
   }

 }
