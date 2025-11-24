/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.test;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import nl.jqno.equalsverifier.EqualsVerifier;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import de.powerstat.fb.mini.AIN;
import de.powerstat.fb.mini.Alert;
import de.powerstat.fb.mini.Blind;
import de.powerstat.fb.mini.Button;
import de.powerstat.fb.mini.ColorControl;
import de.powerstat.fb.mini.ColorModes;
import de.powerstat.fb.mini.Device;
import de.powerstat.fb.mini.Energy;
import de.powerstat.fb.mini.EtsiUnitInfo;
import de.powerstat.fb.mini.Functions;
import de.powerstat.fb.mini.GroupInfo;
import de.powerstat.fb.mini.HANFUNInterfaces;
import de.powerstat.fb.mini.HANFUNUnits;
import de.powerstat.fb.mini.HKR;
import de.powerstat.fb.mini.HkrErrorCodes;
import de.powerstat.fb.mini.HkrNextChange;
import de.powerstat.fb.mini.Level;
import de.powerstat.fb.mini.LevelControl;
import de.powerstat.fb.mini.Power;
import de.powerstat.fb.mini.Powermeter;
import de.powerstat.fb.mini.SimpleOnOff;
import de.powerstat.fb.mini.Switch;
import de.powerstat.fb.mini.Temperature;
import de.powerstat.fb.mini.TemperatureCelsius;
import de.powerstat.fb.mini.UnixTimestamp;
import de.powerstat.fb.mini.Version;
import de.powerstat.fb.mini.Voltage;
import de.powerstat.validation.values.Percent;
import de.powerstat.validation.values.Seconds;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;


/**
 * Device tests.
 */
