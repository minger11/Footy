package environment;

import java.util.Iterator;

import repast.simphony.context.Context;

public class Mover {

	Context<Object> context;
	
	Mover(Context context){
		this.context = context;
	}
	
	public void init(){
		
	}
	
	public void step(){
		Iterator<Object> players = context.getObjects(Player.class).iterator();
		while (players.hasNext()){
			Player player = (Player)players.next();
			player.moveToVector();
			player.head.moveToVector();
		}
		Iterator<Object> balls = context.getObjects(Ball.class).iterator();
		while (balls.hasNext()){
			Ball ball = (Ball)balls.next();
			ball.moveToVector();
		}
	}
}
