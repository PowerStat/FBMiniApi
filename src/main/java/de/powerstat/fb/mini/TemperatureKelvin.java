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
 * TemperatureKelvin 2700-6500.
 *
 * @param temperature TemperatureKelvin (2700-6500)
 */
@ValueObject
public record TemperatureKelvin(int temperature) implements Comparable<TemperatureKelvin>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param temperature TemperatureKelvin (2700-6500)
   * @throws IndexOutOfBoundsException If temperature ist &lt; 2700 or &gt; 6500
   */
  public TemperatureKelvin
   {
    if ((temperature < 2700) || (temperature > 6500))
     {
      throw new IndexOutOfBoundsException("temperature must be >= 2700 and <= 6500"); //$NON-NLS-1$
     }
   }


  /**
   * TemperatureKelvin factory.
   *
   * @param temperature TemperatureKelvin (2700-6500)
   * @return TemperatureKelvin object
   * @throws IndexOutOfBoundsException If temperature ist &lt; 2700 or &gt; 6500
   */
  public static TemperatureKelvin of(final int temperature)
   {
    return new TemperatureKelvin(temperature);
   }


  /**
   * TemperatureKelvin factory.
   *
   * @param temperature TemperatureKelvin (2700-6500)
   * @return TemperatureKelvin object
   * @throws IndexOutOfBoundsException If temperature ist &lt; 2700 or &gt; 6500
   * @throws NumberFormatException If temperature does not contain a parsable int.
   */
  public static TemperatureKelvin of(final String temperature)
   {
    return new TemperatureKelvin(Integer.parseInt(temperature));
   }


  /**
   * Returns the value of this TemperatureKelvin as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(temperature);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final TemperatureKelvin obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(temperature, obj.temperature);
   }

 }
