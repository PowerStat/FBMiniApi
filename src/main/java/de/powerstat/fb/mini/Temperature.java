/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Temperature.
 *
 * @param temperature Temperature in deci celsius.
 * @param offset Temperature offset in deci celsius.
 */
@ValueObject
public record Temperature(TemperatureCelsius temperature, TemperatureCelsius offset) implements Comparable<Temperature>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param temperature Temperature in deci celsius.
   * @param offset Temperature offset in deci celsius.
   * @throws NullPointerException When temperature or offset is null
   */
  public Temperature
   {
    Objects.requireNonNull(temperature, "temperature"); //$NON-NLS-1$
    Objects.requireNonNull(offset, "offset"); //$NON-NLS-1$
   }


  /**
   * Temperature factory.
   *
   * @param temperature Temperature in deci celsius.
   * @param offset Temperature offset in deci celsius.
   * @return Temperature object
   * @throws NullPointerException When temperature or offset is null
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
