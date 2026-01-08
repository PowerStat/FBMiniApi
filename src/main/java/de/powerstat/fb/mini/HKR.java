/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;
import de.powerstat.ddd.values.science.Percent;


/**
 * HKR.
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
@ValueObject
public record HKR(TemperatureCelsius tist, TemperatureCelsius tsoll, TemperatureCelsius absenk, TemperatureCelsius komfort, Boolean lock, Boolean devicelock, HkrErrorCodes errorcode, boolean windowsopenactive, UnixTimestamp windowopenactiveendtime, boolean boostactive, UnixTimestamp boostactiveendtime, boolean batterylow, Percent battery, HkrNextChange nextchange, boolean summeractive, boolean holidayactive, boolean adaptiveHeatingActive, boolean adaptiveHeatingRunning) implements Comparable<HKR>, IValueObject
 {
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
  public HKR
   {
    Objects.requireNonNull(absenk, "absenk"); //$NON-NLS-1$
    Objects.requireNonNull(komfort, "komfort"); //$NON-NLS-1$
    Objects.requireNonNull(errorcode, "errorcode"); //$NON-NLS-1$
    // Objects.requireNonNull(windowopenactiveendtime, "windowopenactiveendtime"); //$NON-NLS-1$
    // Objects.requireNonNull(boostactiveendtime, "boostactiveendtime"); //$NON-NLS-1$
    Objects.requireNonNull(battery, "battery"); //$NON-NLS-1$
    Objects.requireNonNull(nextchange, "nextchange"); //$NON-NLS-1$
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
