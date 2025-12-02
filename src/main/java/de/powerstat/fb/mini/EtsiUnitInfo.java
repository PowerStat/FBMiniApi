/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.EnumSet;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Etsi unit info.
 */
@ValueObject
public final class EtsiUnitInfo implements Comparable<EtsiUnitInfo>, IValueObject
 {
  /**
   * Etsi device id.
   */
  private final long etsideviceid;

  /**
   * Unit type.
   */
  private final HANFUNUnits unittype;

  /**
   * Interfaces.
   */
  private final EnumSet<HANFUNInterfaces> interfaces;


  /**
   * Etsi unit info.
   *
   * @param etsideviceid Etsi device id
   * @param unittype Unit type
   * @param interfaces Interfaces
   * @throws NullPointerException When unittype or interfaces is null
   * @throws IndexOutOfBoundsException When etsideviceid &lt; 0
   */
  private EtsiUnitInfo(final long etsideviceid, final HANFUNUnits unittype, final EnumSet<HANFUNInterfaces> interfaces)
   {
    super();
    if (etsideviceid < 0)
     {
      throw new IndexOutOfBoundsException("etsideviceid must be >= 0");
     }
    Objects.requireNonNull(unittype, "unittype"); //$NON-NLS-1$
    Objects.requireNonNull(interfaces, "interfaces"); //$NON-NLS-1$
    this.etsideviceid = etsideviceid;
    this.unittype = unittype;
    this.interfaces = interfaces;
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
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(etsideviceid, unittype, interfaces);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final @Nullable Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final EtsiUnitInfo other))
     {
      return false;
     }
    boolean result = etsideviceid == other.etsideviceid;
    if (result)
     {
      result = (unittype == other.unittype);
      if (result)
       {
        result = interfaces.equals(other.interfaces);
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this EtsiUnitInfo.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "EtsiUnitInfo[etsideviceid=, unittype=, interfaces=]"
   *
   * @return String representation of this EtsiUnitInfo
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(51);
    builder.append("EtsiUnitInfo[etsideviceid=").append(etsideviceid)
      .append(", unittype=").append(unittype)
      .append(", interfaces=").append(interfaces)
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
