/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Switch.
 */
public final class Switch implements Comparable<Switch>, IValueObject
 {
  /**
   * State true: on; false: off; unknown: null.
   */
  private final Boolean state;

  /**
   * Mode: true: auto; false: manuell; unknown: null.
   */
  private final Boolean mode;

  /**
   * Lock: true: locked; false: unlocked; unknown: null.
   */
  private final Boolean lock;

  /**
   * Device lock: true: locked; false: unlocked; unknown: null.
   */
  private final Boolean devicelock;


  /**
   * Constructor.
   *
   * @param state true: on; false: off; unknown: null
   * @param mode true: auto; false: manuell; unknown: null
   * @param lock true: locked; false: unlocked; unknown: null
   * @param devicelock true: locked; false: unlocked; unknown: null
   */
  private Switch(final Boolean state, final Boolean mode, final Boolean lock, final Boolean devicelock)
   {
    super();
    this.state = state;
    this.mode = mode;
    this.lock = lock;
    this.devicelock = devicelock;
   }


  /**
   * Switch factory.
   *
   * @param state true: on; false: off; unknown: null
   * @param mode true: auto; false: manuell; unknown: null
   * @param lock true: locked; false: unlocked; unknown: null
   * @param devicelock true: locked; false: unlocked; unknown: null
   * @return Switch object
   */
  public static Switch of(final Boolean state, final Boolean mode, final Boolean lock, final Boolean devicelock)
   {
    return new Switch(state, mode, lock, devicelock);
   }


  /**
   * Returns the value of this Switch as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(state);
   }


  /**
   * Get state.
   *
   * @return State.
   */
  public Boolean getState()
   {
    return state;
   }


  /**
   * Get mode.
   *
   * @return Mode.
   */
  public Boolean getMode()
   {
    return mode;
   }


  /**
   * Get lock.
   *
   * @return Lock.
   */
  public Boolean getLock()
   {
    return lock;
   }


  /**
   * Get device lock.
   *
   * @return Device lock.
   */
  public Boolean getDevicelock()
   {
    return devicelock;
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
    return Objects.hash(state, mode, lock, devicelock);
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
    if (!(obj instanceof final Switch other))
     {
      return false;
     }
    boolean result = state == other.state;
    if (result)
     {
      result = mode == other.mode;
      if (result)
       {
        result = lock == other.lock;
        if (result)
         {
          result = devicelock == other.devicelock;
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Switch.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Switch[state=false, mode=false, lock=false, devicelock=false]"
   *
   * @return String representation of this Switch
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(41);
    builder.append("Switch[state=").append(state)
      .append(", mode=").append(mode)
      .append(", lock=").append(lock)
      .append(", devicelock=").append(devicelock)
      .append(']');
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
  public int compareTo(final Switch obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Boolean.compare(state, obj.state);
    if (result == 0)
     {
      result = Boolean.compare(mode, obj.mode);
      if (result == 0)
       {
        result = Boolean.compare(lock, obj.lock);
        if (result == 0)
         {
          result = Boolean.compare(devicelock, obj.devicelock);
         }
       }
     }
    return result;
   }

 }
