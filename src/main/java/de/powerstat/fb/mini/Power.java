/*
 * Copyright (C) 2020-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Power in mW.
 *
 * @param power Power in mW.
 */
@ValueObject
public record Power(long powerMW) implements Comparable<Power>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param power Power in mW.
   */
  public Power
   {
   /*
    if (power < 0) // Only for consumers - Producers will become negative
     {
      throw new IndexOutOfBoundsException("power must be >= 0"); //$NON-NLS-1$
     }
    */
   }


  /**
   * Power factory.
   *
   * @param power Power in mW.
   * @return Power object
   */
  public static Power of(final long power)
   {
    return new Power(power);
   }


  /**
   * Power factory.
   *
   * @param power Power in mW.
   * @return Power object
   * @throws NumberFormatException If power does not contain a parsable long.
   */
  public static Power of(final String power)
   {
    return new Power(Long.parseLong(power));
   }


  /**
   * Returns the value of this Power as a String in milli watt.
   *
   * @return The numeric value represented by this object after conversion to type String in mW
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(powerMW);
   }


  /**
   * Get power in watt.
   *
   * @return Power in W
   */
  public long getPowerWatt()
   {
    return powerMW / 1000;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Power obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(powerMW, obj.powerMW);
   }

 }
