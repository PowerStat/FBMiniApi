/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;
import java.util.regex.Pattern;


/**
 * Aktor Identifikationsnummer (AIN).
 */
public final class AIN implements Comparable<AIN>
 {
  /**
   * AIN regexp.
   */
  private static final Pattern AIN_REGEXP = Pattern.compile("^[0-9]{12}(-[0-9])?$"); //$NON-NLS-1$

  /**
   * Space regexp.
   */
  private static final Pattern SPACE_REGEXP = Pattern.compile("\\s"); //$NON-NLS-1$

  /**
   * Aktor Identifikationsnummer.
   */
  private final String aiNr;


  /**
   * Constructor.
   *
   * @param ain AIN or MAC
   * @throws NullPointerException if ain is null
   * @throws IllegalArgumentException if ain is not a correct ain
   *
   * TODO MAC
   */
  public AIN(final String ain)
   {
    super();
    Objects.requireNonNull(ain, "ain"); //$NON-NLS-1$
    if ((ain.length() < 12) || (ain.length() > 15))
     {
      throw new IllegalArgumentException("AIN with wrong length"); //$NON-NLS-1$
     }
    final String intAIN = AIN.SPACE_REGEXP.matcher(ain).replaceAll(""); //$NON-NLS-1$
    if (!AIN.AIN_REGEXP.matcher(intAIN).matches())
     {
      throw new IllegalArgumentException("AIN with wrong format"); //$NON-NLS-1$
     }
    this.aiNr = intAIN;
   }


  /**
   * AIN factory.
   *
   * @param ain AIN
   * @return AIN object
   */
  public static AIN of(final String ain)
   {
    return new AIN(ain);
   }


  /**
   * Get ain string.
   *
   * @return AIN string
   * @deprecated Use stringValue() instead
   */
  @Deprecated
  public String getAIN()
   {
    return this.aiNr;
   }


  /**
   * Returns the value of this AIN as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  public String stringValue()
   {
    return this.aiNr;
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
    return this.aiNr.hashCode();
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
    if (!(obj instanceof AIN))
     {
      return false;
     }
    final AIN other = (AIN)obj;
    return this.aiNr.equals(other.aiNr);
   }


  /**
   * Returns the string representation of this AIN.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "AIN[ain=000000000000]"
   *
   * @return String representation of this AIN
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final StringBuilder builder = new StringBuilder();
    builder.append("AIN[ain=").append(this.aiNr).append(']'); //$NON-NLS-1$
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
  public int compareTo(final AIN obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return this.aiNr.compareTo(obj.aiNr);
   }

 }
