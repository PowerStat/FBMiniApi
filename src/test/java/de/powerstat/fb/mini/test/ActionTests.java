/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.fb.mini.Action;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;



/**
 * Action tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class ActionTests
 {
  /**
   * Default constructor.
   */
  /* default */ ActionTests()
   {
    super();
   }


  /**
   * Test correct Action.
   *
   * @param action Action
   */
  @ParameterizedTest
  @ValueSource(strings = {"GetPersistentData"})
  /* default */ void testActionCorrect(final String action)
   {
    final Action cleanAction = Action.of(action);
    assertEquals(action, cleanAction.stringValue(), "Action not as expected"); //$NON-NLS-1$
   }


  /**
   * Test Action with wrong lengths.
   *
   * @param action Action
   */
  @ParameterizedTest
  @ValueSource(strings = {"12345", "123456789012345678901234567890123"})
  /* default */ void testActionLength(final String action)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Action cleanAction = */ Action.of(action);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Action.
   *
   * @param action Action
   */
  @ParameterizedTest
  @ValueSource(strings = {"Get+PersistentData"})
  /* default */ void testAinWrong(final String action)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Action cleanAction = */ Action.of(action);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Action action = Action.of("GetPersistentData");
    assertEquals("GetPersistentData", action.stringValue(), "Action not as expected"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Action action1 = Action.of("GetPersistentData");
    final Action action2 = Action.of("GetPersistentData");
    final Action action3 = Action.of("AnotherCommand"); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(action1.hashCode(), action2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(action1.hashCode(), action3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final Action action1 = Action.of("GetPersistentData");
    final Action action2 = Action.of("GetPersistentData");
    final Action action3 = Action.of("OtherCommand"); //$NON-NLS-1$
    final Action action4 = Action.of("GetPersistentData");
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(action1.equals(action1), "action11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(action1.equals(action2), "action12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(action2.equals(action1), "action21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(action2.equals(action4), "action24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(action1.equals(action4), "action14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(action1.equals(action3), "action13 are equal"), //$NON-NLS-1$
      () -> assertFalse(action3.equals(action1), "action31 are equal"), //$NON-NLS-1$
      () -> assertFalse(action1.equals(null), "action10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Action action = Action.of("GetPersistentData");
    assertEquals("Action[action=GetPersistentData]", action.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Action action1 = Action.of("GetPersistentData");
    final Action action2 = Action.of("GetPersistentData");
    final Action action3 = Action.of("OtherPersistentData"); //$NON-NLS-1$
    final Action action4 = Action.of("PersistentData"); //$NON-NLS-1$
    final Action action5 = Action.of("GetPersistentData");
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(action1.compareTo(action2) == -action2.compareTo(action1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(action1.compareTo(action3) == -action3.compareTo(action1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((action4.compareTo(action3) > 0) && (action3.compareTo(action1) > 0) && (action4.compareTo(action1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((action1.compareTo(action2) == 0) && (Math.abs(action1.compareTo(action5)) == Math.abs(action2.compareTo(action5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((action1.compareTo(action2) == 0) && action1.equals(action2), "equals") //$NON-NLS-1$
    );
   }

 }
