package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;

/**
 * Class to deal with moving agents
 * @author user
 *
 */

public class MovingAgent extends SimpleAgent{

	private Vector3d velocity;
	private double rotation;
	private double mass;
	
	MovingAgent(Context context, int x, int y) {
		super(context, x, y);
		velocity = new Vector3d(0.0,0.0,0.0);
		rotation=.000;
		if(this instanceof Player){
			mass = (Integer)RunEnvironment.getInstance().getParameters().getValue("body_weight");
		} else if(this instanceof Ball){
			mass = (Integer)RunEnvironment.getInstance().getParameters().getValue("ball_weight");
		}
	}
	
	void setVelocity(Vector3d x){
		velocity = x;
	}
	
	Vector3d getVelocity(){
		return velocity;
	}
	
	void setRotation(double x){
		rotation = x;
	}
	
	double getRotation(){
		return rotation;
	}
	
	double getMass(){
		return mass;
	}
	
}
