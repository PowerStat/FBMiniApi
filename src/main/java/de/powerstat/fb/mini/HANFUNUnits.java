/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * HAN-Fun Units.
 */
@ValueObject
public enum HANFUNUnits implements IValueObject
 {
  /**
   * Simple button.
   */
  SIMPLE_BUTTON(273),

  /**
   * Simple on/off switchable.
   */
  SIMPLE_ON_OFF_SWITCHABLE(256),

  /**
   * Simple on/off switch.
   */
  SIMPLE_ON_OFF_SWITCH(257),

  /**
   * AC outlet.
   */
  AC_OUTLET(262),

  /**
   * AC outlet simple power metering.
   */
  AC_OUTLET_SIMPLE_POWER_METERING(263),

  /**
   * Simple light.
   */
  SIMPLE_LIGHT(264),

  /**
   * Dimmable light.
   */
  DIMMABLE_LIGHT(265),

  /**
   * Dimmer switch.
   */
  DIMMER_SWITCH(266),

  /**
   * Color bulb.
   */
  COLOR_BULB(277),

  /**
   * Dimmable color bulb.
   */
  DIMMABLE_COLOR_BULB(278),

  /**
   * Blind.
   */
  BLIND(281),

  /**
   * Lamellar.
   */
  LAMELLAR(282),

  /**
   * Simple detector.
   */
  SIMPLE_DETECTOR(512),

  /**
   * Door open/close detector.
   */
  DOOR_OPEN_CLOSE_DETECTOR(513),

  /**
   * Window open/close detector.
   */
  WINDOW_OPEN_CLOSE_DETECTOR(514),

  /**
   * Motion detector.
   */
  MOTION_DETECTOR(515),

  /**
   * Flood detector.
   */
  FLOOD_DETECTOR(518),

  /**
   * Glas break detector.
   */
  GLAS_BREAK_DETECTOR(519),

  /**
   * Vibration detector.
   */
  VIBRATION_DETECTOR(520),

  /**
   * Siren.
   */
  SIREN(640);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  HANFUNUnits(final int action)
   {
    this.action = action;
   }


  /**
   * HANFUNUnits factory.
   *
   * @param value Enum name string
   * @return HANFUNUnits enum
   */
  public static HANFUNUnits of(final String value)
   {
    return HANFUNUnits.valueOf(value);
   }


  /**
   * HANFUNUnits factory.
   *
   * @param action Action number
   * @return HANFUNUnits enum
   */
  public static HANFUNUnits of(final int action)
   {
    for (final HANFUNUnits code : HANFUNUnits.values())
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
   * Returns the value of this HANFUNUnits as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
