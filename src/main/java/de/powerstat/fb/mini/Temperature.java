/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;


/**
 * Temperature in deci celsius.
 */
public class Temperature implements Comparable<Temperature>
 {
  /**
   * Temperature in deci celsius.
   */
  private final long temperature;


  /**
   * Constructor.
   *
   * @param temperature Temperature in deci celsius (20 degrees will be 200 deci degrees)
   * @throws IndexOutOfBoundsException If temperature is < -2732
   */
  public Temperature(final long temperature)
   {
    super();
    if (temperature < -2732)
     {
      throw new IndexOutOfBoundsException("temperature must be >= -2732"); //$NON-NLS-1$
     }
    this.temperature = temperature;
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
   * Get temperature in deci celsius.
   *
   * @return Temperature in deci celsius
   */
  public long getTemperatureDeciCelsius()
   {
    return this.temperature;
   }


  /**
   * Get temperature in celsius.
   *
   * @return Temperature in celsius
   */
  public long getTemperatureCelsius()
   {
    return this.temperature / 10;
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
    return Long.hashCode(this.temperature);
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
    if (!(obj instanceof Temperature))
     {
      return false;
     }
    final Temperature other = (Temperature)obj;
    return this.temperature == other.temperature;
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
    final StringBuilder builder = new StringBuilder(25);
    builder.append("Temperature[temperature=").append(this.temperature).append(']'); //$NON-NLS-1$
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
    return Long.compare(this.temperature, obj.temperature);
   }

 }
