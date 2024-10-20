/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import de.powerstat.validation.interfaces.IValueObject;


/**
 * Scenario type.
 */
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
    return this.action;
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
