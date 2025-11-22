/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.List;
import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Hs.
 */
public final class Hs implements Comparable<Hs>, IValueObject
 {
  /**
   * Index 1-2.
   */
  private final int index;

  /**
   * Color name enum.
   */
  private final int nameEnum;

  /**
   * Color name.
   */
  private final String name;

  /**
   * Color.
   */
  private final List<Color> colors;


  /**
   * Constructor.
   *
   * @param index Index 1-12
   * @param nameEnum Color names enum &gt;= 0
   * @param name Color name
   * @param colors List of Color
   * @throws NullPointerException if name or colors is null
   * @throws IndexOutOfBoundsException if index is not 1-12 or nameEnum &lt; 0
   * @throws IllegalArgumentException If colors has not 3 entries
   */
  private Hs(final int index, final int nameEnum, final String name, final List<Color> colors)
   {
    super();
    Objects.requireNonNull(name, "name"); //$NON-NLS-1$
    Objects.requireNonNull(colors, "colors"); //$NON-NLS-1$
    if ((index < 1) || (index > 12))
     {
      throw new IndexOutOfBoundsException("index must be 1-12"); //$NON-NLS-1$
     }
    if ((nameEnum < 0))
     {
      throw new IndexOutOfBoundsException("nameEnum must be >= 0"); //$NON-NLS-1$
     }
    if (colors.size() != 3)
     {
      throw new IllegalArgumentException("colors must have 3 entries");
     }
    this.index = index;
    this.nameEnum = nameEnum;
    this.name = name;
    this.colors = List.copyOf(colors);
   }


  /**
   * Hs factory.
   *
   * @param index Index 1-12
   * @param nameEnum Color names enum &gt;= 0
   * @param name Color name
   * @param colors List of Color
   * @return Hs object
   * @throws NullPointerException if name or colors is null
   * @throws IndexOutOfBoundsException if index is not 1-12 or nameEnum &lt; 0
   * @throws IllegalArgumentException If colors has not 12 entries
   */
  public static Hs of(final int index, final int nameEnum, final String name, final List<Color> colors)
   {
    return new Hs(index, nameEnum, name, colors);
   }


  /**
   * Returns the value of this Hs as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return "";
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
    return Objects.hash(index, nameEnum, name, colors);
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
    if (!(obj instanceof final Hs other))
     {
      return false;
     }
    boolean result = (index == other.index);
    if (result)
     {
      result = (nameEnum == other.nameEnum);
      if (result)
       {
        result = name.equals(other.name);
        if (result)
         {
          result = colors.equals(other.colors);
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Hs.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Hs[index=1, nameEnum=5569, name=Rot, colors=Color[]]"
   *
   * @return String representation of this Hs
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(37);
    builder.append("Hs[index=").append(index).append(", nameEnum=").append(nameEnum).append(", name=").append(name).append(", colors=").append(colors).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Hs obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Integer.compare(index, obj.index);
    if (result == 0)
     {
      result = Integer.compare(nameEnum, obj.nameEnum);
      if (result == 0)
       {
        result = name.compareTo(obj.name);
        if (result == 0)
         {
          // result = this.colors.compareTo(obj.colors); // TODO
         }
       }
     }
    return result;
   }

 }
