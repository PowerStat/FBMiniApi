/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Level 0-255.
 */
public final class Level implements Comparable<Level>, IValueObject
 {
  /**
   * Level 0-255.
   */
  private final int level;


  /**
   * Constructor.
   *
   * @param level Level (0-255)
   * @throws IndexOutOfBoundsException If level ist &lt; 0 or &gt; 255
   */
  private Level(final int level)
   {
    super();
    if ((level < 0) || (level > 255))
     {
      throw new IndexOutOfBoundsException("level must be >= 0 and <= 255"); //$NON-NLS-1$
     }
    this.level = level;
   }


  /**
   * Level factory.
   *
   * @param level Level (0-255)
   * @return Level object
   * @throws IndexOutOfBoundsException If level ist &lt; 0 or &gt; 255
   */
  public static Level of(final int level)
   {
    return new Level(level);
   }


  /**
   * Level factory.
   *
   * @param level Level (0-255)
   * @return Level object
   * @throws IndexOutOfBoundsException If level ist &lt; 0 or &gt; 255
   * @throws NumberFormatException If level does not contain a parsable int.
   */
  public static Level of(final String level)
   {
    return new Level(Integer.parseInt(level));
   }


  /**
   * Returns the value of this Level as an int.
   *
   * @return The numeric value represented by this object after conversion to type int (0-255)
   */
  public int intValue()
   {
    return this.level;
   }


  /**
   * Returns the value of this Level as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(this.level);
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
    return Integer.hashCode(this.level);
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
    if (!(obj instanceof final Level other))
     {
      return false;
     }
    return this.level == other.level;
   }


  /**
   * Returns the string representation of this Level.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Level[level=255]"
   *
   * @return String representation of this Level (0-255)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Level[level=").append(this.level).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Level obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(this.level, obj.level);
   }


 }
