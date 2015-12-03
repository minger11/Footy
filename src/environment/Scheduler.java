package environment;

import java.util.Iterator;

import repast.simphony.engine.schedule.ScheduledMethod;

/** 
 * Schedules the operation of the simulation
 * @author user
 *
 */

public class Scheduler {

	private long time;
	
	Scheduler(){
	}

	@ScheduledMethod(start=0)
	public void init(){
		time = System.currentTimeMillis();
		MessageBoard.init();
		Iterator<Object> it = Sim.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			Senses.init(player);
			player.getBrain().init();
			Reflexes.init(player);
		}
		Mover.init();
		Referee.init();
	}
	
	@ScheduledMethod ( start = 1 , interval = 1)
	public void step(){
		while(System.currentTimeMillis()<time+Sim.schedulerDelayInMilliseconds){
			//stall for ten thousandths of a second
		}
		time = System.currentTimeMillis();
		MessageBoard.step();
		Iterator<Object> it = Sim.context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			Senses.step(player);
			player.getBrain().step();
			Reflexes.step(player);
			Physics.update(player);
		}
		Iterator<Object> itra = Sim.context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
			Physics.update(ball);
		}
		Mover.step();
		Referee.step();
	}
}
