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
 * Saturation 0-255.
 *
 * @param saturation Saturation (0-255)
 */
@ValueObject
public record Saturation(int saturation) implements Comparable<Saturation>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param saturation Saturation (0-255)
   * @throws IndexOutOfBoundsException If saturation ist &lt; 0 or &gt; 255
   */
  public Saturation
   {
    if ((saturation < 0) || (saturation > 255))
     {
      throw new IndexOutOfBoundsException("saturation must be >= 0 and <= 255"); //$NON-NLS-1$
     }
   }


  /**
   * Saturation factory.
   *
   * @param saturation Saturation (0-255)
   * @return Saturation object
   * @throws IndexOutOfBoundsException If saturation ist &lt; 0 or &gt; 255
   */
  public static Saturation of(final int saturation)
   {
    return new Saturation(saturation);
   }


  /**
   * Saturation factory.
   *
   * @param saturation Saturation (0-255)
   * @return Saturation object
   * @throws IndexOutOfBoundsException If saturation ist &lt; 0 or &gt; 255
   * @throws NumberFormatException If saturation does not contain a parsable int.
   */
  public static Saturation of(final String saturation)
   {
    return new Saturation(Integer.parseInt(saturation));
   }


  /**
   * Returns the value of this Saturation as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(saturation);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Saturation obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(saturation, obj.saturation);
   }


 }
