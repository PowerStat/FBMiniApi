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
 * Hkr next change.
 *
 * @param endperiod Next change timestamp
 * @param tchange Target temperature
 */
@ValueObject
public record HkrNextChange(UnixTimestamp endperiod, TemperatureCelsius tchange) implements Comparable<HkrNextChange>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param endperiod Next change timestamp
   * @param tchange Target temperature
   * @throws NullPointerException When one of the parameters is null
   */
  public HkrNextChange
   {
    Objects.requireNonNull(endperiod, "endperiod"); //$NON-NLS-1$
    Objects.requireNonNull(tchange, "tchange"); //$NON-NLS-1$
   }


  /**
   * HkrNextChange factory.
   *
   * @param endperiod Next change timestamp
   * @param tchange Target temperature
   * @return HkrNextChange object
   * @throws NullPointerException When one of the parameters is null
   */
  public static HkrNextChange of(final UnixTimestamp endperiod, final TemperatureCelsius tchange)
   {
    return new HkrNextChange(endperiod, tchange);
   }


  /**
   * Returns the value of this HkrNextChange as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return endperiod.stringValue();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final HkrNextChange obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = endperiod.compareTo(obj.endperiod);
    if (result == 0)
     {
      result = tchange.compareTo(obj.tchange);
     }
    return result;
   }

 }
