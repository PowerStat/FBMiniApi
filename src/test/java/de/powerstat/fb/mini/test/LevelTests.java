/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
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

import de.powerstat.fb.mini.Level;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Level tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class LevelTests
 {
  /**
   * Default constructor.
   */
  /* default */ LevelTests()
   {
    super();
   }


  /**
   * Is a level value.
   *
   * @param level Level
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 127, 255})
  /* default */ void testIsLevelValue(final int level)
   {
    assertEquals(level, Level.of(level).intValue(), "Not a level value!"); //$NON-NLS-1$
   }


  /**
   * Is not a level value.
   *
   * @param level Level (0-255)
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 256})
  /* default */ void testIsNotAlevelValue(final int level)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Level level = */ Level.of(level);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a level string value.
   */
  @Test
  /* default */ void testIsLevelString()
   {
    assertEquals(127, Level.of("127").intValue(), "Not a level value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Level.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Level level = Level.of(1);
    assertEquals("Level[level=1]", level.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Level level1 = Level.of(1);
    final Level level2 = Level.of(1);
    final Level level3 = Level.of(2);
    final Level level4 = Level.of(3);
    final Level level5 = Level.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(level1.compareTo(level2) == -level2.compareTo(level1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(level1.compareTo(level3) == -level3.compareTo(level1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((level4.compareTo(level3) > 0) && (level3.compareTo(level1) > 0) && (level4.compareTo(level1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((level1.compareTo(level2) == 0) && (Math.abs(level1.compareTo(level5)) == Math.abs(level2.compareTo(level5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((level1.compareTo(level2) == 0) && level1.equals(level2), "equals") //$NON-NLS-1$
    );
   }

 }
