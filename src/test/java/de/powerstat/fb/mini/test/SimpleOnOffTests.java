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
import de.powerstat.fb.mini.SimpleOnOff;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * SimpleOnOff tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class SimpleOnOffTests
 {
  /**
   * Default constructor.
   */
  /* default */ SimpleOnOffTests()
   {
    super();
   }


  /**
   * Test correct SimpleOnOff.
   *
   * @param state SimpleOnOff
   */
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  /* default */ void testSimpleOnOffCorrect(final boolean state)
   {
    final SimpleOnOff cleanState = SimpleOnOff.of(state);
    assertEquals(state, cleanState.booleanValue(), "SimpleOnOff not as expected"); //$NON-NLS-1$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final SimpleOnOff state = SimpleOnOff.of(false);
    assertEquals("false", state.stringValue(), "SimpleOnOff not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(SimpleOnOff.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final SimpleOnOff state = SimpleOnOff.of(false);
    assertEquals("SimpleOnOff[state=false]", state.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final SimpleOnOff state1 = SimpleOnOff.of(false);
    final SimpleOnOff state2 = SimpleOnOff.of(false);
    final SimpleOnOff state3 = SimpleOnOff.of(true);
    final SimpleOnOff state4 = SimpleOnOff.of(true);
    final SimpleOnOff state5 = SimpleOnOff.of(false);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(state1.compareTo(state2) == -state2.compareTo(state1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(state1.compareTo(state3) == -state3.compareTo(state1), "reflexive2"), //$NON-NLS-1$
      // () -> assertTrue((state4.compareTo(state3) > 0) && (state3.compareTo(state1) > 0) && (state4.compareTo(state1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((state1.compareTo(state2) == 0) && (Math.abs(state1.compareTo(state5)) == Math.abs(state2.compareTo(state5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((state1.compareTo(state2) == 0) && state1.equals(state2), "equals") //$NON-NLS-1$
    );
   }

 }
