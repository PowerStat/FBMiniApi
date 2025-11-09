/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.NoSuchElementException;
import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Optional of a specific type.
 *
 * @param <T> Use only value objects
 */
public final class OptionalOf<T extends IValueObject> // extends Hue & Saturation & TemperatureKelvin
 {
  /**
   * Value object.
   */
  private final IValueObject value;


  /**
   * Constructor.
   *
   * @param value Value Object or null
   */
  public OptionalOf(final T value)
   {
    super();
    this.value = value;
   }


  /**
   * Returns the value of the value object as an String.
   *
   * @return The string value represented by this object or "".
   */
  public String stringValue()
   {
    return (value == null) ? "" : value.stringValue();
   }


  /**
   * Returns the value of the value object as an int.
   *
   * @return The numeric value represented by this object after conversion to type int or -1.
   * @throws NumberFormatException If level does not contain a parsable int.
   */
  public int intValue()
   {
    return (value == null) ? -1 : Integer.parseInt(value.stringValue());
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
    return Objects.hash(value);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @throws NoSuchElementException If there is no entry in this GroupOf
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final OptionalOf<?> other))
    // if ((obj == null) || (this.getClass() != obj.getClass()))
     {
      return false;
     }
    return value.equals(other.value);
   }


  /**
   * Returns the string representation of this OptionalOf.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "OptionalOf&lt;&gt;[, ...]"
   *
   * @return String representation of this OptionalOf
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("OptionalOf<>[value="); //$NON-NLS-1$
    builder.append(value);
    builder.append(']');
    return builder.toString();
   }

 }
