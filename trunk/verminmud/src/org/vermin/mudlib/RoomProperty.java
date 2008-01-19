/*
 * Created on 30.1.2005
 *
 */
package org.vermin.mudlib;

/**
 * Enumerates possible properties that <code>Room</code>s can have.
 * 
 * @author tadex
 *
 */
public enum RoomProperty {
    
    /**
     * This room is indoors.
     */
    INDOORS,
    
    /**
     * Teleportation to and from this room is forbidden.
     */
    NO_TELEPORT,
    
    /**
     * Requires aviation. The room has no solid floor.
     */
    REQUIRES_AVIATION,
    
    /**
     * Magic has no effect in this room.
     * No magical spells can be cast in this room, but spells
     * cast from other rooms may still affect people in this room
     * (eg. summon).
     */
    DEAD_MAGIC,
    
    /**
     * Divine spells have no effect in this room.
     */
    JUMALAN_SELAN_TAKANA,
    
    /**
     * It is raining.
     */
    RAINS,
    
    /**
     * The sun is above the horizon.
     */
    DAY
}
