/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.EnumSet;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Etsi unit info.
 *
 * @param etsideviceid Etsi device id
 * @param unittype Unit type
 * @param interfaces Interfaces
 */
@ValueObject
public record EtsiUnitInfo(long etsideviceid, HANFUNUnits unittype, EnumSet<HANFUNInterfaces> interfaces) implements Comparable<EtsiUnitInfo>, IValueObject
 {
  /**
   * Etsi unit info.
   *
   * @param etsideviceid Etsi device id
   * @param unittype Unit type
   * @param interfaces Interfaces
   * @throws NullPointerException When unittype or interfaces is null
   * @throws IndexOutOfBoundsException When etsideviceid &lt; 0
   */
  public EtsiUnitInfo
   {
    if (etsideviceid < 0)
     {
      throw new IndexOutOfBoundsException("etsideviceid must be >= 0");
     }
    Objects.requireNonNull(unittype, "unittype"); //$NON-NLS-1$
    Objects.requireNonNull(interfaces, "interfaces"); //$NON-NLS-1$
   }


  /**
   * EtsiUnitInfo factory.
   *
   * @param etsideviceid Etsi device id
   * @param unittype Unit type
   * @param interfaces Interfaces
   * @return EtsiUnitInfo object
   * @throws NullPointerException When unittype or interfaces is null
   * @throws IndexOutOfBoundsException When etsideviceid &lt; 0
   */
  public static EtsiUnitInfo of(final long etsideviceid, final HANFUNUnits unittype, final EnumSet<HANFUNInterfaces> interfaces)
   {
    return new EtsiUnitInfo(etsideviceid, unittype, interfaces);
   }


  /**
   * Returns the value of this EtsiUnitInfo as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(etsideviceid);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final EtsiUnitInfo obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = Long.compare(etsideviceid, obj.etsideviceid);
    if (result == 0)
     {
      result = unittype.compareTo(obj.unittype);
      if (result == 0)
       {
        // this.interfaces.compareTo(obj.interfaces);
       }
     }
    return result;
   }

 }
