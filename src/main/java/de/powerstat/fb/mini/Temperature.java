/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Temperature.
 */
public final class Temperature implements Comparable<Temperature>, IValueObject
 {
  /**
   * Temperature in deci celsius.
   */
  private final TemperatureCelsius temperature;

  /**
   * Temperature offset in deci celsius.
   */
  private final TemperatureCelsius offset;


  /**
   * Constructor.
   *
   * @param temperature Temperature in deci celsius.
   * @param offset Temperature offset in deci celsius.
   * @throws NullPointerException When temperature or offset is null
   */
  private Temperature(final TemperatureCelsius temperature, final TemperatureCelsius offset)
   {
    super();
    Objects.requireNonNull(temperature, "temperature"); //$NON-NLS-1$
    Objects.requireNonNull(offset, "offset"); //$NON-NLS-1$
    this.temperature = temperature;
    this.offset = offset;
   }


  /**
   * Temperature factory.
   *
   * @param temperature Temperature in deci celsius.
   * @param offset Temperature offset in deci celsius.
   * @throws NullPointerException When temperature or offset is null
   * @return Temperature object
   */
  public static Temperature of(final TemperatureCelsius temperature, final TemperatureCelsius offset)
   {
    return new Temperature(temperature, offset);
   }


  /**
   * Get temperature (temperature + offset).
   *
   * @return Temperature + offset
   */
  public TemperatureCelsius temperatureValue()
   {
    return temperature.add(offset);
   }


  /**
   * Returns the value of this Temerature as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return temperatureValue().stringValue();
   }


  /**
   * Get temperature.
   *
   * @return Temperature
   */
  public TemperatureCelsius getTemperature()
   {
    return temperature;
   }


  /**
   * Get temperature offset.
   *
   * @return Temperature offset
   */
  public TemperatureCelsius getOffset()
   {
    return offset;
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
    return Objects.hash(temperature, offset);
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
    boolean result = temperature.equals(other.temperature);
    if (result)
     {
      result = offset.equals(other.offset);
     }
    return result;
   }


  /**
   * Returns the string representation of this Temperature.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Temperature[temperature=0, offset=0]"
   *
   * @return String representation of this Temperature
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Temperature[temperature=").append(temperature).append(", offset=").append(offset).append(']'); //$NON-NLS-1$
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
    final TemperatureCelsius real1 = this.temperatureValue();
    final TemperatureCelsius real2 = obj.temperatureValue();
    return real1.compareTo(real2);
   }

 }
