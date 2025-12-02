/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * URI query parameter.
 */
@ValueObject
public final class URIQueryParameter implements Comparable<URIQueryParameter>, IValueObject
 {
  /**
   * Parameter key.
   */
  private final String key;

  /**
   * Paramater value.
   */
  private final String value;


  /**
   * Constructor.
   *
   * @param key Parameter key name
   * @param value Parameter value could be null
   * @throws NullPointerException When key is null
   * @throws IllegalArgumentException When key is empty/blank
   */
  private URIQueryParameter(final String key, final String value)
   {
    super();
    Objects.requireNonNull(key, "key"); //$NON-NLS-1$
    if (key.isBlank())
     {
      throw new IllegalArgumentException("key is empty"); //$NON-NLS-1$
     }
    // TODO length check, regexp ?
    // TODO uri encoding
    this.key = key;
    this.value = value;
   }


  /**
   * URIQueryParamater factory.
   *
   * @param key Parameter key name
   * @param value Parameter value
   * @return URIQueryParamater object
   */
  public static URIQueryParameter of(final String key, final String value)
   {
    return new URIQueryParameter(key, value);
   }


  /**
   * Level factory.
   *
   * @param uriQueryParameter "key=value"
   * @return URIQueryParamater object
   * @throws IllegalArgumentException When not in "key=value" format
   */
  public static URIQueryParameter of(final String uriQueryParameter)
   {
    final String[] keyValue = uriQueryParameter.split("=");
    if (keyValue.length != 2)
     {
      throw new IllegalArgumentException("Query parameter with wrong format (must be key=value)"); //$NON-NLS-1$
     }
    return new URIQueryParameter(keyValue[0], keyValue[1]);
   }


  /**
   * Returns the value of this URIQueryParameter as a String.
   *
   * @return The value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    return key + ((value == null) ? "" : ("=" + value));
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
    return Objects.hash(key, value);
   }


  /**
   * Equal fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return true: equal; false: not equal
   */
  private static <T> boolean equalField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final URIQueryParameter other))
     {
      return false;
     }
    boolean result = key.equals(other.key);
    if (result)
     {
      result = equalField(value, other.value);
     }
    return result;
   }


  /**
   * Returns the string representation of this URIQueryParameter.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "URIQueryParameter[key=abc, value=123]"
   *
   * @return String representation of this URIQueryParameter
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    // TODO uri decoding ?
    final var builder = new StringBuilder(31);
    builder.append("URIQueryParameter[key=").append(key);
    if (value != null)
     {
      builder.append(", value=").append(value);
     }
    builder.append(']');
    return builder.toString();
   }


  /**
   * Compare fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return 0: equal; 1 field 1 greater than field 2; -1 field 1 smaller than field 2
   */
  private static <T extends Comparable<T>> int compareField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? ((obj2 == null) ? 0 : -1) : ((obj2 == null) ? 1 : obj1.compareTo(obj2));
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final URIQueryParameter obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = key.compareTo(obj.key);
    if (result == 0)
     {
      result = compareField(value, obj.value);
     }
    return result;
   }

 }
