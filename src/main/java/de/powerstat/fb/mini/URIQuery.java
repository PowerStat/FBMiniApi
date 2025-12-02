/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;


/**
 * URI query.
 *
 * @param <T> Parameter type
 */
@ValueObject
public final class URIQuery<T extends URIQueryParameter> implements Iterable<T>
 {
  /**
   * History.
   */
  private final Set<T> queries = new TreeSet<>();


  /**
   * Constructor.
   */
  public URIQuery()
   {
    super();
   }


  /**
   * Returns the string value of this URIQuery.
   *
   * "?key1=value1&amp;key2=value2..."
   *
   * @return String representation of this URIQuery and its parameters
   */
  public String stringValue()
   {
    final var builder = new StringBuilder();
    final int initLength = builder.length();
    for (final T entry : queries)
     {
      builder.append(entry.stringValue()).append('&');
     }
    if (builder.length() > initLength)
     {
      builder.setLength(builder.length() - 1);
      builder.insert(0, '?');
     }
    return builder.toString();
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
    if (queries.isEmpty())
     {
      return 0;
     }
    return Objects.hash(queries);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @throws NoSuchElementException If there is no entry in this HistoryOf
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof URIQuery<?>))
     {
      return false;
     }
    final URIQuery<T> other = (URIQuery<T>)obj;
    if (queries.isEmpty() || other.queries.isEmpty())
     {
      return (queries.isEmpty() && other.queries.isEmpty());
     }
    return queries.equals(other.queries);
   }


  /**
   * Returns the string representation of this URIQuery.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "URIQuery&lt;&gt;[?key1=value1&amp;key2=value2...]"
   *
   * @return String representation of this URIQuery
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("URIQuery<>["); //$NON-NLS-1$
    final int initLength = builder.length();
    for (final T entry : queries)
     {
      builder.append(entry.stringValue()).append('&');
     }
    if (builder.length() > initLength)
     {
      builder.setLength(builder.length() - 1);
     }
    builder.append(']');
    return builder.toString();
   }


  /**
   * Is empty.
   *
   * @return true: empty; false otherwise
   */
  public boolean isEmpty()
   {
    return queries.isEmpty();
   }


  /**
   * Add entry.
   *
   * @param entry URIQuerParameter object
   * @throws NullPointerException If entry is null
   */
  public void addEntry(final T entry)
   {
    Objects.requireNonNull(entry, "entry"); //$NON-NLS-1$
    queries.add(entry);
   }


  /**
   * Returns an iterator over the URIQueryParamater in this URIQuery. The parameters are returned in no particular order.
   *
   * @return Iterator
   */
  @Override
  public Iterator<T> iterator()
   {
    return queries.iterator();
   }

 }
