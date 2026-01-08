/*
 * Copyright (C) 2020-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Temperature in deci celsius.
 *
 * @param temperature Temperature in deci celsius (20 degrees will be 200 deci degrees)
 */
@ValueObject
public record TemperatureCelsius(long temperatureCelsius) implements Comparable<TemperatureCelsius>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param temperature Temperature in deci celsius (20 degrees will be 200 deci degrees)
   * @throws IndexOutOfBoundsException If temperature is &lt; -2732
   */
  public TemperatureCelsius
   {
    if (temperatureCelsius < -2732)
     {
      throw new IndexOutOfBoundsException("temperatureCelsius must be >= -2732"); //$NON-NLS-1$
     }
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
    return TemperatureCelsius.of(temperatureCelsius + other.temperatureCelsius);
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
    return TemperatureCelsius.of(temperatureCelsius - other.temperatureCelsius);
   }

 }
