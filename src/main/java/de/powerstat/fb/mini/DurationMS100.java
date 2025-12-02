/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Duration in 100 ms (0-Integer.MAX_VALUE).
 */
@ValueObject
public final class DurationMS100 implements Comparable<DurationMS100>, IValueObject
 {
  /**
   * Duration in 100 ms (0-Integer.MAX_VALUE).
   */
  private final int duration;


  /**
   * Constructor.
   *
   * @param duration DurationMS100 (0-Integer.MAX_VALUE)
   * @throws IndexOutOfBoundsException If duration ist &lt; 0
   */
  private DurationMS100(final int duration)
   {
    super();
    if (duration < 0)
     {
      throw new IndexOutOfBoundsException("duration must be >= 0"); //$NON-NLS-1$
     }
    this.duration = duration;
   }


  /**
   * DurationMS100 factory.
   *
   * @param duration DurationMS100 (0-Integer.MAX_VALUE)
   * @return DurationMS100 object
   * @throws IndexOutOfBoundsException If duration ist &lt; 0
   */
  public static DurationMS100 of(final int duration)
   {
    return new DurationMS100(duration);
   }


  /**
   * DurationMS100 factory.
   *
   * @param duration DurationMS100 (0-Integer.MAX_VALUE)
   * @return DurationMS100 object
   * @throws IndexOutOfBoundsException If duration ist &lt; 0
   * @throws NumberFormatException If duration does not contain a parsable int.
   */
  public static DurationMS100 of(final String duration)
   {
    return new DurationMS100(Integer.parseInt(duration));
   }


  /**
   * Returns the value of this DurationMS100 as an int.
   *
   * @return The numeric value represented by this object after conversion to type int (0-Integer.MAX_VALUE)
   */
  public int intValue()
   {
    return duration;
   }


  /**
   * Returns the value of this DurationMS100 as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(duration);
   }


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Integer.hashCode(duration);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final DurationMS100 other))
     {
      return false;
     }
    return duration == other.duration;
   }


  /**
   * Returns the string representation of this DurationMS100.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "DurationMS100[duration=255]"
   *
   * @return String representation of this DurationMS100 (0-255)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(24);
    builder.append("DurationMS100[duration=").append(duration).append(']'); //$NON-NLS-1$
    return builder.toString();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final DurationMS100 obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(duration, obj.duration);
   }

 }
