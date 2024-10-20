/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

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
    assertEquals("0", cleanMetadata.stringValue(), "Metadata not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test correct Metadata.
   */
  @Test
  /* default */ void testMetadataCorrect2()
   {
    final Metadata cleanMetadata = Metadata.of(-1, ScenarioType.COMING);
    assertEquals("COMING", cleanMetadata.stringValue(), "Metadata not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
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
    assertEquals("0", metadata.stringValue(), "Metadata not as expected"); //$NON-NLS-1$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue2()
   {
    final Metadata metadata = Metadata.of(-1, ScenarioType.COMING);
    assertEquals("COMING", metadata.stringValue(), "Metadata not as expected"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final Metadata metadata1 = Metadata.of(0, ScenarioType.UNDEFINED);
    final Metadata metadata2 = Metadata.of(0, ScenarioType.UNDEFINED);
    final Metadata metadata3 = Metadata.of(-1, ScenarioType.COMING);
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(metadata1.hashCode(), metadata2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(metadata1.hashCode(), metadata3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final Metadata metadata1 = Metadata.of(0, ScenarioType.UNDEFINED);
    final Metadata metadata2 = Metadata.of(0, ScenarioType.UNDEFINED);
    final Metadata metadata3 = Metadata.of(-1, ScenarioType.COMING);
    final Metadata metadata4 = Metadata.of(0, ScenarioType.UNDEFINED);
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(metadata1.equals(metadata1), "metadata11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(metadata1.equals(metadata2), "metadata12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(metadata2.equals(metadata1), "metadata21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(metadata2.equals(metadata4), "metadata24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(metadata1.equals(metadata4), "metadata14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(metadata1.equals(metadata3), "metadata13 are equal"), //$NON-NLS-1$
      () -> assertFalse(metadata3.equals(metadata1), "metadata31 are equal"), //$NON-NLS-1$
      () -> assertFalse(metadata1.equals(null), "metadata10 is equal") //$NON-NLS-1$
    );
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
    final Metadata metadata3 = Metadata.of(1, ScenarioType.UNDEFINED); //$NON-NLS-1$
    final Metadata metadata4 = Metadata.of(-1, ScenarioType.COMING); //$NON-NLS-1$
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
