/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.ColorModes;


/**
 * Color modes tests.
 */
final class ColorModesTests
 {
  /**
   * Default constructor.
   */
  /* default */ ColorModesTests()
   {
    super();
   }


  /**
   * Factory string test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(1, ColorModes.of("HUE_SATURATION").getAction(), "Action not as expected");
   }


  /**
   * Factory int test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(1, ColorModes.of(1).getAction(), "Action not as expected");
   }


  /**
   * Factory int test.
   */
  @Test
  /* default */ void testFactory3()
   {

    assertThrows(IllegalArgumentException.class, () ->
     {
      /* ColorModes modes = */ ColorModes.of(99);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of ColorModes.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(1, ColorModes.HUE_SATURATION.getAction(), "HUE_SATURATION action not as expected"), //$NON-NLS-1$
      () -> assertEquals(4, ColorModes.COLOR_TEMPERATURE.getAction(), "COLOR_TEMPERAURE action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final ColorModes code = ColorModes.HUE_SATURATION;
    assertEquals("HUE_SATURATION", code.stringValue(), "stringValue not as expected");
   }

 }
