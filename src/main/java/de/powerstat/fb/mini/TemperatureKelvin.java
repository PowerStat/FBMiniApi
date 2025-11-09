/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * TemperatureKelvin 2700-6500.
 */
public final class TemperatureKelvin implements Comparable<TemperatureKelvin>, IValueObject
 {
  /**
   * TemperatureKelvin 2700-6500.
   */
  private final int temperature;


  /**
   * Constructor.
   *
   * @param temperature TemperatureKelvin (2700-6500)
   * @throws IndexOutOfBoundsException If temperature ist &lt; 2700 or &gt; 6500
   */
  private TemperatureKelvin(final int temperature)
   {
    super();
    if ((temperature < 2700) || (temperature > 6500))
     {
      throw new IndexOutOfBoundsException("temperature must be >= 2700 and <= 6500"); //$NON-NLS-1$
     }
    this.temperature = temperature;
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
   * Returns the value of this TemperatureKelvin as an int.
   *
   * @return The numeric value represented by this object after conversion to type int (2700-6500)
   */
  public int intValue()
   {
    return this.temperature;
   }


  /**
   * Returns the value of this TemperatureKelvin as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(this.temperature);
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
    return Integer.hashCode(this.temperature);
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
    if (!(obj instanceof final TemperatureKelvin other))
     {
      return false;
     }
    return this.temperature == other.temperature;
   }


  /**
   * Returns the string representation of this TemperatureKelvin.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "TemperatureKelvin[temperature=2700]"
   *
   * @return String representation of this TemperatureKelvin (2700-6500)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("TemperatureKelvin[temperature=").append(this.temperature).append(']'); //$NON-NLS-1$
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
  public int compareTo(final TemperatureKelvin obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(this.temperature, obj.temperature);
   }

 }
