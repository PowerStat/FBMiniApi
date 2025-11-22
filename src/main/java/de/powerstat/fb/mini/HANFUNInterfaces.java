/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import de.powerstat.validation.interfaces.IValueObject;


/**
 * HAN-FUN interfaces.
 */
public enum HANFUNInterfaces implements IValueObject
 {
  /**
   * Keep alive.
   */
  KEEP_ALIVE(277),

  /**
   * Alert.
   */
  ALERT(256),

  /**
   * On off.
   */
  ON_OFF(512),

  /**
   * Level control.
   */
  LEVEL_CTRL(513),

  /**
   * Color control.
   */
  COLOR_CTRL(514),

  /**
   * Open close.
   */
  OPEN_CLOSE(516),

  /**
   * Open close configuration .
   */
  OPEN_CLOSE_CONFIG(517),

  /**
   * Simple button.
   */
  SIMPLE_BUTTON(772),

  /**
   * SU OTA update.
   */
  SUOTA_UPDATE(1024);

  // TODO 0xf7.. properitary


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  HANFUNInterfaces(final int action)
   {
    this.action = action;
   }


  /**
   * HANFUNInterfaces factory.
   *
   * @param value Enum name string
   * @return HANFUNInterfaces enum
   */
  public static HANFUNInterfaces of(final String value)
   {
    return HANFUNInterfaces.valueOf(value);
   }


  /**
   * HANFUNInterfaces factory.
   *
   * @param action Action number
   * @return HANFUNInterfaces enum
   */
  public static HANFUNInterfaces of(final int action)
   {
    for (final HANFUNInterfaces code : HANFUNInterfaces.values())
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
    return action;
   }


  /**
   * Returns the value of this HANFUNInterfaces as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
