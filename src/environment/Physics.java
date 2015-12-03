package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

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
	
	private Physics(){	
	}
	
	//------------------------------------------------------------------------------------------------//
	
	/**
	 * Updates the various positions, velocities and rotations based on the state and the physical limitations of the simulation
	 * @param player
	 */
	public static void update(Context<Object> context){
		checkCollisions(context);
	}
		
	/**
	 * Checks for collisions in the simulation
	 */
	private static void checkCollisions(Context<Object> context){
		
		//Create a list of collisions
		List<Collision> collisionList = new ArrayList<Collision>();
		
		//Iterate through the players and assign each player to variable player1
		Iterator<Object> players1 = context.getObjects(Player.class).iterator();
		while (players1.hasNext()){
			Player player1 = (Player)players1.next();
			
			//Check collisions with the boundary
			checkBoundaryCollision(player1, Params.playerRadius, collisionList);
		
			//Iterate through the balls
			Iterator<Object> balls = context.getObjects(Ball.class).iterator();
			while (balls.hasNext()){
				Ball ball = (Ball)balls.next();
				
				//Get the distance between the ball and the player
				Vector3d vector = new Vector3d();
				vector.sub(player1.getPositionVector(), ball.getPositionVector());
				double distance = Math.abs(vector.length());
				
				//If the distance is too short a collision has occurred
				if(distance<(Params.playerRadius-.01)){
					
					//If the ball has no current player, the ball has collided with the player
					if(ball.getPlayer()==null){
						System.out.println("collision");
						Collision collision = new Collision(player1, ball);
						collisionList.add(collision);
					}
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
					try{
						if(collide.getAgent1().equals(player2)&&collide.getAgent2().equals(player1)){
							alreadyCollided = true;
						}
					} catch(Exception e){
						
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
						if(distance<(2*Params.playerRadius)){
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
			checkBoundaryCollision(ball, Params.playerRadius, collisionList);
		}
	}
	
	/**
	 * Checks for a boundary collision based on an agent, their radius
	 * Adds the collision to the passed collisionlist
	 * @param agent - the agent to check for collision
	 * @param radius - the radius of the agent
	 * @param collisionList - the list of collisions to add any collisions to
	 */
	private static void checkBoundaryCollision(MovingAgent agent, double radius, List<Collision> collisionList){
		if(agent.getPositionVector().y+radius>=Params.displayHeight||
				agent.getPositionVector().y-radius<=0||
						agent.getPositionVector().x+radius>=Params.displayWidth||
								agent.getPositionVector().x-radius<=0){
			
			Collision coll = new Collision(agent);
			collisionList.add(coll);
		}
	}
	
	
	//------------------------------------------------------------------------------------------------//
	
	/**
	 * Updates the ball's various rotation and movement vectors based on the physical limitations of the simulation
	 * @param player
	 */
	public static void update(Ball ball){
		updateVelocity(ball);
		updateRotation(ball);
	}
	
	/**
	 * Updates the velocity vector of the ball
	 * @param ball
	 */
	private static void updateVelocity(Ball ball){
		//if the ball has a player, the ball's velocity is set to the players velocity
		if(ball.getPlayer()!=null){
			ball.setVelocity(ball.getPlayer().getVelocity());
			
			//otherwise if the ball has effort (energy has been applied to ball)
		} else if(ball.getMovement().getEffort()!=null){
			
			//set the effort applied to the ball in a new vector
			Vector3d effort = ball.getMovement().getEffort();
			
			//If the vector exceeds the ball's max speed, throttle it back 
			if (effort.length() > Params.ballMaxSpeed){
				effort.normalize();
				effort.scale(Params.ballMaxSpeed);
			}
			
			//Set the balls velocity to the vector
			ball.setVelocity(effort);
			
			//reset the balls effort vector to null (as no external energy will be applied from now)
			ball.getMovement().setEffort(null);
		}
	
	}
		
	/**
	 * Handles positional and angular changes when a ball is possessed by a player
	 */
	private static void updateRotation(Ball ball){
		
		//If the ball has a player
		if(ball.getPlayer()!=null){
				Vector3d newPosition = new Vector3d();
				
				//set a vector equal to the possessing players position plus the vector from the centre of the player to the outermost reach of the arms 
				newPosition.add(ball.getPlayer().getPositionVector(), Utils.getVector(ball.getPlayer().getArms().getRotation(), Params.playerRadius));
				
				//set the balls position to be equals to this vector
				ball.setPositionVector(newPosition);
				
				//set the balls rotation to be equal to that of the player
				ball.setRotation(Utils.checkRadian(ball.getPlayer().getRotation()));
		} 
	}
	
	
	//------------------------------------------------------------------------------------------------//
	
	/**
	 * Updates the player's various rotation and movement vectors and spoken messages based on the physical limitations of the simulation
	 * @param player
	 */
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
	
	/**
	 * Executes the player's ball handling intentions if the player has a ball
	 * @param player
	 */
	private static void ballHandling(Player player){
		try{
				Ball ball = Utils.getBall(player);
				
				//If there is no pass effort, the player wants to carry ball
				if(player.getMovement().getPassEffort().length()==0){
					
					//If the ball has no player, this player has just received the ball
					if(ball.getPlayer()!=player){
						
						//Set the ball's player to be this player
						ball.setPlayer(player);
					}
					
					//If there is pass effort, send the player and ball to the makePass method
				} else {
					makePass(ball);
				}
			} 
		catch(Exception e){
			}
	}
	
	/**
	 * Updates the rotation of the player, head and arms
	 * @param player
	 */
	private static void updateRotation(Player player){
		
		//Check the speeds
		checkHeadRotationSpeed(player);
		checkArmsRotationSpeed(player);
		checkRotationSpeed(player);
		
		//Check the head and arms relative to the body
		checkHeadToBodyRotation(player);
		checkArmsToBodyRotation(player);
		
		//Update the true values after checking them with the Utils check radian method
		player.getArms().setRotation(Utils.checkRadian(player.getMovement().getAbsoluteArmsTurn()));
		player.getHead().setRotation(Utils.checkRadian(player.getMovement().getAbsoluteHeadTurn()));
		player.setRotation(Utils.checkRadian(player.getMovement().getAbsoluteTurn()));
	}
	
	private static void updateVelocity(Player player){
		player.getVelocity().scale(Params.playerVelocityDecay);
		
		//check energy
		double moveEnergy = player.getMovement().getMoveEnergy() * Params.timeScale;
		if(moveEnergy > Params.maxMoveEnergy){
			player.getMovement().setMoveEnergy(Params.maxMoveEnergy);
		}
		
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = player.getMovement().getEffort();
		//Represents the difference between the desired and current positions
		//velocityUpdate.sub(desiredPosition, currentPosition);

		if(velocityUpdate.length()>0){
			//double predAcceleration = (Double)param.getValue("predAcceleration");
			//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
			velocityUpdate.normalize();
			velocityUpdate.scale((Params.playerAcceleration)*moveEnergy);
			// Apply the update to the velocity
			player.getVelocity().add(velocityUpdate);
			
			double maxSpeed = getMaxSpeed(player);
			
			// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
			if (player.getVelocity().length() > (maxSpeed) ){
				player.getVelocity().normalize();
				player.getVelocity().scale(maxSpeed);
				//player.getVelocity().scale(timeScale);
			}
			
			player.getHead().setVelocity(player.getVelocity());
		}
	}
	
	/**
	 * Performs a pass
	 * @param ball - the ball
	 */
	private static void makePass(Ball ball){
		
		//first make sure the angle of the pass is within view of the angle of the arms
		if(Utils.inView(Utils.getAngle(ball.getPlayer().getMovement().getPassEffort()), Params.armsAngle, ball.getPlayer().getArms().getRotation())){
			
			//assign the player's ball effort to the ball's effort
			ball.getMovement().setEffort(ball.getPlayer().getMovement().getPassEffort());
			
			//set the ball's player to null as the ball has left the player
			ball.setPlayer(null);
		}
	}
	
	/**
	 * Ensures that the head is not rotating at a faster rate than allowed by the simulation
	 * @param player
	 */
	private static void checkHeadRotationSpeed(Player player){
		if(player.getMovement().getRelativeHeadTurn()>Params.maxHeadRotationSpeed){
			player.getMovement().setHeadTurn(Params.maxHeadRotationSpeed);
		} else if(player.getMovement().getRelativeHeadTurn()<-Params.maxHeadRotationSpeed){
			player.getMovement().setHeadTurn(-Params.maxHeadRotationSpeed);
		}
	}
	
	/**
	 * Ensures that the arms are not rotating at a faster rate than allowed by the simulation
	 * @param player
	 */
	private static void checkArmsRotationSpeed(Player player){
		if(player.getMovement().getRelativeArmsTurn()>Params.maxArmsRotationSpeed){
			player.getMovement().setArmsTurn(Params.maxArmsRotationSpeed);
		} else if(player.getMovement().getRelativeArmsTurn()<-Params.maxArmsRotationSpeed){
			player.getMovement().setArmsTurn(-Params.maxArmsRotationSpeed);
		}
	}
	
	/**
	 * Ensures that the player is not rotating at a faster rate, based on its speed, than allowed by the simulation 
	 * @param player
	 */
	private static void checkRotationSpeed(Player player){	
		
		//for each unit of speed, the incremental loss in rotation speed
		double incrementalLoss = (Params.maxRotationSpeedAtMaxSpeed-Params.maxRotationSpeedAtRest)/(Params.maxForwardSpeed);
		
		//the maximum rotation speed based on the players current speed
		double maxRotationSpeed = ((incrementalLoss*player.getVelocity().length()))+Params.maxRotationSpeedAtRest;
		
		//check the players rotation speed
		if(player.getMovement().getRelativeTurn()>maxRotationSpeed){
			player.getMovement().setTurn(maxRotationSpeed);
		} else if(player.getMovement().getRelativeTurn()<-maxRotationSpeed){
			player.getMovement().setTurn(-maxRotationSpeed);
		}
	}
	
	/**
	 * Checks the angle of the head in relation to the body is within the accepted limits
	 * @param player
	 */
	private static void checkHeadToBodyRotation(Player player){
		
		//the absolute value of the proposed head angle
		double headPlusBodyRotation = Utils.RelativeToAbsolute(player.getMovement().getRelativeHeadTurn()+player.getMovement().getRelativeTurn(), player.getHead().getRotation());
		
		//gets the relative angle of the head in relation to the body
		double diff = Utils.absoluteToRelative(headPlusBodyRotation, player.getRotation());
		
		//ensures the head cannot go past a certain angle in relation to the body and if so limits the angle to this value
		if(diff>=Params.maxHeadToBodyTurn){
			diff=Params.maxHeadToBodyTurn;
			player.getMovement().setHeadTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getHead().getRotation()));
		} else if(diff<=-Params.maxHeadToBodyTurn){
			diff=-Params.maxHeadToBodyTurn;
			player.getMovement().setHeadTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getHead().getRotation()));
		}
	}
	
	/**
	 * Checks the angle of the arms in relation to the body is within the accepted limits
	 * @param player
	 */
	private static void checkArmsToBodyRotation(Player player){
		
		//the absolute value of the proposed arms angle
		double armsPlusBodyRotation = Utils.RelativeToAbsolute(player.getMovement().getRelativeArmsTurn()+player.getMovement().getRelativeTurn(), player.getArms().getRotation());
		
		//gets the relative angle of the head in relation to the body
		double diff = Utils.absoluteToRelative(armsPlusBodyRotation,player.getRotation());
		
		//ensures the head cannot go past a certain angle in relation to the body and if so limits the angle to this value
		if(diff>=Params.maxArmsToBodyTurn){
			diff=Params.maxArmsToBodyTurn;
			player.getMovement().setArmsTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getArms().getRotation()));
		} else if(diff<=-Params.maxArmsToBodyTurn){
			diff=-Params.maxArmsToBodyTurn;
			player.getMovement().setArmsTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getArms().getRotation()));
		}
	}
	
	/**
	 * Determines what the maximum speed of the player is based on the direction of the velocity and the angle of the body
	 * @param player - the player
	 * @return - a double that represents the maximum speed
	 */
	private static double getMaxSpeed(Player player){
		
		//get the absolute angle of the velocity
		double velocityAngle = Utils.getAngle(player.getVelocity());
		
		//get the relative angle of the velocity to the players rotation
		double velocityToBody = Utils.absoluteToRelative(velocityAngle, player.getRotation());
		
		double maxSpeed;
		
		//if velocity is forward in relation to the players body
		if(velocityToBody<=Math.PI/4&&velocityToBody>=-Math.PI/4){
			maxSpeed = Params.maxForwardSpeed;
			
			//if velocity is backward in relation to the players body
		} else if(velocityToBody>=3*Math.PI/4||velocityToBody<=-3*Math.PI/4){
			maxSpeed = Params.maxBackwardSpeed;
			
			//if the velocity is sideward in relation to the players body
		} else {
			maxSpeed = Params.maxSideWaySpeed;
		}
		return maxSpeed;
	}

}