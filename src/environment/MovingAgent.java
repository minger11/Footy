package environment;

import javax.vecmath.Vector3d;

/**
 * Class to deal with moving agents
 * @author user
 *
 */

public class MovingAgent extends SimpleAgent{

	/**
	 * The velocity vector
	 */
	private Vector3d velocity;
	/**
	 * The rotation angle (in radians)
	 */
	private double rotation;
	/**
	 * The mass (in grams)
	 */
	private double mass;
	
	MovingAgent(double x, double y) {
		super(x, y);
		velocity = new Vector3d(0.0,0.0,0.0);
		rotation=.000;
		
		//Sets the mass based on the object
		if(this instanceof Player){
			mass = Params.playerWeight;
		} else if(this instanceof Ball){
			mass = Params.ballWeight;
		}
	}
	
	/**
	 * Sets the velocity
	 * @param x - the new velocity Vector3d
	 */
	void setVelocity(Vector3d x){
		velocity = x;
	}
	
	/**
	 * Simple getter
	 * @return the velocity Vector3d
	 */
	Vector3d getVelocity(){
		return velocity;
	}
	
	/**
	 * Sets the rotation
	 * @param x - the angle (in radians)
	 */
	void setRotation(double x){
		rotation = x;
	}
	
	/**
	 * Simple getter
	 * @return the rotation (in radians)
	 */
	double getRotation(){
		return rotation;
	}
	
	/**
	 * Simple getter
	 * @return the mass (in grams)
	 */
	double getMass(){
		return mass;
	}
	
}
