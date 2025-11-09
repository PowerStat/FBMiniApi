/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import de.powerstat.validation.interfaces.IValueObject;


/**
 * Color modes.
 */
public enum ColorModes implements IValueObject
 {
  /**
   * Hue/saturation mode.
   */
  HUE_SATURATION(1),

  /**
   * Color temperature mode.
   */
  COLOR_TEMPERATURE(4);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  ColorModes(final int action)
   {
    this.action = action;
   }


  /**
   * ColorModes factory.
   *
   * @param value Enum name string
   * @return ColorModes enum
   */
  public static ColorModes of(final String value)
   {
    return ColorModes.valueOf(value);
   }


  /**
   * ColorModes factory.
   *
   * @param action Action number
   * @return ColorModes enum
   */
  public static ColorModes of(final int action)
   {
    for (final ColorModes code : ColorModes.values())
     {
      if (code.action == action)
       {
        return code;
       }
     }
    throw new IllegalArgumentException("action out of range");
   }


  /**
   * Get action number.
   *
   * @return Action number
   */
  public int getAction()
   {
    return this.action;
   }


  /**
   * Returns the value of this ColorModes as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
