/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;


/**
 * URI query.
 */
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
    for (final T entry : this.queries)
     {
      builder.append(entry.stringValue());
      builder.append('&');
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
    if (this.queries.isEmpty())
     {
      return 0;
     }
    return Objects.hash(this.queries);
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
  public boolean equals(final Object obj)
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
    if ((this.queries.isEmpty()) || (other.queries.isEmpty()))
     {
      return ((this.queries.isEmpty()) && (other.queries.isEmpty()));
     }
    return this.queries.equals(other.queries);
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
    for (final T entry : this.queries)
     {
      builder.append(entry.stringValue());
      builder.append("&"); //$NON-NLS-1$
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
    return this.queries.isEmpty();
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
    this.queries.add(entry);
   }


  /**
   * Returns an iterator over the URIQueryParamater in this URIQuery. The parameters are returned in no particular order.
   *
   * @return Iterator
   */
  @Override
  public Iterator<T> iterator()
   {
    return this.queries.iterator();
   }

 }
