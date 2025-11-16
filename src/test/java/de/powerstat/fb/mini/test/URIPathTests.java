/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

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
   * Test get parent.
   */
  @Test
  /* default */ void testGetParent1()
   {
    assertEquals(URIPath.of("/hello"), URIPath.of("/hello/world").getParent(), "Not a uri path parameter!"); //$NON-NLS-1$
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
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(URIPath.class).verify();
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
