/*
 * Copyright (C) 2020 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;


/**
 * Aktor Identifikationsnummer (AIN).
 */
public final class AIN implements Comparable<AIN>
 {
  /**
   * AIN.
   */
  private final String ain;


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
    if ((ain.length() != 12) && (ain.length() != 13))
     {
      throw new IllegalArgumentException("AIN with wrong length"); //$NON-NLS-1$
     }
    final String intAIN = ain.replaceAll("\\s", ""); //$NON-NLS-1$ //$NON-NLS-2$
    if (!intAIN.matches("^[0-9]{12}$")) //$NON-NLS-1$
     {
      throw new IllegalArgumentException("AIN with wrong format"); //$NON-NLS-1$
     }
    this.ain = intAIN;
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
   */
  public String getAIN()
   {
    return this.ain;
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
    return this.ain.hashCode();
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
    return this.ain.equals(other.ain);
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
    builder.append("AIN[ain=").append(this.ain).append(']'); //$NON-NLS-1$
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
    return this.ain.compareTo(obj.ain);
   }

 }
