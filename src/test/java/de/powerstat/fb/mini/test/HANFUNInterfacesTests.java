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

import de.powerstat.fb.mini.HANFUNInterfaces;


/**
 * Funcions tests.
 */
final class HANFUNInterfacesTests
 {
  /**
   * Default constructor.
   */
  /* default */ HANFUNInterfacesTests()
   {
    super();
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(277, HANFUNInterfaces.of("KEEP_ALIVE").getAction(), "Action not as expected");
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(277, HANFUNInterfaces.of(277).getAction(), "Action not as expected");
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* HANFUNInterfaces hanfun = */ HANFUNInterfaces.of(999);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of HANFUNInterfaces.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(277, HANFUNInterfaces.KEEP_ALIVE.getAction(), "KEEP_ALIVE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(256, HANFUNInterfaces.ALERT.getAction(), "ALERT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(512, HANFUNInterfaces.ON_OFF.getAction(), "ON_OFF action not as expected"), //$NON-NLS-1$
      () -> assertEquals(513, HANFUNInterfaces.LEVEL_CTRL.getAction(), "LEVEL_CTRL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(514, HANFUNInterfaces.COLOR_CTRL.getAction(), "COLOR_CTRL action not as expected"), //$NON-NLS-1$
      () -> assertEquals(516, HANFUNInterfaces.OPEN_CLOSE.getAction(), "OPEN_CLOSE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(517, HANFUNInterfaces.OPEN_CLOSE_CONFIG.getAction(), "OPEN_CLOSE_CONFIG action not as expected"), //$NON-NLS-1$
      () -> assertEquals(772, HANFUNInterfaces.SIMPLE_BUTTON.getAction(), "SIMPLE_BUTTON action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1024, HANFUNInterfaces.SUOTA_UPDATE.getAction(), "SUOTA_UPDATE action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final HANFUNInterfaces code = HANFUNInterfaces.KEEP_ALIVE;
    assertEquals("KEEP_ALIVE", code.stringValue(), "stringValue not as expected");
   }

 }
