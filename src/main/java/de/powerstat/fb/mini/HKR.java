/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;
import de.powerstat.validation.values.Percent;


/**
 * HKR.
 */
public final class HKR implements Comparable<HKR>, IValueObject
 {
  /**
   * Actual temperature.
   */
  private final TemperatureCelsius tist;

  /**
   * Target temperature.
   */
  private final TemperatureCelsius tsoll;

  /**
   * Reduced temperature.
   */
  private final TemperatureCelsius absenk;

  /**
   * Convenience temperature.
   */
  private final TemperatureCelsius komfort;

  /**
   * Keylock on UI/API true/false or null when unknown or error.
   */
  private final Boolean lock;

  /**
   * Keylock on device true/false or null when unknown or error.
   */
  private final Boolean devicelock;

  /**
   * Error code.
   */
  private final HkrErrorCodes errorcode;

  /**
   * Window open active.
   */
  private final boolean windowsopenactive;

  /**
   * Window open active end time.
   */
  private final UnixTimestamp windowopenactiveendtime;

  /**
   * Boost mode active.
   */
  private final boolean boostactive;

  /**
   * Boost mode active end time.
   */
  private final UnixTimestamp boostactiveendtime;

  /**
   * Battery low.
   */
  private final boolean batterylow;

  /**
   * Battery loading level in percent.
   */
  private final Percent battery;

  /**
   * Next temperature change.
   */
  private final HkrNextChange nextchange;

  /**
   * Heater off mode.
   */
  private final boolean summeractive;

  /**
   * Holiday period.
   */
  private final boolean holidayactive;

  /**
   * Adaptive heating active.
   */
  private final boolean adaptiveHeatingActive;

  /**
   * Adaptive heating running.
   */
  private final boolean adaptiveHeatingRunning;


  /**
   * Constructor.
   *
   * @param tist Actual temperature
   * @param tsoll Target temperature
   * @param absenk Reduced temperature
   * @param komfort Convenience temperature
   * @param lock Keylock on UI/API true/false or null when unknown or error
   * @param devicelock Keylock on device true/false or null when unknown or error
   * @param errorcode Error code
   * @param windowsopenactive Window open active
   * @param windowopenactiveendtime Window open active end time
   * @param boostactive Boost mode active
   * @param boostactiveendtime Boost mode active end time
   * @param batterylow Battery low
   * @param battery Battery loading level in percent
   * @param nextchange Next temperature change
   * @param summeractive Heater off mode
   * @param holidayactive Holiday period
   * @param adaptiveHeatingActive Adaptive heating active
   * @param adaptiveHeatingRunning Adaptive heating running
   */
  private HKR(final TemperatureCelsius tist, final TemperatureCelsius tsoll, final TemperatureCelsius absenk, final TemperatureCelsius komfort, final Boolean lock, final Boolean devicelock, final HkrErrorCodes errorcode, final boolean windowsopenactive, final UnixTimestamp windowopenactiveendtime, final boolean boostactive, final UnixTimestamp boostactiveendtime, final boolean batterylow, final Percent battery, final HkrNextChange nextchange, final boolean summeractive, final boolean holidayactive, final boolean adaptiveHeatingActive, final boolean adaptiveHeatingRunning)
   {
    Objects.requireNonNull(absenk, "absenk"); //$NON-NLS-1$
    Objects.requireNonNull(komfort, "komfort"); //$NON-NLS-1$
    Objects.requireNonNull(errorcode, "errorcode"); //$NON-NLS-1$
    // Objects.requireNonNull(windowopenactiveendtime, "windowopenactiveendtime"); //$NON-NLS-1$
    // Objects.requireNonNull(boostactiveendtime, "boostactiveendtime"); //$NON-NLS-1$
    Objects.requireNonNull(battery, "battery"); //$NON-NLS-1$
    Objects.requireNonNull(nextchange, "nextchange"); //$NON-NLS-1$
    this.tist = tist;
    this.tsoll = tsoll;
    this.absenk = absenk;
    this.komfort = komfort;
    this.lock = lock;
    this.devicelock = devicelock;
    this.errorcode = errorcode;
    this.windowsopenactive = windowsopenactive;
    this.windowopenactiveendtime = windowopenactiveendtime;
    this.boostactive = boostactive;
    this.boostactiveendtime = boostactiveendtime;
    this.batterylow = batterylow;
    this.battery = battery;
    this.nextchange = nextchange;
    this.summeractive = summeractive;
    this.holidayactive = holidayactive;
    this.adaptiveHeatingActive = adaptiveHeatingActive;
    this.adaptiveHeatingRunning = adaptiveHeatingRunning;
   }


  /**
   * HKR factory.
   *
   * @param tist Actual temperature
   * @param tsoll Target temperature
   * @param absenk Reduced temperature
   * @param komfort Convenience temperature
   * @param lock Keylock on UI/API true/false or null when unknown or error
   * @param devicelock Keylock on device true/false or null when unknown or error
   * @param errorcode Error code
   * @param windowsopenactive Window open active
   * @param windowopenactiveendtime Window open active end time
   * @param boostactive Boost mode active
   * @param boostactiveendtime Boost mode active end time
   * @param batterylow Battery low
   * @param battery Battery loading level in percent
   * @param nextchange Next temperature change
   * @param summeractive Heater off mode
   * @param holidayactive Holiday period
   * @param adaptiveHeatingActive Adaptive heating active
   * @param adaptiveHeatingRunning Adaptive heating running
   * @return HKR object
   */
  public static HKR of(final TemperatureCelsius tist, final TemperatureCelsius tsoll, final TemperatureCelsius absenk, final TemperatureCelsius komfort, final Boolean lock, final Boolean devicelock, final HkrErrorCodes errorcode, final boolean windowsopenactive, final UnixTimestamp windowopenactiveendtime, final boolean boostactive, final UnixTimestamp boostactiveendtime, final boolean batterylow, final Percent battery, final HkrNextChange nextchange, final boolean summeractive, final boolean holidayactive, final boolean adaptiveHeatingActive, final boolean adaptiveHeatingRunning)
   {
    return new HKR(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
   }


  /**
   * Returns the value of this HKR as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return (this.tist == null) ? "" : this.tist.stringValue();
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
    return Objects.hash(this.tist, this.tsoll, this.absenk, this.komfort, this.lock, this.devicelock, this.errorcode, this.windowsopenactive, this.windowopenactiveendtime, this.boostactive, this.boostactiveendtime, this.batterylow, this.battery, this.nextchange, this.summeractive, this.holidayactive, this.adaptiveHeatingActive, this.adaptiveHeatingRunning);
   }


  /**
   * Equal fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return true: equal; false: not equal
   */
  private static <T> boolean equalField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
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
    if (!(obj instanceof final HKR other))
     {
      return false;
     }
    boolean result = equalField(this.tist, other.tist);
    if (result)
     {
      result = equalField(this.tsoll, other.tsoll);
      if (result)
       {
        result = this.absenk.equals(other.absenk);
        if (result)
         {
          result = this.komfort.equals(other.komfort);
          if (result)
           {
            result = equalField(this.lock, other.lock);
            if (result)
             {
              result = equalField(this.devicelock, other.devicelock);
              if (result)
               {
                result = this.errorcode.equals(other.errorcode);
                if (result)
                 {
                  result = this.windowsopenactive == other.windowsopenactive;
                  if (result)
                   {
                    result = equalField(this.windowopenactiveendtime, other.windowopenactiveendtime);
                    if (result)
                     {
                      result = this.boostactive == other.boostactive;
                      if (result)
                       {
                        result = equalField(this.boostactiveendtime, other.boostactiveendtime);
                        if (result)
                         {
                          result = this.batterylow == other.batterylow;
                          if (result)
                           {
                            result = this.battery.equals(other.battery);
                            if (result)
                             {
                              result = this.nextchange.equals(other.nextchange);
                              if (result)
                               {
                                result = this.summeractive == other.summeractive;
                                if (result)
                                 {
                                  result = this.holidayactive == other.holidayactive;
                                  if (result)
                                   {
                                    result = this.adaptiveHeatingActive == other.adaptiveHeatingActive;
                                    if (result)
                                     {
                                      result = this.adaptiveHeatingRunning == other.adaptiveHeatingRunning;
                                     }
                                   }
                                 }
                               }
                             }
                           }
                         }
                       }
                     }
                   }
                 }
               }
             }
           }
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this HKR.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "HKR[]"
   *
   * @return String representation of this HKR
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("HKR[tist=").append(this.tist)
      .append(", tsoll=").append(this.tsoll)
      .append(", absenk=").append(this.absenk)
      .append(", komfort=").append(this.komfort)
      .append(", lock=").append(this.lock)
      .append(", devicelock=").append(this.devicelock)
      .append(", errorcode=").append(this.errorcode)
      .append(", windowsopenactive=").append(this.windowsopenactive)
      .append(", windowopenactiveendtime=").append(this.windowopenactiveendtime)
      .append(", boostactive=").append(this.boostactive)
      .append(", boostactiveendtime=").append(this.boostactiveendtime)
      .append(", batterylow=").append(this.batterylow)
      .append(", battery=").append(this.battery)
      .append(", nextchange=").append(this.nextchange)
      .append(", summeractive=").append(this.summeractive)
      .append(", holidayactive=").append(this.holidayactive)
      .append(", adaptiveHeatingActive=").append(this.adaptiveHeatingActive)
      .append(", adaptiveHeatingRunning=").append(this.adaptiveHeatingRunning)
      .append(']');
    return builder.toString();
   }


  /**
   * Compare fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return 0: equal; 1 field 1 greater than field 2; -1 field 1 smaller than field 2
   */
  private static <T extends Comparable<T>> int compareField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? ((obj2 == null) ? 0 : -1) : ((obj2 == null) ? 1 : obj1.compareTo(obj2));
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final HKR obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = compareField(this.tist, obj.tist);
    if (result == 0)
     {
      result = compareField(this.tsoll, obj.tsoll);
      if (result == 0)
       {
        result = this.absenk.compareTo(obj.absenk);
        if (result == 0)
         {
          result = this.komfort.compareTo(obj.komfort);
          if (result == 0)
           {
            result = compareField(this.lock, obj.lock);
            if (result == 0)
             {
              result = compareField(this.devicelock, obj.devicelock);
              if (result == 0)
               {
                result = this.errorcode.compareTo(obj.errorcode);
                if (result == 0)
                 {
                  result = Boolean.compare(this.windowsopenactive, obj.windowsopenactive);
                  if (result == 0)
                   {
                    result = compareField(this.windowopenactiveendtime, obj.windowopenactiveendtime);
                    if (result == 0)
                     {
                      result = Boolean.compare(this.boostactive, obj.boostactive);
                      if (result == 0)
                       {
                        result = compareField(this.boostactiveendtime, obj.boostactiveendtime);
                        if (result == 0)
                         {
                          result = Boolean.compare(this.batterylow, obj.batterylow);
                          if (result == 0)
                           {
                            result = this.battery.compareTo(obj.battery);
                            if (result == 0)
                             {
                              result = this.nextchange.compareTo(obj.nextchange);
                              if (result == 0)
                               {
                                result = Boolean.compare(this.summeractive, obj.summeractive);
                                if (result == 0)
                                 {
                                  result = Boolean.compare(this.holidayactive, obj.holidayactive);
                                  if (result == 0)
                                   {
                                    result = Boolean.compare(this.adaptiveHeatingActive, obj.adaptiveHeatingActive);
                                    if (result == 0)
                                     {
                                      result = Boolean.compare(this.adaptiveHeatingRunning, obj.adaptiveHeatingRunning);
                                     }
                                   }
                                 }
                               }
                             }
                           }
                         }
                       }
                     }
                   }
                 }
               }
             }
           }
         }
       }
     }
    return result;
   }

 }
