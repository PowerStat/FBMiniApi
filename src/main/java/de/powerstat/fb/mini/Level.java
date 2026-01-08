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
 * Level 0-255.
 *
 * @param level Level (0-255)
 */
@ValueObject
public record Level(int level) implements Comparable<Level>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param level Level (0-255)
   * @throws IndexOutOfBoundsException If level ist &lt; 0 or &gt; 255
   */
  public Level
   {
    if ((level < 0) || (level > 255))
     {
      throw new IndexOutOfBoundsException("level must be >= 0 and <= 255"); //$NON-NLS-1$
     }
   }


  /**
   * Level factory.
   *
   * @param level Level (0-255)
   * @return Level object
   * @throws IndexOutOfBoundsException If level ist &lt; 0 or &gt; 255
   */
  public static Level of(final int level)
   {
    return new Level(level);
   }


  /**
   * Level factory.
   *
   * @param level Level (0-255)
   * @return Level object
   * @throws IndexOutOfBoundsException If level ist &lt; 0 or &gt; 255
   * @throws NumberFormatException If level does not contain a parsable int.
   */
  public static Level of(final String level)
   {
    return new Level(Integer.parseInt(level));
   }


  /**
   * Returns the value of this Level as a String.
   *
   * @return The numeric value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(level);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Level obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Integer.compare(level, obj.level);
   }


 }
