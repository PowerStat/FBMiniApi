/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Color;
import de.powerstat.fb.mini.Hs;
import de.powerstat.fb.mini.Hue;
import de.powerstat.fb.mini.Saturation;
import de.powerstat.fb.mini.Value;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Hs tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class HsTests
 {
  /**
   * HS not as expected.
   */
  private static final String HS_NOT_AS_EXPECTED = "Hs not as expected";

  /**
   * Red.
   */
  private static final String ROT = "Rot";


  /**
   * Default constructor.
   */
  /* default */ HsTests()
   {
    super();
   }


  /**
   * Test correct Hs.
   *
   * @param index Index 1-12
   */
  @ParameterizedTest
  @ValueSource(ints = {1, 12})
  /* default */ void testHsCorrect1(final int index)
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final Hs cleanHs = Hs.of(index, 5569, ROT, colors);
    assertEquals("", cleanHs.stringValue(), HS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test correct Hs.
   */
  @Test
  /* default */ void testHsCorrect2()
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final Hs cleanHs = Hs.of(1, 0, ROT, colors);
    assertEquals("", cleanHs.stringValue(), HS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test wrong Hs.
   *
   * @param index Index 1-12
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 13})
  /* default */ void testHsWrong1(final int index)
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Hs cleanHs = */ Hs.of(index, 5569, ROT, colors);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Hs.
   */
  @Test
  /* default */ void testHsWrong2()
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Hs cleanHs = */ Hs.of(1, -1, ROT, colors);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Hs.
   */
  @Test
  /* default */ void testHsWrong3()
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    assertThrows(NullPointerException.class, () ->
     {
      /* final Hs cleanHs = */ Hs.of(1, 5569, null, colors);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Hs.
   */
  @Test
  /* default */ void testHsWrong4()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Hs cleanHs = */ Hs.of(1, 5569, ROT, null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Hs.
   */
  @Test
  /* default */ void testHsWrong5()
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Hs cleanHs = */ Hs.of(1, 5569, ROT, colors);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final Hs hs = Hs.of(1, 5569, ROT, colors);
    assertEquals("", hs.stringValue(), HS_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Hs.class).withNonnullFields("name", "colors").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final List<Color> colors1 = new ArrayList<>();
    colors1.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final Hs hs = Hs.of(1, 5569, ROT, colors1);
   assertEquals("Hs[index=1, nameEnum=5569, name=Rot, colors=[Color[index=1, hue=Hue[hue=0], saturation=Saturation[saturation=0], value=Value[value=0]], Color[index=2, hue=Hue[hue=0], saturation=Saturation[saturation=0], value=Value[value=0]], Color[index=3, hue=Hue[hue=0], saturation=Saturation[saturation=0], value=Value[value=0]]]]", hs.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final List<Color> colors1 = new ArrayList<>();
    colors1.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final List<Color> colors2 = new ArrayList<>();
    colors2.add(Color.of(1, Hue.of(1), Saturation.of(1), Value.of(1)));
    colors2.add(Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1)));
    colors2.add(Color.of(3, Hue.of(1), Saturation.of(1), Value.of(1)));
    final List<Color> colors3 = new ArrayList<>();
    colors3.add(Color.of(1, Hue.of(2), Saturation.of(2), Value.of(2)));
    colors3.add(Color.of(2, Hue.of(2), Saturation.of(2), Value.of(2)));
    colors3.add(Color.of(3, Hue.of(2), Saturation.of(2), Value.of(2)));
    final Hs hs1 = Hs.of(1, 5569, ROT, colors1);
    final Hs hs2 = Hs.of(1, 5569, ROT, colors1);
    final Hs hs3 = Hs.of(2, 5570, "Grün", colors2); //$NON-NLS-1$
    final Hs hs4 = Hs.of(3, 5571, "Blau", colors3); //$NON-NLS-1$
    final Hs hs5 = Hs.of(1, 5569, ROT, colors1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(hs1.compareTo(hs2) == -hs2.compareTo(hs1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(hs1.compareTo(hs3) == -hs3.compareTo(hs1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((hs4.compareTo(hs3) > 0) && (hs3.compareTo(hs1) > 0) && (hs4.compareTo(hs1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((hs1.compareTo(hs2) == 0) && (Math.abs(hs1.compareTo(hs5)) == Math.abs(hs2.compareTo(hs5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((hs1.compareTo(hs2) == 0) && hs1.equals(hs2), "equals") //$NON-NLS-1$
    );
   }

 }
