/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.EnumSet;
import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Color control.
 */
public final class ColorControl implements Comparable<ColorControl>, IValueObject
 {
  /**
   * Supported modes.
   */
  private final EnumSet<ColorModes> supportedModes;

  /**
   * Current mode.
   */
  private final ColorModes currentMode;

  /**
   * Full color support: true: setunmappedcolor supported, false otherwise.
   */
  private final boolean fullcolorsupport;

  /**
   * true: Colordefaults value set, false otherwise.
   */
  private final boolean mapped;

  /**
   * Hue value, only when currentMode = 1.
   */
  private final Hue hue;

  /**
   * Saturation value, only when currentMode = 1.
   */
  private final Saturation saturation;

  /**
   * Only when mapped is true: corrected value by Colordefaults.
   */
  private final Hue unmappedHue;

  /**
   * Only when mapped is true: corrected value by Colordefaults.
   */
  private final Saturation unmappedSaturation;

  /**
   * Color temperature.
   */
  private final TemperatureKelvin temperature;


  /**
   * Constructor.
   *
   * @param supportedModes Supported color modes
   * @param currentMode Current color mode, or null when unknown
   * @param fullcolorsupport Full color support: true: setunmappedcolor supported, false otherwise
   * @param mapped true: Colordefaults value set, false otherwise
   * @param hue Hue value, only when currentMode = 1, otherwise null
   * @param saturation Saturation value, only when currentMode = 1, otherwise null
   * @param unmappedHue Only when mapped is true: corrected value by Colordefaults, othweise null
   * @param unmappedSaturation Only when mapped is true: corrected value by Colordefaults, othweise null
   * @param temperature Color temperature
   * @throws NullPointerException When supportedModes is null or currentMode is HUE_SATURATION and hue, saturation, unmappedHue, unmappedSaturation is null, or currentMode is COLOR_TEMPERATURE and temperature is null
   * @throws IllegalArumentException When conditions of currentMode, mapped are not met, or if supportesModes is empty
   */
  private ColorControl(final EnumSet<ColorModes> supportedModes, final ColorModes currentMode, final boolean fullcolorsupport, final boolean mapped, final Hue hue, final Saturation saturation, final Hue unmappedHue, final Saturation unmappedSaturation, final TemperatureKelvin temperature)
   {
    super();
    Objects.requireNonNull(supportedModes, "supportedModes"); //$NON-NLS-1$
    if (supportedModes.equals(EnumSet.noneOf(ColorModes.class)))
     {
      throw new IllegalArgumentException("Supported modes must be set");
     }
    this.supportedModes = supportedModes;
    this.currentMode = currentMode;
    if (currentMode == ColorModes.HUE_SATURATION)
     {
      Objects.requireNonNull(hue, "hue"); //$NON-NLS-1$
      Objects.requireNonNull(saturation, "saturation"); //$NON-NLS-1$
      Objects.requireNonNull(unmappedHue, "unmappedHue"); //$NON-NLS-1$
      Objects.requireNonNull(unmappedSaturation, "unmappedSaturation"); //$NON-NLS-1$
      if (temperature != null)
       {
        throw new IllegalArgumentException("currentMode = HUE_SATURATION, but temperature has been set");
       }
      if (!mapped)
       {
        if (!hue.equals(unmappedHue) || !saturation.equals(unmappedSaturation))
         {
          throw new IllegalArgumentException("hue and unmapedHue, or saturation and unmappedSaturation are not equal");
         }
       }
     }
    else // if ((currentMode == null) || (currentMode == ColorModes.COLOR_TEMPERATURE))
     {
      if (currentMode == ColorModes.COLOR_TEMPERATURE)
       {
        Objects.requireNonNull(temperature, "temperature"); //$NON-NLS-1$
       }
      /*
      else // if (currentMode == null)
       {
       }
      */
      if ((hue != null) || (saturation != null) || (unmappedHue != null) || (unmappedSaturation != null))
       {
        throw new IllegalArgumentException("currentMode is COLOR_TEMPERATURE or unknwon, but hue, saturation, unmappedHue or unmappedSaturation has been set");
       }
     }
    this.fullcolorsupport = fullcolorsupport;
    this.mapped = mapped;
    this.hue = hue;
    this.saturation = saturation;
    this.unmappedHue = unmappedHue;
    this.unmappedSaturation = unmappedSaturation;
    this.temperature = temperature;
   }


  /**
   * ColorControl factory.
   *
   * @param supportedModes Supported color modes
   * @param currentMode Current color mode, or null when unknown
   * @param fullcolorsupport Full color support: true: setunmappedcolor supported, false otherwise
   * @param mapped true: Colordefaults value set, false otherwise
   * @param hue Hue value, only when currentMode = 1, otherwise null
   * @param saturation Saturation value, only when currentMode = 1, otherwise null
   * @param unmappedHue Only when mapped is true: corrected value by Colordefaults, othweise null
   * @param unmappedSaturation Only when mapped is true: corrected value by Colordefaults, othweise null
   * @param temperature Color temperature
   * @return ColorControl object
   * @throws NullPointerException When supportedModes is null or currentMode is HUE_SATURATION and hue, saturation, unmappedHue, unmappedSaturation is null, or currentMode is COLOR_TEMPERATURE and temperature is null
   * @throws IllegalArgumentException When conditions of currentMode, mapped are not met, or if supportesModes is empty
   */
  public static ColorControl of(final EnumSet<ColorModes> supportedModes, final ColorModes currentMode, final boolean fullcolorsupport, final boolean mapped, final Hue hue, final Saturation saturation, final Hue unmappedHue, final Saturation unmappedSaturation, final TemperatureKelvin temperature)
   {
    return new ColorControl(supportedModes, currentMode, fullcolorsupport, mapped, hue, saturation, unmappedHue, unmappedSaturation, temperature);
   }


  /**
   * Returns the value of this ColorControl as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return "";
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
    return Objects.hash(supportedModes, currentMode, fullcolorsupport, mapped, hue, saturation, unmappedHue, unmappedSaturation, temperature);
   }


  /**
   * Equal fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return true: equal; false: not equal
   */
  private static <T> boolean equalField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
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
    if (!(obj instanceof final ColorControl other))
     {
      return false;
     }
    boolean result = equalField(supportedModes, other.supportedModes);
    if (result)
     {
      result = currentMode == other.currentMode;
      if (result)
       {
        result = fullcolorsupport == other.fullcolorsupport;
        if (result)
         {
          result = mapped == other.mapped;
          if (result)
           {
            result = equalField(hue, other.hue);
            if (result)
             {
              result = equalField(saturation, other.saturation);
              if (result)
               {
                result = equalField(unmappedHue, other.unmappedHue);
                if (result)
                 {
                  result = equalField(unmappedSaturation, other.unmappedSaturation);
                  if (result)
                   {
                    result = equalField(temperature, other.temperature);
                   }
                 }
               }
             }
           }
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this ColorControl.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "ColorControl[supportedModes=, currentMode=, fullcolorsupport= false, mapped=false, hue=, saturation=, unmappedHue=, unmappedSaturation=, temperature=]"
   *
   * @return String representation of this ColorControl
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("ColorControl[supportedModes=").append(supportedModes)
      .append(", currentMode=").append(currentMode)
      .append(", fullcolorsupport=").append(fullcolorsupport)
      .append(", mapped=").append(mapped)
      .append(", hue=").append(hue)
      .append(", saturation=").append(saturation)
      .append(", unmappedHue=").append(unmappedHue)
      .append(", unmappedSaturation=").append(unmappedSaturation)
      .append(", temperature=").append(temperature)
      .append(']');
    return builder.toString();
   }


  /**
   * Compare fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return 0: equal; 1 field 1 greater than field 2; -1 field 1 smaller than field 2
   */
  private static <T extends Comparable<T>> int compareField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? ((obj2 == null) ? 0 : -1) : ((obj2 == null) ? 1 : obj1.compareTo(obj2));
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final ColorControl obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = 0; // this.supportedModes.compareTo(obj.supportedModes);
    if (result == 0)
     {
      result = compareField(currentMode, obj.currentMode);
      if (result == 0)
       {
        result = Boolean.compare(fullcolorsupport, obj.fullcolorsupport);
        if (result == 0)
         {
          result = Boolean.compare(mapped, obj.mapped);
          if (result == 0)
           {
            result = compareField(hue, obj.hue);
            if (result == 0)
             {
              result = compareField(saturation, obj.saturation);
              if (result == 0)
               {
                result = compareField(unmappedHue, obj.unmappedHue);
                if (result == 0)
                 {
                  result = compareField(unmappedSaturation, obj.unmappedSaturation);
                  if (result == 0)
                   {
                    result = compareField(temperature, obj.temperature);
                   }
                 }
               }
             }
           }
         }
       }
     }
    return result;
   }

 }
