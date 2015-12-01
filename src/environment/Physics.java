package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

/**
 * A class representing physical laws.
 * Potentially have various copies of this class 
 * One copy could be used by the model
 * Other copies could be used by players to represent their interpretation of physical laws
 * Would need to use a level of truth parameter in the constructor (overload) for subjective copies
 * Could also include uncertainty in knowledge of own ability and knowledge of opponents ability to represent this
 * Movements should be passed here first, could also pass movements to a MODERATOR type class
 * @author user
 *
 */

public final class Physics {

	private static double timeScale = .1;
	private static double acceleration = 1;
	private static double maxForwardSpeed = 7;
	private static double maxBackwardSpeed = 5;
	private static double maxSideWaySpeed = 4;
	private static double ballMaxSpeed = 14;
	private static Parameters params = RunEnvironment.getInstance().getParameters();
		
		
	private Physics(){
		
	}
	
	public static void update(Context context){
		checkCollisions(context);
	}
	
	public static void update(Ball ball){
		updateVelocity(ball);
		updateRotation(ball);
	}
	
	public static void update(Player player){
		sendMessage(player);
		ballHandling(player);
		
		updateRotation(player);
		updateVelocity(player);
	}
	
	/**
	 * Adds a string message to the messageBoard only if the current player has no pending messages
	 * @param message - the string to be posted
	 */
	private static void sendMessage(Player player){
		//If the current message is not null
		if(player.getMovement().getMessage()!=null){
			
			//Initialize a bool to indicate whether the player has no pending messages on the messageboard
			boolean noPendingMessage = true;
			
			//Iterate through the pending messages on the messageboard
			Iterator<Message> it = MessageBoard.getPending().iterator();
			while(it.hasNext()){
				
				//If the player has a pending message set the bool to false
				if(it.next().getSender().equals(player)){
					noPendingMessage = false;
				}
			}
			
			//Add the message to the message board only if the player has no pending messages
			if(noPendingMessage)MessageBoard.addMessage(player, player.getMovement().getMessage());
		}
	}
	
	private static void ballHandling(Player player){
		Iterator<Object> it = player.getContext().getObjects(Ball.class).iterator();
		Ball ball;
		if(it.hasNext()){
			ball = (Ball)it.next();
				Vector3d vectorToBall = Utils.getVector(ball.getPositionVector(), player.getPositionVector());
				//Player has ball
				if((Math.abs(vectorToBall.length())<=((Integer)params.getValue("body_radius")+(Integer)params.getValue("ball_radius")))){
					if((!(ball.getPlayer()!=null))||(ball.getPlayer().equals(player))){
							//Player wants to carry ball, balls player is current player
							if(player.getMovement().getPassEffort().equals(player.getMovement().getEffort())){
								ball.setPlayer(player);
								ball.getMovement().setEffort(player.getMovement().getPassEffort());
								ball.getMovement().setTurn(player.getRotation());
							} 
							//Player wants to move the ball, balls player is now null
							else {
								ball.setPlayer(null);
								ball.getMovement().setEffort(player.getMovement().getPassEffort());
							}
					}
				}
		}
	}
	
	private static void updateRotation(Player player){
		player.getHead().setRotation(player.getMovement().getHeadTurn());
		player.setRotation(player.getMovement().getTurn());
	}
	
	private static void updateVelocity(Player player){
		player.getVelocity().scale(.99);
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = player.getMovement().getEffort();
		//Represents the difference between the desired and current positions
		//velocityUpdate.sub(desiredPosition, currentPosition);

		
		//double predAcceleration = (Double)param.getValue("predAcceleration");
		//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
		velocityUpdate.scale(acceleration * timeScale);
		// Apply the update to the velocity
		player.getVelocity().add(velocityUpdate);
		
		// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
		if (player.getVelocity().length() > maxForwardSpeed ){
			player.getVelocity().normalize();
			player.getVelocity().scale(maxForwardSpeed);
			player.getVelocity().scale(timeScale);
		}
		
		player.getHead().setVelocity(player.getVelocity());
		
	}
	
	/**
	 * Checks for a boundary collision based on an agent, their radius
	 * Adds the collision to the passed collisionlist
	 * @param agent - the agent to check for collision
	 * @param radius - the radius of the agent
	 * @param collisionList - the list of collisions to add any collisions to
	 */
	private static void checkBoundaryCollision(MovingAgent agent, int radius, List<Collision> collisionList){
		if(agent.getPositionVector().y+radius>=(Integer)params.getValue("display_height")||
				agent.getPositionVector().y-radius<=0||
						agent.getPositionVector().x+radius>=(Integer)params.getValue("display_width")||
								agent.getPositionVector().x-radius<=0){
			
			Collision coll = new Collision(agent);
			collisionList.add(coll);
		}
	}
	
