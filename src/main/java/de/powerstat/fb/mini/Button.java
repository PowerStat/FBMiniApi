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
 * Button.
 */
@ValueObject
public record Button(AIN identifier, long id, String name, UnixTimestamp lastpressed) implements Comparable<Button>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param identifier AIN
   * @param id Id
   * @param name Name
   * @param lastpressed Last pressed unix timestamp
   * @throws IllegalArgumentException When id &lt; 0 or name.length() > 40
   */
  public Button
   {
    if (id < 0)
     {
      throw new IllegalArgumentException("id < 0"); //$NON-NLS-1$
     }
    if ((name != null) && (name.length() > 40))
     {
      throw new IllegalArgumentException("name to long: " + name.length()); //$NON-NLS-1$
     }
   }


  /**
   * Button factory.
   *
   * @param identifier AIN
   * @param id Id
   * @param name Name
   * @param lastpressed Last pressed unix timestamp
   * @return Button object
   * @throws IllegalArgumentException When id &lt; 0 or name.length() > 40
   */
  public static Button of(final AIN identifier, final long id, final String name, final UnixTimestamp lastpressed)
   {
    return new Button(identifier, id, name, lastpressed);
   }


  /**
   * Returns the value of this Button as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return name;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Button obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = identifier.compareTo(obj.identifier);
    if (result == 0)
     {
      result = Long.compare(id, obj.id);
      if (result == 0)
       {
        result = name.compareTo(obj.name);
        if (result == 0)
         {
          result = lastpressed.compareTo(obj.lastpressed);
         }
       }
     }
    return result;
   }

 }
