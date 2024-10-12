/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.fb.mini.URIQueryParameter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * URIQueryParameter tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class URIQueryParameterTests
 {
  /**
   * Default constructor.
   */
  /* default */ URIQueryParameterTests()
   {
    super();
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor1()
   {
    assertEquals("abc=123", URIQueryParameter.of("abc", "123").stringValue(), "Not a uri query parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    assertEquals("abc=", URIQueryParameter.of("abc", "").stringValue(), "Not a uri query parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    assertEquals("abc", URIQueryParameter.of("abc", null).stringValue(), "Not a uri query parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor4()
   {
    assertEquals("abc=123", URIQueryParameter.of("abc=123").stringValue(), "Not a uri query parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor failure test.
   */
  @Test
  /* default */ void testConstructorFail1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final URIQueryParameter param = */ URIQueryParameter.of(null, null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Constructor failure test.
   */
  @Test
  /* default */ void testConstructorFail2()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final URIQueryParameter param = */ URIQueryParameter.of("abc");
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Constructor failure test.
   */
  @Test
  /* default */ void testConstructorFail3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final URIQueryParameter param = */ URIQueryParameter.of("abc=123=def");
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final URIQueryParameter param1 = URIQueryParameter.of("abc", "123");
    final URIQueryParameter param2 = URIQueryParameter.of("abc", "123");
    final URIQueryParameter param3 = URIQueryParameter.of("abc", "");
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(param1.hashCode(), param2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(param1.hashCode(), param3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final URIQueryParameter param1 = URIQueryParameter.of("abc", "123");
    final URIQueryParameter param2 = URIQueryParameter.of("abc", "123");
    final URIQueryParameter param3 = URIQueryParameter.of("bcd", "456");
    final URIQueryParameter param4 = URIQueryParameter.of("abc", "123");
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(param1.equals(param1), "param11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(param1.equals(param2), "param12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(param2.equals(param1), "param21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(param2.equals(param4), "param24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(param1.equals(param4), "param14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(param1.equals(param3), "param13 are equal"), //$NON-NLS-1$
      () -> assertFalse(param3.equals(param1), "param31 are equal"), //$NON-NLS-1$
      () -> assertFalse(param1.equals(null), "param10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final URIQueryParameter param = URIQueryParameter.of("abc", "123");
    assertEquals("URIQueryParameter[key=abc, value=123]", param.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final URIQueryParameter param1 = URIQueryParameter.of("abc", "123");
    final URIQueryParameter param2 = URIQueryParameter.of("abc", "123");
    final URIQueryParameter param3 = URIQueryParameter.of("bcd", "234");
    final URIQueryParameter param4 = URIQueryParameter.of("cde", "345");
    final URIQueryParameter param5 = URIQueryParameter.of("abc", "123");
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param2) == -param2.compareTo(param1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param3) == -param3.compareTo(param1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((param4.compareTo(param3) > 0) && (param3.compareTo(param1) > 0) && (param4.compareTo(param1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && (Math.abs(param1.compareTo(param5)) == Math.abs(param2.compareTo(param5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && param1.equals(param2), "equals") //$NON-NLS-1$
    );
   }

 }
