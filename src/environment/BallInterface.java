package environment;

import repast.simphony.context.Context;

public class BallInterface extends ObjectInterface{
	
	protected Ball ball;

	BallInterface(Context context, Ball ball){
		super(context);
		this.ball = ball;
	}
}
