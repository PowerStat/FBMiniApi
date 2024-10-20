/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Metadata.
 */
public final class Metadata implements Comparable<Metadata>, IValueObject
 {
  /**
   * Icon id for icon font or -1 if undefined.
   */
  private final int icon;

  /**
   * Scenario type.
   */
  private final ScenarioType type;


  /**
   * Constructor.
   *
   * Only one of the following parameters must be defined.
   *
   * @param icon Icon id > 0 or -1 if undefined
   * @param type ScenarioType
   * @throws NullPointerException if type is null
   * @throws IndexOutOfBoundsException If icon ist &lt; -1
   */
  private Metadata(final int icon, final ScenarioType type)
   {
    super();
    Objects.requireNonNull(type, "type"); //$NON-NLS-1$
    if (icon < -1)
     {
      throw new IndexOutOfBoundsException("icon < -1");
     }
    if ((icon == -1) && ScenarioType.UNDEFINED.equals(type))
     {
      throw new IllegalArgumentException("One of icon or type must be set");
     }
    if ((icon >= -0) && !ScenarioType.UNDEFINED.equals(type))
     {
      throw new IllegalArgumentException("Only one of icon or type must be set");
     }
    this.icon = icon;
    this.type = type;
   }


  /**
   * Metadata factory.
   *
   *
   * Only one of the following parameters must be defined.
   *
   * @param icon Icon id > 0 or -1 if undefined
   * @param type ScenarioType
   * @return Metadata object
   * @throws NullPointerException if type is null
   * @throws IndexOutOfBoundsException If icon ist &lt; -1
   */
  public static Metadata of(final int icon, final ScenarioType type)
   {
    return new Metadata(icon, type);
   }


  /**
   * Returns the value of this Metadata as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return (this.icon >= 0) ? String.valueOf(this.icon) : this.type.stringValue();
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
    return Objects.hash(this.icon, this.type);
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
    if (!(obj instanceof final Metadata other))
     {
      return false;
     }
    boolean result = (this.icon == other.icon);
    if (result && (this.icon == -1))
     {
      result = this.type.equals(other.type);
     }
    return result;
   }


  /**
   * Returns the string representation of this Metadata.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Metadata[icon=0, type=UNDEFINED]"
   *
   * @return String representation of this Metadata
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Metadata[icon=").append(this.icon).append(", type=").append(this.type).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Metadata obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Integer.compare(this.icon, obj.icon);
    if ((result == 0) && (this.icon == -1))
     {
      result = this.type.compareTo(obj.type);
     }
    else if ((this.icon == -1) || (obj.icon == -1))
     {
      result = (this.icon == -1) ? 1 : -1;
     }
    return result;
   }

 }
