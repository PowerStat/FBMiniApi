/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini.json;


/**
 * FB Metadata json support class.
 */
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
    return this.icon;
   }


  /**
   * Get type.
   *
   * @return Type
   */
  public String getType()
   {
    return this.type;
   }

 }
