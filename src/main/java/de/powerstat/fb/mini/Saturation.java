/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Saturation 0-255.
 */
public final class Saturation implements Comparable<Saturation>, IValueObject
 {
  /**
   * Saturation 0-255.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
  private final int saturation;


  /**
   * Constructor.
   *
   * @param saturation Saturation (0-255)
   * @throws IndexOutOfBoundsException If saturation ist &lt; 0 or &gt; 255
   */
  private Saturation(final int saturation)
   {
    super();
    if ((saturation < 0) || (saturation > 255))
     {
      throw new IndexOutOfBoundsException("saturation must be >= 0 and <= 255"); //$NON-NLS-1$
     }
    this.saturation = saturation;
   }


  /**
   * Saturation factory.
   *
   * @param saturation Saturation (0-255)
   * @return Saturation object
   * @throws IndexOutOfBoundsException If saturation ist &lt; 0 or &gt; 255
   */
  public static Saturation of(final int saturation)
   {
    return new Saturation(saturation);
   }


  /**
   * Saturation factory.
   *
   * @param saturation Saturation (0-255)
   * @return Saturation object
   * @throws IndexOutOfBoundsException If saturation ist &lt; 0 or &gt; 255
   * @throws NumberFormatException If saturation does not contain a parsable int.
   */
  public static Saturation of(final String saturation)
   {
    return new Saturation(Integer.parseInt(saturation));
   }


  /**
   * Returns the value of this Saturation as an int.
   *
   * @return The numeric value represented by this object after conversion to type int (0-255)
   */
  public int intValue()
   {
    return saturation;
   }


  /**
   * Returns the value of this Saturation as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(saturation);
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
    return Integer.hashCode(saturation);
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
    if (!(obj instanceof final Saturation other))
     {
      return false;
     }
    return saturation == other.saturation;
   }


  /**
   * Returns the string representation of this Saturation.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Saturation[saturation=255]"
   *
   * @return String representation of this Saturation (0-255)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(23);
    builder.append("Saturation[saturation=").append(saturation).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Saturation obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(saturation, obj.saturation);
   }


 }
