/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Trigger.
 */
public final class Trigger implements Comparable<Trigger>, IValueObject
 {
  /**
   * AIN.
   */
  private final AIN ain;

  /**
   * Trigger name.
   */
  private final String name;

  /**
   * Active or inactive.
   */
  private final boolean active;


  /**
   * Constructor.
   *
   * @param ain AIN
   * @param name Name
   * @param active true: active; false: inactive
   */
  private Trigger(final AIN ain, final String name, final boolean active)
   {
    super();
    Objects.requireNonNull(ain, "ain"); //$NON-NLS-1$
    Objects.requireNonNull(name, "name"); //$NON-NLS-1$
    if (name.isEmpty() || (name.length() > 40))
     {
      throw new IllegalArgumentException("name with wrong length: " + name.length()); //$NON-NLS-1$
     }
    // Name regexp
    this.ain = ain;
    this.active = active;
    this.name = name;
   }


  /**
   * Trigger factory.
   *
   * @param ain AIN
   * @param name Trigger name
   * @param active true: active, false: inactive
   * @return Trigger object
   */
  public static Trigger of(final AIN ain, final String name, final boolean active)
   {
    return new Trigger(ain, name, active);
   }


  /**
   * Returns the name of this Trigger.
   *
   * @return The name represented by this object.
   */
  @Override
  public String stringValue()
   {
    return name;
   }


  /**
   * Returns the AIN of this Trigger.
   *
   * @return The ain represented by this object.
   */
  public AIN ainValue()
   {
    return ain;
   }


  /**
   * Returns the active value of this Trigger.
   *
   * @return The active value represented by this object.
   */
  public boolean isActive()
   {
    return active;
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
    return Objects.hash(ain, name, active);
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
    if (!(obj instanceof final Trigger other))
     {
      return false;
     }
    boolean result = ain.equals(other.ain);
    if (result)
     {
      result = name.equals(other.name);
      if (result)
       {
        result = (active == other.active);
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Trigger.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Trigger[AIN[ain=000000000000], name=abc, active=true]"
   *
   * @return String representation of this Trigger
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(29);
    builder.append("Trigger[ain=").append(ain).append(", name=").append(name).append(", active=").append(active).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Trigger obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = ain.compareTo(obj.ain);
    if (result == 0)
     {
      result = name.compareTo(obj.name);
      if (result == 0)
       {
        result = Boolean.compare(active, obj.active);
       }
     }
    return result;
   }

 }
