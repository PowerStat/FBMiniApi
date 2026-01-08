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
 * Hue 0-359 degrees.
 *
 * @param hue Hue (0-359)
 */
@ValueObject
public record Hue(int hue) implements Comparable<Hue>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param hue Hue (0-359)
   * @throws IndexOutOfBoundsException If hue ist &lt; 0 or &gt; 359
   */
  public Hue
   {
    if ((hue < 0) || (hue > 359))
     {
      throw new IndexOutOfBoundsException("hue must be >= 0 and <= 359"); //$NON-NLS-1$
     }
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
