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

import de.powerstat.fb.mini.Version;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Version tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class VersionTests
 {
  /**
   * Version not as expected.
   */
  private static final String VERSION_NOT_AS_EXPECTED = "Version not as expected";

  /**
   * Version 1.0.
   */
  private static final String VERSION_1_0 = "1.0";


  /**
   * Default constructor.
   */
  /* default */ VersionTests()
   {
    super();
   }


  /**
   * Test correct Version.
   */
  @Test
  /* default */ void testVersionCorrect1()
   {
    final Version cleanVersion = Version.of(1, 0);
    assertEquals(VERSION_1_0, cleanVersion.stringValue(), VERSION_NOT_AS_EXPECTED);
   }


  /**
   * Test correct Version.
   */
  @Test
  /* default */ void testVersionCorrect2()
   {
    final Version cleanVersion = Version.of(VERSION_1_0);
    assertEquals(VERSION_1_0, cleanVersion.stringValue(), VERSION_NOT_AS_EXPECTED);
   }


  /**
   * Test wrong Version.
   */
  @Test
  /* default */ void testVersionWrong1()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Version cleanVerion = */ Version.of(-1, 0);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Version.
   */
  @Test
  /* default */ void testVersionWrong2()
   {
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final Version cleanVerion = */ Version.of(0, -1);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Version.
   */
  @Test
  /* default */ void testVersionWrong3()
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Version cleanVerion = */ Version.of("1");
     }, "IndexOutOfBoundsException expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Version.
   */
  @Test
  /* default */ void testVersionWrong4()
   {
    assertThrows(NumberFormatException.class, () ->
     {
      /* final Version cleanVerion = */ Version.of("A.0");
     }, "NumberFormatException expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Version.
   */
  @Test
  /* default */ void testVersionWrong5()
   {
    assertThrows(NumberFormatException.class, () ->
     {
      /* final Version cleanVerion = */ Version.of("1.B");
     }, "NumberFormatException expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Version version = Version.of(1, 0);
    assertEquals(VERSION_1_0, version.stringValue(), VERSION_NOT_AS_EXPECTED);
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Version.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Version version = Version.of(1, 0);
    assertEquals("Version[major=1, minor=0]", version.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Version version1 = Version.of(1, 0);
    final Version version2 = Version.of(1, 0);
    final Version version3 = Version.of(2, 1);
    final Version version4 = Version.of(3, 2);
    final Version version5 = Version.of(1, 0);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(version1.compareTo(version2) == -version2.compareTo(version1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(version1.compareTo(version3) == -version3.compareTo(version1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((version4.compareTo(version3) > 0) && (version3.compareTo(version1) > 0) && (version4.compareTo(version1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((version1.compareTo(version2) == 0) && (Math.abs(version1.compareTo(version5)) == Math.abs(version2.compareTo(version5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((version1.compareTo(version2) == 0) && version1.equals(version2), "equals") //$NON-NLS-1$
    );
   }

 }
