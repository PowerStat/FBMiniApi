/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;


/**
 * Energy in Wh.
 */
public class Energy implements Comparable<Energy>
 {
  /**
   * Energy in Wh.
   */
  private final long energy;


  /**
   * Constructor.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @throws IndexOutOfBoundsException If energy is less than zero
   */
  public Energy(final long energy)
   {
    super();
    if (energy < 0)
     {
      throw new IndexOutOfBoundsException("energy must be >= 0"); //$NON-NLS-1$
     }
    this.energy = energy;
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
   * Get energy in watt hours.
   *
   * @return Energy in Wh
   */
  public long getEnergyWattHours()
   {
    return this.energy;
   }


  /**
   * Get energy in kilo watt hours.
   *
   * @return Energy in KWh
   */
  public long getEnergyKiloWattHours()
   {
    return this.energy / 1000;
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
    return Long.hashCode(this.energy);
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
    return this.energy == other.energy;
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
    final StringBuilder builder = new StringBuilder();
    builder.append("Energy[energy=").append(this.energy).append(']'); //$NON-NLS-1$
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
    return Long.compare(this.energy, obj.energy);
   }

 }
