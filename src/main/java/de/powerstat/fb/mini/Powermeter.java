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
 * Powermeter.
 *
 * @param voltage Voltage
 * @param power Power
 * @param energy Energy
 */
@ValueObject
public record Powermeter(Voltage voltage, Power power, Energy energy) implements Comparable<Powermeter>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param voltage Voltage
   * @param power Power
   * @param energy Energy
   * @throws NullPointerException When one of the parameters is null
   */
  public Powermeter
   {
    Objects.requireNonNull(voltage, "voltage"); //$NON-NLS-1$
    Objects.requireNonNull(power, "power"); //$NON-NLS-1$
    Objects.requireNonNull(energy, "energy"); //$NON-NLS-1$
   }


  /**
   * Powermeter factory.
   *
   * @param voltage Voltage
   * @param power Power
   * @param energy Energy
   * @return Powermeter object
   * @throws NullPointerException When one of the parameters is null
   */
  public static Powermeter of(final Voltage voltage, final Power power, final Energy energy)
   {
    return new Powermeter(voltage, power, energy);
   }


  /**
   * Returns the value of this Powermeter as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return power.stringValue();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Powermeter obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = voltage.compareTo(obj.voltage);
    if (result == 0)
     {
      result = power.compareTo(obj.power);
      if (result == 0)
       {
        result = energy.compareTo(obj.energy);
       }
     }
    return result;
   }

 }
