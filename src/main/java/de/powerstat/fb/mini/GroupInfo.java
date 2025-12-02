/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Group info.
 */
@ValueObject
public final class GroupInfo implements Comparable<GroupInfo>, IValueObject
 {
  /**
   * Master device id, 0 when no group set.
   */
  private final long masterdeviceid;

  /**
   * Group member internal id's.
   */
  private final List<Long> members;


  /**
   * Constructor.
   *
   * @param masterdeviceid Master device id
   * @param members Group member internal id's
   * @throws IndexOutOfBoundsException When masterdeviceid &lt; 0
   * @throws NullPointerException When members is null
   * @throws IllegalArgumentException When mebers is empty
   */
  private GroupInfo(final long masterdeviceid, final List<Long> members)
   {
    super();
    if (masterdeviceid < 0)
     {
      throw new IndexOutOfBoundsException("masterdeviceid < 0");
     }
    Objects.requireNonNull(members, "members"); //$NON-NLS-1$
    if (members.isEmpty())
     {
      throw new IllegalArgumentException("members without any member");
     }
    this.masterdeviceid = masterdeviceid;
    this.members = new ArrayList<>(members);
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
   * Calculate hash code.
   *
   * @return Hash
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
   {
    return Objects.hash(masterdeviceid, members);
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
    if (!(obj instanceof final GroupInfo other))
     {
      return false;
     }
    boolean result = masterdeviceid == other.masterdeviceid;
    if (result)
     {
      result = members.equals(other.members);
     }
    return result;
   }


  /**
   * Returns the string representation of this GroupInfo.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "GroupInfo[masterdeviceid=0, members=1,2]"
   *
   * @return String representation of this GroupInfo
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(36);
    builder.append("GroupInfo[masterdeviceid=").append(masterdeviceid).append(", members=").append(Arrays.toString(members.toArray())).append(']'); //$NON-NLS-1$
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
