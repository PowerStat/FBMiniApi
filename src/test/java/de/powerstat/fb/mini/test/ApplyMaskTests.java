/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.ApplyMask;


/**
 * ApplyMask tests.
 */
final class ApplyMaskTests
 {
  /**
   * Default constructor.
   */
  /* default */ ApplyMaskTests()
   {
    super();
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(0, ApplyMask.of("HKR_SUMMER").getAction(), "Action not as expected");
   }


  /**
   * Factory int test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(2, ApplyMask.of(2).getAction(), "Action not as expected");
   }


  /**
   * Factory int failure test.
   */
  @Test
  /* default */ void testFactory3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* ApplyMask mask = */ ApplyMask.of(99);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of ApplyMask.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(0, ApplyMask.HKR_SUMMER.getAction(), "HKR_SUMMER action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1, ApplyMask.HKR_TEMPERATURE.getAction(), "HKR_TEMPERATURE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(2, ApplyMask.HKR_HOLIDAYS.getAction(), "HKR_HOLIDAYS action not as expected"), //$NON-NLS-1$
      () -> assertEquals(3, ApplyMask.HKR_TIME_TABLE.getAction(), "HKR_TIME_TABLE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(4, ApplyMask.RELAY_MANUAL.getAction(), "RELAY_MANUAL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(5, ApplyMask.RELAY_AUTOMATIC.getAction(), "RELAY_AUTOMATIC action not as expected"), //$NON-NLS-1$
      () -> assertEquals(6, ApplyMask.LEVEL.getAction(), "LEVEL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(7, ApplyMask.COLOR.getAction(), "COLOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(8, ApplyMask.DIALHELPER.getAction(), "DIALHELPER action not as expected"), //$NON-NLS-1$
      () -> assertEquals(9, ApplyMask.SUN_SIMULATION.getAction(), "SUN_SIMULATION action not as expected"), //$NON-NLS-1$
      () -> assertEquals(10, ApplyMask.SUB_TEMPLATES.getAction(), "SUB_TEMPLATES action not as expected"), //$NON-NLS-1$
      () -> assertEquals(11, ApplyMask.MAIN_WIFI.getAction(), "MAIN_WIFI action not as expected"), //$NON-NLS-1$
      () -> assertEquals(12, ApplyMask.GUEST_WIFI.getAction(), "GUEST_WIFI action not as expected"), //$NON-NLS-1$
      () -> assertEquals(13, ApplyMask.TAM_CONTROL.getAction(), "TAM_CONTROL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(14, ApplyMask.HTTP_REQUEST.getAction(), "HTTP_REQUEST action not as expected"), //$NON-NLS-1$
      () -> assertEquals(15, ApplyMask.TIMER_CONTROL.getAction(), "TIMER_CONTROL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(16, ApplyMask.SWITCH_MASTER.getAction(), "SWITCH_MASTER action not as expected"), //$NON-NLS-1$
      () -> assertEquals(17, ApplyMask.CUSTOM_NOTIFICATION.getAction(), "CUSTOM_NOTIFICATION action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final ApplyMask code = ApplyMask.HKR_SUMMER;
    assertEquals("HKR_SUMMER", code.stringValue(), "stringValue not as expected");
   }

 }
