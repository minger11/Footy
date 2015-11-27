package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class SimpleAgent {

	private NdPoint positionPoint;
	private Grid<Object> grid;
	private ContinuousSpace<Object> space;
	private Context<Object> context;
	private Parameters params;
	private Vector3d positionVector;
	
	SimpleAgent(Context context, int x, int y) {
		context.add(this);
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		positionVector = new Vector3d(x, y, 0.0);
		moveToVector();
		this.context = context;
		params = RunEnvironment.getInstance().getParameters();
	}
	
	Context<Object> getContext(){
		return context;
	}
	
	ContinuousSpace<Object> getSpace() {
		return space;
	}
	
	Grid<Object> getGrid(){
		return grid;
	}
	
	/**
	 * Moves the agent on the displays and updates the position variable
	 * @param vector
	 */
	void moveToVector(){
		
		//Physically moves the agent on the continuous space and grid
		space.moveTo(this, positionVector.getX(), positionVector.getY());
		grid.moveTo(this, (int)positionVector.getX(), (int)positionVector.getY());
		
		//Sets the new NdPoint as the currentPosition variable within the simpleagent superclass 
		positionPoint = space.getLocation(this);
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
