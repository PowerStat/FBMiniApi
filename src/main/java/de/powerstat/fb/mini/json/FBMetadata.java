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
public record FBMetadata(int icon, String type)
 {
  /**
   * Default constructor.
   */
  public FBMetadata()
   {
    this(0, "");
   }


  /**
   * Constructor.
   *
   * @param icon Icon number
   * @param type Type
   */
  public FBMetadata
   {
   }

 }
