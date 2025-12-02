/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;
import de.powerstat.validation.values.Percent;


/**
 * HKR.
 */
@ValueObject
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
    return (tist == null) ? "" : tist.stringValue();
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
    return Objects.hash(tist, tsoll, absenk, komfort, lock, devicelock, errorcode, windowsopenactive, windowopenactiveendtime, boostactive, boostactiveendtime, batterylow, battery, nextchange, summeractive, holidayactive, adaptiveHeatingActive, adaptiveHeatingRunning);
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
  @SuppressWarnings({"NestedIfDepth", "PMD.AvoidDeeplyNestedIfStmts"})
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final HKR other))
     {
      return false;
     }
    boolean result = equalField(tist, other.tist);
    if (result)
     {
      result = equalField(tsoll, other.tsoll);
      if (result)
       {
        result = absenk.equals(other.absenk);
        if (result)
         {
          result = komfort.equals(other.komfort);
          if (result)
           {
            result = equalField(lock, other.lock);
            if (result)
             {
              result = equalField(devicelock, other.devicelock);
              if (result)
               {
                result = (errorcode == other.errorcode);
                if (result)
                 {
                  result = windowsopenactive == other.windowsopenactive;
                  if (result)
                   {
                    result = equalField(windowopenactiveendtime, other.windowopenactiveendtime);
                    if (result)
                     {
                      result = boostactive == other.boostactive;
                      if (result)
                       {
                        result = equalField(boostactiveendtime, other.boostactiveendtime);
                        if (result)
                         {
                          result = batterylow == other.batterylow;
                          if (result)
                           {
                            result = battery.equals(other.battery);
                            if (result)
                             {
                              result = nextchange.equals(other.nextchange);
                              if (result)
                               {
                                result = summeractive == other.summeractive;
                                if (result)
                                 {
                                  result = holidayactive == other.holidayactive;
                                  if (result)
                                   {
                                    result = adaptiveHeatingActive == other.adaptiveHeatingActive;
                                    if (result)
                                     {
                                      result = adaptiveHeatingRunning == other.adaptiveHeatingRunning;
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
    final var builder = new StringBuilder(266);
    builder.append("HKR[tist=").append(tist)
      .append(", tsoll=").append(tsoll)
      .append(", absenk=").append(absenk)
      .append(", komfort=").append(komfort)
      .append(", lock=").append(lock)
      .append(", devicelock=").append(devicelock)
      .append(", errorcode=").append(errorcode)
      .append(", windowsopenactive=").append(windowsopenactive)
      .append(", windowopenactiveendtime=").append(windowopenactiveendtime)
      .append(", boostactive=").append(boostactive)
      .append(", boostactiveendtime=").append(boostactiveendtime)
      .append(", batterylow=").append(batterylow)
      .append(", battery=").append(battery)
      .append(", nextchange=").append(nextchange)
      .append(", summeractive=").append(summeractive)
      .append(", holidayactive=").append(holidayactive)
      .append(", adaptiveHeatingActive=").append(adaptiveHeatingActive)
      .append(", adaptiveHeatingRunning=").append(adaptiveHeatingRunning)
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
  @SuppressWarnings({"NestedIfDepth", "PMD.AvoidDeeplyNestedIfStmts"})
  @Override
  public int compareTo(final HKR obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = compareField(tist, obj.tist);
    if (result == 0)
     {
      result = compareField(tsoll, obj.tsoll);
      if (result == 0)
       {
        result = absenk.compareTo(obj.absenk);
        if (result == 0)
         {
          result = komfort.compareTo(obj.komfort);
          if (result == 0)
           {
            result = compareField(lock, obj.lock);
            if (result == 0)
             {
              result = compareField(devicelock, obj.devicelock);
              if (result == 0)
               {
                result = errorcode.compareTo(obj.errorcode);
                if (result == 0)
                 {
                  result = Boolean.compare(windowsopenactive, obj.windowsopenactive);
                  if (result == 0)
                   {
                    result = compareField(windowopenactiveendtime, obj.windowopenactiveendtime);
                    if (result == 0)
                     {
                      result = Boolean.compare(boostactive, obj.boostactive);
                      if (result == 0)
                       {
                        result = compareField(boostactiveendtime, obj.boostactiveendtime);
                        if (result == 0)
                         {
                          result = Boolean.compare(batterylow, obj.batterylow);
                          if (result == 0)
                           {
                            result = battery.compareTo(obj.battery);
                            if (result == 0)
                             {
                              result = nextchange.compareTo(obj.nextchange);
                              if (result == 0)
                               {
                                result = Boolean.compare(summeractive, obj.summeractive);
                                if (result == 0)
                                 {
                                  result = Boolean.compare(holidayactive, obj.holidayactive);
                                  if (result == 0)
                                   {
                                    result = Boolean.compare(adaptiveHeatingActive, obj.adaptiveHeatingActive);
                                    if (result == 0)
                                     {
                                      result = Boolean.compare(adaptiveHeatingRunning, obj.adaptiveHeatingRunning);
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
