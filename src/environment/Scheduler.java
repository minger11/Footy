package environment;

import java.util.Iterator;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;

/** 
 * Schedules the operation of the simulation
 * @author user
 *
 */

public class Scheduler {

	private Context<Object> context;
	private long time;
	private int delay=10;
	
	Scheduler(Context<Object> context){
		this.context = context;
	}

	@ScheduledMethod(start=0)
	public void init(){
		time = System.currentTimeMillis();
		MessageBoard.init();
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			Senses.init(player);
			player.getBrain().init();
			Reflexes.init(player);
		}
		Iterator<Object> itra = context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
		}
		Mover.init(context);
		Referee.init(context);
	}
	
	@ScheduledMethod ( start = 1 , interval = 1)
	public void step(){
		while(System.currentTimeMillis()<time+delay){
			//stall for ten thousandths of a second
		}
		time = System.currentTimeMillis();
		MessageBoard.step();
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			Senses.step(player);
			player.getBrain().step();
			Reflexes.step(player);
			Physics.update(player);
		}
		Iterator<Object> itra = context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
			Physics.update(ball);
		}
		Mover.step();
		Referee.step();
	}
}
