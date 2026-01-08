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
 * Version major.minor.
 *
 * @param major Major version number
 * @param minor Minor version number
 */
@ValueObject
public record Version(int major, int minor) implements Comparable<Version>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param major Major version number
   * @param minor Minor version number
   * @throws IllegalArgumentException When major or minor &lt; 0
   */
  public Version
   {
    if (major < 0)
     {
      throw new IllegalArgumentException("Major version number must be >= 0");
     }
    if (minor < 0)
     {
      throw new IllegalArgumentException("Minor version number must be >= 0");
     }
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
