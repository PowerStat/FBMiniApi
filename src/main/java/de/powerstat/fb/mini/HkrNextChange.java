/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Hkr next change.
 */
public final class HkrNextChange implements Comparable<HkrNextChange>, IValueObject
 {
  /**
   * Next change timestamp.
   */
  private final UnixTimestamp endperiod;

  /**
   * Target temperature.
   */
  private final TemperatureCelsius tchange;


  /**
   * Constructor.
   *
   * @param endperiod Next change timestamp
   * @param tchange Target temperature
   * @throws NullPointerException When one of the parameters is null
   */
  private HkrNextChange(final UnixTimestamp endperiod, final TemperatureCelsius tchange)
   {
    super();
    Objects.requireNonNull(endperiod, "endperiod"); //$NON-NLS-1$
    Objects.requireNonNull(tchange, "tchange"); //$NON-NLS-1$
    this.endperiod = endperiod;
    this.tchange = tchange;
   }


  /**
   * HkrNextChange factory.
   *
   * @param endperiod Next change timestamp
   * @param tchange Target temperature
   * @return HkrNextChange object
   * @throws NullPointerException When one of the parameters is null
   */
  public static HkrNextChange of(final UnixTimestamp endperiod, final TemperatureCelsius tchange)
   {
    return new HkrNextChange(endperiod, tchange);
   }


  /**
   * Returns the value of this HkrNextChange as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return endperiod.stringValue();
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
    return Objects.hash(endperiod, tchange);
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
    if (!(obj instanceof final HkrNextChange other))
     {
      return false;
     }
    boolean result = endperiod.equals(other.endperiod);
    if (result)
     {
      result = tchange.equals(other.tchange);
     }
    return result;
   }


  /**
   * Returns the string representation of this HkrNextChange.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "HkrNextChange[endperiod=, tchange=]"
   *
   * @return String representation of this HkrNextChange
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(35);
    builder.append("HkrNextChange[endperiod=").append(endperiod).append(", tchange=").append(tchange).append(']'); //$NON-NLS-1$
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
  public int compareTo(final HkrNextChange obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = endperiod.compareTo(obj.endperiod);
    if (result == 0)
     {
      result = tchange.compareTo(obj.tchange);
     }
    return result;
   }

 }
