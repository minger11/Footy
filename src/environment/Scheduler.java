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
		MessageBoard.init();
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			player.init();
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
		MessageBoard.step();
		Iterator<Object> it = context.getObjects(Player.class).iterator();
		while(it.hasNext()){
			Player player = (Player)it.next();
			player.step();
			Physics physics = new Physics(player);
		}
		Iterator<Object> itra = context.getObjects(Ball.class).iterator();
		while(itra.hasNext()){
			Ball ball = (Ball)itra.next();
			Physics physics = new Physics(ball);
		}
		Mover.step();
		Referee.step();
	}
}
