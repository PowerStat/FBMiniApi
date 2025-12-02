/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Functions.
 */
@ValueObject
public enum Functions implements IValueObject
 {
  /**
   * HAN-FUN device.
   */
  HANFUN_DEVICE(0),

  /**
   * Reserved.
   */
  RESERVED1(1),

  /**
   * Light, bulb.
   */
  BULB(2),

  /**
   * Reserved.
   */
  RESERVED2(3),

  /**
   * Alarm sensor.
   */
  ALARM_SENSOR(4),

  /**
   * AVM button.
   */
  AVM_BUTTON(5),

  /**
   * AVM radiator controller.
   */
  AVM_RADIATOR_CONTROLLER(6),

  /**
   * AVM energy gauge.
   */
  AVM_ENERGY_GAUGE(7),

  /**
   * Temperature sensor.
   */
  TEMPERATURE_SENSOR(8),

  /**
   * AVM switch socket.
   */
  AVM_SWITCHSOCKET(9),

  /**
   * AVM dect repeater.
   */
  AVM_DECT_REPEATER(10),

  /**
   * AVM microfone.
   */
  AVM_MICROFONE(11),

  /**
   * Reserved.
   */
  RESERVED3(12),

  /**
   * HAN-FUN unit.
   */
  HANFUN_UNIT(13),

  /**
   * Reserved.
   */
  RESERVED4(14),

  /**
   * Device that could be switched on/off.
   */
  DEVICE_ONOFF(15),

  /**
   * Device with dimm, high or niveau level.
   */
  DEVICE_WITH_LEVEL(16),

  /**
   * Bulb with color.
   */
  BULB_WITH_COLOR(17),

  /**
   * Blind.
   */
  BLIND(18),

  /**
   * Reserved.
   */
  RESERVED5(19),

  /**
   * Other error.
   */
  HUMIDITY_SENSOR(20);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  Functions(final int action)
   {
    this.action = action;
   }


  /**
   * Functions factory.
   *
   * @param value Enum name string
   * @return Functions enum
   */
  public static Functions of(final String value)
   {
    return Functions.valueOf(value);
   }


  /**
   * Functions factory.
   *
   * @param action Action number
   * @return Functions enum
   */
  public static Functions of(final int action)
   {
    for (final Functions code : Functions.values())
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
   * Returns the value of this Functions as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
