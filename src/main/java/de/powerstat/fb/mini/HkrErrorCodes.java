/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import de.powerstat.validation.interfaces.IValueObject;


/**
 * HKR error codes.
 */
public enum HkrErrorCodes implements IValueObject
 {
  /**
   * No error.
   */
  NO_ERROR(0),

  /**
   * No adaptation possible. Device correctly installed on the radiator?
   */
  NO_ADAPTION(1),

  /**
   * Valve lift too short or battery power too weak. Open and close the valve tappet several times by hand or insert new batteries.
   */
  VALVE_LIFT(2),

  /**
   * No valve movement possible. Valve tappet free?
   */
  NO_VALVE_MOVEMENT(3),

  /**
   * The installation is currently being prepared.
   */
  PREPARING(4),

  /**
   * The radiator controller is in installation mode and can be mounted on the heating valve.
   */
  INSTALL_MODE(5),

  /**
   * The radiator controller now adapts to the stroke of the heating valve.
   */
  ADAPTING(6);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  HkrErrorCodes(final int action)
   {
    this.action = action;
   }


  /**
   * HkrErrorCodes factory.
   *
   * @param value Enum name string
   * @return HkrErrorCodes enum
   */
  public static HkrErrorCodes of(final String value)
   {
    return HkrErrorCodes.valueOf(value);
   }


  /**
   * HkrErrorCodes factory.
   *
   * @param action Action number
   * @return HkrErrorCodes enum
   */
  public static HkrErrorCodes of(final int action)
   {
    for (final HkrErrorCodes code : HkrErrorCodes.values())
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
   * Returns the value of this HkrErrorCodes as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
