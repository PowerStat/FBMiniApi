/*
 * Copyright (C) 2024-2025 Dipl.-Inform. Kai Hofmann. All rights reserved!
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements; and to You under the Apache License, Version 2.0.
 */
package de.powerstat.fb.mini.json;


import org.jmolecules.ddd.annotation.ValueObject;


/**
 * FB Metadata json support class.
 */
@ValueObject
public final class FBMetadata
 {
  /**
   * Icon number.
   */
  private int icon;

  /**
   * Scenario type string.
   */
  private String type;


  /**
   * Default constructor.
   */
  public FBMetadata()
   {
    super();
    type = "";
   }


  /**
   * Constructor.
   *
   * @param icon Icon number
   * @param type Type
   */
  public FBMetadata(final int icon, final String type)
   {
    super();
    this.icon = icon;
    this.type = type;
   }


  /**
   * Get icon.
   *
   * @return Icon
   */
  public int getIcon()
   {
    return icon;
   }


  /**
   * Get type.
   *
   * @return Type
   */
  public String getType()
   {
    return type;
   }

 }
