package org.vermin.mudlib;

import java.util.*;

public interface Party {

	/**
	 * Returns the party members in the order of their joining.
	 *
	 * @return a list of party members
	 */
	public List<Living> membersInJoinOrder();

	/**
	 * Returns the battle position of the given member.
	 * The first value in the array is the row and the
	 * second value is the column.
	 *
	 * If the member is not in this party, null is returned.
	 */
	public int[] getPosition(Living member);

	/**
	 * Returns the current duration of the party.
	 *
	 * @returns party duration in milliseconds
	 */
	public long getDuration();

	/**
	 * Returns the amount of total experience gained by this party.
	 *
	 * @returns the amount of experience
	 */
	public long getTotalExperience();

	/**
	 * Set the party leader.
	 *
	 * @param leader the new leader
	 */
	public void setLeader(Living leader);

	/**
	 * Get the sum of the levels of party members.
	 *
	 * @param total level of the party
	 */
	public int getTotalLevel();
		
	/**
	 * Add experience when this party kills something.
	 * The experience is split between members according
	 * to their level.
	 * If the room parameter is not null, the experience
	 * is awarded only to those members, who are
	 * in the given room.
	 *
	 * @param exp the amount of experience gained
	 * @param room the room the experience was gained in or null
	 */
	public void addExperience(long exp, Room room);

	/**
	 * Get the share of the given member as a value between 0.0 - 1.0.
	 */
	public double getShare(Living m);

	/**
	 * Returns the current party leader.
	 *
	 * @returns the leader
	 */
	public Living getLeader();

	/**
	 * Add a member to this party.
	 * Also sets the battle group and the party of the new member.
	 *
	 * @param member the new member
	 */
	public void addMember(Living member);

	/**
	 * Remove a member from this party.
	 * Also sets the removed member's battle group to
	 * a single battle group and sets the leaving
	 * member's party to null.
	 *
	 * @param member the member to remove
	 */
	public void removeMember(Living member);

	/**
	 * Returns an unordered collection of the members.
	 *
	 * @returns a collection of party members
	 */
	public Collection<Living> members();
	
	/**
	 * Returns the current amount of party members.
	 *
	 * @return the member count > 0
	 */
	public int getMemberCount();

	/**
	 * Check if the someone is a member of this party.
	 *
	 * @param member the member candidate
	 * @returns true if candidate is a member, false otherwise
	 */
	public boolean isMember(Living member);

	/**
	 * Returns the current party name.
	 *
	 * @returns the party name
	 */
	public String getPartyName();

	/**
	 * Set a new party name.
	 *
	 * @param name the new name
	 */
	public void setPartyName(String name);
	
	/**
	 * Toggle the `follow leader' flag for a given member.
	 *
	 * @param member the party member
	 */
	public void toggleFollow(Living member);

	/**
	 * Returns the `follow leader' flag for a given member.
	 *
	 * @param member the party member
	 * @returns true if the member is following leader, false otherwise
	 */
	public boolean isFollowing(Living member);

	/** 
	 * Change party member's position to the given row and column.
	 * This method must take into account any party positioning rules and
	 * notify the party of the movements.
	 *
	 * @param actor the actor who is doing the moving
	 * @param member the member to move
	 * @param row the requested row
	 * @param col the requested column
	 */
	public void setPlace(Living actor, Living member, int row, int col);

	/**
	 * Notice a message to all party members.
	 */
	public void notice(String msg);

	/**
	 * Notice all except the One.
	 * 
	 * @param one  the One
	 */
	public void notice(String msg, Living one);
	
	/**
	 * Add a kill to the party's list of vanquished creatures.
	 *
	 * @param killed the killed creature
	 */
	public void addKill(Living killed);

	/**
	 * Returns a list of last kills in chronological order, newest last.
	 */
	public ArrayList<String> lastKills();

	/**
	 * Returns the experience gained by the given member during this party.
	 */
	public long getExperienceGained(Living member);
	
	/**
	 * Returns the last messages for this party's channel.
	 */
	public Iterable<String> getLastMessages();
	
	/**
	 * Say something to all members.
	 * Adds the message to the party channel.
	 */
	public void say(Living actor, String msg);
	
}
