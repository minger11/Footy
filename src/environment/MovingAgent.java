package environment;

import javax.vecmath.Vector3d;

/**
 * Class to deal with moving agents
 * @author user
 *
 */

public class MovingAgent extends SimpleAgent{

	private Vector3d velocity;
	private double rotation;
	private double mass;
	
	MovingAgent(double x, double y) {
		super(x, y);
		velocity = new Vector3d(0.0,0.0,0.0);
		rotation=.000;
		if(this instanceof Player){
			mass = Sim.playerWeight;
		} else if(this instanceof Ball){
			mass = Sim.ballWeight;
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
