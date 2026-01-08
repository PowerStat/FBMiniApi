/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Locale;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Metadata.
 *
 * @param icon Icon id > 0 or -1 if undefined
 * @param type ScenarioType
 */
@ValueObject
public record Metadata(int icon, ScenarioType type) implements Comparable<Metadata>, IValueObject
 {
  /**
   * Constructor.
   *
   * Only one of the following parameters must be defined.
   *
   * @param icon Icon id > 0 or -1 if undefined
   * @param type ScenarioType
   * @throws NullPointerException if type is null
   * @throws IndexOutOfBoundsException If icon ist &lt; -1
   */
  public Metadata
   {
    Objects.requireNonNull(type, "type"); //$NON-NLS-1$
    if (icon < -1)
     {
      throw new IndexOutOfBoundsException("icon < -1");
     }
    if ((icon == -1) && (ScenarioType.UNDEFINED == type))
     {
      throw new IllegalArgumentException("One of icon or type must be set");
     }
    if ((icon >= 0) && (ScenarioType.UNDEFINED != type))
     {
      throw new IllegalArgumentException("Only one of icon or type must be set");
     }
   }


  /**
   * Metadata factory.
   *
   *
   * Only one of the following parameters must be defined.
   *
   * @param icon Icon id > 0 or -1 if undefined
   * @param type ScenarioType
   * @return Metadata object
   * @throws NullPointerException if type is null
   * @throws IndexOutOfBoundsException If icon ist &lt; -1
   */
  public static Metadata of(final int icon, final ScenarioType type)
   {
    return new Metadata(icon, type);
   }


  /**
   * Returns the value of this Metadata as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return (icon >= 0) ? String.valueOf(icon) : type.stringValue();
   }


  /**
   * Return the value of this Metadata as a json string.
   *
   * @return THe value represented by this object after conversion to type json string.
   */
  public String jsonValue()
   {
    StringBuilder result = new StringBuilder();
    result.append('{');
    if (icon >= 0)
     {
      result.append("\"icon\": ").append(icon);
     }
    else
     {
      result.append("\"type\": \"").append(type.name().toLowerCase(Locale.getDefault())).append('"');
     }
    result.append('}');
    return result.toString();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Metadata obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Integer.compare(icon, obj.icon);
    if ((result == 0) && (icon == -1))
     {
      result = type.compareTo(obj.type);
     }
    else if ((icon == -1) || (obj.icon == -1))
     {
      result = (icon == -1) ? 1 : -1;
     }
    return result;
   }

 }
