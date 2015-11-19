package environment;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import brain.Brain;

public class PlayerInterface extends ObjectInterface{


	protected Player player;
	protected Brain brain;
	
	PlayerInterface(Player player, Context context){
		super(context);
		this.player = player;
		brain = player.getBrain();
		
	}
	
	protected void init(){
		
	}
	
	protected void step(){
		
	}
}
