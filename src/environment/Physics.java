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
	
	public static void update(Context<Object> context){
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
		System.out.println(player.getPositionVector());
		//System.out.println(player.getRotation());
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
		

		try{
				Ball ball = Utils.getBall(player);
				
				//Player wants to carry ball, balls player is current player
				if(player.getMovement().getPassEffort().length()==0){
					if(ball.getPlayer()!=player){
						ball.setPlayer(player);
					}
				} 
				
				//Player wants to move the ball, balls player is now null
				else {
					makePass(player, ball);
				}
			} 
		catch(Exception e){
			}
	}
	
	private static void makePass(Player player, Ball ball){
		//first make sure the angle of the pass is within view of the angle of the arms
		if(Utils.inView(Utils.getAngle(player.getMovement().getPassEffort()), Sim.armsAngle, player.getArms().getRotation())){
			ball.setPlayer(null);
			ball.getMovement().setEffort(player.getMovement().getPassEffort());
			System.out.println("Passing");
		}
	}
	
	private static double getHeadPlusBodyRotation(Player player){
		return Utils.RelativeToAbsolute(player.getMovement().getRelativeHeadTurn()+player.getMovement().getRelativeTurn(), player.getHead().getRotation());
	}
	
	private static double getArmsPlusBodyRotation(Player player){
		return Utils.RelativeToAbsolute(player.getMovement().getRelativeArmsTurn()+player.getMovement().getRelativeTurn(), player.getArms().getRotation());
	}
	
	private static void checkHeadToBodyRotation(Player player){
		//gets the relative angle of the head in relation to the body
		double diff = Utils.absoluteToRelative(getHeadPlusBodyRotation(player),player.getRotation());
		
		//ensures the head cannot go past a certain angle in relation to the body and if so limits the angle to this value
		if(diff>=Sim.maxHeadToBodyTurn){
			diff=Sim.maxHeadToBodyTurn;
			player.getMovement().setHeadTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getHead().getRotation()));
		} else if(diff<=-Sim.maxHeadToBodyTurn){
			diff=-Sim.maxHeadToBodyTurn;
			player.getMovement().setHeadTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getHead().getRotation()));
		}
	}
	
	private static void checkArmsToBodyRotation(Player player){
		//gets the relative angle of the head in relation to the body
		double diff = Utils.absoluteToRelative(getArmsPlusBodyRotation(player),player.getRotation());
		
		//ensures the head cannot go past a certain angle in relation to the body and if so limits the angle to this value
		if(diff>=Sim.maxArmsToBodyTurn){
			diff=Sim.maxArmsToBodyTurn;
			player.getMovement().setArmsTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getArms().getRotation()));
		} else if(diff<=-Sim.maxArmsToBodyTurn){
			diff=-Sim.maxArmsToBodyTurn;
			player.getMovement().setArmsTurn(Utils.absoluteToRelative((Utils.RelativeToAbsolute(diff, player.getRotation())), player.getArms().getRotation()));
		}
	}
	
	private static void checkHeadRotationSpeed(Player player){
		if(player.getMovement().getRelativeHeadTurn()>Sim.maxHeadRotationSpeed){
			player.getMovement().setHeadTurn(Sim.maxHeadRotationSpeed);
		} else if(player.getMovement().getRelativeHeadTurn()<-Sim.maxHeadRotationSpeed){
			player.getMovement().setHeadTurn(-Sim.maxHeadRotationSpeed);
		}
	}
	
	private static void checkArmsRotationSpeed(Player player){
		if(player.getMovement().getRelativeArmsTurn()>Sim.maxArmsRotationSpeed){
			player.getMovement().setArmsTurn(Sim.maxArmsRotationSpeed);
		} else if(player.getMovement().getRelativeArmsTurn()<-Sim.maxArmsRotationSpeed){
			player.getMovement().setArmsTurn(-Sim.maxArmsRotationSpeed);
		}
	}
	
	private static void checkRotationSpeed(Player player){
		
		//rotationspeed = incloss*speed+max@max
		double incrementalLoss = (Sim.maxRotationSpeedAtMaxSpeed-Sim.maxRotationSpeedAtRest)/Sim.maxForwardSpeed;
		double maxRotationSpeed = ((incrementalLoss*player.getVelocity().length())/Sim.timeScale)+Sim.maxRotationSpeedAtRest;
		if(player.getMovement().getRelativeTurn()>maxRotationSpeed){
			player.getMovement().setTurn(maxRotationSpeed);
		} else if(player.getMovement().getRelativeTurn()<-maxRotationSpeed){
			player.getMovement().setTurn(-maxRotationSpeed);
		}
	}
	
	private static void updateRotation(Player player){
		checkHeadRotationSpeed(player);
		checkArmsRotationSpeed(player);
		checkRotationSpeed(player);
		checkHeadToBodyRotation(player);
		checkArmsToBodyRotation(player);
		
		//updates the true values
		player.getArms().setRotation(getArmsPlusBodyRotation(player));
		player.getHead().setRotation(getHeadPlusBodyRotation(player));
		player.setRotation(player.getMovement().getTurn());
	}
	
	private static double getMaxSpeed(Player player){
		double velocityAngle = Utils.getAngle(player.getVelocity());
		double velocityToBody = Utils.absoluteToRelative(velocityAngle, player.getRotation());
		double maxSpeed;
		if(velocityToBody<=Math.PI/4&&velocityToBody>=-Math.PI/4){
			maxSpeed = Sim.maxForwardSpeed;
		} else if(velocityToBody>=3*Math.PI/4||velocityToBody<=-3*Math.PI/4){
			maxSpeed = Sim.maxBackwardSpeed;
		} else {
			maxSpeed = Sim.maxSideWaySpeed;
		}
		return maxSpeed;
	}
	
	private static void updateVelocity(Player player){
		player.getVelocity().scale(Sim.playerVelocityDecay);
		
		//check energy
		double moveEnergy = player.getMovement().getMoveEnergy();
		if(moveEnergy > Sim.maxMoveEnergy){
			player.getMovement().setMoveEnergy(Sim.maxMoveEnergy);
		}
		
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = player.getMovement().getEffort();
		//Represents the difference between the desired and current positions
		//velocityUpdate.sub(desiredPosition, currentPosition);

		if(velocityUpdate.length()>0){
			//double predAcceleration = (Double)param.getValue("predAcceleration");
			//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
			velocityUpdate.normalize();
			velocityUpdate.scale((Sim.acceleration * Sim.timeScale)*(moveEnergy * Sim.timeScale));
			// Apply the update to the velocity
			player.getVelocity().add(velocityUpdate);
			
			double maxSpeed = getMaxSpeed(player);
			
			// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
			if (player.getVelocity().length() > (maxSpeed*Sim.timeScale) ){
				player.getVelocity().normalize();
				player.getVelocity().scale(maxSpeed*Sim.timeScale);
				//player.getVelocity().scale(timeScale);
			}
			
			player.getHead().setVelocity(player.getVelocity());
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
		if(agent.getPositionVector().y+radius>=Sim.displayHeight||
				agent.getPositionVector().y-radius<=0||
						agent.getPositionVector().x+radius>=Sim.displayWidth||
								agent.getPositionVector().x-radius<=0){
			
			Collision coll = new Collision(agent);
			collisionList.add(coll);
		}
	}
	
	/**
	 * Checks for collisions in the model
	 */
	private static void checkCollisions(Context<Object> context){
		
		//Create a list of collisions
		List<Collision> collisionList = new ArrayList<Collision>();
		
		//Iterate through the players and assign each player to variable player1
		Iterator<Object> players1 = context.getObjects(Player.class).iterator();
		while (players1.hasNext()){
			Player player1 = (Player)players1.next();
			
			//Check collisions with the boundary
			checkBoundaryCollision(player1, Sim.bodyRadius, collisionList);
		
			//Iterate through the balls
			Iterator<Object> balls = context.getObjects(Ball.class).iterator();
			while (balls.hasNext()){
				Ball ball = (Ball)balls.next();
				
				//Get the distance between the ball and the player
				Vector3d vector = new Vector3d();
				vector.sub(player1.getPositionVector(), ball.getPositionVector());
				double distance = Math.abs(vector.length());
				
				//If the distance is too short a collision has occurred
				if(distance<(Sim.bodyRadius-.01)){
					
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
						if(distance<(2*Sim.bodyRadius)){
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
			checkBoundaryCollision(ball, Sim.bodyRadius, collisionList);
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
			if (velocity.length() > (Sim.ballMaxSpeed*Sim.timeScale) ){
				velocity.normalize();
				velocity.scale(Sim.ballMaxSpeed*Sim.timeScale);
				//velocity.scale(timeScale);
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
			//if(ball.getMovement().getTurn()!=ball.getRotation()){
				//double angleA = ball.getMovement().getTurn();
				
				/**
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
				*/
				Vector3d newPosition = new Vector3d();
				newPosition.add(ball.getPlayer().getPositionVector(), Utils.getVector(ball.getPlayer().getArms().getRotation(), Sim.bodyRadius));
				ball.setPositionVector(newPosition);
				ball.setRotation(ball.getPlayer().getRotation());
			//}
		} 
	}
}