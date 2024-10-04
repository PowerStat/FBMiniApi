/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.fb.mini.AIN;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * AIN tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class AINTests
 {
  /**
   * AIN of 000000000000.
   */
  private static final String AIN0 = "000000000000";


  /**
   * Default constructor.
   */
  /* default */ AINTests()
   {
    super();
   }


  /**
   * Test correct AIN.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {AINTests.AIN0, "00000 0000000", "00000 0000000-0", "000000000000-0", "Z0000000000000000", "Z000000000000000000", "tmp000000-0000", "tmp000000-00000", "tmp000000-000000", "tmp000000-0000000", "tmp000000-00000000", "tmp000000-000000000"})
  /* default */ void testAinCorrect(final String ain)
   {
    final AIN cleanAin = AIN.of(ain);
    assertEquals(ain.replaceAll("\\s", ""), cleanAin.stringValue(), "AIN not as expected"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test AIN with wrong lengths.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {"00000000000", "0000000000000", "000000000000000"})
  /* default */ void testAinLength(final String ain)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final AIN cleanAin = */ AIN.of(ain);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong AIN.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {"00000000000g", "000000000000-g", "000000000000+0"})
  /* default */ void testAinWrong(final String ain)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final AIN cleanAin = */ AIN.of(ain);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final AIN ain = AIN.of(AINTests.AIN0);
    assertEquals(AINTests.AIN0, ain.stringValue(), "AIN not as expected"); //$NON-NLS-1$
   }


  /**
   * Test isTemplate.
   */
  @Test
  /* default */ void testIsTemplate()
   {
    final AIN ain = AIN.of("tmp000000-0000");
    assertTrue(ain.isTemplate(), "AIN is not a template"); //$NON-NLS-1$
   }


  /**
   * Test isTemplate.
   */
  @Test
  /* default */ void testIsNoTemplate()
   {
    final AIN ain = AIN.of("000000000000-0");
    assertFalse(ain.isTemplate(), "AIN is a template"); //$NON-NLS-1$
   }


  /**
   * Test isZigbee.
   */
  @Test
  /* default */ void testIsZigbee()
   {
    final AIN ain = AIN.of("Z0000000000000000");
    assertTrue(ain.isZigbee(), "AIN is not a zigbee"); //$NON-NLS-1$
   }


  /**
   * Test isZigbee.
   */
  @Test
  /* default */ void testIsNoZigbee()
   {
    final AIN ain = AIN.of("000000000000-0");
    assertFalse(ain.isZigbee(), "AIN is a zigbee"); //$NON-NLS-1$
   }


  /**
   * Test isUnit.
   */
  @ParameterizedTest
  @ValueSource(strings = {"000000000000-0", "Z000000000000000000"})
  /* default */ void testIsUnit(final String ain)
   {
    final AIN cleanAin = AIN.of(ain);
    assertTrue(cleanAin.isUnit(), "AIN is not a unit"); //$NON-NLS-1$
   }


  /**
   * Test isUnit.
   */
  @ParameterizedTest
  @ValueSource(strings = {"000000000000", "Z0000000000000000"})
  /* default */ void testIsNoUnit(final String ain)
   {
    final AIN cleanAin = AIN.of(ain);
    assertFalse(cleanAin.isUnit(), "AIN is a unit"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final AIN ain1 = AIN.of(AINTests.AIN0);
    final AIN ain2 = AIN.of(AINTests.AIN0);
    final AIN ain3 = AIN.of("000000000001"); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(ain1.hashCode(), ain2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(ain1.hashCode(), ain3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final AIN ain1 = AIN.of(AINTests.AIN0);
    final AIN ain2 = AIN.of(AINTests.AIN0);
    final AIN ain3 = AIN.of("000000000001"); //$NON-NLS-1$
    final AIN ain4 = AIN.of(AINTests.AIN0);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(ain1.equals(ain1), "ain11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(ain1.equals(ain2), "ain12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(ain2.equals(ain1), "ain21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(ain2.equals(ain4), "ain24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(ain1.equals(ain4), "ain14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(ain1.equals(ain3), "ain13 are equal"), //$NON-NLS-1$
      () -> assertFalse(ain3.equals(ain1), "ain31 are equal"), //$NON-NLS-1$
      () -> assertFalse(ain1.equals(null), "ain10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final AIN ain = AIN.of(AINTests.AIN0);
    assertEquals("AIN[ain=000000000000]", ain.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final AIN ain1 = AIN.of(AINTests.AIN0);
    final AIN ain2 = AIN.of(AINTests.AIN0);
    final AIN ain3 = AIN.of("000000000001"); //$NON-NLS-1$
    final AIN ain4 = AIN.of("000000000002"); //$NON-NLS-1$
    final AIN ain5 = AIN.of(AINTests.AIN0);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(ain1.compareTo(ain2) == -ain2.compareTo(ain1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(ain1.compareTo(ain3) == -ain3.compareTo(ain1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((ain4.compareTo(ain3) > 0) && (ain3.compareTo(ain1) > 0) && (ain4.compareTo(ain1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((ain1.compareTo(ain2) == 0) && (Math.abs(ain1.compareTo(ain5)) == Math.abs(ain2.compareTo(ain5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((ain1.compareTo(ain2) == 0) && ain1.equals(ain2), "equals") //$NON-NLS-1$
    );
   }

 }
