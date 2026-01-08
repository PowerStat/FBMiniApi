/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;
import de.powerstat.ddd.values.time.Seconds;


/**
 * Unix timestamp &gt; 0.
 *
 * @param seconds Seconds 0 or now until 24 hours in the future
 */
@ValueObject
public record UnixTimestamp(Seconds seconds) implements Comparable<UnixTimestamp>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param seconds Seconds 0 or now until 24 hours in the future
   * @throws IndexOutOfBoundsException When the seconds is less than 0 or not within 24 hours of future
   */
  public UnixTimestamp
   {
   }


  /**
   * UnixTimestamp factory.
   *
   * @param seconds Seconds
   * @return UnixTimestamp object
   */
  public static UnixTimestamp of(final Seconds seconds)
   {
    return new UnixTimestamp(seconds);
   }


  /**
   * UnixTimestamp factory.
   *
   * @param value Seconds
   * @return UnixTimestamp object
   */
  public static UnixTimestamp of(final String value)
   {
    return of(Seconds.of(value));
   }


  /**
   * Returns the value of this UnixTimestamp as a long.
   *
   * @return The numeric value represented by this object after conversion to type long.
   */
  public long longValue()
   {
    return seconds.seconds();
   }


  /**
   * Returns the value of this UnixTimestamp as a String.
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
  public int compareTo(final UnixTimestamp obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return seconds.compareTo(obj.seconds);
   }

 }
