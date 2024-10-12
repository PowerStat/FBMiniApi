/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;
import java.util.regex.Pattern;

import de.powerstat.validation.interfaces.IValueObject;


/**
 * Service type.
 *
 * TR64 URN service type.
 */
public final class ServiceType implements Comparable<ServiceType>, IValueObject
 {
  /**
   * ServiceType regexp.
   *
   * urn:dslforum-org:service:[a-zA-Z0-9_-]+:[1-9]
   */
  private static final Pattern TYPE_REGEXP = Pattern.compile("^urn:dslforum-org:service:[a-zA-Z0-9_-]{1,37}:[1-9]$"); //$NON-NLS-1$

  /**
   * Service type.
   */
  private final String serviceType;


  /**
   * Constructor.
   *
   * @param type Service type string
   * @throws NullPointerException if type is null
   * @throws IllegalArgumentException if type is not a correct type
   */
  private ServiceType(final String type)
   {
    super();
    Objects.requireNonNull(type, "type"); //$NON-NLS-1$
    if ((type.length() < 28) || (type.length() > 64))
     {
      throw new IllegalArgumentException("type with wrong length: " + type.length()); //$NON-NLS-1$
     }
    if (!TYPE_REGEXP.matcher(type).matches())
     {
      throw new IllegalArgumentException("type with wrong format"); //$NON-NLS-1$
     }
    this.serviceType = type;
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
    return this.serviceType;
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
    return this.serviceType.hashCode();
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
    if (!(obj instanceof final ServiceType other))
     {
      return false;
     }
    return this.serviceType.equals(other.serviceType);
   }


  /**
   * Returns the string representation of this ServiceType.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "ServiceType[serviceType=urn:dslforum-org:service:DeviceConfig:1]"
   *
   * @return String representation of this ServiceType
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder();
    builder.append("ServiceType[serviceType=").append(this.serviceType).append(']'); //$NON-NLS-1$
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
  public int compareTo(final ServiceType obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    return this.serviceType.compareTo(obj.serviceType);
   }

 }
