/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.HANFUNUnits;


/**
 * Funcions tests.
 */
final class HANFUNUnitsTests
 {
  /**
   * Default constructor.
   */
  /* default */ HANFUNUnitsTests()
   {
    super();
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(273, HANFUNUnits.of("SIMPLE_BUTTON").getAction(), "Action not as expected");
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(273, HANFUNUnits.of(273).getAction(), "Action not as expected");
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* HANFUNUnits hanfun = */ HANFUNUnits.of(999);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of HANFUNUnits.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(273, HANFUNUnits.SIMPLE_BUTTON.getAction(), "SIMPLE_BUTTON action not as expected"), //$NON-NLS-1$
      () -> assertEquals(256, HANFUNUnits.SIMPLE_ON_OFF_SWITCHABLE.getAction(), "SIMPLE_ON_OFF_SWITCHABLE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(257, HANFUNUnits.SIMPLE_ON_OFF_SWITCH.getAction(), "SIMPLE_ON_OFF_SWITCH action not as expected"), //$NON-NLS-1$
      () -> assertEquals(262, HANFUNUnits.AC_OUTLET.getAction(), "AC_OUTLET action not as expected"), //$NON-NLS-1$
      () -> assertEquals(263, HANFUNUnits.AC_OUTLET_SIMPLE_POWER_METERING.getAction(), "AC_OUTLET_SIMPLE_POWER_METERING action not as expected"), //$NON-NLS-1$
      () -> assertEquals(264, HANFUNUnits.SIMPLE_LIGHT.getAction(), "SIMPLE_LIGHT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(265, HANFUNUnits.DIMMABLE_LIGHT.getAction(), "DIMMABLE_LIGHT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(266, HANFUNUnits.DIMMER_SWITCH.getAction(), "DIMMER_SWITCH action not as expected"), //$NON-NLS-1$
      () -> assertEquals(277, HANFUNUnits.COLOR_BULB.getAction(), "COLOR_BULB action not as expected"), //$NON-NLS-1$
      () -> assertEquals(278, HANFUNUnits.DIMMABLE_COLOR_BULB.getAction(), "DIMMABLE_COLOR_BULB action not as expected"), //$NON-NLS-1$
      () -> assertEquals(281, HANFUNUnits.BLIND.getAction(), "BLIND action not as expected"), //$NON-NLS-1$
      () -> assertEquals(282, HANFUNUnits.LAMELLAR.getAction(), "LAMELLAR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(512, HANFUNUnits.SIMPLE_DETECTOR.getAction(), "SIMPLE_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(513, HANFUNUnits.DOOR_OPEN_CLOSE_DETECTOR.getAction(), "DOOR_OPEN_CLOSE_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(514, HANFUNUnits.WINDOW_OPEN_CLOSE_DETECTOR.getAction(), "WINDOW_OPEN_CLOSE_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(515, HANFUNUnits.MOTION_DETECTOR.getAction(), "MOTION_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(518, HANFUNUnits.FLOOD_DETECTOR.getAction(), "FLOOD_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(519, HANFUNUnits.GLAS_BREAK_DETECTOR.getAction(), "GLAS_BREAK_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(520, HANFUNUnits.VIBRATION_DETECTOR.getAction(), "VIBRATION_DETECTOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(640, HANFUNUnits.SIREN.getAction(), "SIREN action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final HANFUNUnits code = HANFUNUnits.SIMPLE_BUTTON;
    assertEquals("SIMPLE_BUTTON", code.stringValue(), "stringValue not as expected");
   }

 }
