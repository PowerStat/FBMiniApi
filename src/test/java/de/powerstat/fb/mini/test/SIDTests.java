/*
 * Copyright (C) 2022 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
public class SIDTests
 {
  /**
   * Construtor test.
   */
  @Test
  public void constructor1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final SID test = */ new SID(null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Construtor test.
   */
  @Test
  public void constructor2()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final SID test = */ new SID(""); //$NON-NLS-1$
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Construtor test.
   */
  @Test
  public void constructor3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final SID test = */ new SID("0123456789abcdeg"); //$NON-NLS-1$
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Construtor test.
   */
  @Test
  public void constructor4()
   {
    assertNotNull(new SID("0123456789abcdef"), "Could not create SID."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Factory test.
   */
  @Test
  public void of1()
   {
    assertNotNull(SID.of("0123456789abcdef"), "Could not create SID."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Factory invalid and getSID test.
   */
  @Test
  public void ofInvalid()
   {
    assertEquals("0000000000000000", SID.ofInvalid().stringValue(), "Not INVALID."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Is valid session test.
   */
  @Test
  public void isValidSessionFalse()
   {
    assertFalse(SID.ofInvalid().isValidSession(), "Session not as expected."); //$NON-NLS-1$
   }


  /**
   * Is valid session test.
   */
  @Test
  public void isValidSessionTrue()
   {
    assertTrue(SID.of("0000000000000001").isValidSession(), "Session not as expected."); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test hash code.
   */
  @Test
  public void testHashCode()
   {
    final SID sid1 = new SID("0000000000000001"); //$NON-NLS-1$
    final SID sid2 = new SID("0000000000000001"); //$NON-NLS-1$
    final SID sid3 = new SID("0000000000000002"); //$NON-NLS-1$
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(sid1.hashCode(), sid2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(sid1.hashCode(), sid3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  public void testEquals()
   {
    final SID sid1 = new SID("0000000000000001"); //$NON-NLS-1$
    final SID sid2 = new SID("0000000000000001"); //$NON-NLS-1$
    final SID sid3 = new SID("0000000000000002"); //$NON-NLS-1$
    final SID sid4 = new SID("0000000000000001"); //$NON-NLS-1$
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
  public void testToString()
   {
    final SID sid = new SID("0000000000000001"); //$NON-NLS-1$
    assertEquals("SID[sid=0000000000000001]", sid.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  public void testCompareTo()
   {
    final SID sid1 = new SID("0000000000000001"); //$NON-NLS-1$
    final SID sid2 = new SID("0000000000000001"); //$NON-NLS-1$
    final SID sid3 = new SID("0000000000000002"); //$NON-NLS-1$
    final SID sid4 = new SID("0000000000000003"); //$NON-NLS-1$
    final SID sid5 = new SID("0000000000000001"); //$NON-NLS-1$
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(sid1.compareTo(sid2) == -sid2.compareTo(sid1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(sid1.compareTo(sid3) == -sid3.compareTo(sid1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((sid4.compareTo(sid3) > 0) && (sid3.compareTo(sid1) > 0) && (sid4.compareTo(sid1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((sid1.compareTo(sid2) == 0) && (Math.abs(sid1.compareTo(sid5)) == Math.abs(sid2.compareTo(sid5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((sid1.compareTo(sid2) == 0) && sid1.equals(sid2), "equals") //$NON-NLS-1$
    );
   }

 }
