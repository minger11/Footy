package environment;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * head is used to setup the head
 * @author user
 *
 */

public class Head extends MovingAgent{

	Player player;
	
	Head(Context context, int x, int y, Player player){
		super(context, x, y);	
		this.player = player;
	}

}
