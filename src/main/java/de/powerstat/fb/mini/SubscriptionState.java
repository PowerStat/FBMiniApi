/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Subscription state.
 *
 * @param code Subscription code
 * @param latestain Latest AIN
 */
@ValueObject
public record SubscriptionState(SubscriptionCode code, AIN latestain) implements Comparable<SubscriptionState>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param code Subscription code
   * @param latestain Latest AIN
   * @throws NullPointerException When code is null
   */
  public SubscriptionState
   {
    Objects.requireNonNull(code, "code"); //$NON-NLS-1$
   }


  /**
   * SubscriptionState factory.
   *
   * @param code Subscription code
   * @param latestain Latest ain
   * @return SubscriptionState object
   */
  public static SubscriptionState of(final SubscriptionCode code, final AIN latestain)
   {
    return new SubscriptionState(code, latestain);
   }


  /**
   * Returns the value of this SubscriptionState as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return code.stringValue();
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final SubscriptionState obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = code.compareTo(obj.code);
    if (result == 0)
     {
      result = latestain.compareTo(obj.latestain);
     }
    return result;
   }

 }
