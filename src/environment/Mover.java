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
	
	void init(){
		
	}
	
	void step(){
		checkCollisions();
		movePlayers();
		moveBalls();	
	}
	
	/**
	 * Creates a new physics and send the entire context to physics to check for collisions
	 */
	void checkCollisions(){
		Physics physics = new Physics(context);
		physics.checkCollisions();
	}
	
	/**
	 * Iterates through all players and physically moves them and their heads based on their velocity
	 */
	void movePlayers(){
		
		//Iterate through players
		Iterator<Object> players = context.getObjects(Player.class).iterator();
		while (players.hasNext()){
			Player player = (Player)players.next();
			
			//Add the current velocity to the current positionVector
			//Set the heads position Vector to equal the players
			player.getPositionVector().add(player.getVelocity());
			player.getHead().setPositionVector(player.getPositionVector());
			
			//Move the player and head to their respective positionVectors
			player.moveToVector();
			player.getHead().moveToVector();
		}
	}

	/**
	 * Iterates through all balls and physically moves them based on their velocity
	 */
	void moveBalls(){
		
		//Iterate through balls
		Iterator<Object> balls = context.getObjects(Ball.class).iterator();
		while (balls.hasNext()){
			Ball ball = (Ball)balls.next();
			
			//Adds the current velocity to the positionVector
			ball.getPositionVector().add(ball.getVelocity());	
			ball.moveToVector();
		}
	}
}
