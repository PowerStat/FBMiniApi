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
 * Trigger.
 *
 * @param ain AIN
 * @param name Name
 * @param active true: active; false: inactive
 */
@ValueObject
public record Trigger(AIN ain, String name, boolean active) implements Comparable<Trigger>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param ain AIN
   * @param name Name
   * @param active true: active; false: inactive
   */
  public Trigger
   {
    Objects.requireNonNull(ain, "ain"); //$NON-NLS-1$
    Objects.requireNonNull(name, "name"); //$NON-NLS-1$
    if (name.isEmpty() || (name.length() > 40))
     {
      throw new IllegalArgumentException("name with wrong length: " + name.length()); //$NON-NLS-1$
     }
    // TODO Name regexp
   }


  /**
   * Trigger factory.
   *
   * @param ain AIN
   * @param name Trigger name
   * @param active true: active, false: inactive
   * @return Trigger object
   */
  public static Trigger of(final AIN ain, final String name, final boolean active)
   {
    return new Trigger(ain, name, active);
   }


  /**
   * Returns the name of this Trigger.
   *
   * @return The name represented by this object.
   */
  @Override
  public String stringValue()
   {
    return name;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Trigger obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = ain.compareTo(obj.ain);
    if (result == 0)
     {
      result = name.compareTo(obj.name);
      if (result == 0)
       {
        result = Boolean.compare(active, obj.active);
       }
     }
    return result;
   }

 }
