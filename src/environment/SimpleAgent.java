package environment;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class SimpleAgent {

	protected NdPoint currentPosition;
	protected Grid<Object> grid;
	protected ContinuousSpace<Object> space;
	protected Context<Object> context;
	protected Parameters params;
	
	SimpleAgent(Context context, int x, int y) {
		context.add(this);
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		grid.moveTo(this, x, y); 
		space.moveTo(this, x, y); 
		currentPosition = space.getLocation(this);
		this.context = context;
		params = RunEnvironment.getInstance().getParameters();
	}
	
	protected Context<Object> getContext(){
		return context;
	}
	
	protected ContinuousSpace<Object> getSpace() {
		return space;
	}
	
	protected NdPoint getPosition(){
		return currentPosition;
	}
	
	protected void setPosition(NdPoint x){
		currentPosition = x;
	}
	
}
