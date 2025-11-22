/*
 * Copyright (C) 2024 Dipl.-Inform. Kai Hofmann. All rights reserved!
 */
package de.powerstat.fb.mini;


import java.util.Objects;

import de.powerstat.validation.interfaces.IValueObject;


/**
 *  Alert.
 */
public final class Alert implements Comparable<Alert>, IValueObject
 {
  /**
   * Alert state.
   */
  private final AlertState state;

  /**
   * Last alert change timestamp.
   */
  private final UnixTimestamp lastalertchgtimestamp;


  /**
   * Enum for handling of alert state.
   */
  public enum AlertState implements IValueObject
   {
    /**
     * No error.
     */
    NO_ERROR(0),

    /**
     * Barrier.
     */
    BARRIER(1),

    /**
     * Overheat.
     */
    OVERHEAT(2);


    /**
     * No error.
     */
    private static final String NO_ERROR_STR = "NO_ERROR";

    /**
     * Action number.
     */
    private final int action;


    /**
     * Ordinal constructor.
     *
     * @param action Action number
     */
   AlertState(final int action)
     {
      this.action = action;
     }


   /**
    * AlertState factory.
    *
    * @param value Enum name string
    * @return AlertState enum
    */
   public static AlertState of(final String value)
    {
     return AlertState.valueOf(value);
    }


   /**
    * AlertState factory.
    *
    * @param action Action number
    * @return AlertState enum
    */
   public static AlertState of(final int action)
    {
     for (final AlertState code : AlertState.values())
      {
       if (code.action == action)
        {
         return code;
        }
      }
     throw new IllegalArgumentException("action out of range");
    }


    /**
     * Get action number.
     *
     * @return Action number
     */
    public int getAction()
     {
      return action;
     }


    /**
     * Returns the value of this AlertState as a string.
     *
     * @return The text value represented by this object after conversion to type string.
     */
    @Override
    public String stringValue()
     {
      return this.name();
     }


    /**
     * Returns the string representation of this enum.
     *
     * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
     *
     * "NO_ERROR|BARRER|OVERHEAT"
     *
     * @return String representation of this enum (NO_ERROR, BARRIER, OVERHEAT).
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
     {
      return switch (action)
       {
       case 0 -> NO_ERROR_STR;
       case 1 -> "BARRIER"; //$NON-NLS-1$
       case 2 -> "OVERHEAT"; //$NON-NLS-1$
       default -> NO_ERROR_STR;
       };
     }

   }


  /**
   * Constructor.
   *
   * @param state Alert state
   * @param lastalertchgtimestamp Last alert change timestamp
   */
  private Alert(final AlertState state, final UnixTimestamp lastalertchgtimestamp)
   {
    super();
    this.state = state;
    this.lastalertchgtimestamp = lastalertchgtimestamp;
   }


  /**
   * Alert factory.
   *
   * @param state Alert state
   * @param lastalertchgtimestamp Last alert change timestamp
   * @return Alert object
   */
  public static Alert of(final AlertState state, final UnixTimestamp lastalertchgtimestamp)
   {
    return new Alert(state, lastalertchgtimestamp);
   }


  /**
   * Returns the value of this Alert as a string.
   *
   * @return The numeric value represented by this object after conversion to type string.
   */
  @Override
  public String stringValue()
   {
    return state.toString();
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
    return Objects.hash(state, lastalertchgtimestamp);
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
    if (!(obj instanceof final Alert other))
     {
      return false;
     }
    boolean result = (state == other.state);
    if (result)
     {
      result = lastalertchgtimestamp.equals(other.lastalertchgtimestamp);
     }
    return result;
   }


  /**
   * Returns the string representation of this Alert.
   *
   * The exact details of this representation are unspecified and subject to change, but the following may be regarded as typical:
   *
   * "Alert[state=, lastalertchgtimestamp=]"
   *
   * @return String representation of this Alert
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
   {
    final var builder = new StringBuilder(37);
    builder.append("Alert[state=").append(state).append(", lastalertchgtimestamp=").append(lastalertchgtimestamp).append(']'); //$NON-NLS-1$
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
  public int compareTo(final Alert obj)
   {
    Objects.requireNonNull(obj, "obj"); //$NON-NLS-1$
    int result = state.compareTo(obj.state);
    if (result == 0)
     {
      result = lastalertchgtimestamp.compareTo(obj.lastalertchgtimestamp);
     }
    return result;
   }

 }
