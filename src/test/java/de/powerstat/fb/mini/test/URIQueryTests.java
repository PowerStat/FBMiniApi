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

import de.powerstat.fb.mini.URIQuery;
import de.powerstat.fb.mini.URIQueryParameter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * URIQuery tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class URIQueryTests
 {
  /**
   * Default constructor.
   */
  /* default */ URIQueryTests()
   {
    super();
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor1()
   {
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    assertEquals("", query.stringValue(), "Not a uri query!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("abc", "123"));
    assertEquals("?abc=123", query.stringValue(), "Not a uri query!"); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of("abc", "123"));
    query.addEntry(URIQueryParameter.of("def", "456"));
    assertEquals("?abc=123&def=456", query.stringValue(), "Not a uri query!"); //$NON-NLS-1$
   }


  /**
   * Constructor failure test.
   */
  @Test
  /* default */ void testConstructorFail1()
   {
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    assertThrows(NullPointerException.class, () ->
     {
      query.addEntry(null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final URIQuery<URIQueryParameter> param1 = new URIQuery<>();
    param1.addEntry(URIQueryParameter.of("abc", "123"));
    final URIQuery<URIQueryParameter> param2 = new URIQuery<>();
    param2.addEntry(URIQueryParameter.of("abc", "123"));
    final URIQuery<URIQueryParameter> param3 = new URIQuery<>();
    param3.addEntry(URIQueryParameter.of("def", "456"));
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
    final URIQuery<URIQueryParameter> param1 = new URIQuery<>();
    param1.addEntry(URIQueryParameter.of("abc", "123"));
    final URIQuery<URIQueryParameter> param2 = new URIQuery<>();
    param2.addEntry(URIQueryParameter.of("abc", "123"));
    final URIQuery<URIQueryParameter> param3 = new URIQuery<>();
    param3.addEntry(URIQueryParameter.of("def", "456"));
    final URIQuery<URIQueryParameter> param4 = new URIQuery<>();
    param4.addEntry(URIQueryParameter.of("abc", "123"));
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
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    param.addEntry(URIQueryParameter.of("abc", "123"));
    param.addEntry(URIQueryParameter.of("def", "456"));
    assertEquals("URIQuery<>[abc=123&def=456]", param.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test isEmpty.
   */
  @Test
  /* default */ void testisEmptyFalse()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    param.addEntry(URIQueryParameter.of("abc", "123"));
    assertFalse(param.isEmpty(), "isEmpty is true"); //$NON-NLS-1$
   }


  /**
   * Test isEmpty.
   */
  @Test
  /* default */ void testisEmptyTrue()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    assertTrue(param.isEmpty(), "isEmpty is false"); //$NON-NLS-1$
   }


  /**
   * Test iterator.
   */
  @Test
  /* default */ void testIterator1()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    param.addEntry(URIQueryParameter.of("abc", "123"));
    param.addEntry(URIQueryParameter.of("def", "456"));
    int counter = 0;
    for (final URIQueryParameter qparam : param)
     {
      ++counter;
     }
    assertEquals(2, counter, "Iterator does not work as expected");
   }

 }
