/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.json.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.json.FBMetadata;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * FB Metadata tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class FBMetadataTests
 {
  /**
   * FBMetadata not as expected.
   */
  private static final String FB_METADATA_NOT_AS_EXPECTED = "FBMetadata not as expected";


  /**
   * Default constructor.
   */
  /* default */ FBMetadataTests()
   {
    super();
   }


  /**
   * Test correct FBMetadata.
   */
  @Test
  /* default */ void testFBMetadataCorrect1()
   {
    final FBMetadata cleanData = new FBMetadata();
    assertNotNull(cleanData, FB_METADATA_NOT_AS_EXPECTED);
   }


  /**
   * Test correct FBMetadata.
   */
  @Test
  /* default */ void testFBMetadataCorrect2()
   {
    final FBMetadata cleanData = new FBMetadata(1, "test");
    assertAll("testFBMetadataCorrect2", //$NON-NLS-1$
      () -> assertEquals(1, cleanData.getIcon(), FB_METADATA_NOT_AS_EXPECTED),
      () -> assertEquals("test", cleanData.getType(), FB_METADATA_NOT_AS_EXPECTED) //$NON-NLS-1$
    );

   }

 }
