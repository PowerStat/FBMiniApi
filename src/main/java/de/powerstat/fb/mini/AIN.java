/*
 * Copyright (C) 2020-2023 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;
import java.util.regex.Pattern;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Actor Identificationsnumber (AIN).
 *
 * Identification of the actors or template, or MAC address of network device.
 */
public final class AIN implements Comparable<AIN>, IValueObject
 {
  /**
   * AIN regexp.
   *
   * HANFUN: Gerät: 13077 0000258; Unit : 13077 0000258-1
   * Zigbee: Gerät: Z0017880108AFB58C; Unit: Z0017880108AFB58C0B
   * Template: tmp653A18-38AE7FDE9, tmpD0EF2E-3CE52B246, tmpD0EF2E-4711
   */
  private static final Pattern AIN_REGEXP = Pattern.compile("^\\d{12}(-\\d)?|Z[0-9A-F]{16,18}|tmp[0-9A-F]{6}-[0-9A-F]{4,9}$"); //$NON-NLS-1$

  /**
   * Space regexp.
   */
  private static final Pattern SPACE_REGEXP = Pattern.compile("\\s", Pattern.UNICODE_CHARACTER_CLASS); //$NON-NLS-1$

  /**
   * Actor identificationsnumber.
   */
  private final String aiNr;


  /**
   * Constructor.
   *
   * @param ain AIN or MAC
   * @throws NullPointerException if ain is null
   * @throws IllegalArgumentException if ain is not an correct ain
   *
   * TODO MAC
   */
  private AIN(final String ain)
   {
    super();
    Objects.requireNonNull(ain, "ain"); //$NON-NLS-1$
    if ((ain.length() < 12) || (ain.length() > 19))
     {
      throw new IllegalArgumentException("AIN with wrong length: " + ain.length()); //$NON-NLS-1$
     }
    final var intAIN = AIN.SPACE_REGEXP.matcher(ain).replaceAll(""); //$NON-NLS-1$
    if ((intAIN.length() != 12) && (intAIN.length() != 14) && (intAIN.length() != 17) && (intAIN.length() != 19) && (!intAIN.startsWith("tmp")))
     {
      throw new IllegalArgumentException("AIN with wrong length: " + ain.length()); //$NON-NLS-1$
     }
    if (!AIN.AIN_REGEXP.matcher(intAIN).matches())
     {
      throw new IllegalArgumentException("AIN with wrong format"); //$NON-NLS-1$
     }
    aiNr = intAIN;
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
   * Returns the value of this AIN as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return aiNr;
   }


  /**
   * Is template or device/unit.
   *
   * @return true: template; false: device/unit
   */
  public boolean isTemplate()
   {
    return (aiNr.startsWith("tmp"));
   }


  /**
   * Is Zigbee or HANFUN device.
   *
   * @return true: Zigbee; false: HANFUN|template
   */
  public boolean isZigbee()
   {
    return (aiNr.charAt(0) == 'Z');
   }


  /**
   * Is unit or device.
   *
   * @return true: unit; false: device/template
   */
  public boolean isUnit()
   {
    return (aiNr.length() == (isZigbee() ? 19 : 14));
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
    return aiNr.hashCode();
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
    if (!(obj instanceof final AIN other))
     {
      return false;
     }
    return aiNr.equals(other.aiNr);
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
    final var builder = new StringBuilder();
    builder.append("AIN[ain=").append(aiNr).append(']'); //$NON-NLS-1$
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
    return aiNr.compareTo(obj.aiNr);
   }

 }
