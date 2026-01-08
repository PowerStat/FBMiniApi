/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;
import de.powerstat.ddd.values.science.Percent;


/**
 * Level control.
 *
 * @param level Level
 * @param levelpercentage Level in percent
 */
@ValueObject
public record LevelControl(Level level, Percent levelpercentage) implements Comparable<LevelControl>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param level Level
   * @param levelpercentage Level in percent
   * @throws NullPointerException When level or levelpercentage is null
   */
  public LevelControl
   {
    Objects.requireNonNull(level, "level"); //$NON-NLS-1$
    Objects.requireNonNull(levelpercentage, "levelpercentage"); //$NON-NLS-1$
   }


  /**
   * LevelControl factory.
   *
   * @param level Level
   * @param levelpercentage Level in percent
   * @return LevelControl object
   * @throws NullPointerException When level or levelpercentage is null
   */
  public static LevelControl of(final Level level, final Percent levelpercentage)
   {
    return new LevelControl(level, levelpercentage);
   }


  /**
   * Returns the value of this LevelControl as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return level.stringValue();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final LevelControl obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = level.compareTo(obj.level);
    if (result == 0)
     {
      result = levelpercentage.compareTo(obj.levelpercentage);
     }
    return result;
   }

 }
