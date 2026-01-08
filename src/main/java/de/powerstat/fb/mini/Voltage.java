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
 * Voltage in 0,001V.
 *
 * @param voltage Voltage in 0,001V. (must be &gt;= 0)
 */
@ValueObject
public record Voltage(long voltage) implements Comparable<Voltage>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param voltage Voltage in 0,001V. (must be &gt;= 0)
   * @throws IndexOutOfBoundsException If voltage is less than zero
   */
  public Voltage
   {
    if (voltage < 0)
     {
      throw new IndexOutOfBoundsException("voltage must be >= 0"); //$NON-NLS-1$
     }
   }


  /**
   * Voltage factory.
   *
   * @param voltage Voltage in 0,001V. (must be &gt;= 0)
   * @return Voltage object
   * @throws IndexOutOfBoundsException If voltage is less than zero
   */
  public static Voltage of(final long voltage)
   {
    return new Voltage(voltage);
   }


  /**
   * Voltage factory.
   *
   * @param voltage Voltage in 0,001V. (must be &gt;= 0)
   * @return Voltage object
   * @throws IndexOutOfBoundsException If voltage is less than zero
   * @throws NumberFormatException If voltage does not contain a parsable long.
   */
  public static Voltage of(final String voltage)
   {
    return new Voltage(Long.parseLong(voltage));
   }


  /**
   * Returns the value of this Voltage as a String in 0,001V.
   *
   * @return The numeric value represented by this object after conversion to type String in 0,001V.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(voltage);
   }


  /**
   * Get voltage in volt.
   *
   * @return Voltage in Volt
   */
  public long getVoltageVolt()
   {
    return voltage / 1000;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Voltage obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(voltage, obj.voltage);
   }

 }