	/**
	 * Checks for collisions in the model
	 */
	private static void checkCollisions(Context context){
		
		//Create a list of collisions
		List<Collision> collisionList = new ArrayList<Collision>();
		
		//Iterate through the players and assign each player to variable player1
		Iterator<Object> players1 = context.getObjects(Player.class).iterator();
		while (players1.hasNext()){
			Player player1 = (Player)players1.next();
			
			//Check collisions with the boundary
			checkBoundaryCollision(player1, (Integer)params.getValue("body_radius"), collisionList);
		
			//Iterate through the balls
			Iterator<Object> balls = context.getObjects(Ball.class).iterator();
			while (balls.hasNext()){
				Ball ball = (Ball)balls.next();
				
				//Get the distance between the ball and the player
				Vector3d vector = new Vector3d();
				vector.sub(player1.getPositionVector(), ball.getPositionVector());
				double distance = Math.abs(vector.length());
				
				//If the distance is too short a collision has occurred
				if(distance<((Integer)params.getValue("body_radius")+(Integer)params.getValue("ball_radius"))){
					
					/**
					//If the ball has no current player, the ball has collided with the player
					if(ball.getLastPlayer()!=null){
						if(ball.getPlayer()==null&&ball.getLastPlayer()!=player1){
							System.out.println("collision");
							Collision collision = new Collision(player1, ball);
							collisionList.add(collision);
						}
					}*/
				}
			}
			
			//Iterate again through the players and assign each player to variable player2
			Iterator<Object> players2 = context.getObjects(Player.class).iterator();
			while (players2.hasNext()){
				Player player2 = (Player)players2.next();
				
				//Iterate through collisions and set the already collided to true if the collision has already taken place
				boolean alreadyCollided = false;
				Iterator<Collision> collisions = collisionList.iterator();
				while (collisions.hasNext()){
					Collision collide = collisions.next();
					if(collide.getAgent1().equals(player2)&&collide.getAgent2().equals(player1)){
						alreadyCollided = true;
					}
				}
				
				//If the collision has not already been processed
				if(!alreadyCollided){
					
					//Provided the two players are not the same player
					if(player1!=player2){
						Vector3d vector = new Vector3d();
						
						//Get the distance between the two players
						vector.sub(player1.getPositionVector(), player2.getPositionVector());
						double distance = Math.abs(vector.length());
						
						//If the distance is too short a collision has occurred
						if(distance<(2*(Integer)params.getValue("body_radius"))){
							Collision collision = new Collision(player1, player2);
							collisionList.add(collision);
						}
					}
				}
			}
		}
		
		//Iterate through the balls
		Iterator<Object> balls = context.getObjects(Ball.class).iterator();
		while (balls.hasNext()){
			Ball ball = (Ball)balls.next();
			
			//Check if the ball has collided with the boundary
			checkBoundaryCollision(ball, (Integer)params.getValue("ball_radius"), collisionList);
		}
	}
	
	private static void updateVelocity(Ball ball){
		if(ball.getPlayer()!=null){
			ball.setVelocity(ball.getPlayer().getVelocity());
		} else if(ball.getMovement().getEffort()!=null){
			// Vector which will modify the boids velocity vector
			Vector3d velocity = ball.getMovement().getEffort();
			//Represents the difference between the desired and current positions
			//velocityUpdate.sub(desiredVelocity, currentPosition);

			//double predAcceleration = (Double)param.getValue("predAcceleration");
			//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
			
			//velocityUpdate.scale(acceleration * timeScale);

			// Apply the update to the velocity
			//velocity.add(velocityUpdate);
			//velocity = velocityUpdate;
			// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
			if (velocity.length() > ballMaxSpeed ){
				velocity.normalize();
				velocity.scale(ballMaxSpeed);
				velocity.scale(timeScale);
			}
			
			ball.setVelocity(velocity);
			ball.getMovement().setEffort(null);
			
		}
	
	}
		
	/**
	 * Handles positional and angular changes when balls desired angle does not equal current angle
	 * When player holds ball, desiredangle is set through the physics engine. 
	 * These angular changes (such as player body angle changes) can also affect the position of the ball.
	 * This method handles such changes. 
	 */
	private static void updateRotation(Ball ball){
		if(ball.getPlayer()!=null){
			if(ball.getMovement().getTurn()!=ball.getRotation()){
				double angleA = ball.getMovement().getTurn();
				Vector3d distance = new Vector3d();
				distance.sub(ball.getPositionVector(), ball.getPlayer().getPositionVector());
				//Distance between player and ball
				double side = distance.length();
				//Determines the x and y changes that need to be made (from the players current point)
				//To put ball at new angle
				double theta = angleA;
				double x = side*Math.cos(theta);
				double y = side*Math.sin(theta);
				Vector3d angleChange = new Vector3d(ball.getPlayer().getPositionVector().x+x,ball.getPlayer().getPositionVector().y+y,0.0);
				ball.setPositionVector(angleChange);
				ball.setRotation(ball.getMovement().getTurn());
			}
		} else {
			Iterator<Object> iter = ball.getContext().getObjects(Player.class).iterator();
			while(iter.hasNext()){
				Player player = (Player) iter.next();
				Vector3d vectorToBall = new Vector3d();
				vectorToBall.sub(ball.getPositionVector(), player.getPositionVector());
				double playerBodyRadius = (Integer)params.getValue("body_radius");
				//Player has ball
				if(vectorToBall.length()<=playerBodyRadius){
					double moveValue = playerBodyRadius - vectorToBall.length();
					vectorToBall.normalize();
					vectorToBall.scale(moveValue);
					ball.getPositionVector().add(vectorToBall);	
					Vector3d playerVector = new Vector3d();
					playerVector.set(player.getVelocity());
					playerVector.normalize();
					playerVector.scale(playerBodyRadius);
					ball.getMovement().setTurn(player.getPositionVector().angle(playerVector)-player.getPositionVector().angle(ball.getPositionVector()));
					ball.setRotation(ball.getMovement().getTurn());
					}
			}
		}
	}
}