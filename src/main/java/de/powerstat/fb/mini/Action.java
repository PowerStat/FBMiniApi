/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * TR64 Action.
 */
@ValueObject
public final class Action implements Comparable<Action>, IValueObject
 {
  /**
   * ServiceType regexp.
   *
   * [a-zA-Z_-]+
   */
  private static final Pattern ACTION_REGEXP = Pattern.compile("^[a-zA-Z_-]{1,32}$"); //$NON-NLS-1$

  /**
   * Action.
   */
  @SuppressWarnings("PMD.AvoidFieldNameMatchingTypeName")
  private final String action;


  /**
   * Constructor.
   *
   * @param action Action string
   * @throws NullPointerException if action is null
   * @throws IllegalArgumentException if action is not an correct action
   */
  private Action(final String action)
   {
    super();
    Objects.requireNonNull(action, "action"); //$NON-NLS-1$
    if ((action.length() < 6) || (action.length() > 32))
     {
      throw new IllegalArgumentException("action with wrong length: " + action.length()); //$NON-NLS-1$
     }
    if (!ACTION_REGEXP.matcher(action).matches())
     {
      throw new IllegalArgumentException("action with wrong format"); //$NON-NLS-1$
     }
    this.action = action;
   }


  /**
   * Action factory.
   *
   * @param action Action string
   * @return Action object
   */
  public static Action of(final String action)
   {
    return new Action(action);
   }


  /**
   * Returns the value of this Action as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return action;
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
    return action.hashCode();
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @SuppressWarnings("PMD.SimplifyBooleanReturns")
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final Action other))
     {
      return false;
     }
    return action.equals(other.action);
   }


  /**
   * Returns the string representation of this Action.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Action[action=GetPersistentData]"
   *
   * @return String representation of this Action
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("Action[action=").append(action).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Action obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return action.compareTo(obj.action);
   }

 }
