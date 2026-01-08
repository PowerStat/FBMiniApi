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
 * Color.
 *
 * @param index Index (1-3)
 * @param hue Hue
 * @param saturation Saturation
 * @param value Value
 */
@ValueObject
public record Color(int index, Hue hue, Saturation saturation, Value value) implements Comparable<Color>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param index Index (1-3)
   * @param hue Hue
   * @param saturation Saturation
   * @param value Value
   * @throws NullPointerException if hue, saturation or value is null
   * @throws IndexOutOfBundsException if index is not 1-3
   */
  public Color
   {
    Objects.requireNonNull(hue, "hue"); //$NON-NLS-1$
    Objects.requireNonNull(saturation, "saturation"); //$NON-NLS-1$
    Objects.requireNonNull(value, "value"); //$NON-NLS-1$
    if ((index < 1) || (index > 3))
     {
      throw new IndexOutOfBoundsException("Index < 1 or > 3");
     }
   }


  /**
   * Color factory.
   *
   * @param index Index (1-3)
   * @param hue Hue
   * @param saturation Saturation
   * @param value Value
   * @return Color object
   * @throws NullPointerException if hue, saturation or value is null
   * @throws IndexOutOfBoundsException if index is not 1-3
   */
  public static Color of(final int index, final Hue hue, final Saturation saturation, final Value value)
   {
    return new Color(index, hue, saturation, value);
   }


  /**
   * Returns the value of this Color as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return index + ", " + hue + ", " + saturation + ", " + value;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Color obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Integer.compare(index, obj.index);
    if (result == 0)
     {
      result = hue.compareTo(obj.hue);
      if (result == 0)
       {
        result = saturation.compareTo(obj.saturation);
        if (result == 0)
         {
          result = value.compareTo(obj.value);
         }
       }
     }
    return result;
   }

 }
