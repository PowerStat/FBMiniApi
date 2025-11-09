/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
import nl.jqno.equalsverifier.*;

import de.powerstat.fb.mini.Action;
import de.powerstat.validation.values.BIC;
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
  @ValueSource(strings = {"GetPersistentData", "abcdef", "abcdefghijklmnopqrstuvwxyzabcdef"})
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
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(Action.class).withNonnullFields("action").verify();
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
