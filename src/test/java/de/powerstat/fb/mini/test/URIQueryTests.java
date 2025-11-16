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
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
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
