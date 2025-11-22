/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Power in mW.
 */
public final class Power implements Comparable<Power>, IValueObject
 {
  /**
   * Power in mW.
   */
  private final long powerMW;


  /**
   * Constructor.
   *
   * @param power Power in mW.
   */
  private Power(final long power)
   {
    super();
    /*
    if (power < 0) // Only for consumers - Producers will become negative
     {
      throw new IndexOutOfBoundsException("power must be >= 0"); //$NON-NLS-1$
     }
    */
    powerMW = power;
   }


  /**
   * Power factory.
   *
   * @param power Power in mW.
   * @return Power object
   */
  public static Power of(final long power)
   {
    return new Power(power);
   }


  /**
   * Power factory.
   *
   * @param power Power in mW.
   * @return Power object
   * @throws NumberFormatException If power does not contain a parsable long.
   */
  public static Power of(final String power)
   {
    return new Power(Long.parseLong(power));
   }


  /**
   * Returns the value of this Power as a long in milli watt.
   *
   * @return The numeric value represented by this object after conversion to type long in mW
   */
  public long longValue()
   {
    return powerMW;
   }


  /**
   * Returns the value of this Power as a String in milli watt.
   *
   * @return The numeric value represented by this object after conversion to type String in mW
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(powerMW);
   }


  /**
   * Get power in watt.
   *
   * @return Power in W
   */
  public long getPowerWatt()
   {
    return powerMW / 1000;
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
    return Long.hashCode(powerMW);
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
    if (!(obj instanceof final Power other))
     {
      return false;
     }
    return powerMW == other.powerMW;
   }


  /**
   * Returns the string representation of this Power in mW.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Power[power=10150]"
   *
   * @return String representation of this Power in mW
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Power[power=").append(powerMW).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Power obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(powerMW, obj.powerMW);
   }

 }
