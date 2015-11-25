package environment;

import javax.vecmath.Vector3d;
import repast.simphony.context.Context;

/**
 * Class to deal with moving agents
 * @author user
 *
 */

public class MovingAgent extends SimpleAgent{

	//What does a moving agent have that simpleagents do not have?
	//mass?
	//energy?
	protected Vector3d velocity;
	protected double rotation;
	
	MovingAgent(Context context, int x, int y) {
		super(context, x, y);
		velocity = new Vector3d(0.0,0.0,0.0);
		rotation=.000;
	}
	
}
