/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;
import de.powerstat.validation.values.Seconds;


/**
 * Unix timestamp &gt; 0.
 */
public final class UnixTimestamp  implements Comparable<UnixTimestamp>, IValueObject
 {
  /**
   * Seconds since 1970-01-01T00:00:00.
   */
  private final Seconds seconds;


  /**
   * Constructor.
   *
   * @param seconds Seconds 0 or now until 24 hours in the future
   * @throws IndexOutOfBoundsException When the seconds is less than 0 or not within 24 hours of future
   */
  private UnixTimestamp(final Seconds seconds)
   {
    super();
    this.seconds = seconds;
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
    return seconds.longValue();
   }


  /**
   * Returns the value of this UnixTimestamp as a Seconds.
   *
   * @return The numeric value represented by this object after conversion to type Seconds.
   */
  public Seconds secondsValue()
   {
    return seconds;
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
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hashCode(seconds);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    /* Symmetrie Verletzung
    if (obj instanceof final Seconds other)
     {
      return this.seconds.equals(other);
     }
    */
    if (!(obj instanceof final UnixTimestamp other))
     {
      return false;
     }
    return seconds.equals(other.seconds);
   }


  /**
   * Returns the string representation of this UnixTimestamp.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "UnixTimestamp[seconds=Seconds[seconds=0]]"
   *
   * @return String representation of this UnixTimestamp
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(17);
    builder.append("UnixTimestamp[seconds=").append(seconds).append(']'); //$NON-NLS-1$
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
  public int compareTo(final UnixTimestamp obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return seconds.compareTo(obj.seconds);
   }

 }
