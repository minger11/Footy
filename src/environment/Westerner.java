package environment;

import repast.simphony.context.Context;

/**
 * Defenders attempt to stop attackers from scoring tries
 * @author user
 *
 */

public class Westerner extends Player{
	
	/**
	 * Constructor for Defender
	 * Sets the local params variable to be the parameters entered by the user in the runtime environment
	 * @param space - the ContinuousSpace projection the defender will operate in
	 * @param grid - the Grid projection the defender will operate in
	 */
	Westerner(Context context, int x, int y, int number){
		super(context, x, y, number);
	}

}