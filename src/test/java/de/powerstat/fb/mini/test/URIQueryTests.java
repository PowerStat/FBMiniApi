/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

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
   * 456.
   */
  private static final String VALUE_456 = "456";

  /**
   * def.
   */
  private static final String KEY_DEF = "def";

  /**
   * 123.
   */
  private static final String VALUE_123 = "123";

  /**
   * abc.
   */
  private static final String KEY_ABC = "abc";

  /**
   * Not a uri query.
   */
  private static final String NOT_A_URI_QUERY = "Not a uri query!";


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
    assertEquals("", query.stringValue(), NOT_A_URI_QUERY); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of(KEY_ABC, VALUE_123));
    assertEquals("?abc=123", query.stringValue(), NOT_A_URI_QUERY); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    final URIQuery<URIQueryParameter> query = new URIQuery<>();
    query.addEntry(URIQueryParameter.of(KEY_ABC, VALUE_123));
    query.addEntry(URIQueryParameter.of(KEY_DEF, VALUE_456));
    assertEquals("?abc=123&def=456", query.stringValue(), NOT_A_URI_QUERY); //$NON-NLS-1$
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
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(URIQuery.class).withNonnullFields("queries").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    param.addEntry(URIQueryParameter.of(KEY_ABC, VALUE_123));
    param.addEntry(URIQueryParameter.of(KEY_DEF, VALUE_456));
    assertEquals("URIQuery<>[abc=123&def=456]", param.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test isEmpty.
   */
  @Test
  /* default */ void testIsEmptyFalse()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    param.addEntry(URIQueryParameter.of(KEY_ABC, VALUE_123));
    assertFalse(param.isEmpty(), "isEmpty is true"); //$NON-NLS-1$
   }


  /**
   * Test isEmpty.
   */
  @Test
  /* default */ void testIsEmptyTrue()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    assertTrue(param.isEmpty(), "isEmpty is false"); //$NON-NLS-1$
   }


  /**
   * Test iterator.
   */
  @SuppressWarnings({"UnusedLocalVariable", "PMD.UnusedLocalVariable"})
  @Test
  /* default */ void testIterator1()
   {
    final URIQuery<URIQueryParameter> param = new URIQuery<>();
    param.addEntry(URIQueryParameter.of(KEY_ABC, VALUE_123));
    param.addEntry(URIQueryParameter.of(KEY_DEF, VALUE_456));
    int counter = 0;
    for (final URIQueryParameter qparam : param)
     {
      ++counter;
     }
    assertEquals(2, counter, "Iterator does not work as expected");
   }

 }
