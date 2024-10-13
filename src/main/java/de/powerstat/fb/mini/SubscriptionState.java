/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Subscription state.
 */
public final class SubscriptionState implements Comparable<SubscriptionState>, IValueObject
 {
  /**
   * Subscription code.
   */
  private final SubscriptionCode code;

  /**
   * Latest ain.
   */
  private final AIN latestain;


  /**
   * Constructor.
   *
   * @param code Subscription code
   * @param latestain Latest AIN
   * @throws NullPointerException When code is null
   */
  private SubscriptionState(final SubscriptionCode code, final AIN latestain)
   {
    super();
    Objects.requireNonNull(code, "code"); //$NON-NLS-1$
    this.code = code;
    this.latestain = latestain;
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
    return this.code.stringValue();
   }


  /**
   * Returns the SubScriptionCode of this SubscriptionState.
   *
   * @return The SubScriptionCode represented by this object.
   */
  public SubscriptionCode subscriptionCodeValue()
   {
    return this.code;
   }


  /**
   * Returns the AIN of this SubscriptionState.
   *
   * @return The AIN represented by this object.
   */
  public AIN ainValue()
   {
    return this.latestain;
   }


  /**
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(this.code, this.latestain);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final SubscriptionState other))
     {
      return false;
     }
    boolean result = this.code.equals(other.code);
    if (result)
     {
      result = this.latestain.equals(other.latestain);
     }
    return result;
   }


  /**
   * Returns the string representation of this SubscriptionState.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "SubscriptionState[code=SubscriptionCode[code=0], latestain=AIN[ain=000000000000]]"
   *
   * @return String representation of this SubscriptionState
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("SubscriptionState[code=").append(this.code).append(", ").append(this.latestain).append(']'); //$NON-NLS-1$
    return builder.toString();
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
    int result = this.code.compareTo(obj.code);
    if (result == 0)
     {
      result = this.latestain.compareTo(obj.latestain);
     }
    return result;
   }

 }
