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

import de.powerstat.fb.mini.Value;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Value tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class ValueTests
 {
  /**
   * Default constructor.
   */
  /* default */ ValueTests()
   {
    super();
   }


  /**
   * Is a value value.
   *
   * @param value Value
   */
  @ParameterizedTest
  @ValueSource(ints = {0, 127, 255})
  /* default */ void testIsValueValue(final int value)
   {
    assertEquals(value, Value.of(value).intValue(), "Not a value value!"); //$NON-NLS-1$
   }


  /**
   * Is not a value value.
   *
   * @param value Value (0-255)
   */
  @ParameterizedTest
  @ValueSource(ints = {-1, 256})
  /* default */ void testIsNotAvalueValue(final int value)
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Value value = */ Value.of(value);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a value string value.
   */
  @Test
  /* default */ void testIsValueString()
   {
    assertEquals(127, Value.of("127").intValue(), "Not a value value!"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(Value.class).verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Value value = Value.of(1);
    assertEquals("Value[value=1]", value.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final Value value = Value.of(1);
    assertEquals("1", value.stringValue(), "stringValue not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final Value value1 = Value.of(1);
    final Value value2 = Value.of(1);
    final Value value3 = Value.of(2);
    final Value value4 = Value.of(3);
    final Value value5 = Value.of(1);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(value1.compareTo(value2) == -value2.compareTo(value1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(value1.compareTo(value3) == -value3.compareTo(value1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((value4.compareTo(value3) > 0) && (value3.compareTo(value1) > 0) && (value4.compareTo(value1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((value1.compareTo(value2) == 0) && (Math.abs(value1.compareTo(value5)) == Math.abs(value2.compareTo(value5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((value1.compareTo(value2) == 0) && value1.equals(value2), "equals") //$NON-NLS-1$
    );
   }

 }
