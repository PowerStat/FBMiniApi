/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
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


/**
 * AIN tests.
 */
public class AINTests
 {
  /**
   * AIN of 000000000000.
   */
  private static final String AIN0 = "000000000000";


  /**
   * Default constructor.
   */
  public AINTests()
   {
    super();
   }


  /**
   * Test correct AIN.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {AIN0, "00000 0000000"})
  public void ainCorrect(final String ain)
   {
    final AIN cleanAin = AIN.of(ain);
    assertEquals(ain.replaceAll("\\s", ""), cleanAin.getAIN(), "AIN not as expected"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
   }


  /**
   * Test AIN with wrong lengths.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {"00000000000", "0000000000000"})
  public void ainLength(final String ain)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final AIN cleanAin = */ AIN.of(ain);
     }
    );
   }


  /**
   * Test wrong AIN.
   *
   * @param ain AIN
   */
  @ParameterizedTest
  @ValueSource(strings = {"00000000000g"})
  public void ainWrong(final String ain)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final AIN cleanAin = */ AIN.of(ain);
     }
    );
   }


  /**
   * Test get ain.
   */
  @Test
  public void getAin()
   {
    final AIN ain = AIN.of(AIN0);
    assertEquals(AIN0, ain.getAIN(), "AIN not as expected"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  public void testHashCode()
   {
    final AIN bic1 = new AIN(AIN0);
    final AIN bic2 = new AIN(AIN0);
    final AIN bic3 = new AIN("000000000001"); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(bic1.hashCode(), bic2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(bic1.hashCode(), bic3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  public void testEquals()
   {
    final AIN ain1 = new AIN(AIN0);
    final AIN ain2 = new AIN(AIN0);
    final AIN ain3 = new AIN("000000000001"); //$NON-NLS-1$
    final AIN ain4 = new AIN(AIN0);
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
  public void testToString()
   {
    final AIN ain = new AIN(AIN0);
    assertEquals("AIN[ain=000000000000]", ain.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  public void testCompareTo()
   {
    final AIN ain1 = new AIN(AIN0);
    final AIN ain2 = new AIN(AIN0);
    final AIN ain3 = new AIN("000000000001"); //$NON-NLS-1$
    final AIN ain4 = new AIN("000000000002"); //$NON-NLS-1$
    final AIN ain5 = new AIN(AIN0);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(ain1.compareTo(ain2) == -ain2.compareTo(ain1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(ain1.compareTo(ain3) == -ain3.compareTo(ain1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((ain4.compareTo(ain3) > 0) && (ain3.compareTo(ain1) > 0) && (ain4.compareTo(ain1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((ain1.compareTo(ain2) == 0) && (Math.abs(ain1.compareTo(ain5)) == Math.abs(ain2.compareTo(ain5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((ain1.compareTo(ain2) == 0) && ain1.equals(ain2), "equals") //$NON-NLS-1$
    );
   }

 }
