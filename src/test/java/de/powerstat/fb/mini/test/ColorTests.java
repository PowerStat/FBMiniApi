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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.powerstat.fb.mini.Color;
import de.powerstat.fb.mini.Color;
import de.powerstat.fb.mini.Color;
import de.powerstat.fb.mini.Hue;
import de.powerstat.fb.mini.Saturation;
import de.powerstat.fb.mini.Value;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Color tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class ColorTests
 {
  /**
   * Default constructor.
   */
  /* default */ ColorTests()
   {
    super();
   }


  /**
   * Test correct Color.
   */
  @Test
  /* default */ void testColorCorrect()
   {
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final Value value = Value.of(0);
    final Color cleanColor = Color.of(1, hue, saturation, value);
    assertAll("testColorCorrect", //$NON-NLS-1$
      () -> assertEquals(1, cleanColor.intValue(), "index is not equal"), //$NON-NLS-1$
      () -> assertEquals(hue, cleanColor.hueValue(), "hue is not equal"), //$NON-NLS-1$
      () -> assertEquals(saturation, cleanColor.saturationValue(), "saturation is not equal"), //$NON-NLS-1$
      () -> assertEquals(value, cleanColor.valueValue(), "value is not equal") //$NON-NLS-1$
    );
   }


  /**
   * Test Color index failure.
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 4})
  /* default */ void testColorFail1(final int index)
   {
    final Hue hue = Hue.of(0);
    final Saturation saturation = Saturation.of(0);
    final Value value = Value.of(0);
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Color cleanColor = */ Color.of(index, hue, saturation, value);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test Hue failue.
   */
  @Test
  /* default */ void testColorFail2()
   {
    final Saturation saturation = Saturation.of(0);
    final Value value = Value.of(0);
    assertThrows(NullPointerException.class, () ->
     {
      /* final Color cleanColor = */ Color.of(1, null, saturation, value);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


   /**
    * Test Saturation failue.
    */
   @Test
   /* default */ void testColorFail3()
    {
     final Hue hue = Hue.of(0);
     final Value value = Value.of(0);
     assertThrows(NullPointerException.class, () ->
      {
       /* final Color cleanColor = */ Color.of(1, hue, null, value);
      }, "Null pointer exception expected" //$NON-NLS-1$
     );
    }


    /**
     * Test Value failue.
     */
    @Test
    /* default */ void testColorFail4()
     {
      final Hue hue = Hue.of(0);
      final Saturation saturation = Saturation.of(0);
      assertThrows(NullPointerException.class, () ->
       {
        /* final Color cleanColor = */ Color.of(1, hue, saturation, null);
       }, "Null pointer exception expected" //$NON-NLS-1$
      );
     }


   /**
    * Test stringValue.
    */
   @Test
   /* default */ void testStringValue()
    {
     final Color color = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     assertEquals("", color.stringValue(), "Color not as expected"); //$NON-NLS-1$
    }


   /**
    * Test hash code.
    */
   @Test
   /* default */ void testHashCode()
    {
     final Color color1 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     final Color color2 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     final Color color3 = Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1));
     assertAll("testHashCode", //$NON-NLS-1$
       () -> assertEquals(color1.hashCode(), color2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
       () -> assertNotEquals(color1.hashCode(), color3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
     );
    }


   /**
    * Test equals.
    */
   @Test
   @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
   /* default */ void testEquals()
    {
     final Color color1 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     final Color color2 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     final Color color3 = Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1));
     final Color color4 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     assertAll("testEquals", //$NON-NLS-1$
       () -> assertTrue(color1.equals(color1), "color11 is not equal"), //$NON-NLS-1$
       () -> assertTrue(color1.equals(color2), "color12 are not equal"), //$NON-NLS-1$
       () -> assertTrue(color2.equals(color1), "color21 are not equal"), //$NON-NLS-1$
       () -> assertTrue(color2.equals(color4), "color24 are not equal"), //$NON-NLS-1$
       () -> assertTrue(color1.equals(color4), "color14 are not equal"), //$NON-NLS-1$
       () -> assertFalse(color1.equals(color3), "color13 are equal"), //$NON-NLS-1$
       () -> assertFalse(color3.equals(color1), "color31 are equal"), //$NON-NLS-1$
       () -> assertFalse(color1.equals(null), "color10 is equal") //$NON-NLS-1$
     );
    }


   /**
    * Test toString.
    */
   @Test
   /* default */ void testToString()
    {
     final Color color = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     assertEquals("Color[index=1, hue=Hue[hue=0], saturation=Saturation[saturation=0], value=Value[value=0]]", color.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
    }


   /**
    * Test compareTo.
    */
   @Test
   @SuppressWarnings("java:S5785")
   /* default */ void testCompareTo()
    {
     final Color color1 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     final Color color2 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     final Color color3 = Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1)); //$NON-NLS-1$
     final Color color4 = Color.of(3, Hue.of(2), Saturation.of(2), Value.of(2)); //$NON-NLS-1$
     final Color color5 = Color.of(1, Hue.of(0), Saturation.of(0), Value.of(0));
     assertAll("testCompareTo", //$NON-NLS-1$
       () -> assertTrue(color1.compareTo(color2) == -color2.compareTo(color1), "reflexive1"), //$NON-NLS-1$
       () -> assertTrue(color1.compareTo(color3) == -color3.compareTo(color1), "reflexive2"), //$NON-NLS-1$
       () -> assertTrue((color4.compareTo(color3) > 0) && (color3.compareTo(color1) > 0) && (color4.compareTo(color1) > 0), "transitive1"), //$NON-NLS-1$
       () -> assertTrue((color1.compareTo(color2) == 0) && (Math.abs(color1.compareTo(color5)) == Math.abs(color2.compareTo(color5))), "sgn1"), //$NON-NLS-1$
       () -> assertTrue((color1.compareTo(color2) == 0) && color1.equals(color2), "equals") //$NON-NLS-1$
     );
    }

 }
