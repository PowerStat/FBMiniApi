/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Group info.
 *
 * @param masterdeviceid Master device id
 * @param members Group member internal id's
 */
@ValueObject
public record GroupInfo(long masterdeviceid, List<Long> members) implements Comparable<GroupInfo>, IValueObject
 {
  /**
   * Constructor.
   *
   * @param masterdeviceid Master device id
   * @param members Group member internal id's
   * @throws IndexOutOfBoundsException When masterdeviceid &lt; 0
   * @throws NullPointerException When members is null
   * @throws IllegalArgumentException When mebers is empty
   */
  public GroupInfo
   {
    if (masterdeviceid < 0)
     {
      throw new IndexOutOfBoundsException("masterdeviceid < 0");
     }
    Objects.requireNonNull(members, "members"); //$NON-NLS-1$
    if (members.isEmpty())
     {
      throw new IllegalArgumentException("members without any member");
     }
   }


  /**
   * GroupInfo factory.
   *
   * @param masterdeviceid Master device id
   * @param members Group member internal id's
   * @return GroupInfo object
   * @throws IndexOutOfBoundsException When masterdeviceid &lt; 0
   * @throws NullPointerException When members is null
   * @throws IllegalArgumentException When mebers is empty
   */
  public static GroupInfo of(final long masterdeviceid, final List<Long> members)
   {
    return new GroupInfo(masterdeviceid, members);
   }


  /**
   * Returns the value of this GroupInfo as a string.
   *
   * @return The value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return String.valueOf(masterdeviceid);
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final GroupInfo obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    final int result = Long.compare(masterdeviceid, obj.masterdeviceid);
    /*
    if (result == 0)
     {
      result = this.members.compareTo(obj.members); // TODO
     }
    */
    return result;
   }

 }
