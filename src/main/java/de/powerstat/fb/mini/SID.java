/*
 * Copyright (C) 2021-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;
import java.util.regex.Pattern;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Session identifier.
 */
public final class SID implements Comparable<SID>, IValueObject
 {
  /**
   * SID regexp.
   */
  private static final Pattern SID_REGEXP = Pattern.compile("^[0-9a-f]{16}$"); //$NON-NLS-1$

  /**
   * Invalid session identifier.
   */
  private static final SID INVALID = new SID("0000000000000000"); //$NON-NLS-1$


  /**
   * Session identifier.
   */
  private final String sessionId;


  /**
   * Constructor.
   *
   * @param sid Session identifier
   */
  private SID(final String sid)
   {
    super();
    Objects.requireNonNull(sid, "sessionid"); //$NON-NLS-1$
    if (sid.length() != 16)
     {
      throw new IllegalArgumentException("sessionid with wrong length"); //$NON-NLS-1$
     }
    if (!SID.SID_REGEXP.matcher(sid).matches())
     {
      throw new IllegalArgumentException("sessionid with wrong format"); //$NON-NLS-1$
     }
    this.sessionId = sid;
   }


  /**
   * SID factory.
   *
   * @param sid Session identifier
   * @return SID object
   */
  public static SID of(final String sid)
   {
    return new SID(sid);
   }


  /**
   * Invalid SID factory.
   *
   * @return Invalid SID object
   */
  public static SID ofInvalid()
   {
    return SID.INVALID;
   }


  /**
   * Returns the value of this SID as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.sessionId;
   }


  /**
   * Is valid session identifier.
   *
   * @return true: Session is valid, false otherwise
   */
  public boolean isValidSession()
   {
    return !"0000000000000000".equals(this.sessionId); //$NON-NLS-1$
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
    return this.sessionId.hashCode();
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
    if (!(obj instanceof SID))
     {
      return false;
     }
    final SID other = (SID)obj;
    return this.sessionId.equals(other.sessionId);
   }


  /**
   * Returns the string representation of this SID.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "SID[sid=0000000000000000]"
   *
   * @return String representation of this SID
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("SID[sid=").append(this.sessionId).append(']'); //$NON-NLS-1$
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
  public int compareTo(final SID obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return this.sessionId.compareTo(obj.sessionId);
   }

 }
