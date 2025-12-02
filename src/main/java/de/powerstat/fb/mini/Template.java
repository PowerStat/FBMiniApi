/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Template.
 */
@ValueObject
public final class Template implements Comparable<Template>, IValueObject
 {
  /**
   * Template AIN.
   */
  private final AIN identifier;

  /**
   * Internal template id.
   */
  private final long id;

  /**
   * Function bit mask.
   */
  private final EnumSet<Functions> functionbitmask;

  /**
   * Automatically created.
   */
  private final boolean autocreate;

  /**
   * Apply mask bit field.
   */
  private final long applymaskField;

  /**
   * Template name.
   */
  private final String name;

  /**
   * Meta data.
   */
  private final Metadata metadata;

  /**
   * Device reference list.
   */
  private final List<AIN> devices;

  /**
   * Trigger reference list.
   */
  private final List<AIN> triggers;

  /**
   * Sub-templates list reference list.
   */
  private final List<AIN> subtemplates;

  /**
   * Apply mask.
   */
  private final EnumSet<ApplyMask> applymask;


  /**
   * Constructor.
   *
   * @param identifier Template AIN
   * @param id Internal template id
   * @param functionbitmask Function mask, null is the same as EnumSet.noneOf(Functions.class)
   * @param autocreate Auto created on true; false otherwise
   * @param applymaskField Apply mask bit field?
   * @param name Template name
   * @param metadata Meta data
   * @param devices Device reference list, null is the same as an empty list
   * @param triggers Triggers reference list, null is the same as an empty list
   * @param subtemplates Sub-templates reference list, null is the same as an empty list
   * @param applymask Apply mask, null is the same as EnumSet.noneOf(ApplyMask.class)
   * @throws NullPointerException If identifier, name, metadata is null
   * @throws IllegalArgumentException If id &lt; 0, applymaskField &lt; 0, name.length &lt; 1 or &gt; 40
   */
  private Template(final AIN identifier, final long id, final EnumSet<Functions> functionbitmask, final boolean autocreate, final long applymaskField, final String name, final Metadata metadata, final List<AIN> devices, final List<AIN> triggers, final List<AIN> subtemplates, final EnumSet<ApplyMask> applymask)
   {
    super();
    Objects.requireNonNull(identifier, "identifier"); //$NON-NLS-1$
    if (id < 0)
     {
      throw new IllegalArgumentException("id must be >= 0");
     }
    if (applymaskField < 0)
     {
      throw new IllegalArgumentException("applymaskField must be >= 0");
     }
    Objects.requireNonNull(name, "name"); //$NON-NLS-1$
    if (name.isEmpty() || (name.length() > 40))
     {
      throw new IllegalArgumentException("name is smaller than 1 or larger than 40 characters");
     }
    // TODO regexp on name
    Objects.requireNonNull(metadata, "metadata"); //$NON-NLS-1$
    this.identifier = identifier;
    this.id = id;
    this.functionbitmask = (functionbitmask == null) ? EnumSet.noneOf(Functions.class) : functionbitmask;
    this.autocreate = autocreate;
    this.applymaskField = applymaskField;
    this.name = name;
    this.metadata = metadata;
    this.devices = (devices == null) ? new ArrayList<>() : List.copyOf(devices);
    this.triggers = (triggers == null) ? new ArrayList<>() : List.copyOf(triggers);
    this.subtemplates = (subtemplates == null) ? new ArrayList<>() : List.copyOf(subtemplates);
    this.applymask = (applymask == null) ? EnumSet.noneOf(ApplyMask.class) : applymask;
   }


  /**
   * Template factory.
   *
   * @param identifier Template AIN
   * @param id Internal template id
   * @param functionbitmask Function mask, null is the same as EnumSet.noneOf(Functions.class)
   * @param autocreate Auto created on true; false otherwise
   * @param applymaskField Apply mask bit field?
   * @param name Template name
   * @param metadata Meta data
   * @param devices Device reference list, null is the same as an empty list
   * @param triggers Triggers reference list, null is the same as an empty list
   * @param subtemplates Sub-templates reference list, null is the same as an empty list
   * @param applymask Apply mask, null is the same as EnumSet.noneOf(ApplyMask.class)
   * @return Template object
   * @throws NullPointerException If identifier, name, metadata is null
   * @throws IllegalArgumentException If id &lt; 0, applymaskField &lt; 0, name.length &lt; 1 or &gt; 40
   */
  public static Template of(final AIN identifier, final long id, final EnumSet<Functions> functionbitmask, final boolean autocreate, final long applymaskField, final String name, final Metadata metadata, final List<AIN> devices, final List<AIN> triggers, final List<AIN> subtemplates, final EnumSet<ApplyMask> applymask)
   {
    return new Template(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
   }


  /**
   * Returns the value of the templates identifier as a string.
   *
   * @return The identifier represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return identifier.stringValue();
   }


  /**
   * Returns the identifier of this template.
   *
   * @return The identifier represented by this object.
   */
  public AIN getIdentifier()
   {
    return identifier;
   }


  /**
   * Returns the id of this template.
   *
   * @return The id represented by this object.
   */
  public long getId()
   {
    return id;
   }


  /**
   * Returns the functionbitmask of this template.
   *
   * @return The functionbitmask represented by this object.
   */
  public EnumSet<Functions> getFunctionbitmask()
   {
    return functionbitmask;
   }


  /**
   * Returns the autocreate of this template.
   *
   * @return The autocreate represented by this object.
   */
  public boolean getAutocreate()
   {
    return autocreate;
   }


  /**
   * Returns the applymaskField of this template.
   *
   * @return The applymaskField represented by this object.
   */
  public long getApplymaskField()
   {
    return applymaskField;
   }


  /**
   * Returns the name of this template.
   *
   * @return The name represented by this object.
   */
  public String getName()
   {
    return name;
   }


  /**
   * Returns the metadata of this template.
   *
   * @return The metadata represented by this object.
   */
  public Metadata getMetadata()
   {
    return metadata;
   }


  /**
   * Returns the devices of this template.
   *
   * @return The devices represented by this object.
   */
  public List<AIN> getDevices()
   {
    return devices;
   }


  /**
   * Returns the triggers of this template.
   *
   * @return The triggers represented by this object.
   */
  public List<AIN> getTriggers()
   {
    return triggers;
   }


  /**
   * Returns the subtemplates of this template.
   *
   * @return The subtemplates represented by this object.
   */
  public List<AIN> getSubtemplates()
   {
    return subtemplates;
   }


  /**
   * Returns the applymask of this template.
   *
   * @return The applymask represented by this object.
   */
  public EnumSet<ApplyMask> getApplymask()
   {
    return applymask;
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
    return Objects.hash(identifier, id, functionbitmask, autocreate, applymaskField, name, metadata, devices, triggers, subtemplates, applymask);
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
    if (!(obj instanceof final Template other))
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
          result = (autocreate == other.autocreate);
          if (result)
           {
            result = (applymaskField == other.applymaskField);
            if (result)
             {
              result = name.equals(other.name);
              if (result)
               {
                result = metadata.equals(other.metadata);
                if (result)
                 {
                  result = devices.equals(other.devices);
                  if (result)
                   {
                    result = triggers.equals(other.triggers);
                    if (result)
                     {
                      result = subtemplates.equals(other.subtemplates);
                      if (result)
                       {
                        result = applymask.equals(other.applymask);
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
   * Returns the string representation of this Template.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Template[]"
   *
   * @return String representation of this Template
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(140);
    builder.append("Template[identifier=").append(identifier)
      .append(", id=").append(id)
      .append(", functionbitmask=").append(functionbitmask)
      .append(", autocreate=").append(autocreate)
      .append(", applymaskField=").append(applymaskField)
      .append(", name=").append(name)
      .append(", metadata=").append(metadata)
      .append(", devices=").append(devices)
      .append(", triggers=").append(triggers)
      .append(", subtemplates=").append(subtemplates)
      .append(", applymask=").append(applymask)
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
  public int compareTo(final Template obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return identifier.compareTo(obj.identifier);
   }

 }
