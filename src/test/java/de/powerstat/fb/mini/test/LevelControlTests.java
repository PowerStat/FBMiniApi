/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Level;
import de.powerstat.fb.mini.LevelControl;
import de.powerstat.validation.values.Percent;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * LevelControl tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class LevelControlTests
 {
  /**
   * Default constructor.
   */
  /* default */ LevelControlTests()
   {
    super();
   }


  /**
   * Test correct LevelControl.
   */
  @Test
  /* default */ void testLevelControlCorrect()
   {
    final LevelControl cleanLevelControl = LevelControl.of(Level.of(128), Percent.of(50));
    assertEquals("128", cleanLevelControl.stringValue(), "LevelControl not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final LevelControl cleanLevelControl = LevelControl.of(Level.of(128), Percent.of(50));
    assertEquals("128", cleanLevelControl.stringValue(), "LevelControl not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(LevelControl.class).withNonnullFields("level", "levelpercentage").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final LevelControl levelControl = LevelControl.of(Level.of(128), Percent.of(50));
    assertEquals("LevelControl[level=Level[level=128], levelpercentage=Percent[percent=50]]", levelControl.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final LevelControl levelControl1 = LevelControl.of(Level.of(0), Percent.of(0));
    final LevelControl levelControl2 = LevelControl.of(Level.of(0), Percent.of(0));
    final LevelControl levelControl3 = LevelControl.of(Level.of(128), Percent.of(50));
    final LevelControl levelControl4 = LevelControl.of(Level.of(189), Percent.of(75));
    final LevelControl levelControl5 = LevelControl.of(Level.of(0), Percent.of(0));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(levelControl1.compareTo(levelControl2) == -levelControl2.compareTo(levelControl1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(levelControl1.compareTo(levelControl3) == -levelControl3.compareTo(levelControl1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((levelControl4.compareTo(levelControl3) > 0) && (levelControl3.compareTo(levelControl1) > 0) && (levelControl4.compareTo(levelControl1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((levelControl1.compareTo(levelControl2) == 0) && (Math.abs(levelControl1.compareTo(levelControl5)) == Math.abs(levelControl2.compareTo(levelControl5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((levelControl1.compareTo(levelControl2) == 0) && levelControl1.equals(levelControl2), "equals") //$NON-NLS-1$
    );
   }

 }
