/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import de.powerstat.validation.interfaces.IValueObject;


/**
 * Apply mask.
 */
public enum ApplyMask implements IValueObject
 {
  /**
   * Radiator switch off in summer.
   */
  HKR_SUMMER(0),

  /**
   * Radiator target temperature.
   */
  HKR_TEMPERATURE(1),

  /**
   * Radiator vacation setup.
   */
  HKR_HOLIDAYS(2),

  /**
   * Radiator time table.
   */
  HKR_TIME_TABLE(3),

  /**
   * Switchable plug, bulb, actor on/off.
   */
  RELAY_MANUAL(4),

  /**
   * Switchable plug, bulb, actor automatic by time table.
   */
  RELAY_AUTOMATIC(5),

  /**
   * Level, brightness of bulb or blind.
   */
  LEVEL(6),

  /**
   * Color, color temperature.
   */
  COLOR(7),

  /**
   * Dial announcement.
   */
  DIALHELPER(8),

  /**
   * Sunrise/sunset simulation.
   */
  SUN_SIMULATION(9),

  /**
   * Grouped templates, szenarios.
   */
  SUB_TEMPLATES(10),

  /**
   * WLAN on/off.
   */
  MAIN_WIFI(11),

  /**
   * Guest WLAN on/off.
   */
  GUEST_WIFI(12),

  /**
   * Answering machine on/off.
   */
  TAM_CONTROL(13),

  /**
   * send user defined http request.
   */
  HTTP_REQUEST(14),

  /**
   * Activate radiator boost/window override.
   */
  TIMER_CONTROL(15),

  /**
   * Copy device state.
   */
  SWITCH_MASTER(16),

  /**
   * Send pushmail/app-notification.
   */
  CUSTOM_NOTIFICATION(17),

  /**
   * Triggers.
   */
  TRIGGERS(18);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  ApplyMask(final int action)
   {
    this.action = action;
   }


  /**
   * ApplyMask factory.
   *
   * @param value Enum name string
   * @return ApplyMask enum
   */
  public static ApplyMask of(final String value)
   {
    return ApplyMask.valueOf(value);
   }


  /**
   * ApplyMask factory.
   *
   * @param action Action number
   * @return ApplyMask enum
   */
  public static ApplyMask of(final int action)
   {
    for (final ApplyMask code : ApplyMask.values())
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
   * Returns the value of this ApplyMask as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
