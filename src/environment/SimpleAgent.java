package environment;

import javax.vecmath.Vector3d;

import repast.simphony.space.continuous.NdPoint;

/**
 * The most basic form of agents used in the simulation. Every agent is a simple agent.
 * @author user
 *
 */

public class SimpleAgent {

	/**
	 * The current position of the agent. This is only updated at the end of each step by the mover.
	 */
	private NdPoint positionPoint;
	/**
	 * The current position of the agent. This is updated (potentially multiple times) during each step by physics before being used by the mover to update the positionPoint.
	 */
	private Vector3d positionVector;
	
	SimpleAgent(double x, double y) {
		Params.context.add(this);
		positionVector = new Vector3d(x, y, 0.0);
		moveToVector();
	}

	
	/**
	 * Moves the agent on the display and updates the positionPoint
	 */
	void moveToVector(){
		
		//Physically moves the agent on the continuous space
		Params.space.moveTo(this, positionVector.getX(), positionVector.getY());
		
		//Sets the new NdPoint as the currentPosition variable within the simpleagent superclass 
		positionPoint = Params.space.getLocation(this);
	}
	
	/**
	 * Simple getter
	 * @return the agents current position as a Vector
	 */
	Vector3d getPositionVector(){
		return positionVector;
	}
	
	/**
	 * Simple setter
	 * @param vector - the new position vector
	 */
	void setPositionVector(Vector3d vector){
		positionVector = vector;
	}
	
	/**
	 * Simple getter
	 * @return the agents current position as an NDPoint
	 */
	NdPoint getPositionPoint(){
		return positionPoint;
	}
	
	/**
	 * Simple setter 
	 * @param x - the new position point
	 */
	void setPositionPoint(NdPoint x){
		positionPoint = x;
	}
	
}
