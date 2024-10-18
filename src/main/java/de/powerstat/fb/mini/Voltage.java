/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Voltage in 0,001V.
 */
public final class Voltage implements Comparable<Voltage>, IValueObject
 {
  /**
   * Voltage in 0,001V.
   */
  private final long voltage;


  /**
   * Constructor.
   *
   * @param voltage Voltage in 0,001V. (must be &gt;= 0)
   * @throws IndexOutOfBoundsException If voltage is less than zero
   */
  private Voltage(final long voltage)
   {
    super();
    if (voltage < 0)
     {
      throw new IndexOutOfBoundsException("voltage must be >= 0"); //$NON-NLS-1$
     }
    this.voltage = voltage;
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
   * Returns the value of this Voltage as a long in 0,001V.
   *
   * @return The numeric value represented by this object after conversion to type long in 0,001V.
   */
  public long longValue()
   {
    return this.voltage;
   }


  /**
   * Returns the value of this Voltage as a String in 0,001V.
   *
   * @return The numeric value represented by this object after conversion to type String in 0,001V.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(this.voltage);
   }


  /**
   * Get voltage in volt.
   *
   * @return Voltage in Volt
   */
  public long getVoltageVolt()
   {
    return this.voltage / 1000;
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
    return Long.hashCode(this.voltage);
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
    if (!(obj instanceof final Voltage other))
     {
      return false;
     }
    return this.voltage == other.voltage;
   }


  /**
   * Returns the string representation of this Voltage in 0,001V.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Voltage[Voltage=228201]"
   *
   * @return String representation of this Voltage in 0,001V
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Voltage[voltage=").append(this.voltage).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Voltage obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(this.voltage, obj.voltage);
   }

 }
