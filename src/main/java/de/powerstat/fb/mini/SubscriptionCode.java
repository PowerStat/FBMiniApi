/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Subscription code.
 */
@ValueObject
public enum SubscriptionCode implements IValueObject
 {
  /**
   * No registration in progress.
   */
  NO_PROGRESS(0),

  /**
   * Registration in progress.
   */
  IN_PROGRESS(1),

  /**
   * Timeout.
   */
  TIMEOUT(2),

  /**
   * Other error.
   */
  OTHER_ERROR(3);


  /**
   * Action number.
   */
  private final int action;


  /**
   * Ordinal constructor.
   *
   * @param action Action number
   */
  SubscriptionCode(final int action)
   {
    this.action = action;
   }


  /**
   * SubscriptionCode factory.
   *
   * @param value Enum name string
   * @return SubscriptionCode enum
   */
  public static SubscriptionCode of(final String value)
   {
    return SubscriptionCode.valueOf(value);
   }


  /**
   * SubscriptionCode factory.
   *
   * @param action Action number
   * @return SubscriptionCode enum
   */
  public static SubscriptionCode of(final int action)
   {
    for (final SubscriptionCode code : SubscriptionCode.values())
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
   * Returns the value of this SubscriptionCode as a string.
   *
   * @return The text value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return this.name();
   }

 }
