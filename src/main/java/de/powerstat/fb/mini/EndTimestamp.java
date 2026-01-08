/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.time.Instant;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;
import de.powerstat.ddd.values.time.Seconds;


/**
 * End timestamp could be 0 or maximum 24 hours in the future.
 *
 * @param seconds Seconds 0 or now until 24 hours in the future
 */
@ValueObject
public record EndTimestamp(Seconds seconds) implements Comparable<EndTimestamp>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param seconds Seconds 0 or now until 24 hours in the future
   * @throws IndexOutOfBoundsException When the seconds is less than 0 or not within 24 hours of future
   */
  public EndTimestamp
   {
    if (((seconds.seconds() != 0) && (seconds.seconds() <= Instant.now().getEpochSecond())) || (seconds.seconds() > (Instant.now().getEpochSecond() + 86400)))
     {
      throw new IndexOutOfBoundsException("Seconds are not 0 or within 24 hours in the future"); //$NON-NLS-1$
     }
   }


  /**
   * EndTimestamp factory.
   *
   * @param seconds Seconds 0 or now until 24 hours in the future
   * @return EndTimestamp object
   */
  public static EndTimestamp of(final Seconds seconds)
   {
    return new EndTimestamp(seconds);
   }


  /**
   * EndTimestamp factory.
   *
   * @param value Seconds 0 or now until 24 hours in the future
   * @return EndTimestamp object
   */
  public static EndTimestamp of(final String value)
   {
    return of(Seconds.of(value));
   }


  /**
   * Returns the value of this EndTimestamp as a long.
   *
   * @return The numeric value represented by this object after conversion to type long.
   */
  public long longValue()
   {
    return seconds.seconds();
   }


  /**
   * Returns the value of this EndTimestamp as a String.
   *
   * @return The numeric value represented by this object after conversion to type String.
   */
  @Override
  public String stringValue()
   {
    return seconds.stringValue();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final EndTimestamp obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return seconds.compareTo(obj.seconds);
   }

 }
