package org.vermin.mudlib.outworld;

import org.vermin.mudlib.*;

public interface OutworldRoomFactory {

	public enum ExitStrategy {
		/**
		 * Make all exits to passable rooms.
		 */
		ALL,
		
		/**
		 * Do not make diagonal exits
		 */
		NO_DIAGONAL,
		
		/**
		 * Make only the diagonal exits
		 */
		ONLY_DIAGONAL,
		
		/**
		 * Make diagonal exits when adjacent rooms are passable
		 */
		STRICT_DIAGONAL,
		
		/**
		 * Make diagonal exits when atleast one adjacent room is passable
		 */
		CORNER_DIAGONAL,
		
		/**
		 * Do not make any exits.
		 * This only makes sense if there are manually added exits
		 * for locations with this strategy.
		 */
		NONE
	};
	
	public Area getMainArea(OutworldLoader loader);
	
	public Room createRoom(OutworldLoader loader, int x, int y, byte[] type);

	public boolean isPassable(byte[] type);
	
	public String getLegend(byte[] type);

	/* Return the map legend as
	 * legend[n][map char, description]
	 */
	public String[][] getMapLegend();

	public boolean needToFly(byte[] type);
	public boolean isUnderwater(byte[] type);
	public ExitStrategy getExitStrategy(byte[] type);
	
}
