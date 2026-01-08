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
 * Switch.
 *
 * @param state true: on; false: off; unknown: null
 * @param mode true: auto; false: manuell; unknown: null
 * @param lock true: locked; false: unlocked; unknown: null
 * @param devicelock true: locked; false: unlocked; unknown: null
 */
@ValueObject
public record Switch(Boolean state, Boolean mode, Boolean lock, Boolean devicelock) implements Comparable<Switch>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param state true: on; false: off; unknown: null
   * @param mode true: auto; false: manuell; unknown: null
   * @param lock true: locked; false: unlocked; unknown: null
   * @param devicelock true: locked; false: unlocked; unknown: null
   */
  public Switch
   {
   }


  /**
   * Switch factory.
   *
   * @param state true: on; false: off; unknown: null
   * @param mode true: auto; false: manuell; unknown: null
   * @param lock true: locked; false: unlocked; unknown: null
   * @param devicelock true: locked; false: unlocked; unknown: null
   * @return Switch object
   */
  public static Switch of(final Boolean state, final Boolean mode, final Boolean lock, final Boolean devicelock)
   {
    return new Switch(state, mode, lock, devicelock);
   }


  /**
   * Returns the value of this Switch as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(state);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Switch obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Boolean.compare(state, obj.state);
    if (result == 0)
     {
      result = Boolean.compare(mode, obj.mode);
      if (result == 0)
       {
        result = Boolean.compare(lock, obj.lock);
        if (result == 0)
         {
          result = Boolean.compare(devicelock, obj.devicelock);
         }
       }
     }
    return result;
   }

 }
