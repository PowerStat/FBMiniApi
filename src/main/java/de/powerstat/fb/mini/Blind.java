/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;

import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;

/**
 * Blind.
 */
public final class Blind implements Comparable<Blind>, IValueObject
 {
  /**
   * Mode; false: manuel; true: auto; null: unknown or error.
   */
  private final Boolean mode;

  /**
   * End positions set; false: not configured; true: configured; null: unknown.
   */
  private final Boolean endpositionsset;


  /**
   * Constructor.
   *
   * @param mode false: manuel; mode true: auto; null: unknown or error.
   * @param endpositionsset false: not configured; true: configured; null: unknown
   */
  private Blind(final Boolean mode, final Boolean endpositionsset)
   {
    super();
    this.mode = mode;
    this.endpositionsset = endpositionsset;
   }


  /**
   * Blind factory.
   *
   * @param mode false: manuel; mode true: auto; null: unknown or error.
   * @param endpositionsset false: not configured; true: configured; null: unknown
   * @return Blind object
   */
  public static Blind of(final Boolean mode, final Boolean endpositionsset)
   {
    return new Blind(mode, endpositionsset);
   }


  /**
   * Returns the value of this Blind as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return mode.toString();
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
    return Objects.hash(mode, endpositionsset);
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
    if (!(obj instanceof final Blind other))
     {
      return false;
     }
    boolean result = mode.equals(other.mode);
    if (result)
     {
      result = endpositionsset.equals(other.endpositionsset);
     }
    return result;
   }


  /**
   * Returns the string representation of this Blind.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Blind[mode=, endpositionsset=]"
   *
   * @return String representation of this Blind
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(30);
    builder.append("Blind[mode=").append(mode).append(", endpositionsset=").append(endpositionsset).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Blind obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = mode.compareTo(obj.mode);
    if (result == 0)
     {
      result = endpositionsset.compareTo(obj.endpositionsset);
     }
    return result;
   }

 }
