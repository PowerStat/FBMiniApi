/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Temperature in deci celsius.
 */
public final class Temperature implements Comparable<Temperature>, IValueObject
 {
  /**
   * Temperature in deci celsius.
   */
  private final long temperatureCelsius;


  /**
   * Constructor.
   *
   * @param temperature Temperature in deci celsius (20 degrees will be 200 deci degrees)
   * @throws IndexOutOfBoundsException If temperature is &lt; -2732
   */
  private Temperature(final long temperature)
   {
    super();
    if (temperature < -2732)
     {
      throw new IndexOutOfBoundsException("temperature must be >= -2732"); //$NON-NLS-1$
     }
    this.temperatureCelsius = temperature;
   }


  /**
   * Temperature factory.
   *
   * @param temperature Temperature in deci celsius. (must be &gt;= -2732)
   * @return Temperature object
   * @throws IndexOutOfBoundsException If temperature is less than -2732
   */
  public static Temperature of(final long temperature)
   {
    return new Temperature(temperature);
   }


  /**
   * Temperature factory.
   *
   * @param temperature Temperature in deci celsius (must be &gt;= -2732)
   * @return Temperature object
   * @throws IndexOutOfBoundsException If temperature is less than -2732
   * @throws NumberFormatException If temperature does not contain a parsable long.
   */
  public static Temperature of(final String temperature)
   {
    return new Temperature(Long.parseLong(temperature));
   }


  /**
   * Returns the value of this Energy as a long in deci celsius.
   *
   * @return The numeric value represented by this object after conversion to type long in deci celsius
   */
  public long longValue()
   {
    return this.temperatureCelsius;
   }


  /**
   * Returns the value of this Energy as a String in deci celsius.
   *
   * @return The numeric value represented by this object after conversion to type String in deci celsius
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(this.temperatureCelsius);
   }


  /**
   * Get temperature in celsius.
   *
   * @return Temperature in celsius
   */
  public long getTemperatureCelsius()
   {
    return this.temperatureCelsius / 10;
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
    return Long.hashCode(this.temperatureCelsius);
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
    if (!(obj instanceof final Temperature other))
     {
      return false;
     }
    return this.temperatureCelsius == other.temperatureCelsius;
   }


  /**
   * Returns the string representation of this Temperature in deci celsius.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Temperature[temperature=205]"
   *
   * @return String representation of this Temperature in deci celsius
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(25);
    builder.append("Temperature[temperature=").append(this.temperatureCelsius).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Temperature obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(this.temperatureCelsius, obj.temperatureCelsius);
   }

 }
