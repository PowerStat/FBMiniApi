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

import de.powerstat.fb.mini.ScenarioType;


/**
 * ScenarioType tests.
 */
final class ScenarioTypeTests
 {
  /**
   * Default constructor.
   */
  /* default */ ScenarioTypeTests()
   {
    super();
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(1, ScenarioType.of("COMING").getAction(), "Action not as expected");
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(1, ScenarioType.of(1).getAction(), "Action not as expected");
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* ScenarioType type = */ ScenarioType.of(999);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of ScenarioType.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(0, ScenarioType.UNDEFINED.getAction(), "UNDEFINED action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1, ScenarioType.COMING.getAction(), "COMING action not as expected"), //$NON-NLS-1$
      () -> assertEquals(2, ScenarioType.LEAVING.getAction(), "LEAVING action not as expected"), //$NON-NLS-1$
      () -> assertEquals(3, ScenarioType.GENERIC.getAction(), "GENERIC action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final ScenarioType code = ScenarioType.COMING;
    assertEquals("COMING", code.stringValue(), "stringValue not as expected");
   }

 }
