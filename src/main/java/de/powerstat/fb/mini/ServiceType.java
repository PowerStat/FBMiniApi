/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.Objects;
import java.util.regex.Pattern;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Service type.
 *
 * TR64 URN service type.
 *
 * @param type Service type string
 */
@ValueObject
public record ServiceType(String type) implements Comparable<ServiceType>, IValueObject
 {
  /**
   * ServiceType regexp.
   *
   * urn:dslforum-org:service:[a-zA-Z0-9_-]+:[1-9]
   */
  private static final Pattern TYPE_REGEXP = Pattern.compile("^urn:dslforum-org:service:[a-zA-Z0-9_-]{1,37}:[1-9]$"); //$NON-NLS-1$


  /**
   * Constructor.
   *
   * @param type Service type string
   * @throws NullPointerException if type is null
   * @throws IllegalArgumentException if type is not a correct type
   */
  public ServiceType
   {
    Objects.requireNonNull(type, "type"); //$NON-NLS-1$
    if ((type.length() < 28) || (type.length() > 64))
     {
      throw new IllegalArgumentException("type with wrong length: " + type.length()); //$NON-NLS-1$
     }
    if (!TYPE_REGEXP.matcher(type).matches())
     {
      throw new IllegalArgumentException("type with wrong format"); //$NON-NLS-1$
     }
   }


  /**
   * ServiceType factory.
   *
   * @param type ServiceType string
   * @return ServiceType object
   */
  public static ServiceType of(final String type)
   {
    return new ServiceType(type);
   }


  /**
   * Returns the value of this ServiceType as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return type;
   }


  /**
   * Compare with another object.
   *
   * @param obj Object to compare with
   * @return 0: equal; 1: greater; -1: smaller
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(final ServiceType obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return type.compareTo(obj.type);
   }

 }
