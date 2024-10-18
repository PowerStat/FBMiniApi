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

import java.time.Instant;

import org.junit.jupiter.api.Test;

import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * UnixTimestamp tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class UnixTimestampTests
 {
  /**
   * Default constructor.
   */
  /* default */ UnixTimestampTests()
   {
    super();
   }


  /**
   * Is a timestamp 0.
   */
  @Test
  /* default */ void testIsUnixTimestamp0()
   {
    assertEquals(0, UnixTimestamp.of(Seconds.of(0)).longValue(), "Not an UnixTimestamp!"); //$NON-NLS-1$
   }


  /**
   * Is a timestamp now + 10.
   */
  @Test
  /* default */ void testIsUnixTimestampNow10()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    assertEquals(now, UnixTimestamp.of(Seconds.of(now)).longValue(), "Not an UnixTimestamp!"); //$NON-NLS-1$
   }


  /**
   * Is a timestamp now + 86400.
   */
  @Test
  /* default */ void testIsUnixTimestampNow86400()
   {
    final long then = Instant.now().getEpochSecond() + 86400;
    assertEquals(then, UnixTimestamp.of(Seconds.of(then)).longValue(), "Not an UnixTimestamp!"); //$NON-NLS-1$
   }


  /**
   * Is a UnixTimestamp string value.
   */
  @Test
  /* default */ void testIsUnixTimestampString()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    assertEquals(now, UnixTimestamp.of(String.valueOf(now)).longValue(), "Not an UnixTimestamp value!"); //$NON-NLS-1$
   }


  /**
   * Test hash code.
   */
  @Test
  /* default */ void testHashCode()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    final UnixTimestamp timestamp1 = UnixTimestamp.of(Seconds.of(now));
    final UnixTimestamp timestamp2 = UnixTimestamp.of(Seconds.of(now));
    final UnixTimestamp timestamp3 = UnixTimestamp.of(Seconds.of(now + 20));
    assertAll("testHashCode", //$NON-NLS-1$
      () -> assertEquals(timestamp1.hashCode(), timestamp2.hashCode(), "hashCodes are not equal"), //$NON-NLS-1$
      () -> assertNotEquals(timestamp1.hashCode(), timestamp3.hashCode(), "hashCodes are equal") //$NON-NLS-1$
    );
   }


  /**
   * Test equals.
   */
  @Test
  @SuppressWarnings({"PMD.EqualsNull", "java:S5785"})
  /* default */ void testEquals()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    final UnixTimestamp timestamp1 = UnixTimestamp.of(Seconds.of(now));
    final UnixTimestamp timestamp2 = UnixTimestamp.of(Seconds.of(now));
    final UnixTimestamp timestamp3 = UnixTimestamp.of(Seconds.of(now + 20));
    final UnixTimestamp timestamp4 = UnixTimestamp.of(Seconds.of(now));
    assertAll("testEquals", //$NON-NLS-1$
      () -> assertTrue(timestamp1.equals(timestamp1), "timestamp11 is not equal"), //$NON-NLS-1$
      () -> assertTrue(timestamp1.equals(timestamp2), "timestamp12 are not equal"), //$NON-NLS-1$
      () -> assertTrue(timestamp2.equals(timestamp1), "timestamp21 are not equal"), //$NON-NLS-1$
      () -> assertTrue(timestamp2.equals(timestamp4), "timestamp24 are not equal"), //$NON-NLS-1$
      () -> assertTrue(timestamp1.equals(timestamp4), "timestamp14 are not equal"), //$NON-NLS-1$
      () -> assertFalse(timestamp1.equals(timestamp3), "timestamp13 are equal"), //$NON-NLS-1$
      () -> assertFalse(timestamp3.equals(timestamp1), "timestamp31 are equal"), //$NON-NLS-1$
      () -> assertFalse(timestamp1.equals(null), "timestamp10 is equal") //$NON-NLS-1$
    );
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Seconds now = Seconds.of(0);
    final UnixTimestamp timestamp = UnixTimestamp.of(now);
    assertEquals("UnixTimestamp[seconds=Seconds[seconds=0]]", timestamp.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    final UnixTimestamp timestamp1 = UnixTimestamp.of(Seconds.of(now));
    final UnixTimestamp timestamp2 = UnixTimestamp.of(Seconds.of(now));
    final UnixTimestamp timestamp3 = UnixTimestamp.of(Seconds.of(now + 10));
    final UnixTimestamp timestamp4 = UnixTimestamp.of(Seconds.of(now + 20));
    final UnixTimestamp timestamp5 = UnixTimestamp.of(Seconds.of(now));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(timestamp1.compareTo(timestamp2) == -timestamp2.compareTo(timestamp1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(timestamp1.compareTo(timestamp3) == -timestamp3.compareTo(timestamp1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((timestamp4.compareTo(timestamp3) > 0) && (timestamp3.compareTo(timestamp1) > 0) && (timestamp4.compareTo(timestamp1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((timestamp1.compareTo(timestamp2) == 0) && (Math.abs(timestamp1.compareTo(timestamp5)) == Math.abs(timestamp2.compareTo(timestamp5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((timestamp1.compareTo(timestamp2) == 0) && timestamp1.equals(timestamp2), "equals") //$NON-NLS-1$
    );
   }

 }
