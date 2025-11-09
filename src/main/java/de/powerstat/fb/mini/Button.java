/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
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
    this.lastpressedtimestamp = lastpressed;
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
    return this.name;
   }


  /**
   * Get identifier.
   *
   * @return Identifier
   */
  public AIN getIdentifier()
   {
    return this.identifier;
   }


  /**
   * Get id.
   *
   * @return Id
   */
  public long getId()
   {
    return this.id;
   }


  /**
   * Get name.
   *
   * @return Name
   */
  public String getName()
   {
    return this.name;
   }


  /**
   * Get last presses timestamp.
   *
   * @return UnixTimestamp
   */
  public UnixTimestamp getLastPressedTimestamp()
   {
    return this.lastpressedtimestamp;
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
    return Objects.hash(this.identifier, this.id, this.name, this.lastpressedtimestamp);
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
    boolean result = this.identifier.equals(other.identifier);
    if (result)
     {
      result = (this.id == other.id);
      if (result)
       {
        result = this.name.equals(other.name);
        if (result)
         {
          result = this.lastpressedtimestamp.equals(other.lastpressedtimestamp);
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
    final var builder = new StringBuilder();
    builder.append("Button[identifier=").append(this.identifier)
      .append(", id=").append(this.id)
      .append(", name=").append(this.name)
      .append(" ,lastpressedtimestamp=").append(this.lastpressedtimestamp)
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
    int result = this.identifier.compareTo(obj.identifier);
    if (result == 0)
     {
      result = Long.compare(this.id, obj.id);
      if (result == 0)
       {
        result = this.name.compareTo(obj.name);
        if (result == 0)
         {
          result = this.lastpressedtimestamp.compareTo(obj.lastpressedtimestamp);
         }
       }
     }
    return result;
   }

 }
