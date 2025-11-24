/*
 * Copyright (C) 2020-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Temperature in deci celsius.
 */
public final class TemperatureCelsius implements Comparable<TemperatureCelsius>, IValueObject
 {
  /**
   * Temperature in deci celsius.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
  private final long temperatureCelsius;


  /**
   * Constructor.
   *
   * @param temperature Temperature in deci celsius (20 degrees will be 200 deci degrees)
   * @throws IndexOutOfBoundsException If temperature is &lt; -2732
   */
  private TemperatureCelsius(final long temperature)
   {
    super();
    if (temperature < -2732)
     {
      throw new IndexOutOfBoundsException("temperature must be >= -2732"); //$NON-NLS-1$
     }
    temperatureCelsius = temperature;
   }


  /**
   * Temperature factory.
   *
   * @param temperature Temperature in deci celsius. (must be &gt;= -2732)
   * @return Temperature object
   * @throws IndexOutOfBoundsException If temperature is less than -2732
   */
  public static TemperatureCelsius of(final long temperature)
   {
    return new TemperatureCelsius(temperature);
   }


  /**
   * Temperature factory.
   *
   * @param temperature Temperature in deci celsius (must be &gt;= -2732)
   * @return Temperature object
   * @throws IndexOutOfBoundsException If temperature is less than -2732
   * @throws NumberFormatException If temperature does not contain a parsable long.
   */
  public static TemperatureCelsius of(final String temperature)
   {
    return new TemperatureCelsius(Long.parseLong(temperature));
   }


  /**
   * Returns the value of this Energy as a long in deci celsius.
   *
   * @return The numeric value represented by this object after conversion to type long in deci celsius
   */
  public long longValue()
   {
    return temperatureCelsius;
   }


  /**
   * Returns the value of this Energy as a String in deci celsius.
   *
   * @return The numeric value represented by this object after conversion to type String in deci celsius
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(temperatureCelsius);
   }


  /**
   * Get temperature in celsius.
   *
   * @return Temperature in celsius
   */
  public long getTemperatureCelsius()
   {
    return temperatureCelsius / 10;
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
    return Long.hashCode(temperatureCelsius);
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
    if (!(obj instanceof final TemperatureCelsius other))
     {
      return false;
     }
    return temperatureCelsius == other.temperatureCelsius;
   }


  /**
   * Returns the string representation of this Temperature in deci celsius.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "TemperatureCelsius[temperature=205]"
   *
   * @return String representation of this Temperature in deci celsius
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(32);
    builder.append("TemperatureCelsius[temperature=").append(temperatureCelsius).append(']'); //$NON-NLS-1$
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
  public int compareTo(final TemperatureCelsius obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(temperatureCelsius, obj.temperatureCelsius);
   }


  /**
   * Add another temperature.
   *
   * @param other TemperatureCelsius
   * @return New temperature
   */
  public TemperatureCelsius add(final TemperatureCelsius other)
   {
    // TODO overflow
    return TemperatureCelsius.of(this.longValue() + other.longValue());
   }


  /**
   * Subtract another temperature.
   *
   * @param other TemperatureCelsius
   * @return New temperature
   */
  public TemperatureCelsius subtract(final TemperatureCelsius other)
   {
    // TODO underflow
    return TemperatureCelsius.of(this.longValue() - other.longValue());
   }

 }
