/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Button.
 */
public final class Button implements Comparable<Button>, IValueObject
 {
  /**
   * Identifier.
   */
  private final AIN identifier;

  /**
   * Id.
   */
  private final long id;

  /**
   * Name.
   */
  private final String name;

  /**
   * Last pressed unix timestamp.
   */
  private final UnixTimestamp lastpressedtimestamp;


  /**
   * Constructor.
   *
   * @param identifier AIN
   * @param id Id
   * @param name Name
   * @param lastpressed Last pressed unix timestamp
   * @throws IllegalArgumentException When id &lt; 0 or name.length() > 40
   */
  private Button(final AIN identifier, final long id, final String name, final UnixTimestamp lastpressed)
   {
    super();
    if (id < 0)
     {
      throw new IllegalArgumentException("id < 0"); //$NON-NLS-1$
     }
    if ((name != null) && (name.length() > 40))
     {
      throw new IllegalArgumentException("name to long: " + name.length()); //$NON-NLS-1$
     }
    this.identifier = identifier;
    this.id = id;
    this.name = name;
    lastpressedtimestamp = lastpressed;
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
   * Get identifier.
   *
   * @return Identifier
   */
  public AIN getIdentifier()
   {
    return identifier;
   }


  /**
   * Get id.
   *
   * @return Id
   */
  public long getId()
   {
    return id;
   }


  /**
   * Get name.
   *
   * @return Name
   */
  public String getName()
   {
    return name;
   }


  /**
   * Get last presses timestamp.
   *
   * @return UnixTimestamp
   */
  public UnixTimestamp getLastPressedTimestamp()
   {
    return lastpressedtimestamp;
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
    return Objects.hash(identifier, id, name, lastpressedtimestamp);
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
    if (!(obj instanceof final Button other))
     {
      return false;
     }
    boolean result = identifier.equals(other.identifier);
    if (result)
     {
      result = (id == other.id);
      if (result)
       {
        result = name.equals(other.name);
        if (result)
         {
          result = lastpressedtimestamp.equals(other.lastpressedtimestamp);
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Button.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Button[identifier=000000000000, id=0, name=, lastpressedtimestamp=]"
   *
   * @return String representation of this AIN Button
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(54);
    builder.append("Button[identifier=").append(identifier)
      .append(", id=").append(id)
      .append(", name=").append(name)
      .append(" ,lastpressedtimestamp=").append(lastpressedtimestamp)
      .append(']');
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
          result = lastpressedtimestamp.compareTo(obj.lastpressedtimestamp);
         }
       }
     }
    return result;
   }

 }
