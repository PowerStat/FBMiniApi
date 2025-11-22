/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Value 0-255.
 */
public final class Value implements Comparable<Value>, IValueObject
 {
  /**
   * Value 0-255.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
  private final int value;


  /**
   * Constructor.
   *
   * @param value Value (0-255)
   * @throws IndexOutOfBoundsException If value ist &lt; 0 or &gt; 255
   */
  private Value(final int value)
   {
    super();
    if ((value < 0) || (value > 255))
     {
      throw new IndexOutOfBoundsException("value must be >= 0 and <= 255"); //$NON-NLS-1$
     }
    this.value = value;
   }


  /**
   * Value factory.
   *
   * @param value Value (0-255)
   * @return Value object
   * @throws IndexOutOfBoundsException If value ist &lt; 0 or &gt; 255
   */
  public static Value of(final int value)
   {
    return new Value(value);
   }


  /**
   * Value factory.
   *
   * @param value Value (0-255)
   * @return Value object
   * @throws IndexOutOfBoundsException If value ist &lt; 0 or &gt; 255
   * @throws NumberFormatException If value does not contain a parsable int.
   */
  public static Value of(final String value)
   {
    return new Value(Integer.parseInt(value));
   }


  /**
   * Returns the value of this Value as an int.
   *
   * @return The numeric value represented by this object after conversion to type int (0-255)
   */
  public int intValue()
   {
    return value;
   }


  /**
   * Returns the value of this Value as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(value);
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
    return Integer.hashCode(value);
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
    if (!(obj instanceof final Value other))
     {
      return false;
     }
    return value == other.value;
   }


  /**
   * Returns the string representation of this Value.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Value[value=255]"
   *
   * @return String representation of this Value (0-255)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Value[value=").append(value).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Value obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(value, obj.value);
   }


 }
