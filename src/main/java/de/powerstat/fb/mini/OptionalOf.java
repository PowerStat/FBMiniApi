/*
 * Copyright (C) 2024-2026 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini;


import java.util.NoSuchElementException;
import java.util.Objects;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.jmolecules.ddd.annotation.ValueObject;

import de.powerstat.ddd.interfaces.IValueObject;


/**
 * Optional of a specific type.
 *
 * @param <T> Use only value objects
 * @param value Value Object or null
 */
@ValueObject
public record OptionalOf<T extends IValueObject>(T value) // extends Hue & Saturation & TemperatureKelvin
 {
  /**
   * Constructor.
   *
   * @param value Value Object or null
   */
  public OptionalOf
   {
   }


  /**
   * Returns the value of the value object as an String.
   *
   * @return The string value represented by this object or "".
   */
  public String stringValue()
   {
    return (value == null) ? "" : value.stringValue();
   }


  /**
   * Returns the value of the value object as an int.
   *
   * @return The numeric value represented by this object after conversion to type int or -1.
   * @throws NumberFormatException If level does not contain a parsable int.
   */
  public int intValue()
   {
    return (value == null) ? -1 : Integer.parseInt(value.stringValue());
   }

 }
