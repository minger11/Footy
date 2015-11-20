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
	
	Scheduler(Context<Object> context){
		this.context = context;
	}

	@ScheduledMethod(start=0)
	public void init(){
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			player.init();
		}
		Iterator<Object> iter = context.getObjects(Referee.class).iterator();
		while(iter.hasNext()){
			Referee ref = (Referee)iter.next();
			ref.init();
		}
		Iterator<Object> itra = context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
			ball.init();
		}
	}
	
	@ScheduledMethod ( start = 1 , interval = 1000)
	public void step(){
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			player.step();
		}
		Iterator<Object> iter = context.getObjects(Referee.class).iterator();
		while(iter.hasNext()){
			Referee ref = (Referee)iter.next();
			ref.step();
		}
		Iterator<Object> itra = context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
			ball.step();
		}
	}
}
