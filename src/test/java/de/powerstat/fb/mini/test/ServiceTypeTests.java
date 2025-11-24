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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.ServiceType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * ServiceType tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class ServiceTypeTests
 {
  /**
   * URN.
   */
  private static final String URN = "urn:dslforum-org:service:DeviceConfig:1";


  /**
   * Default constructor.
   */
  /* default */ ServiceTypeTests()
   {
    super();
   }


  /**
   * Test correct ServiceType.
   *
   * @param type ServiceType
   */
  @ParameterizedTest
  @ValueSource(strings = {URN, "urn:dslforum-org:service:a:1", "urn:dslforum-org:service:1234567890123456789012345678901234567:1"})
  /* default */ void testServiceTypeCorrect(final String type)
   {
    final ServiceType cleanType = ServiceType.of(type);
    assertEquals(type, cleanType.stringValue(), "ServiceType not as expected"); //$NON-NLS-1$
   }


  /**
   * Test ServiceType with wrong lengths.
   *
   * @param type ServiceType
   */
  @ParameterizedTest
  @ValueSource(strings = {"123456789012345678901234567", "12345678901234567890123456789012345678901234567890123456789012345"})
  /* default */ void testServiceTypeLength(final String type)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ServiceType cleanType = */ ServiceType.of(type);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong ServiceType.
   *
   * @param type ServiceType
   */
  @ParameterizedTest
  @ValueSource(strings = {"urn:dslforum-org:service:DeviceConfig:0", "urn:dslforum+org:service:DeviceConfig:1"})
  /* default */ void testServiceTypeWrong(final String type)
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final ServiceType cleanType = */ ServiceType.of(type);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final ServiceType type = ServiceType.of(URN);
    assertEquals(URN, type.stringValue(), "ServiceType not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(ServiceType.class).withNonnullFields("serviceType").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final ServiceType type = ServiceType.of(URN);
    assertEquals("ServiceType[serviceType=urn:dslforum-org:service:DeviceConfig:1]", type.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final ServiceType type1 = ServiceType.of(URN);
    final ServiceType type2 = ServiceType.of(URN);
    final ServiceType type3 = ServiceType.of("urn:dslforum-org:service:DeviceConfig:2"); //$NON-NLS-1$
    final ServiceType type4 = ServiceType.of("urn:dslforum-org:service:OtherConfig:1"); //$NON-NLS-1$
    final ServiceType type5 = ServiceType.of(URN);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(type1.compareTo(type2) == -type2.compareTo(type1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(type1.compareTo(type3) == -type3.compareTo(type1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((type4.compareTo(type3) > 0) && (type3.compareTo(type1) > 0) && (type4.compareTo(type1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((type1.compareTo(type2) == 0) && (Math.abs(type1.compareTo(type5)) == Math.abs(type2.compareTo(type5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((type1.compareTo(type2) == 0) && type1.equals(type2), "equals") //$NON-NLS-1$
    );
   }

 }
