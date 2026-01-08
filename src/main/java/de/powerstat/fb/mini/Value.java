/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Value 0-255.
 *
 * @param value Value (0-255)
 */
@ValueObject
public record Value(int value) implements Comparable<Value>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param value Value (0-255)
   * @throws IndexOutOfBoundsException If value ist &lt; 0 or &gt; 255
   */
  public Value
   {
    if ((value < 0) || (value > 255))
     {
      throw new IndexOutOfBoundsException("value must be >= 0 and <= 255"); //$NON-NLS-1$
     }
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
