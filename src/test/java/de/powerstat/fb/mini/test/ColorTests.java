/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import nl.jqno.equalsverifier.EqualsVerifier;

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
   * Null pointer exception expected.
   */
  private static final String NULL_POINTER_EXCEPTION_EXPECTED = "Null pointer exception expected"; //$NON-NLS-1$


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
   *
   * @param index Color index
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
     }, NULL_POINTER_EXCEPTION_EXPECTED
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
      }, NULL_POINTER_EXCEPTION_EXPECTED
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
       }, NULL_POINTER_EXCEPTION_EXPECTED
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
    * Equalsverifier.
    */
   @Test
   /* default */ void testEqualsContract()
    {
     EqualsVerifier.forClass(Color.class).withNonnullFields("hue", "saturation", "value").verify();
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
     final Color color3 = Color.of(2, Hue.of(1), Saturation.of(1), Value.of(1));
     final Color color4 = Color.of(3, Hue.of(2), Saturation.of(2), Value.of(2));
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
