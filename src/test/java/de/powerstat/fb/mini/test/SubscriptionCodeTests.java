/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.SubscriptionCode;


/**
 * SubscriptionCode tests.
 */
final class SubscriptionCodeTests
 {
  /**
   * Default constructor.
   */
  /* default */ SubscriptionCodeTests()
   {
    super();
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory1()
   {
    assertEquals(0, SubscriptionCode.of("NO_PROGRESS").getAction(), "Action not as expected");
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory2()
   {
    assertEquals(1, SubscriptionCode.of(1).getAction(), "Action not as expected");
   }


  /**
   * Factory test.
   */
  @Test
  /* default */ void testFactory3()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* SubscriptionCode code = */ SubscriptionCode.of(999);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test getAction of SubscriptionCode.
   */
  @Test
  /* default */ void testGetAction()
   {
    assertAll("getAction", //$NON-NLS-1$
      () -> assertEquals(0, SubscriptionCode.NO_PROGRESS.getAction(), "NO_PROGRESS action not as expected"), //$NON-NLS-1$
      () -> assertEquals(1, SubscriptionCode.IN_PROGRESS.getAction(), "IN_PROGRESS action not as expected"), //$NON-NLS-1$
      () -> assertEquals(2, SubscriptionCode.TIMEOUT.getAction(), "TIMEOUT action not as expected"), //$NON-NLS-1$
      () -> assertEquals(3, SubscriptionCode.OTHER_ERROR.getAction(), "OTHER_ERROR action not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final SubscriptionCode code = SubscriptionCode.NO_PROGRESS;
    assertEquals("NO_PROGRESS", code.stringValue(), "stringValue not as expected");
   }

 }
