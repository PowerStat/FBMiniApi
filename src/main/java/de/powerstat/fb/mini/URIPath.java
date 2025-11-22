/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Arrays;
import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * URI path.
 */
public final class URIPath implements Comparable<URIPath>, IValueObject
 {
  /**
   * Path parts withou /.
   */
  private final String[] pathparts;


  /**
   * Constructor.
   *
   * @param path Path "[/pathpart]*"
   */
  private URIPath(final String path)
   {
    // TODO length
    // TODO Regexp
    pathparts = path.split("/"); // TODO null/empty as first entry?
   }


  /**
   * URIPath factory.
   *
   * @param path Path "[/pathpart]*"
   * @return URIPath
   */
  public static URIPath of(final String path)
   {
    return new URIPath(path);
   }


  /**
   * Returns the value of this URIPath as a String.
   *
   * @return The value represented by this object after conversion to type String
   */
  @Override
  public String stringValue()
   {
    final var builder = new StringBuilder();
    for (final String part : pathparts)
     {
      if (!part.isBlank())
       {
        builder.append('/').append(part);
       }
     }
    if (builder.isEmpty())
     {
      builder.append('/');
     }
    return builder.toString();
   }


  /**
   * Get parent URIPath.
   *
   * @return Parent URIPath
   */
  public URIPath getParent()
   {
    final String path = stringValue();
    final int lastIndex = path.lastIndexOf('/');
    return of(path.substring(0, lastIndex));
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
    return Arrays.hashCode(pathparts);
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
    if (!(obj instanceof final URIPath other))
     {
      return false;
     }
    return Arrays.equals(pathparts, other.pathparts);
   }


  /**
   * Returns the string representation of this URIPath.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "URIPath[/path]"
   *
   * @return String representation of this URIPath
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("URIPath[path=").append(stringValue()).append(']');
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
  public int compareTo(final URIPath obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    final int result = Arrays.compare(pathparts, obj.pathparts);
    return result;
   }

 }
