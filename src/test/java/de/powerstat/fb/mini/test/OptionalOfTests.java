/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.Level;
import de.powerstat.fb.mini.OptionalOf;
import de.powerstat.validation.interfaces.IValueObject;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * OptionalOf tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class OptionalOfTests
 {
  /**
   * toString not equal.
   */
  private static final String TO_STRING_NOT_EQUAL = "toString not equal";


  /**
   * Default constructor.
   */
  /* default */ OptionalOfTests()
   {
    super();
   }


  /**
   * Test toString().
   */
  @Test
  /* default */ void testToString1()
   {
    final OptionalOf<Level> level = new OptionalOf<>(Level.of(1));
    assertEquals("OptionalOf<>[value=Level[level=1]]", level.toString(), TO_STRING_NOT_EQUAL); //$NON-NLS-1$
   }


  /**
   * Test stringValue().
   */
  @Test
  /* default */ void testStringValue1()
   {
    assertEquals("50", new OptionalOf<>(Level.of(50)).stringValue(), TO_STRING_NOT_EQUAL); //$NON-NLS-1$
   }


  /**
   * Is an optional value.
   */
  @Test
  /* default */ void testIsOptionalValue()
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
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(OptionalOf.class).withNonnullFields("value").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final OptionalOf<Level> level = new OptionalOf<>(Level.of(1));
    assertEquals("OptionalOf<>[value=Level[level=1]]", level.toString(), TO_STRING_NOT_EQUAL); //$NON-NLS-1$
   }

 }
