package environment;

import repast.simphony.context.Context;

/**
 * Attackers try to get past defenders and score tries
 * @author user
 *
 */

public class Attacker extends Player{
	
	/**
	 * The constructor for the Attacker
	 * inits the parameters for space, grid and params
	 * @param space - the space projection
	 * @param grid - the grid projection
	 */
	Attacker(Context context, int x, int y, int number) {
		super(context, x, y, number);
	}
	
}

