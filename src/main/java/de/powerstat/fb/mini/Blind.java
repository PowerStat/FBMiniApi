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
 * Blind.
 */
@ValueObject
public record Blind(Boolean mode, Boolean endpositionsset) implements Comparable<Blind>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param mode false: manuel; mode true: auto; null: unknown or error.
   * @param endpositionsset false: not configured; true: configured; null: unknown
   */
  public Blind
   {
   }


  /**
   * Blind factory.
   *
   * @param mode false: manuel; mode true: auto; null: unknown or error.
   * @param endpositionsset false: not configured; true: configured; null: unknown
   * @return Blind object
   */
  public static Blind of(final Boolean mode, final Boolean endpositionsset)
   {
    return new Blind(mode, endpositionsset);
   }


  /**
   * Returns the value of this Blind as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return mode.toString();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Blind obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = mode.compareTo(obj.mode);
    if (result == 0)
     {
      result = endpositionsset.compareTo(obj.endpositionsset);
     }
    return result;
   }

 }
