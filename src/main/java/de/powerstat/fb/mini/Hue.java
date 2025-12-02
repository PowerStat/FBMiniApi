/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Hue 0-359 degrees.
 */
@ValueObject
public final class Hue implements Comparable<Hue>, IValueObject
 {
  /**
   * Hue 0-359.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
  private final int hue;


  /**
   * Constructor.
   *
   * @param hue Hue (0-359)
   * @throws IndexOutOfBoundsException If hue ist &lt; 0 or &gt; 359
   */
  private Hue(final int hue)
   {
    super();
    if ((hue < 0) || (hue > 359))
     {
      throw new IndexOutOfBoundsException("hue must be >= 0 and <= 359"); //$NON-NLS-1$
     }
    this.hue = hue;
   }


  /**
   * Hue factory.
   *
   * @param hue Hue (0-359)
   * @return Hue object
   * @throws IndexOutOfBoundsException If hue ist &lt; 0 or &gt; 359
   */
  public static Hue of(final int hue)
   {
    return new Hue(hue);
   }


  /**
   * Hue factory.
   *
   * @param hue Hue (0-359)
   * @return Hue object
   * @throws IndexOutOfBoundsException If lhue ist &lt; 0 or &gt; 359
   * @throws NumberFormatException If hue does not contain a parsable int.
   */
  public static Hue of(final String hue)
   {
    return new Hue(Integer.parseInt(hue));
   }


  /**
   * Returns the value of this Hue as an int.
   *
   * @return The numeric value represented by this object after conversion to type int (0-359)
   */
  public int intValue()
   {
    return hue;
   }


  /**
   * Returns the value of this Hue as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(hue);
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
    return Integer.hashCode(hue);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final Hue other))
     {
      return false;
     }
    return hue == other.hue;
   }


  /**
   * Returns the string representation of this Hue.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Hue[hue=359]"
   *
   * @return String representation of this Hue (0-359)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Hue[hue=").append(hue).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Hue obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(hue, obj.hue);
   }

 }
