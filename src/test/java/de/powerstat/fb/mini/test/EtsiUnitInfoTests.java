/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import de.powerstat.fb.mini.EtsiUnitInfo;
import de.powerstat.fb.mini.HANFUNInterfaces;
import de.powerstat.fb.mini.HANFUNUnits;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Etsi unit info tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class EtsiUnitInfoTests
 {
  /**
   * Default constructor.
   */
  /* default */ EtsiUnitInfoTests()
   {
    super();
   }


  /**
   * Test correct EtsiUnitInfo.
   */
  @Test
  /* default */ void testEtsiUnitInfoCorrect()
   {
    final EtsiUnitInfo cleanEtsiUnitInfo = EtsiUnitInfo.of(0, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
    assertEquals("0", cleanEtsiUnitInfo.stringValue(), "EtsiUnitInfo not as expected"); //$NON-NLS-1$
   }


  /**
   * Test wrong EtsiUnitInfo.
   */
  @Test /* default */ void testEtsiUnitInfoWrong()
   {
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final EtsiUnitInfo cleanEtsiUnitInfo = */ EtsiUnitInfo.of(-1, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final EtsiUnitInfo cleanEtsiUnitInfo = EtsiUnitInfo.of(0, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
    assertEquals("0", cleanEtsiUnitInfo.stringValue(), "EtsiUnitInfo not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(EtsiUnitInfo.class).withNonnullFields("unittype", "interfaces").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final EtsiUnitInfo cleanEtsiUnitInfo = EtsiUnitInfo.of(0, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
    assertEquals("EtsiUnitInfo[etsideviceid=0, unittype=SIMPLE_BUTTON, interfaces=[KEEP_ALIVE]]", cleanEtsiUnitInfo.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final EtsiUnitInfo etsiUnitInfo1 = EtsiUnitInfo.of(0, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
    final EtsiUnitInfo etsiUnitInfo2 = EtsiUnitInfo.of(0, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
    final EtsiUnitInfo etsiUnitInfo3 = EtsiUnitInfo.of(1, HANFUNUnits.SIMPLE_ON_OFF_SWITCHABLE, EnumSet.of(HANFUNInterfaces.ALERT));
    final EtsiUnitInfo etsiUnitInfo4 = EtsiUnitInfo.of(2, HANFUNUnits.SIMPLE_ON_OFF_SWITCH, EnumSet.of(HANFUNInterfaces.ON_OFF));
    final EtsiUnitInfo etsiUnitInfo5 = EtsiUnitInfo.of(0, HANFUNUnits.SIMPLE_BUTTON, EnumSet.of(HANFUNInterfaces.KEEP_ALIVE));
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(etsiUnitInfo1.compareTo(etsiUnitInfo2) == -etsiUnitInfo2.compareTo(etsiUnitInfo1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(etsiUnitInfo1.compareTo(etsiUnitInfo3) == -etsiUnitInfo3.compareTo(etsiUnitInfo1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((etsiUnitInfo4.compareTo(etsiUnitInfo3) > 0) && (etsiUnitInfo3.compareTo(etsiUnitInfo1) > 0) && (etsiUnitInfo4.compareTo(etsiUnitInfo1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((etsiUnitInfo1.compareTo(etsiUnitInfo2) == 0) && (Math.abs(etsiUnitInfo1.compareTo(etsiUnitInfo5)) == Math.abs(etsiUnitInfo2.compareTo(etsiUnitInfo5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((etsiUnitInfo1.compareTo(etsiUnitInfo2) == 0) && etsiUnitInfo1.equals(etsiUnitInfo2), "equals") //$NON-NLS-1$
    );
   }

 }
