package environment;

import java.util.Iterator;
import repast.simphony.context.Context;

/**
 * The mover physically moves movable objects in space after checking them for collisions
 * @author user
 *
 */

public class Mover {

	Context<Object> context;
	
	Mover(Context context){
		this.context = context;
	}
	
	public void init(){
		
	}
	
	public void step(){
		checkCollisions();
		movePlayers();
		moveBalls();	
	}
	
	/**
	 * Creates a new physics and send the entire context to physics to check for collisions
	 */
	public void checkCollisions(){
		Physics physics = new Physics(context);
		physics.checkCollisions();
	}
	
	/**
	 * Iterates through all players and physically moves them and their heads based on their velocity
	 */
	public void movePlayers(){
		
		//Iterate through players
		Iterator<Object> players = context.getObjects(Player.class).iterator();
		while (players.hasNext()){
			Player player = (Player)players.next();
			
			//Add the current velocity to the current positionVector
			//Set the heads position Vector to equal the players
			player.positionVector.add(player.velocity);
			player.head.positionVector = player.positionVector;
			
			//Move the player and head to their respective positionVectors
			player.moveToVector();
			player.head.moveToVector();
		}
	}

	/**
	 * Iterates through all balls and physically moves them based on their velocity
	 */
	public void moveBalls(){
		
		//Iterate through balls
		Iterator<Object> balls = context.getObjects(Ball.class).iterator();
		while (balls.hasNext()){
			Ball ball = (Ball)balls.next();
			
			//Adds the current velocity to the positionVector
			ball.positionVector.add(ball.velocity);	
			ball.moveToVector();
		}
	}
}
