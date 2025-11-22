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

import de.powerstat.fb.mini.URIQueryParameter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * URIQueryParameter tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class URIQueryParameterTests
 {
  /**
   * Illegal argument exception expected.
   */
  private static final String ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED = "Illegal argument exception expected"; //$NON-NLS-1$

  /**
   * Not a uri query parameter.
   */
  private static final String NOT_A_URI_QUERY_PARAMETER = "Not a uri query parameter!";

  /**
   * 123.
   */
  private static final String VALUE_123 = "123";

  /**
   * Abc.
   */
  private static final String ABC = "abc";

  /**
   * ABC = 123.
   */
  private static final String ABC_123 = "abc=123";


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
    assertEquals(ABC_123, URIQueryParameter.of(ABC, VALUE_123).stringValue(), NOT_A_URI_QUERY_PARAMETER);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    assertEquals("abc=", URIQueryParameter.of(ABC, "").stringValue(), NOT_A_URI_QUERY_PARAMETER); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    assertEquals(ABC, URIQueryParameter.of(ABC, null).stringValue(), NOT_A_URI_QUERY_PARAMETER);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor4()
   {
    assertEquals(ABC_123, URIQueryParameter.of(ABC_123).stringValue(), NOT_A_URI_QUERY_PARAMETER);
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
      /* final URIQueryParameter param = */ URIQueryParameter.of(ABC);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
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
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Constructor failure test.
   */
  @Test
  /* default */ void testConstructorFail4()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final URIQueryParameter param = */ URIQueryParameter.of("", VALUE_123);
     }, ILLEGAL_ARGUMENT_EXCEPTION_EXPECTED
    );
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(URIQueryParameter.class).withNonnullFields("key").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final URIQueryParameter param = URIQueryParameter.of(ABC, VALUE_123);
    assertEquals("URIQueryParameter[key=abc, value=123]", param.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final URIQueryParameter param1 = URIQueryParameter.of(ABC, VALUE_123);
    final URIQueryParameter param2 = URIQueryParameter.of(ABC, VALUE_123);
    final URIQueryParameter param3 = URIQueryParameter.of("bcd", "234");
    final URIQueryParameter param4 = URIQueryParameter.of("cde", "345");
    final URIQueryParameter param5 = URIQueryParameter.of(ABC, VALUE_123);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param2) == -param2.compareTo(param1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param3) == -param3.compareTo(param1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((param4.compareTo(param3) > 0) && (param3.compareTo(param1) > 0) && (param4.compareTo(param1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && (Math.abs(param1.compareTo(param5)) == Math.abs(param2.compareTo(param5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && param1.equals(param2), "equals") //$NON-NLS-1$
    );
   }

 }
