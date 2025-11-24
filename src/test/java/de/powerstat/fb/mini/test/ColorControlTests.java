/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import java.util.EnumSet;

import de.powerstat.fb.mini.ColorControl;
import de.powerstat.fb.mini.ColorModes;
import de.powerstat.fb.mini.Hue;
import de.powerstat.fb.mini.Saturation;
import de.powerstat.fb.mini.TemperatureKelvin;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Color control tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class ColorControlTests
 {
  /**
   * Illegal argument exception expected.
   */
  private static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED = "Illegal argument exception expected"; //$NON-NLS-1$

  /**
   * Null pointer exception expected.
   */
  private static final String NULL_POINTER_EXCEPTION_EXPECTED = "Null pointer exception expected"; //$NON-NLS-1$

  /**
   * Color control not as expected.
   */
  private static final String COLOR_CONTROL_NOT_AS_EXPECTED = "ColorControl not as expected";


  /**
   * Default constructor.
   */
  /* default */ ColorControlTests()
   {
    super();
   }


  /**
   * Test correct ColorControl.
   */
  @Test
  /* default */ void testColorControlCorrect1()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final ColorControl cleanColorControl = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, null);
    assertEquals("", cleanColorControl.stringValue(), COLOR_CONTROL_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test correct ColorControl.
   */
  @Test
  /* default */ void testColorControlCorrect2()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final ColorControl cleanColorControl = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, true, hue, saturation, hue, saturation, null);
    assertEquals("", cleanColorControl.stringValue(), COLOR_CONTROL_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test correct ColorControl.
   */
  @Test
  /* default */ void testColorControlCorrect3()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final ColorControl cleanColorControl = ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, null, null, null, null, TemperatureKelvin.of(2700));
    assertEquals("", cleanColorControl.stringValue(), COLOR_CONTROL_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(null, ColorModes.COLOR_TEMPERATURE, false, false, null, null, null, null, TemperatureKelvin.of(2700));
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong2()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.noneOf(ColorModes.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, null, null, null, null, TemperatureKelvin.of(2700));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong3()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(NullPointerException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, null, saturation, hue, saturation, null);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong4()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(NullPointerException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, null, hue, saturation, null);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong5()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(NullPointerException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, null, saturation, null);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong6()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(NullPointerException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, null, null);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong7()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, TemperatureKelvin.of(2700));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong8()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, Hue.of(1), saturation, null);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong9()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, Saturation.of(1), null);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong10()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    assertThrows(NullPointerException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, null, null, null, null, null);
     }, NULL_POINTER_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong11()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, Hue.of(0), null, null, null, TemperatureKelvin.of(2700));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong12()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, null, Saturation.of(0), null, null, TemperatureKelvin.of(2700));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong13()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, null, null, Hue.of(0), null, TemperatureKelvin.of(2700));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test wrong ColorControl.
   */
  @Test
  /* default */ void testColorControlWrong14()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ColorControl cleanColorControl = */ ColorControl.of(supportedModes, ColorModes.COLOR_TEMPERATURE, false, false, null, null, null, Saturation.of(0), TemperatureKelvin.of(2700));
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final ColorControl cleanColorControl = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, null);
    assertEquals("", cleanColorControl.stringValue(), COLOR_CONTROL_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(ColorControl.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final ColorControl colorControl = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, null);
    assertEquals("ColorControl[supportedModes=[HUE_SATURATION, COLOR_TEMPERATURE], currentMode=HUE_SATURATION, fullcolorsupport=false, mapped=false, hue=Hue[hue=0], saturation=Saturation[saturation=0], unmappedHue=Hue[hue=0], unmappedSaturation=Saturation[saturation=0], temperature=null]", colorControl.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final EnumSet<ColorModes> supportedModes = EnumSet.allOf(ColorModes.class);
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final ColorControl colorControl1 = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, null);
    final ColorControl colorControl2 = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, null);
    final ColorControl colorControl3 = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, true, false, hue, saturation, hue, saturation, null);
    final ColorControl colorControl4 = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, true, true, hue, saturation, hue, saturation, null);
    final ColorControl colorControl5 = ColorControl.of(supportedModes, ColorModes.HUE_SATURATION, false, false, hue, saturation, hue, saturation, null);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(colorControl1.compareTo(colorControl2) == -colorControl2.compareTo(colorControl1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(colorControl1.compareTo(colorControl3) == -colorControl3.compareTo(colorControl1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((colorControl4.compareTo(colorControl3) > 0) && (colorControl3.compareTo(colorControl1) > 0) && (colorControl4.compareTo(colorControl1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((colorControl1.compareTo(colorControl2) == 0) && (Math.abs(colorControl1.compareTo(colorControl5)) == Math.abs(colorControl2.compareTo(colorControl5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((colorControl1.compareTo(colorControl2) == 0) && colorControl1.equals(colorControl2), "equals") //$NON-NLS-1$
    );
   }

 }
