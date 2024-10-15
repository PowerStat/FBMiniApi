/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.fb.mini.Color;
import de.powerstat.fb.mini.Hs;
import de.powerstat.fb.mini.Hue;
import de.powerstat.fb.mini.Saturation;
import de.powerstat.fb.mini.Value;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 *
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class HsTests
 {
  /**
   * Default constructor.
   */
  /* default */ HsTests()
   {
    super();
   }


  /**
   * Test correct Hs.
   */
  @Test
  /* default */ void testHsCorrect()
   {
    final List<Color> colors = new ArrayList<>();
    colors.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final Hs cleanHs = Hs.of(1, 5569, "Rot", colors);
    assertEquals("", cleanHs.stringValue(), "Hs not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
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
      /* final Hs cleanHs = */ Hs.of(index, 5569, "Rot", colors);
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
      /* final Hs cleanHs = */ Hs.of(1, -1, "Rot", colors);
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
      /* final Hs cleanHs = */ Hs.of(1, 5569, "Rot", null);
     }, "Null pointer exception expected" //$NON-NLS-1$
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
    final Hs hs = Hs.of(1, 5569, "Rot", colors);
    assertEquals("", hs.stringValue(), "Hs not as expected"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final List<Color> colors1 = new ArrayList<>();
    colors1.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final List<Color> colors2 = new ArrayList<>();
    colors2.add(Color.of(1, Hue.of(1), Saturation.of(1), Value.of(1)));
    colors2.add(Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1)));
    colors2.add(Color.of(3, Hue.of(1), Saturation.of(1), Value.of(1)));
    final Hs hs1 = Hs.of(1, 5569, "Rot", colors1);
    final Hs hs2 = Hs.of(1, 5569, "Rot", colors1);
    final Hs hs3 = Hs.of(2, 5570, "Grün",colors2); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(hs1.hashCode(), hs2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(hs1.hashCode(), hs3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final List<Color> colors1 = new ArrayList<>();
    colors1.add(Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(2, Hue.of(0), Saturation.of(0), Value.of(0)));
    colors1.add(Color.of(3, Hue.of(0), Saturation.of(0), Value.of(0)));
    final List<Color> colors2 = new ArrayList<>();
    colors2.add(Color.of(1, Hue.of(1), Saturation.of(1), Value.of(1)));
    colors2.add(Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1)));
    colors2.add(Color.of(3, Hue.of(1), Saturation.of(1), Value.of(1)));
    final Hs hs1 = Hs.of(1, 5569, "Rot", colors1);
    final Hs hs2 = Hs.of(1, 5569, "Rot", colors1);
    final Hs hs3 = Hs.of(2, 5570, "Grün", colors2); //$NON-NLS-1$
    final Hs hs4 = Hs.of(1, 5569, "Rot", colors1);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(hs1.equals(hs1), "hs11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(hs1.equals(hs2), "hs12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(hs2.equals(hs1), "hs21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(hs2.equals(hs4), "hs24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(hs1.equals(hs4), "hs14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(hs1.equals(hs3), "hs13 are equal"), //$NON-NLS-1$
      () -> assertFalse(hs3.equals(hs1), "hs31 are equal"), //$NON-NLS-1$
      () -> assertFalse(hs1.equals(null), "hs10 is equal") //$NON-NLS-1$
    );
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
    final Hs hs = Hs.of(1, 5569, "Rot", colors1);
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
    final Hs hs1 = Hs.of(1, 5569, "Rot", colors1);
    final Hs hs2 = Hs.of(1, 5569, "Rot", colors1);
    final Hs hs3 = Hs.of(2, 5570, "Grün", colors2); //$NON-NLS-1$
    final Hs hs4 = Hs.of(3, 5571, "Blau", colors3); //$NON-NLS-1$
    final Hs hs5 = Hs.of(1, 5569, "Rot", colors1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(hs1.compareTo(hs2) == -hs2.compareTo(hs1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(hs1.compareTo(hs3) == -hs3.compareTo(hs1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((hs4.compareTo(hs3) > 0) && (hs3.compareTo(hs1) > 0) && (hs4.compareTo(hs1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((hs1.compareTo(hs2) == 0) && (Math.abs(hs1.compareTo(hs5)) == Math.abs(hs2.compareTo(hs5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((hs1.compareTo(hs2) == 0) && hs1.equals(hs2), "equals") //$NON-NLS-1$
    );
   }

 }
