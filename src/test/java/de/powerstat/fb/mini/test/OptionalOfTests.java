/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.Level;
import de.powerstat.fb.mini.OptionalOf;
import de.powerstat.validation.interfaces.IValueObject;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * OptionalOf tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
public class OptionalOfTests
 {
  /**
   * Default constructor.
   */
  /* default */ OptionalOfTests()
   {
    super();
   }


  /**
   * Test stringValue().
   */
  @Test
  /* default */ void testStringValue1()
   {
    final OptionalOf<Level> level = new OptionalOf<>(Level.of(1));
    assertEquals("OptionalOf<>[value=Level[level=1]]", level.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test stringValue().
   */
  @Test
  /* default */ void testStringValue2()
   {
    assertEquals("", new OptionalOf<>((IValueObject)null).stringValue(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Is an optional value.
   */
  @Test
  /* default */ void testIsOptinalValue()
   {
    assertEquals(-1, new OptionalOf<>((IValueObject)null).intValue(), "Not an optional value!"); //$NON-NLS-1$
   }


  /**
   * Is a value.
   */
  @Test
  /* default */ void testIsValue()
   {
    assertEquals(50, (new OptionalOf<>(Level.of(50))).intValue(), "Not a value!"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final OptionalOf<Level> level1 = new OptionalOf<>(Level.of(1));
    final OptionalOf<Level> level2 = new OptionalOf<>(Level.of(1));
    final OptionalOf<Level> level3 = new OptionalOf<>(Level.of(2));
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(level1.hashCode(), level2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(level1.hashCode(), level3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final OptionalOf<Level> level1 = new OptionalOf<>(Level.of(1));
    final OptionalOf<Level> level2 = new OptionalOf<>(Level.of(1));
    final OptionalOf<Level> level3 = new OptionalOf<>(Level.of(2));
    final OptionalOf<Level> level4 = new OptionalOf<>(Level.of(1));
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(level1.equals(level1), "level11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(level1.equals(level2), "level12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(level2.equals(level1), "level21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(level2.equals(level4), "level24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(level1.equals(level4), "level14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(level1.equals(level3), "level13 are equal"), //$NON-NLS-1$
      () -> assertFalse(level3.equals(level1), "level31 are equal"), //$NON-NLS-1$
      () -> assertFalse(level1.equals(null), "level10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final OptionalOf<Level> level = new OptionalOf<>(Level.of(1));
    assertEquals("OptionalOf<>[value=Level[level=1]]", level.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }

 }
