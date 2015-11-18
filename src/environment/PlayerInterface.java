package environment;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import brain.Brain;

public class PlayerInterface {

	protected Context<Object> context;
	protected ContinuousSpace<Object> space;
	protected Grid<Object> grid;
	protected Player player;
	protected Brain brain;
	protected Parameters params;
	
	PlayerInterface(Player player, Context context){
		this.context = context;
		grid = (Grid)context.getProjection("grid");
		space = (ContinuousSpace)context.getProjection("space");
		this.player = player;
		brain = player.getBrain();
		params = RunEnvironment.getInstance().getParameters();
	}
	
	protected void init(){
		
	}
	
	protected void step(){
		
	}
}
