package environment;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class ObjectInterface {

	protected Context<Object> context;
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected Parameters params;
	
	ObjectInterface(Context context){
		this.context = context;
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		params = RunEnvironment.getInstance().getParameters();
	}
}
