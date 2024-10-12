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

import de.powerstat.fb.mini.URIPath;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * URIPath tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class URIPathTests
 {
  /**
   * Default constructor.
   */
  /* default */ URIPathTests()
   {
    super();
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor1()
   {
    assertEquals("/", URIPath.of("").stringValue(), "Not a uri path parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    assertEquals("/", URIPath.of("/").stringValue(), "Not a uri path parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    assertEquals("/hello", URIPath.of("/hello").stringValue(), "Not a uri path parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor4()
   {
    assertEquals("/hello", URIPath.of("hello").stringValue(), "Not a uri path parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor5()
   {
    assertEquals("/hello/world", URIPath.of("/hello/world").stringValue(), "Not a uri path parameter!"); //$NON-NLS-1$
   }


  /**
   * Constructor failure test.
   */
  @Test
  /* default */ void testConstructorFail1()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final URIPath path = */ URIPath.of(null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final URIPath param1 = URIPath.of("/abc");
    final URIPath param2 = URIPath.of("/abc");
    final URIPath param3 = URIPath.of("/def");
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
    final URIPath param1 = URIPath.of("/abc");
    final URIPath param2 = URIPath.of("/abc");
    final URIPath param3 = URIPath.of("/bcd");
    final URIPath param4 = URIPath.of("/abc");
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
    final URIPath param = URIPath.of("/abc");
    assertEquals("URIPath[path=/abc]", param.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final URIPath param1 = URIPath.of("/abc");
    final URIPath param2 = URIPath.of("/abc");
    final URIPath param3 = URIPath.of("/bcd");
    final URIPath param4 = URIPath.of("/cde");
    final URIPath param5 = URIPath.of("/abc");
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param2) == -param2.compareTo(param1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param3) == -param3.compareTo(param1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((param4.compareTo(param3) > 0) && (param3.compareTo(param1) > 0) && (param4.compareTo(param1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && (Math.abs(param1.compareTo(param5)) == Math.abs(param2.compareTo(param5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && param1.equals(param2), "equals") //$NON-NLS-1$
    );
   }

 }
