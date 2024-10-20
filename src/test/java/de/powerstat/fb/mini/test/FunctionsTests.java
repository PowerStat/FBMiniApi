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

import de.powerstat.fb.mini.Functions;


/**
 * Funcions tests.
 */
final class FunctionsTests
 {
  /**
   * Default constructor.
   */
  /* default */ FunctionsTests()
   {
    super();
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(0, Functions.of("HANFUN_DEVICE").getAction(), "Action not as expected");
   }


  /**
   * Test getAction of Functions.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(0, Functions.HANFUN_DEVICE.getAction(), "HANFUN_DEVICE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1, Functions.RESERVED1.getAction(), "RESERVED1 action not as expected"), //$NON-NLS-1$
      () -> assertEquals(2, Functions.BULB.getAction(), "BULB action not as expected"), //$NON-NLS-1$
      () -> assertEquals(3, Functions.RESERVED2.getAction(), "RESERVED2 action not as expected"), //$NON-NLS-1$
      () -> assertEquals(4, Functions.ALARM_SENSOR.getAction(), "ALARM_SENSOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(5, Functions.AVM_BUTTON.getAction(), "AVM_BUTTON action not as expected"), //$NON-NLS-1$
      () -> assertEquals(6, Functions.AVM_RADIATOR_CONTROLLER.getAction(), "AVM_RADIATOR_CONTROLLER action not as expected"), //$NON-NLS-1$
      () -> assertEquals(7, Functions.AVM_ENERGY_GAUGE.getAction(), "AVM_ENERGY_GAUGE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(8, Functions.TEMPERATURE_SENSOR.getAction(), "TEMPERATURE_SENSOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(9, Functions.AVM_SWITCHSOCKET.getAction(), "AVM_SWITCHSOCKET action not as expected"), //$NON-NLS-1$
      () -> assertEquals(10, Functions.AVM_DECT_REPEATER.getAction(), "AVM_DECT_REPEATER action not as expected"), //$NON-NLS-1$
      () -> assertEquals(11, Functions.AVM_MICROFONE.getAction(), "AVM_MICROFONE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(12, Functions.RESERVED3.getAction(), "RESERVED3 action not as expected"), //$NON-NLS-1$
      () -> assertEquals(13, Functions.HANFUN_UNIT.getAction(), "HANFUN_UNIT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(14, Functions.RESERVED4.getAction(), "RESERVED4 action not as expected"), //$NON-NLS-1$
      () -> assertEquals(15, Functions.DEVICE_ONOFF.getAction(), "DEVICE_ONOFF action not as expected"), //$NON-NLS-1$
      () -> assertEquals(16, Functions.DEVICE_WITH_LEVEL.getAction(), "DEVICE_WITH_LEVEL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(17, Functions.BULB_WITH_COLOR.getAction(), "BULB_WITH_COLOR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(18, Functions.BLIND.getAction(), "BLIND action not as expected"), //$NON-NLS-1$
      () -> assertEquals(19, Functions.RESERVED5.getAction(), "RESERVED5 action not as expected"), //$NON-NLS-1$
      () -> assertEquals(20, Functions.HUMIDITY_SENSOR.getAction(), "HUMIDITY_SENSOR action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Functions code = Functions.HANFUN_DEVICE;
    assertEquals("HANFUN_DEVICE", code.stringValue(), "stringValue not as expected");
   }

 }
