/*
 * Copyright (C) 2022-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.fb.mini.SID;


/**
 * SID tests.
 */
final class SIDTests
 {
  /**
   * SID 1.
   */
  private static final String SID_ONE = "0000000000000001";


  /**
   * Default constructor.
   */
  /* default */ SIDTests()
   {
    super();
   }


  /**
   * Construtor test.
   */
  @Test
  /* default */ void testConstructor1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final SID test = */ SID.of(null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Construtor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final SID test = */ SID.of(""); //$NON-NLS-1$
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Construtor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final SID test = */ SID.of("0123456789abcdeg"); //$NON-NLS-1$
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Construtor test.
   */
  @Test
  /* default */ void testConstructor4()
   {
    assertNotNull(SID.of("0123456789abcdef"), "Could not create SID."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testOf1()
   {
    assertNotNull(SID.of("0123456789abcdef"), "Could not create SID."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Factory invalid and getSID test.
   */
  @Test
  /* default */ void testOfInvalid()
   {
    assertEquals("0000000000000000", SID.ofInvalid().stringValue(), "Not INVALID."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Is valid session test.
   */
  @Test
  /* default */ void testIsValidSessionFalse()
   {
    assertFalse(SID.ofInvalid().isValidSession(), "Session not as expected."); //$NON-NLS-1$
   }


  /**
   * Is valid session test.
   */
  @Test
  /* default */ void testIsValidSessionTrue()
   {
    assertTrue(SID.of(SID_ONE).isValidSession(), "Session not as expected."); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final SID sid1 = SID.of(SID_ONE);
    final SID sid2 = SID.of(SID_ONE);
    final SID sid3 = SID.of("0000000000000002"); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(sid1.hashCode(), sid2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(sid1.hashCode(), sid3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  // @SuppressFBWarnings("EC_NULL_ARG")
  /* default */ void testEquals()
   {
    final SID sid1 = SID.of(SID_ONE);
    final SID sid2 = SID.of(SID_ONE);
    final SID sid3 = SID.of("0000000000000002"); //$NON-NLS-1$
    final SID sid4 = SID.of(SID_ONE);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(sid1.equals(sid1), "sid11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(sid1.equals(sid2), "sid12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(sid2.equals(sid1), "sid21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(sid2.equals(sid4), "sid24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(sid1.equals(sid4), "sid14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(sid1.equals(sid3), "sid13 are equal"), //$NON-NLS-1$
      () -> assertFalse(sid3.equals(sid1), "sid31 are equal"), //$NON-NLS-1$
      () -> assertFalse(sid1.equals(null), "sid10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final SID sid = SID.of(SID_ONE);
    assertEquals("SID[sid=0000000000000001]", sid.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final SID sid1 = SID.of(SID_ONE);
    final SID sid2 = SID.of(SID_ONE);
    final SID sid3 = SID.of("0000000000000002"); //$NON-NLS-1$
    final SID sid4 = SID.of("0000000000000003"); //$NON-NLS-1$
    final SID sid5 = SID.of(SID_ONE);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(sid1.compareTo(sid2) == -sid2.compareTo(sid1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(sid1.compareTo(sid3) == -sid3.compareTo(sid1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((sid4.compareTo(sid3) > 0) && (sid3.compareTo(sid1) > 0) && (sid4.compareTo(sid1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((sid1.compareTo(sid2) == 0) && (Math.abs(sid1.compareTo(sid5)) == Math.abs(sid2.compareTo(sid5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((sid1.compareTo(sid2) == 0) && sid1.equals(sid2), "equals") //$NON-NLS-1$
    );
   }

 }
