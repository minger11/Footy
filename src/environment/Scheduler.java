package environment;

import java.util.Iterator;

import repast.simphony.engine.schedule.ScheduledMethod;

/** 
 * Schedules the operation of the simulation
 * @author user
 */

public class Scheduler {

	/**
	 * Variable used to keep track of the last step
	 */
	private long time;
	
	Scheduler(){
	}

	/**
	 * Called only once at the beginning of the simulation
	 */
	@ScheduledMethod(start=0)
	public void init(){
		
		//set the current time 
		time = System.currentTimeMillis();
		
		//iterate through each player
		Iterator<Object> it = Params.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			
			//initialize the player's senses, brain and reflexes
			Senses.sense(player);
			player.getBrain().init();
			Reflexes.react(player);
		}
		
		//have the referee start the game
		Referee.startGame();
	}
	
	/**
	 * Called immediately after the init method and recalled each time the previous step is finished
	 */
	@ScheduledMethod ( start = 1 , interval = 1)
	public void step(){
		
		//pauses the simulation if the scheduler delay has not pass since the last time step
		while(System.currentTimeMillis()<time+Params.schedulerDelay){
			//stall for the scheduled delay
		}
		
		//resets the time to be used for the next time step
		time = System.currentTimeMillis();
		
		//update the messageboard
		MessageBoard.update();
		
		//Iterates through each player
		Iterator<Object> it = Params.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			
			//Calls the step method on the player's senses, brain and reflexes
			Senses.sense(player);
			player.getBrain().step();
			Reflexes.react(player);
			
			//run a physics update on the player
			Physics.update(player);
		}
		
		//Iterates through each ball
		Iterator<Object> itra = Params.context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
			
			//run a physics update on the ball
			Physics.update(ball);
		}
		
		//run a physics update on the entire simulation (context)
		Physics.update(Params.context);
		
		//call the mover to move the players and balls
		Mover.movePlayers();
		Mover.moveBalls();
		
		//have the referee check the state of the game
		Referee.checkGame();
	}
}
