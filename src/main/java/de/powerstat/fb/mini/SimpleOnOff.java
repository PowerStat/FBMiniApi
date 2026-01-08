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
 * Simple on off.
 *
 * @param state true: on; false: off
 */
@ValueObject
public record SimpleOnOff(boolean state) implements Comparable<SimpleOnOff>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param state true: on; false: off
   */
  public SimpleOnOff
   {
   }


  /**
   * SimpleOnOff factory.
   *
   * @param state true: on; false: off
   * @return SimpleOnOff object
   */
  public static SimpleOnOff of(final boolean state)
   {
    return new SimpleOnOff(state);
   }


  /**
   * Returns the value of this SimpleOnOff as a string.
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
  public int compareTo(final SimpleOnOff obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Boolean.compare(state, obj.state);
   }

 }
