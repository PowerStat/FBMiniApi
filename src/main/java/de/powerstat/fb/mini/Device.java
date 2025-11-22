/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;
import de.powerstat.validation.values.Percent;


/**
 * Device.
 */
public final class Device implements Comparable<Device>, IValueObject
 {
  /**
   * Identifier.
   */
  private final AIN identifier;

  /**
   * Id.
   */
  private final long id;

  /**
   * Function bitmask.
   */
  private final EnumSet<Functions> functionbitmask;

  /**
   * Firmware version.
   */
  private final Version fwversion;

  /**
   * Manufacturer.
   */
  private final String manufacturer;

  /**
   * Productname.
   */
  private final String productname;

  /**
   * Device is present.
   */
  private final boolean present;

  /**
   * TX busy.
   */
  private final boolean txbusy;

  /**
   * Name.
   */
  private final String name;

  /**
   * Batterylow.
   */
  private final boolean batterylow;

  /**
   * Battery.
   */
  private final Percent battery;

  /**
   * Switch.
   */
  private final Switch switchState;

  /**
   * Simple on/off.
   */
  private final SimpleOnOff simpleonoff;

  /**
   * Powermeter.
   */
  private final Powermeter powermeter;

  /**
   * Temperature.
   */
  private final Temperature temperature;

  /**
   * Humidity in percent.
   */
  private final Percent humidity;

  /**
   * HKR.
   */
  private final HKR hkr;

  /**
   * Buttons.
   */
  private final List<Button> buttons;

  /**
   * Level control.
   */
  private final LevelControl levelControl;

  /**
   * Color control.
   */
  private final ColorControl colorControl;

  /**
   * Etsi unit info.
   */
  private final EtsiUnitInfo etsiunitinfo;

  /**
   * Alert.
   */
  private final Alert alert;

  /**
   * Blind.
   */
  private final Blind blind;

  /**
   * Group info (only within group).
   */
  private final GroupInfo groupinfo;


  /**
   * Constructor.
   *
   * @param identifier AIN
   * @param id Id
   * @param functionbitmask Function bit mask
   * @param fwversion Firmware version
   * @param manufacturer Manufacturer name
   * @param productname Product name
   * @param present Device Present
   * @param txbusy TX busy
   * @param name Device name
   * @param batterylow Battery low
   * @param battery Battery load in percent
   * @param switchState Switch state
   * @param simpleonoff Simple on/off
   * @param powermeter Powermeter
   * @param temperature Temperature
   * @param humidity Humidity
   * @param hkr HKR
   * @param buttons Buttons
   * @param levelControl Level control
   * @param colorControl Color control
   * @param etsiunitinfo Etsi unit info
   * @param alert Alert
   * @param blind Blind
   * @param groupinfo Group info
   * @throws NullPointerException When identifier or fwversion is null
   * @throws IndexOutOfBoundsException When id &lt; 0
   */
  private Device(final AIN identifier, final long id, final EnumSet<Functions> functionbitmask, final Version fwversion, final String manufacturer, final String productname, final boolean present, final boolean txbusy, final String name, final boolean batterylow, final Percent battery, final Switch switchState, final SimpleOnOff simpleonoff, final Powermeter powermeter, final Temperature temperature, final Percent humidity, final HKR hkr, final List<Button> buttons, final LevelControl levelControl, final ColorControl colorControl, final EtsiUnitInfo etsiunitinfo, final Alert alert, final Blind blind, final GroupInfo groupinfo)
   {
    super();
    Objects.requireNonNull(identifier, "identifier"); //$NON-NLS-1$
    Objects.requireNonNull(fwversion, "fwversion"); //$NON-NLS-1$
    if (id < 0)
     {
      throw new IndexOutOfBoundsException("id < 0");
     }
    this.identifier = identifier;
    this.id = id;
    if (functionbitmask == null)
     {
      this.functionbitmask = EnumSet.noneOf(Functions.class);
     }
    else
     {
      this.functionbitmask = functionbitmask;
     }
    this.fwversion = fwversion;
    this.manufacturer = manufacturer;
    this.productname = productname;
    this.present = present;
    this.txbusy = txbusy;
    this.name = name;
    this.batterylow = batterylow;
    this.battery = battery;
    this.switchState = switchState;
    this.simpleonoff = simpleonoff;
    this.powermeter = powermeter;
    this.temperature = temperature;
    this.humidity = humidity;
    this.hkr = hkr;
    this.buttons = (buttons == null) ? null : new ArrayList<>(buttons);
    this.levelControl = levelControl;
    this.colorControl = colorControl;
    this.etsiunitinfo = etsiunitinfo;
    this.alert = alert;
    this.blind = blind;
    this.groupinfo = groupinfo;
   }


  /**
   * Device factory.
   *
   * @param identifier AIN
   * @param id Id
   * @param functionbitmask Function bit mask
   * @param fwversion Firmware version
   * @param manufacturer Manufacturer name
   * @param productname Product name
   * @param present Device Present
   * @param txbusy TX busy
   * @param name Device name
   * @param batterylow Battery low
   * @param battery Battery load in percent
   * @param switchState Switch state
   * @param simpleonoff Simple on/off
   * @param powermeter Powermeter
   * @param temperature Temperature
   * @param humidity Humidity
   * @param hkr HKR
   * @param buttons Buttons
   * @param levelControl Level control
   * @param colorControl Color control
   * @param etsiunitinfo Etsi unit info
   * @param alert Alert
   * @param blind Blind
   * @param groupinfo Group info
   * @return Device object
   * @throws NullPointerException When identifier or fwversion is null
   * @throws IndexOutOfBoundsException When id &lt; 0
   */
  public static Device of(final AIN identifier, final long id, final EnumSet<Functions> functionbitmask, final Version fwversion, final String manufacturer, final String productname, final boolean present, final boolean txbusy, final String name, final boolean batterylow, final Percent battery, final Switch switchState, final SimpleOnOff simpleonoff, final Powermeter powermeter, final Temperature temperature, final Percent humidity, final HKR hkr, final List<Button> buttons, final LevelControl levelControl, final ColorControl colorControl, final EtsiUnitInfo etsiunitinfo, final Alert alert, final Blind blind, final GroupInfo groupinfo)
   {
    return new Device(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
   }


  /**
   * Returns the value of this Device as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return identifier.stringValue();
   }


  /**
   * Get identifier.
   *
   * @return Identifier
   */
  public AIN getIdentifier()
   {
    return identifier;
   }


  /**
   * Get id.
   *
   * @return Id
   */
  public long getId()
   {
    return id;
   }


  /**
   * Get functionbitmask.
   *
   * @return Functionbitmask
   */
  public EnumSet<Functions> getFunctionbitmask()
   {
    return EnumSet.copyOf(functionbitmask);
   }


  /**
   * Get firmware version.
   *
   * @return Version
   */
  public Version getFwversion()
   {
    return fwversion;
   }


  /**
   * Get manufacturer.
   *
   * @return Manufacturer name or null
   */
  public String getManufacturer()
   {
    return manufacturer;
   }


  /**
   * Get productname.
   *
   * @return Productname or null
   */
  public String getProductname()
   {
    return productname;
   }


  /**
   * Is present.
   *
   * @return Present
   */
  public boolean isPresent()
   {
    return present;
   }


  /**
   * Is present.
   *
   * @return Present
   */
  public boolean isTxbusy()
   {
    return txbusy;
   }


  /**
   * Get name.
   *
   * @return Name or null
   */
  public String getName()
   {
    return name;
   }


  /**
   * Is battery low.
   *
   * @return true: battery low; false: otherwise
   */
  public boolean isBatterylow()
   {
    return batterylow;
   }


  /**
   * Get battery charge in percent.
   *
   * @return Percent or null
   */
  public Percent getBattery()
   {
    return battery;
   }


  /**
   * Get switch.
   *
   * @return Switch or null
   */
  public Switch getSwitch()
   {
    return switchState;
   }


  /**
   * Get simple on/off.
   *
   * @return SimpleOnOff or null
   */
  public SimpleOnOff getSimpleOnOff()
   {
    return simpleonoff;
   }


  /**
   * Get powermeter.
   *
   * @return Powermeter or null
   */
  public Powermeter getPowermeter()
   {
    return powermeter;
   }


  /**
   * Get temperature.
   *
   * @return Temperature or null
   */
  public Temperature getTemperature()
   {
    return temperature;
   }


  /**
   * Get humidity.
   *
   * @return Humidity or null
   */
  public Percent getHumidity()
   {
    return humidity;
   }


  /**
   * Get hkr.
   *
   * @return HKR or null
   */
  public HKR getHkr()
   {
    return hkr;
   }


  /**
   * Get buttons.
   *
   * @return List&lt;Button&gt;
   */
  public List<Button> getButtons()
   {
    return new ArrayList<>(buttons);
   }


  /**
   * Get LevelControl.
   *
   * @return LevelControl or null
   */
  public LevelControl getLevelControl()
   {
    return levelControl;
   }


  /**
   * Get ColorControl.
   *
   * @return ColorControl or null
   */
  public ColorControl getColorControl()
   {
    return colorControl;
   }


  /**
   * Get EtsiUnitInfo.
   *
   * @return EtsiUnitInfo or null
   */
  public EtsiUnitInfo getEtsiUnitInfo()
   {
    return etsiunitinfo;
   }


  /**
   * Get Alert.
   *
   * @return Alert or null
   */
  public Alert getAlert()
   {
    return alert;
   }


  /**
   * Get Blind.
   *
   * @return Blind or null
   */
  public Blind getBlind()
   {
    return blind;
   }


  /**
   * Get GroupInfo.
   *
   * @return GroupInfo or null
   */
  public GroupInfo getGroupInfo()
   {
    return groupinfo;
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
    return Objects.hash(identifier, id, functionbitmask, fwversion, manufacturer, productname, present, txbusy, name, batterylow, battery, switchState, simpleonoff, powermeter, temperature, humidity, hkr, buttons, levelControl, colorControl, etsiunitinfo, alert, blind, groupinfo);
   }


  /**
   * Equal fields.
   *
   * @param <T> Field type
   * @param obj1 Field 1 (this)
   * @param obj2 Field 2 (other)
   * @return true: equal; false: not equal
   */
  private static <T> boolean equalField(final T obj1, final T obj2)
   {
    return (obj1 == null) ? (obj2 == null) : obj1.equals(obj2);
   }


  /**
   * Is equal with another object.
   *
   * @param obj Object
   * @return true when equal, false otherwise
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @SuppressWarnings({"NestedIfDepth", "PMD.AvoidDeeplyNestedIfStmts"})
  @Override
  public boolean equals(final Object obj)
   {
    if (this == obj)
     {
      return true;
     }
    if (!(obj instanceof final Device other))
     {
      return false;
     }
    boolean result = identifier.equals(other.identifier);
    if (result)
     {
      result = (id == other.id);
      if (result)
       {
        result = functionbitmask.equals(other.functionbitmask);
        if (result)
         {
          result = fwversion.equals(other.fwversion);
          if (result)
           {
            result = equalField(manufacturer, other.manufacturer);
            if (result)
             {
              result = equalField(productname, other.productname);
              if (result)
               {
                result = present == other.present;
                if (result)
                 {
                  result = txbusy == other.txbusy;
                  if (result)
                   {
                    result = equalField(name, other.name);
                    if (result)
                     {
                      result = batterylow == other.batterylow;
                      if (result)
                       {
                        result = equalField(battery, other.battery);
                        if (result)
                         {
                          result = equalField(switchState, other.switchState);
                          if (result)
                           {
                            result = equalField(simpleonoff, other.simpleonoff);
                            if (result)
                             {
                              result = equalField(powermeter, other.powermeter);
                              if (result)
                               {
                                result = equalField(temperature, other.temperature);
                                if (result)
                                 {
                                  result = equalField(humidity, other.humidity);
                                  if (result)
                                   {
                                    result = equalField(hkr, other.hkr);
                                    if (result)
                                     {
                                      result = equalField(buttons, other.buttons);
                                      if (result)
                                       {
                                        result = equalField(levelControl, other.levelControl);
                                        if (result)
                                         {
                                          result = equalField(colorControl, other.colorControl);
                                          if (result)
                                           {
                                            result = equalField(etsiunitinfo, other.etsiunitinfo);
                                            if (result)
                                             {
                                              result = equalField(alert, other.alert);
                                              if (result)
                                               {
                                                result = equalField(blind, other.blind);
                                                if (result)
                                                 {
                                                  result = equalField(groupinfo, other.groupinfo);
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
               }
             }
           }
         }
       }
     }
    return result;
   }


  /**
   * Returns the string representation of this Device.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Device[identifier=000000000000, id=0, functionbitmask=0, fwversion=, manufacturer=, productname=, present=false, txbusy=false, name=, batterylow=false, battery=0, switch=, simpleonoff=, powermeter=, temperature=, humidity=, hkr=, buttons=, levelControl=, colorControl=, etsiunitinfo=, alert=, blind=, groupinfo=]"
   *
   * @return String representation of this Device
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(282);
    builder.append("Device[identifier=").append(identifier)
      .append(", id=").append(id)
      .append(", functionbitmask=").append(functionbitmask)
      .append(", fwversion=").append(fwversion)
      .append(", manufacturer=").append(manufacturer)
      .append(", productname=").append(productname)
      .append(", present=").append(present)
      .append(", txbusy=").append(txbusy)
      .append(", name=").append(name)
      .append(", batterylow=").append(batterylow)
      .append(", battery=").append(battery)
      .append(", switch=").append(switchState)
      .append(", simpleonoff=").append(simpleonoff)
      .append(", powermeter=").append(powermeter)
      .append(", temperature=").append(temperature)
      .append(", humidity=").append(humidity)
      .append(", hkr=").append(hkr)
      .append(", buttons=").append(buttons)
      .append(", levelControl=").append(levelControl)
      .append(", colorControl=").append(colorControl)
      .append(", etsiunitinfo=").append(etsiunitinfo)
      .append(", alert=").append(alert)
      .append(", blind=").append(blind)
      .append(", groupinfo=").append(groupinfo)
      .append(']');
    return builder.toString();
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
  public int compareTo(final Device obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = identifier.compareTo(obj.identifier);
    if (result == 0)
     {
      result = Long.compare(id, obj.id);
      if (result == 0)
       {
        // functionbitmask
        result = fwversion.compareTo(obj.fwversion);
        if (result == 0)
         {
          result = compareField(manufacturer, obj.manufacturer);
          if (result == 0)
           {
            result = compareField(productname, obj.productname);
            if (result == 0)
             {
              result = Boolean.compare(present, obj.present);
              if (result == 0)
               {
                result = Boolean.compare(txbusy, obj.txbusy);
                if (result == 0)
                 {
                  result = compareField(name, obj.name);
                  if (result == 0)
                   {
                    result = Boolean.compare(batterylow, obj.batterylow);
                    if (result == 0)
                     {
                      result = compareField(battery, obj.battery);
                      if (result == 0)
                       {
                        result = compareField(switchState, obj.switchState);
                        if (result == 0)
                         {
                          result = compareField(simpleonoff, obj.simpleonoff);
                          if (result == 0)
                           {
                            result = compareField(powermeter, obj.powermeter);
                            if (result == 0)
                             {
                              result = compareField(temperature, obj.temperature);
                              if (result == 0)
                               {
                                result = compareField(humidity, obj.humidity);
                                if (result == 0)
                                 {
                                  result = compareField(hkr, obj.hkr);
                                  if (result == 0)
                                   {
                                    // result = compareField(this.buttons, obj.buttons);
                                    if (result == 0)
                                     {
                                      result = compareField(levelControl, obj.levelControl);
                                      if (result == 0)
                                       {
                                        result = compareField(colorControl, obj.colorControl);
                                        if (result == 0)
                                         {
                                          result = compareField(etsiunitinfo, obj.etsiunitinfo);
                                          if (result == 0)
                                           {
                                            result = compareField(alert, obj.alert);
                                            if (result == 0)
                                             {
                                              result = compareField(blind, obj.blind);
                                              if (result == 0)
                                               {
                                                result = compareField(groupinfo, obj.groupinfo);
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
             }
           }
         }
       }
     }
    return result;
   }

 }
