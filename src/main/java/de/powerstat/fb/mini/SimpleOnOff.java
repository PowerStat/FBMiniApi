/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Simple on off.
 */
public final class SimpleOnOff implements Comparable<SimpleOnOff>, IValueObject
 {
  /**
   * State: on, off.
   */
  private final boolean state;


  /**
   * Constructor.
   *
   * @param state true: on; false: off
   */
  private SimpleOnOff(final boolean state)
   {
    this.state = state;
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
   * Returns the value of this SimpleOnOff as a boolean.
   *
   * @return The value represented by this object after conversion to type boolean.
   */
  public boolean booleanValue()
   {
    return state;
   }


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Boolean.hashCode(state);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final SimpleOnOff other))
     {
      return false;
     }
    return state == other.state;
   }


  /**
   * Returns the string representation of this SimpleOnOff.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "SimpleOnOff[state=false]"
   *
   * @return String representation of this SimpleOnOff
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(19);
    builder.append("SimpleOnOff[state=").append(state).append(']'); //$NON-NLS-1$
    return builder.toString();
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
