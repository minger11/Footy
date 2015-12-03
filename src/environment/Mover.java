package environment;

import java.util.Iterator;

/**
 * The mover physically moves movable objects in space after checking them for collisions
 * @author user
 *
 */

public final class Mover {

	private Mover(){
	}
	
	public static void init(){
	}
	
	public static void step(){
			movePlayers();
			moveBalls();	
	}
	
	/**
	 * Iterates through all players and physically moves them and their heads based on their velocity
	 */
	private static void movePlayers(){
		
		//Iterate through players
		Iterator<Object> players = Sim.context.getObjects(Player.class).iterator();
		while (players.hasNext()){
			Player player = (Player)players.next();
			//Add the current velocity to the current positionVector
			//Set the heads position Vector to equal the players
			player.getPositionVector().add(player.getVelocity());
			player.getHead().setPositionVector(player.getPositionVector());
			player.getArms().setPositionVector(player.getPositionVector());
			
			//Move the player and head to their respective positionVectors
			player.moveToVector();
			player.getHead().moveToVector();
			player.getArms().moveToVector();
		}
	}

	/**
	 * Iterates through all balls and physically moves them based on their velocity
	 */
	private static void moveBalls(){
		
		//Iterate through balls
		Iterator<Object> balls = Sim.context.getObjects(Ball.class).iterator();
		while (balls.hasNext()){
			Ball ball = (Ball)balls.next();
			
			//Adds the current velocity to the positionVector
			ball.getPositionVector().add(ball.getVelocity());	
			ball.moveToVector();
		}
	}
}
