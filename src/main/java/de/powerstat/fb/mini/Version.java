/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Version major.minor.
 */
public final class Version implements Comparable<Version>, IValueObject
 {
  /**
   * Major version number.
   */
  private final int major;

  /**
   * Minor version number.
   */
  private final int minor;


  /**
   * Constructor.
   *
   * @param major Major version number
   * @param minor Minor version number
   * @throws IllegalArgumentException When major or minor &lt; 0
   */
  private Version(final int major, final int minor)
   {
    super();
    if (major < 0)
     {
      throw new IllegalArgumentException("Major version number must be >= 0");
     }
    if (minor < 0)
     {
      throw new IllegalArgumentException("Minor version number must be >= 0");
     }
    this.major = major;
    this.minor = minor;
   }


  /**
   * Version factory.
   *
   * @param major Major version number
   * @param minor Minor version number
   * @return Version object
   * @throws IllegalArgumentException When major or minor &lt; 0
   */
  public static Version of(final int major, final int minor)
   {
    return new Version(major, minor);
   }


  /**
   * Version factory.
   *
   * @param version Version
   * @return Version object
   * @throws IllegalArgumentException When major or minor &lt; 0
   * @throws IndexOutOfBoundsException When the version is not of the format "major.minor"
   * @throws NumberFormatException When the version number could not be parsed
   */
  public static Version of(final String version)
   {
    final String[] versions = version.split("\\.");
    final int major = Integer.parseInt(versions[0]);
    final int minor = Integer.parseInt(versions[1]);
    return new Version(major, minor);
   }


  /**
   * Returns the value of this Version as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return major + "." + minor;
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
    return Objects.hash(major, minor);
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
    if (!(obj instanceof final Version other))
     {
      return false;
     }
    boolean result = major == other.major;
    if (result)
     {
      result = minor == other.minor;
     }
    return result;
   }


  /**
   * Returns the string representation of this Version.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Version[major=1, minor=0]"
   *
   * @return String representation of this Version
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(23);
    builder.append("Version[major=").append(major).append(", minor=").append(minor).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Version obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Integer.compare(major, obj.major);
    if (result == 0)
     {
      result = Integer.compare(minor, obj.minor);
     }
    return result;
   }

 }
