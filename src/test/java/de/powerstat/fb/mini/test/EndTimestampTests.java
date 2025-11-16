/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.EndTimestamp;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * EndTimestamp tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR", "RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT"})
final class EndTimestampTests
 {
  /**
   * Default constructor.
   */
  /* default */ EndTimestampTests()
   {
    super();
   }


  /**
   * Is a timestamp 0.
   */
  @Test
  /* default */ void testIsEndTimestamp0()
   {
    assertEquals(0, EndTimestamp.of(Seconds.of(0)).longValue(), "Not an EndTimestamp!"); //$NON-NLS-1$
   }


  /**
   * Is a timestamp now+10.
   */
  @Test
  /* default */ void testIsEndTimestampNow10()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    assertEquals(now, EndTimestamp.of(Seconds.of(now)).longValue(), "Not an EndTimestamp!"); //$NON-NLS-1$
   }


  /**
   * Is a timestamp now+86400.
   */
  @Test
  /* default */ void testIsEndTimestampNow86400()
   {
    final long then = Instant.now().getEpochSecond() + 86400;
    assertEquals(then, EndTimestamp.of(Seconds.of(then)).longValue(), "Not an EndTimestamp!"); //$NON-NLS-1$
   }


  /**
   * Is not a timestamp.
   */
  @Test
  /* default */ void testIsNotATimestampPast()
   {
    final long now = Instant.now().getEpochSecond();
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final EndTimestamp timestamp = */ EndTimestamp.of(Seconds.of(now));
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is not a timestamp.
   */
  @Test
  /* default */ void testIsNotATimestampFarFuture()
   {
    final long farFuture = Instant.now().getEpochSecond() + 86401;
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final EndTimestamp timestamp = */ EndTimestamp.of(Seconds.of(farFuture));
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Is a EndTimestamp string value.
   */
  @Test
  /* default */ void testIsEndTimestampString()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    assertEquals(now, EndTimestamp.of(String.valueOf(now)).longValue(), "Not an EndTimestamp value!"); //$NON-NLS-1$
   }


  /**
   * Is a EndTimestamp SecondsValue value.
   */
  @Test
  /* default */ void testSecondsValue()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    Seconds secs = Seconds.of(now);
    final EndTimestamp timestamp = EndTimestamp.of(String.valueOf(now));
    assertEquals(secs, timestamp.secondsValue(), "Not an EndTimestamp value!"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(EndTimestamp.class).withNonnullFields("seconds").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final Seconds now = Seconds.of(0);
    final EndTimestamp timestamp = EndTimestamp.of(now);
    assertEquals("EndTimestamp[seconds=Seconds[seconds=0]]", timestamp.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final long now = Instant.now().getEpochSecond() + 10;
    final EndTimestamp timestamp1 = EndTimestamp.of(Seconds.of(now));
    final EndTimestamp timestamp2 = EndTimestamp.of(Seconds.of(now));
    final EndTimestamp timestamp3 = EndTimestamp.of(Seconds.of(now + 10));
    final EndTimestamp timestamp4 = EndTimestamp.of(Seconds.of(now + 20));
    final EndTimestamp timestamp5 = EndTimestamp.of(Seconds.of(now));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(timestamp1.compareTo(timestamp2) == -timestamp2.compareTo(timestamp1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(timestamp1.compareTo(timestamp3) == -timestamp3.compareTo(timestamp1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((timestamp4.compareTo(timestamp3) > 0) && (timestamp3.compareTo(timestamp1) > 0) && (timestamp4.compareTo(timestamp1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((timestamp1.compareTo(timestamp2) == 0) && (Math.abs(timestamp1.compareTo(timestamp5)) == Math.abs(timestamp2.compareTo(timestamp5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((timestamp1.compareTo(timestamp2) == 0) && timestamp1.equals(timestamp2), "equals") //$NON-NLS-1$
    );
   }

 }
