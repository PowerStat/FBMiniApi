/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Scenario type.
 */
@ValueObject
public enum ScenarioType implements IValueObject
 {
  /**
   * Undefined.
   */
  UNDEFINED(0),

  /**
   * Coming.
   */
  COMING(1),

  /**
   * Leaving.
   */
  LEAVING(2),

  /**
   * Generic.
   */
  GENERIC(3);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  ScenarioType(final int action)
   {
    this.action = action;
   }


  /**
   * ScenarioType factory.
   *
   * @param value Enum name string
   * @return ScenarioType enum
   */
  public static ScenarioType of(final String value)
   {
    return ScenarioType.valueOf(value);
   }


  /**
   * ScenarioType factory.
   *
   * @param action Action number
   * @return ScenarioCode enum
   */
  public static ScenarioType of(final int action)
   {
    for (final ScenarioType code : ScenarioType.values())
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
   * Returns the value of this ScenarioType as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
