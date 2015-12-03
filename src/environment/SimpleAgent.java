package environment;

import javax.vecmath.Vector3d;

import repast.simphony.space.continuous.NdPoint;

public class SimpleAgent {

	private NdPoint positionPoint;
	private Vector3d positionVector;
	
	SimpleAgent(double x, double y) {
		Sim.context.add(this);
		positionVector = new Vector3d(x, y, 0.0);
		moveToVector();
	}

	
	/**
	 * Moves the agent on the displays and updates the position variable
	 * @param vector
	 */
	void moveToVector(){
		//Physically moves the agent on the continuous space and grid
		Sim.space.moveTo(this, positionVector.getX(), positionVector.getY());
		//Sets the new NdPoint as the currentPosition variable within the simpleagent superclass 
		positionPoint = Sim.space.getLocation(this);
	}
	
	Vector3d getPositionVector(){
		return positionVector;
	}
	
	void setPositionVector(Vector3d vector){
		positionVector = vector;
	}
	
	NdPoint getPositionPoint(){
		return positionPoint;
	}
	
	void setPositionPoint(NdPoint x){
		positionPoint = x;
	}
	
}
