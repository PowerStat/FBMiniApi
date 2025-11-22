/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Color.
 */
public final class Color implements Comparable<Color>, IValueObject
 {
  /**
   * Index 1-3.
   */
  private final int index;

  /**
   * Hue.
   */
  private final Hue hue;

  /**
   * Saturation.
   */
  private final Saturation saturation;

  /**
   * Value.
   */
  private final Value value;


  /**
   * Constructor.
   *
   * @param index Index (1-3)
   * @param hue Hue
   * @param saturation Saturation
   * @param value Value
   * @throws NullPointerException if hue, saturation or value is null
   * @throws IndexOutOfBundsException if index is not 1-3
   */
  private Color(final int index, final Hue hue, final Saturation saturation, final Value value)
   {
    super();
    Objects.requireNonNull(hue, "hue"); //$NON-NLS-1$
    Objects.requireNonNull(saturation, "saturation"); //$NON-NLS-1$
    Objects.requireNonNull(value, "value"); //$NON-NLS-1$
    if ((index < 1) || (index > 3))
     {
      throw new IndexOutOfBoundsException("Index < 1 or > 3");
     }
    this.index = index;
    this.hue = hue;
    this.saturation = saturation;
    this.value = value;
   }


  /**
   * Color factory.
   *
   * @param index Index (1-3)
   * @param hue Hue
   * @param saturation Saturation
   * @param value Value
   * @return Color object
   * @throws NullPointerException if hue, saturation or value is null
   * @throws IndexOutOfBoundsException if index is not 1-3
   */
  public static Color of(final int index, final Hue hue, final Saturation saturation, final Value value)
   {
    return new Color(index, hue, saturation, value);
   }


  /**
   * Returns the value of this Color as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return "";
   }


  /**
   * Returns the index of this Color.
   *
   * @return The index represented by this object.
   */
  public int intValue()
   {
    return index;
   }


  /**
   * Returns the hue of this Color.
   *
   * @return The hue represented by this object.
   */
  public Hue hueValue()
   {
    return hue;
   }


  /**
   * Returns the saturation of this Color.
   *
   * @return The saturation represented by this object.
   */
  public Saturation saturationValue()
   {
    return saturation;
   }


  /**
   * Returns the value of this Color.
   *
   * @return The value represented by this object.
   */
  public Value valueValue()
   {
    return value;
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
    return Objects.hash(index, hue, saturation, value);
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
    if (!(obj instanceof final Color other))
     {
      return false;
     }
    boolean result = (index == other.index);
    if (result)
     {
      result = hue.equals(other.hue);
      if (result)
       {
        result = saturation.equals(other.saturation);
        if (result)
         {
          result = value.equals(other.value);
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Color.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Color[index=1, hue=Hue[hue=0], saturation=Saturation[saturation=0], value=Value[value=0]]"
   *
   * @return String representation of this Color
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(40);
    builder.append("Color[index=").append(index).append(", hue=").append(hue).append(", saturation=").append(saturation).append(", value=").append(value).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Color obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Integer.compare(index, obj.index);
    if (result == 0)
     {
      result = hue.compareTo(obj.hue);
      if (result == 0)
       {
        result = saturation.compareTo(obj.saturation);
        if (result == 0)
         {
          result = value.compareTo(obj.value);
         }
       }
     }
    return result;
   }

 }
