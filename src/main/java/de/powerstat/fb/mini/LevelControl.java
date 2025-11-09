/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;
import de.powerstat.validation.values.Percent;


/**
 * Level control.
 */
public final class LevelControl implements Comparable<LevelControl>, IValueObject
 {
  /**
   * Level.
   */
  private final Level level;

  /**
   * Level percentage.
   */
  private final Percent levelpercentage;


  /**
   * Constructor.
   *
   * @param level Level
   * @param levelpercentage Level in percent
   * @throws NullPointerException When level or levelpercentage is null
   */
  private LevelControl(final Level level, final Percent levelpercentage)
   {
    super();
    Objects.requireNonNull(level, "level"); //$NON-NLS-1$
    Objects.requireNonNull(levelpercentage, "levelpercentage"); //$NON-NLS-1$
    this.level = level;
    this.levelpercentage = levelpercentage;
   }


  /**
   * LevelControl factory.
   *
   * @param level Level
   * @param levelpercentage Level in percent
   * @return LevelControl object
   * @throws NullPointerException When level or levelpercentage is null
   */
  public static LevelControl of(final Level level, final Percent levelpercentage)
   {
    return new LevelControl(level, levelpercentage);
   }


  /**
   * Returns the value of this LevelControl as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.level.stringValue();
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
    return Objects.hash(this.level, this.levelpercentage);
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
    if (!(obj instanceof final LevelControl other))
     {
      return false;
     }
    boolean result = this.level.equals(other.level);
    if (result)
     {
      result = this.levelpercentage.equals(other.levelpercentage);
     }
    return result;
   }


  /**
   * Returns the string representation of this LevelControl.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "LevelControl[level=, levelpercentage=]"
   *
   * @return String representation of this LevelControl
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("LevelControl[level=").append(this.level).append(", levelpercentage=").append(this.levelpercentage).append(']'); //$NON-NLS-1$
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
  public int compareTo(final LevelControl obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = this.level.compareTo(obj.level);
    if (result == 0)
     {
      result = this.levelpercentage.compareTo(obj.levelpercentage);
     }
    return result;
   }

 }
