/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
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
   * Path separator.
   */
  private static final String PATH_SEPARATOR = "/";

  /**
   * Abc path.
   */
  private static final String ABC_PATH = "/abc";

  /**
   * Hello world path.
   */
  private static final String HELLO_WORLD_PATH = "/hello/world";

  /**
   * Hello path.
   */
  private static final String HELLO_PATH = "/hello";

  /**
   * Not a uri path parameter.
   */
  private static final String NOT_A_URI_PATH_PARAMETER = "Not a uri path parameter!";


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
    assertEquals(PATH_SEPARATOR, URIPath.of("").stringValue(), NOT_A_URI_PATH_PARAMETER); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor2()
   {
    assertEquals(PATH_SEPARATOR, URIPath.of(PATH_SEPARATOR).stringValue(), NOT_A_URI_PATH_PARAMETER);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor3()
   {
    assertEquals(HELLO_PATH, URIPath.of(HELLO_PATH).stringValue(), NOT_A_URI_PATH_PARAMETER);
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor4()
   {
    assertEquals(HELLO_PATH, URIPath.of("hello").stringValue(), NOT_A_URI_PATH_PARAMETER); //$NON-NLS-1$
   }


  /**
   * Constructor test.
   */
  @Test
  /* default */ void testConstructor5()
   {
    assertEquals(HELLO_WORLD_PATH, URIPath.of(HELLO_WORLD_PATH).stringValue(), NOT_A_URI_PATH_PARAMETER);
   }


  /**
   * Test get parent.
   */
  @Test
  /* default */ void testGetParent1()
   {
    assertEquals(URIPath.of(HELLO_PATH), URIPath.of(HELLO_WORLD_PATH).getParent(), NOT_A_URI_PATH_PARAMETER);
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
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(URIPath.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final URIPath param = URIPath.of(ABC_PATH);
    assertEquals("URIPath[path=/abc]", param.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final URIPath param1 = URIPath.of(ABC_PATH);
    final URIPath param2 = URIPath.of(ABC_PATH);
    final URIPath param3 = URIPath.of("/bcd");
    final URIPath param4 = URIPath.of("/cde");
    final URIPath param5 = URIPath.of(ABC_PATH);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param2) == -param2.compareTo(param1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(param1.compareTo(param3) == -param3.compareTo(param1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((param4.compareTo(param3) > 0) && (param3.compareTo(param1) > 0) && (param4.compareTo(param1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && (Math.abs(param1.compareTo(param5)) == Math.abs(param2.compareTo(param5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((param1.compareTo(param2) == 0) && param1.equals(param2), "equals") //$NON-NLS-1$
    );
   }

 }
