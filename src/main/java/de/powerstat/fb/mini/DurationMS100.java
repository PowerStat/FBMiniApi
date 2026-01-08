/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Duration in 100 ms (0-Integer.MAX_VALUE).
 *
 * @param duration DurationMS100 (0-Integer.MAX_VALUE)
 */
@ValueObject
public record DurationMS100(int duration) implements Comparable<DurationMS100>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param duration DurationMS100 (0-Integer.MAX_VALUE)
   * @throws IndexOutOfBoundsException If duration ist &lt; 0
   */
  public DurationMS100
   {
    if (duration < 0)
     {
      throw new IndexOutOfBoundsException("duration must be >= 0"); //$NON-NLS-1$
     }
   }


  /**
   * DurationMS100 factory.
   *
   * @param duration DurationMS100 (0-Integer.MAX_VALUE)
   * @return DurationMS100 object
   * @throws IndexOutOfBoundsException If duration ist &lt; 0
   */
  public static DurationMS100 of(final int duration)
   {
    return new DurationMS100(duration);
   }


  /**
   * DurationMS100 factory.
   *
   * @param duration DurationMS100 (0-Integer.MAX_VALUE)
   * @return DurationMS100 object
   * @throws IndexOutOfBoundsException If duration ist &lt; 0
   * @throws NumberFormatException If duration does not contain a parsable int.
   */
  public static DurationMS100 of(final String duration)
   {
    return new DurationMS100(Integer.parseInt(duration));
   }


  /**
   * Returns the value of this DurationMS100 as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(duration);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final DurationMS100 obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(duration, obj.duration);
   }

 }
