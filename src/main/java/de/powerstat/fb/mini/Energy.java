/*
 * Copyright (C) 2020-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Energy in Wh.
 *
 * @param energy Energy in Wh. (must be &gt;= 0)
 */
@ValueObject
public record Energy(long energyWh) implements Comparable<Energy>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @throws IndexOutOfBoundsException If energy is less than zero
   */
  public Energy
   {
    if (energyWh < 0)
     {
      throw new IndexOutOfBoundsException("energy must be >= 0"); //$NON-NLS-1$
     }
   }


  /**
   * Energy factory.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @return Energy object
   * @throws IndexOutOfBoundsException If energy is less than zero
   */
  public static Energy of(final long energy)
   {
    return new Energy(energy);
   }


  /**
   * Energy factory.
   *
   * @param energy Energy in Wh. (must be &gt;= 0)
   * @return Energy object
   * @throws IndexOutOfBoundsException If energy is less than zero
   * @throws NumberFormatException If energy does not contain a parsable long.
   */
  public static Energy of(final String energy)
   {
    return new Energy(Long.parseLong(energy));
   }


  /**
   * Returns the value of this Energy as a String in watt hours.
   *
   * @return The numeric value represented by this object after conversion to type String in Wh.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(energyWh);
   }


  /**
   * Get energy in kilo watt hours.
   *
   * @return Energy in KWh
   */
  public long getEnergyKiloWattHours()
   {
    return energyWh / 1000;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final Energy obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return Long.compare(energyWh, obj.energyWh);
   }

 }
