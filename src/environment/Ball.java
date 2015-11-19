package environment;

import repast.simphony.context.Context;

public class Ball extends SimpleAgent{
	
	protected BallMovement movement;
	
	Ball(Context context, int x, int y){
		super(context, x, y);
		movement = new BallMovement(context, this);
	}
}
