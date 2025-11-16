/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import java.util.ArrayList;
import java.util.List;

import de.powerstat.fb.mini.GroupInfo;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Group info tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class GroupInfoTests
 {
  /**
   * Default constructor.
   */
  /* default */ GroupInfoTests()
   {
    super();
   }


  /**
   * Test correct GroupInfo.
   */
  @Test
  /* default */ void testGroupInfoCorrect()
   {
    final List<Long> members = new ArrayList<>();
    members.add(1L);
    final GroupInfo cleanGroupInfo = GroupInfo.of(0, members);
    assertEquals("0", cleanGroupInfo.stringValue(), "GroupInfo not as expected"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test wrong GroupInfo.
   */
  @Test
  /* default */ void testGroupInfoWrong1()
   {
    final List<Long> members = new ArrayList<>();
    members.add(1L);
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final GroupInfo cleanGroupInfo = */ GroupInfo.of(-1, members);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong GroupInfo.
   */
  @Test
  /* default */ void testGroupInfoWrong2()
   {
    assertThrows(NullPointerException.class, () ->
     {
      /* final GroupInfo cleanGroupInfo = */ GroupInfo.of(0, null);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong GroupInfo.
   */
  @Test
  /* default */ void testGroupInfoWrong3()
   {
    final List<Long> members = new ArrayList<>();
    assertThrows(IllegalArgumentException.class, () ->
     {
      /* final GroupInfo cleanGroupInfo = */ GroupInfo.of(0, members);
     }, "Illegal argument exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final List<Long> members = new ArrayList<>();
    members.add(1L);
    final GroupInfo groupInfo = GroupInfo.of(0, members);
    assertEquals("0", groupInfo.stringValue(), "GroupInfo not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  public void equalsContract()
   {
    EqualsVerifier.forClass(GroupInfo.class).withNonnullFields("members").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final List<Long> members = new ArrayList<>();
    members.add(1L);
    final GroupInfo groupInfo = GroupInfo.of(0, members);
    assertEquals("GroupInfo[masterdeviceid=0, members=[1]]", groupInfo.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final List<Long> members = new ArrayList<>();
    members.add(1L);
    final GroupInfo groupInfo1 = GroupInfo.of(0, members);
    final GroupInfo groupInfo2 = GroupInfo.of(0, members);
    final GroupInfo groupInfo3 = GroupInfo.of(1, members);
    final GroupInfo groupInfo4 = GroupInfo.of(2, members);
    final GroupInfo groupInfo5 = GroupInfo.of(0, members);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(groupInfo1.compareTo(groupInfo2) == -groupInfo2.compareTo(groupInfo1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(groupInfo1.compareTo(groupInfo3) == -groupInfo3.compareTo(groupInfo1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((groupInfo4.compareTo(groupInfo3) > 0) && (groupInfo3.compareTo(groupInfo1) > 0) && (groupInfo4.compareTo(groupInfo1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((groupInfo1.compareTo(groupInfo2) == 0) && (Math.abs(groupInfo1.compareTo(groupInfo5)) == Math.abs(groupInfo2.compareTo(groupInfo5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((groupInfo1.compareTo(groupInfo2) == 0) && groupInfo1.equals(groupInfo2), "equals") //$NON-NLS-1$
    );
   }

 }
