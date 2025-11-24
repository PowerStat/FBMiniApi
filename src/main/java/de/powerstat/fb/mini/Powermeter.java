/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Powermeter.
 */
public final class Powermeter implements Comparable<Powermeter>, IValueObject
 {
  /**
   * Voltage.
   */
  private final Voltage voltage;

  /**
   * Power.
   */
  private final Power power;

  /**
   * Energy.
   */
  private final Energy energy;


  /**
   * Constructor.
   *
   * @param voltage Voltage
   * @param power Power
   * @param energy Energy
   * @throws NullPointerException When one of the parameters is null
   */
  private Powermeter(final Voltage voltage, final Power power, final Energy energy)
   {
    super();
    Objects.requireNonNull(voltage, "voltage"); //$NON-NLS-1$
    Objects.requireNonNull(power, "power"); //$NON-NLS-1$
    Objects.requireNonNull(energy, "energy"); //$NON-NLS-1$
    this.voltage = voltage;
    this.power = power;
    this.energy = energy;
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
   * Get voltage.
   *
   * @return Voltage.
   */
  public Voltage getVoltage()
   {
    return voltage;
   }


  /**
   * Get power.
   *
   * @return Power.
   */
  public Power getPower()
   {
    return power;
   }


  /**
   * Get energy.
   *
   * @return Energy.
   */
  public Energy getEnergy()
   {
    return energy;
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
    return Objects.hash(voltage, power, energy);
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
    if (!(obj instanceof final Powermeter other))
     {
      return false;
     }
    boolean result = voltage.equals(other.voltage);
    if (result)
     {
      result = power.equals(other.power);
      if (result)
       {
        result = energy.equals(other.energy);
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Powermeter.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Powermeter[voltage=0, power=0, energy=0]"
   *
   * @return String representation of this Powermeter
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(37);
    builder.append("Powermeter[voltage=").append(voltage)
      .append(", power=").append(power)
      .append(", energy=").append(energy)
      .append(']');
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