@SuppressFBWarnings({"RV_NEGATING_RESULT_OF_COMPARETO", "EC_NULL_ARG", "SPP_USE_ZERO_WITH_COMPARATOR"})
final class DeviceTests
 {
  /**
   * AIN zero.
   */
  private static final String AIN_ZERO = "000000000000";


  /**
   * Default constructor.
   */
  /* default */ DeviceTests()
   {
    super();
   }


  /**
   * Test correct Device.
   */
  @Test
  /* default */ void testDeviceCorrect1()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 1;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = Percent.of(0);
    final Switch switchState = Switch.of(null, null, null, null);
    final SimpleOnOff simpleonoff = SimpleOnOff.of(false);
    final Powermeter powermeter = Powermeter.of(Voltage.of(0), Power.of(0), Energy.of(0));
    final Temperature temperature = Temperature.of(TemperatureCelsius.of(0), TemperatureCelsius.of(0));
    final Percent humidity = Percent.of(0);
    final HKR hkr = HKR.of(null, null, TemperatureCelsius.of(0), TemperatureCelsius.of(0), null, null, HkrErrorCodes.NO_ERROR, false, null, false, null, false, Percent.of(0), HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(0)), false, false, false, false);
    final List<Button> buttons = new ArrayList<>();
    buttons.add(Button.of(null, 0, null, null));
    final LevelControl levelControl = LevelControl.of(Level.of(0), Percent.of(0));
    final ColorControl colorControl = ColorControl.of(EnumSet.allOf(ColorModes.class), null, false, false, null, null, null, null, null);
    final EtsiUnitInfo etsiunitinfo = EtsiUnitInfo.of(0, HANFUNUnits.BLIND, EnumSet.noneOf(HANFUNInterfaces.class));
    final Alert alert = Alert.of(null, null);
    final Blind blind = Blind.of(null, null);
    final List<Long> members = new ArrayList<>();
    members.add(Long.valueOf(0));
    final GroupInfo groupinfo = GroupInfo.of(0, members);
    final Device cleanDevice = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    assertAll("Device", //$NON-NLS-1$
      () -> assertEquals(AIN_ZERO, cleanDevice.stringValue(), "stringValue not as expected"), //$NON-NLS-1$
      () -> assertEquals(identifier, cleanDevice.getIdentifier(), "Identifier not as expected"), //$NON-NLS-1$
      () -> assertEquals(id, cleanDevice.getId(), "Id not as expected"), //$NON-NLS-1$
      () -> assertEquals(functionbitmask, cleanDevice.getFunctionbitmask(), "Functionbitmask not as expected"), //$NON-NLS-1$
      () -> assertEquals(fwversion, cleanDevice.getFwversion(), "Fwversion not as expected"), //$NON-NLS-1$
      () -> assertEquals(manufacturer, cleanDevice.getManufacturer(), "Manufacturer not as expected"), //$NON-NLS-1$
      () -> assertEquals(productname, cleanDevice.getProductname(), "Productname not as expected"), //$NON-NLS-1$
      () -> assertEquals(present, cleanDevice.isPresent(), "Present not as expected"), //$NON-NLS-1$
      () -> assertEquals(txbusy, cleanDevice.isTxbusy(), "Txbusy not as expected"), //$NON-NLS-1$
      () -> assertEquals(name, cleanDevice.getName(), "Name not as expected"), //$NON-NLS-1$
      () -> assertEquals(batterylow, cleanDevice.isBatterylow(), "Batterylow not as expected"), //$NON-NLS-1$
      () -> assertEquals(battery, cleanDevice.getBattery(), "Battery not as expected"), //$NON-NLS-1$
      () -> assertEquals(switchState, cleanDevice.getSwitch(), "Switch not as expected"), //$NON-NLS-1$
      () -> assertEquals(simpleonoff, cleanDevice.getSimpleOnOff(), "SimpleOnOff not as expected"), //$NON-NLS-1$
      () -> assertEquals(powermeter, cleanDevice.getPowermeter(), "Powermeter not as expected"), //$NON-NLS-1$
      () -> assertEquals(temperature, cleanDevice.getTemperature(), "Temperature not as expected"), //$NON-NLS-1$
      () -> assertEquals(humidity, cleanDevice.getHumidity(), "Humidity not as expected"), //$NON-NLS-1$
      () -> assertEquals(hkr, cleanDevice.getHkr(), "Hkr not as expected"), //$NON-NLS-1$
      () -> assertEquals(buttons, cleanDevice.getButtons(), "Buttons not as expected"), //$NON-NLS-1$
      () -> assertEquals(levelControl, cleanDevice.getLevelControl(), "LevelControl not as expected"), //$NON-NLS-1$
      () -> assertEquals(colorControl, cleanDevice.getColorControl(), "ColorControl not as expected"), //$NON-NLS-1$
      () -> assertEquals(etsiunitinfo, cleanDevice.getEtsiUnitInfo(), "EtsiUnitInfo not as expected"), //$NON-NLS-1$
      () -> assertEquals(alert, cleanDevice.getAlert(), "Alert not as expected"), //$NON-NLS-1$
      () -> assertEquals(blind, cleanDevice.getBlind(), "Blind not as expected"), //$NON-NLS-1$
      () -> assertEquals(groupinfo, cleanDevice.getGroupInfo(), "GroupInfo not as expected") //$NON-NLS-1$
    );
   }


  /**
   * Test correct Device.
   */
  @Test
  /* default */ void testDeviceCorrect2()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 1;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = true;
    final boolean txbusy = true;
    final String name = null;
    final boolean batterylow = true;
    final Percent battery = Percent.of(0);
    final Switch switchState = Switch.of(null, null, null, null);
    final SimpleOnOff simpleonoff = SimpleOnOff.of(false);
    final Powermeter powermeter = Powermeter.of(Voltage.of(0), Power.of(0), Energy.of(0));
    final Temperature temperature = Temperature.of(TemperatureCelsius.of(0), TemperatureCelsius.of(0));
    final Percent humidity = Percent.of(0);
    final HKR hkr = HKR.of(null, null, TemperatureCelsius.of(0), TemperatureCelsius.of(0), null, null, HkrErrorCodes.NO_ERROR, false, null, false, null, false, Percent.of(0), HkrNextChange.of(UnixTimestamp.of(Seconds.of(0)), TemperatureCelsius.of(0)), false, false, false, false);
    final List<Button> buttons = new ArrayList<>();
    buttons.add(Button.of(null, 0, null, null));
    final LevelControl levelControl = LevelControl.of(Level.of(0), Percent.of(0));
    final ColorControl colorControl = ColorControl.of(EnumSet.allOf(ColorModes.class), null, false, false, null, null, null, null, null);
    final EtsiUnitInfo etsiunitinfo = EtsiUnitInfo.of(0, HANFUNUnits.BLIND, EnumSet.noneOf(HANFUNInterfaces.class));
    final Alert alert = Alert.of(null, null);
    final Blind blind = Blind.of(null, null);
    final List<Long> members = new ArrayList<>();
    members.add(Long.valueOf(0));
    final GroupInfo groupinfo = GroupInfo.of(0, members);
    final Device cleanDevice = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    assertAll("Device", //$NON-NLS-1$
      () -> assertEquals(AIN_ZERO, cleanDevice.stringValue(), "stringValue not as expected"), //$NON-NLS-1$
      () -> assertEquals(identifier, cleanDevice.getIdentifier(), "Identifier not as expected"), //$NON-NLS-1$
      () -> assertEquals(id, cleanDevice.getId(), "Id not as expected"), //$NON-NLS-1$
      () -> assertEquals(functionbitmask, cleanDevice.getFunctionbitmask(), "Functionbitmask not as expected"), //$NON-NLS-1$
      () -> assertEquals(fwversion, cleanDevice.getFwversion(), "Fwversion not as expected"), //$NON-NLS-1$
      () -> assertEquals(manufacturer, cleanDevice.getManufacturer(), "Manufacturer not as expected"), //$NON-NLS-1$
      () -> assertEquals(productname, cleanDevice.getProductname(), "Productname not as expected"), //$NON-NLS-1$
      () -> assertEquals(present, cleanDevice.isPresent(), "Present not as expected"), //$NON-NLS-1$
      () -> assertEquals(txbusy, cleanDevice.isTxbusy(), "Txbusy not as expected"), //$NON-NLS-1$
      () -> assertEquals(name, cleanDevice.getName(), "Name not as expected"), //$NON-NLS-1$
      () -> assertEquals(batterylow, cleanDevice.isBatterylow(), "Batterylow not as expected"), //$NON-NLS-1$
      () -> assertEquals(battery, cleanDevice.getBattery(), "Battery not as expected"), //$NON-NLS-1$
      () -> assertEquals(switchState, cleanDevice.getSwitch(), "Switch not as expected"), //$NON-NLS-1$
      () -> assertEquals(simpleonoff, cleanDevice.getSimpleOnOff(), "SimpleOnOff not as expected"), //$NON-NLS-1$
      () -> assertEquals(powermeter, cleanDevice.getPowermeter(), "Powermeter not as expected"), //$NON-NLS-1$
      () -> assertEquals(temperature, cleanDevice.getTemperature(), "Temperature not as expected"), //$NON-NLS-1$
      () -> assertEquals(humidity, cleanDevice.getHumidity(), "Humidity not as expected"), //$NON-NLS-1$
      () -> assertEquals(hkr, cleanDevice.getHkr(), "Hkr not as expected"), //$NON-NLS-1$
      () -> assertEquals(buttons, cleanDevice.getButtons(), "Buttons not as expected"), //$NON-NLS-1$
      () -> assertEquals(levelControl, cleanDevice.getLevelControl(), "LevelControl not as expected"), //$NON-NLS-1$
      () -> assertEquals(colorControl, cleanDevice.getColorControl(), "ColorControl not as expected"), //$NON-NLS-1$
      () -> assertEquals(etsiunitinfo, cleanDevice.getEtsiUnitInfo(), "EtsiUnitInfo not as expected"), //$NON-NLS-1$
      () -> assertEquals(alert, cleanDevice.getAlert(), "Alert not as expected"), //$NON-NLS-1$
      () -> assertEquals(blind, cleanDevice.getBlind(), "Blind not as expected"), //$NON-NLS-1$
      () -> assertEquals(groupinfo, cleanDevice.getGroupInfo(), "GroupInfo not as expected") //$NON-NLS-1$
    );
   }



  /**
   * Test correct Device.
   */
  @Test
  /* default */ void testDeviceCorrect3()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 0;
    final EnumSet<Functions> functionbitmask = EnumSet.noneOf(Functions.class);
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    final Device cleanDevice = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    assertEquals(AIN_ZERO, cleanDevice.stringValue(), "Device not as expected"); //$NON-NLS-1$
   }


  /**
   * Test wrong Device.
   */
  @Test
  /* default */ void testDeviceWrong1()
   {
    final AIN identifier = null;
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    assertThrows(NullPointerException.class, () ->
     {
      /* final Device cleanDevice = */ Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Device.
   */
  @Test
  /* default */ void testDeviceWrong2()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final Version fwversion = null;
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    assertThrows(NullPointerException.class, () ->
     {
      /* final Device cleanDevice = */ Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
     }, "Null pointer exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test wrong Device.
   */
  @Test
  /* default */ void testDeviceWrong3()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = -1;
    final EnumSet<Functions> functionbitmask = null;
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    assertThrows(IndexOutOfBoundsException.class, () ->
     {
      /* final Device cleanDevice = */ Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
     }, "Index out of bounds exception expected" //$NON-NLS-1$
    );
   }


  /**
   * Test stringValue.
   */
  @Test
  /* default */ void testStringValue()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    final Device cleanDevice = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    assertEquals(AIN_ZERO, cleanDevice.stringValue(), "Device not as expected"); //$NON-NLS-1$
   }


  /**
   * Equalsverifier.
   */
  @Test
  /* default */ void testEqualsContract()
   {
    EqualsVerifier.forClass(Device.class).withNonnullFields("identifier", "functionbitmask", "fwversion").verify();
   }


  /**
   * Test toString.
   */
  @Test
  /* default */ void testToString()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    final Device device = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    assertEquals("Device[identifier=AIN[ain=000000000000], id=0, functionbitmask=[], fwversion=Version[major=0, minor=1], manufacturer=null, productname=null, present=false, txbusy=false, name=null, batterylow=false, battery=null, switch=null, simpleonoff=null, powermeter=null, temperature=null, humidity=null, hkr=null, buttons=null, levelControl=null, colorControl=null, etsiunitinfo=null, alert=null, blind=null, groupinfo=null]", device.toString(), "toString not equal"); //$NON-NLS-1$ //$NON-NLS-2$
   }


  /**
   * Test compareTo.
   */
  @Test
  @SuppressWarnings("java:S5785")
  /* default */ void testCompareTo()
   {
    final AIN identifier = AIN.of(AIN_ZERO);
    final long id = 0;
    final EnumSet<Functions> functionbitmask = null;
    final Version fwversion = Version.of(0, 1);
    final String manufacturer = null;
    final String productname = null;
    final boolean present = false;
    final boolean txbusy = false;
    final String name = null;
    final boolean batterylow = false;
    final Percent battery = null;
    final Switch switchState = null;
    final SimpleOnOff simpleonoff = null;
    final Powermeter powermeter = null;
    final Temperature temperature = null;
    final Percent humidity = null;
    final HKR hkr = null;
    final List<Button> buttons = null;
    final LevelControl levelControl = null;
    final ColorControl colorControl = null;
    final EtsiUnitInfo etsiunitinfo = null;
    final Alert alert = null;
    final Blind blind = null;
    final GroupInfo groupinfo = null;
    final Device device1 = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    final Device device2 = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    final Device device3 = Device.of(AIN.of("000000000001"), id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo); //$NON-NLS-1$
    final Device device4 = Device.of(AIN.of("000000000002"), id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo); //$NON-NLS-1$
    final Device device5 = Device.of(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
    assertAll("testCompareTo", //$NON-NLS-1$
      () -> assertTrue(device1.compareTo(device2) == -device2.compareTo(device1), "reflexive1"), //$NON-NLS-1$
      () -> assertTrue(device1.compareTo(device3) == -device3.compareTo(device1), "reflexive2"), //$NON-NLS-1$
      () -> assertTrue((device4.compareTo(device3) > 0) && (device3.compareTo(device1) > 0) && (device4.compareTo(device1) > 0), "transitive1"), //$NON-NLS-1$
      () -> assertTrue((device1.compareTo(device2) == 0) && (Math.abs(device1.compareTo(device5)) == Math.abs(device2.compareTo(device5))), "sgn1"), //$NON-NLS-1$
      () -> assertTrue((device1.compareTo(device2) == 0) && device1.equals(device2), "equals") //$NON-NLS-1$
    );
   }

 }
