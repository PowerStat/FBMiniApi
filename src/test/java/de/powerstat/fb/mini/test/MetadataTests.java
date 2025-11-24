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

import de.powerstat.fb.mini.Metadata;
import de.powerstat.fb.mini.ScenarioType;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Metadata tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class MetadataTests
 {
  /**
   * Metadata not as expected.
   */
  private static final String METADATA_NOT_AS_EXPECTED = "Metadata not as expected";


  /**
   * Default constructor.
   */
  /* default */ MetadataTests()
   {
    super();
   }


  /**
   * Test correct Metadata.
   */
  @Test
  /* default */ void testMetadataCorrect1()
   {
    final Metadata cleanMetadata = Metadata.of(0, ScenarioType.UNDEFINED);
    assertEquals("0", cleanMetadata.stringValue(), METADATA_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test correct Metadata.
   */
  @Test
  /* default */ void testMetadataCorrect2()
   {
    final Metadata cleanMetadata = Metadata.of(-1, ScenarioType.COMING);
    assertEquals("COMING", cleanMetadata.stringValue(), METADATA_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test wrong Metadata.
   */
  @Test
  /* default */ void testMetadataWrong1()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Metadata cleanMetadata = */ Metadata.of(-1, ScenarioType.UNDEFINED);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Metadata.
   */
  @Test
  /* default */ void testMetadataWrong2()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Metadata cleanMetadata = */ Metadata.of(0, ScenarioType.COMING);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Metadata.
   */
  @Test
  /* default */ void testMetadataWrong3()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final Metadata cleanMetadata = */ Metadata.of(0, null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue1()
   {
    final Metadata metadata = Metadata.of(0, ScenarioType.UNDEFINED);
    assertEquals("0", metadata.stringValue(), METADATA_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue2()
   {
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    assertEquals("COMING", metadata.stringValue(), METADATA_NOT_AS_EXPECTED); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Metadata.class).withNonnullFields("type").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Metadata metadata = Metadata.of(0, ScenarioType.UNDEFINED);
    assertEquals("Metadata[icon=0, type=UNDEFINED]", metadata.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Metadata metadata1 = Metadata.of(0, ScenarioType.UNDEFINED);
    final Metadata metadata2 = Metadata.of(0, ScenarioType.UNDEFINED);
    final Metadata metadata3 = Metadata.of(1, ScenarioType.UNDEFINED);
    final Metadata metadata4 = Metadata.of(-1, ScenarioType.COMING);
    final Metadata metadata5 = Metadata.of(0, ScenarioType.UNDEFINED);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(metadata1.compareTo(metadata2) == -metadata2.compareTo(metadata1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(metadata1.compareTo(metadata3) == -metadata3.compareTo(metadata1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((metadata4.compareTo(metadata3) > 0) && (metadata3.compareTo(metadata1) > 0) && (metadata4.compareTo(metadata1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((metadata1.compareTo(metadata2) == 0) && (Math.abs(metadata1.compareTo(metadata5)) == Math.abs(metadata2.compareTo(metadata5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((metadata1.compareTo(metadata2) == 0) && metadata1.equals(metadata2), "equals") //$NON-NLS-1$
    );
   }

 }
