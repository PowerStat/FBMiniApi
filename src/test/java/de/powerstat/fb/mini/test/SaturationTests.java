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

import de.powerstat.fb.mini.Saturation;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Saturation tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class SaturationTests
 {
  /**
   * Default constructor.
   */
  /* default */ SaturationTests()
   {
    super();
   }


  /**
   * Is a saturation value.
   *
   * @param saturation Saturation
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 127, 255})
  /* default */ void testIsSaturationValue(final int saturation)
   {
    assertEquals(saturation, Saturation.of(saturation).intValue(), "Not a saturation value!"); //$NON-NLS-1$
   }


  /**
   * Is not a saturation value.
   *
   * @param saturation Saturation (0-255)
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 256})
  /* default */ void testIsNotAsaturationValue(final int saturation)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Saturation saturation = */ Saturation.of(saturation);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a saturation string value.
   */
  @Test
  /* default */ void testIsSaturationString()
   {
    assertEquals(127, Saturation.of("127").intValue(), "Not a saturation value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Saturation.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Saturation saturation = Saturation.of(1);
    assertEquals("Saturation[saturation=1]", saturation.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Saturation saturation1 = Saturation.of(1);
    final Saturation saturation2 = Saturation.of(1);
    final Saturation saturation3 = Saturation.of(2);
    final Saturation saturation4 = Saturation.of(3);
    final Saturation saturation5 = Saturation.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(saturation1.compareTo(saturation2) == -saturation2.compareTo(saturation1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(saturation1.compareTo(saturation3) == -saturation3.compareTo(saturation1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((saturation4.compareTo(saturation3) > 0) && (saturation3.compareTo(saturation1) > 0) && (saturation4.compareTo(saturation1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((saturation1.compareTo(saturation2) == 0) && (Math.abs(saturation1.compareTo(saturation5)) == Math.abs(saturation2.compareTo(saturation5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((saturation1.compareTo(saturation2) == 0) && saturation1.equals(saturation2), "equals") //$NON-NLS-1$
    );
   }

 }
