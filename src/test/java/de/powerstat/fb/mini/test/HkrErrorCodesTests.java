/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.HkrErrorCodes;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Hkr error codes tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class HkrErrorCodesTests
 {
  /**
   * Default constructor.
   */
  /* default */ HkrErrorCodesTests()
   {
    super();
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(0, HkrErrorCodes.of("NO_ERROR").getAction(), "Action not as expected");
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(1, HkrErrorCodes.of(1).getAction(), "Action not as expected");
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* HkrErrorCodes code = */ HkrErrorCodes.of(999);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of HkrErrorCodes.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(0, HkrErrorCodes.NO_ERROR.getAction(), "NO_ERROR action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1, HkrErrorCodes.NO_ADAPTION.getAction(), "NO_ADAPTION action not as expected"), //$NON-NLS-1$
      () -> assertEquals(2, HkrErrorCodes.VALVE_LIFT.getAction(), "VALVE_LIFT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(3, HkrErrorCodes.NO_VALVE_MOVEMENT.getAction(), "NO_VALVE_MOVEMENT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(4, HkrErrorCodes.PREPARING.getAction(), "PREPARING action not as expected"), //$NON-NLS-1$
      () -> assertEquals(5, HkrErrorCodes.INSTALL_MODE.getAction(), "INSTALL_MODE action not as expected"), //$NON-NLS-1$
      () -> assertEquals(6, HkrErrorCodes.ADAPTING.getAction(), "ADAPTING action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final HkrErrorCodes code = HkrErrorCodes.NO_ERROR;
    assertEquals("NO_ERROR", code.stringValue(), "stringValue not as expected");
   }

 }
