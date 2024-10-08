/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Energy in Wh.
 */
public final class Energy implements Comparable<Energy>, IValueObject
 {
  /**
   * Energy in Wh.
   */
  private final long energyWh;


  /**
   * Constructor.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @throws IndexOutOfBoundsException If energy is less than zero
   */
  private Energy(final long energy)
   {
    super();
    if (energy < 0)
     {
      throw new IndexOutOfBoundsException("energy must be >= 0"); //$NON-NLS-1$
     }
    this.energyWh = energy;
   }


  /**
   * Energy factory.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @return Energy object
   * @throws IndexOutOfBoundsException If energy is less than zero
   */
  public static Energy of(final long energy)
   {
    return new Energy(energy);
   }


  /**
   * Energy factory.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @return Energy object
   * @throws IndexOutOfBoundsException If energy is less than zero
   * @throws NumberFormatException If energy does not contain a parsable long.
   */
  public static Energy of(final String energy)
   {
    return new Energy(Long.parseLong(energy));
   }


  /**
   * Returns the value of this Energy as a long in watt hours.
   *
   * @return The numeric value represented by this object after conversion to type long in Wh.
   */
  public long longValue()
   {
    return this.energyWh;
   }


  /**
   * Returns the value of this Energy as a String in watt hours.
   *
   * @return The numeric value represented by this object after conversion to type String in Wh.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(this.energyWh);
   }


  /**
   * Get energy in kilo watt hours.
   *
   * @return Energy in KWh
   */
  public long getEnergyKiloWattHours()
   {
    return this.energyWh / 1000;
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
    return Long.hashCode(this.energyWh);
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
    if (!(obj instanceof Energy))
     {
      return false;
     }
    final Energy other = (Energy)obj;
    return this.energyWh == other.energyWh;
   }


  /**
   * Returns the string representation of this Energy in Wh.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Energy[energy=75519]"
   *
   * @return String representation of this Energy in Wh
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Energy[energy=").append(this.energyWh).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Energy obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(this.energyWh, obj.energyWh);
   }

 }
