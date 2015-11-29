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

public class Physics {

	private Context context;
	private double timeScale = .1;
	private double acceleration = 1;
	private double maxForwardSpeed = 7;
	private double maxBackwardSpeed = 5;
	private double maxSideWaySpeed = 4;
	private double ballMaxSpeed = 14;
	Parameters params = RunEnvironment.getInstance().getParameters();
		
	/*
	 * The constructor for general simulation physics
	 */
	Physics(Context context){
		this.context = context;
	}
	
	Physics(Ball ball){
		ballPhysics(ball);
		ballAngularManipulation(ball);
	}
	
	Physics(Player player){
		//updateVelocity
		//updateRotations
		ballHandling(player);
		if(player.getMovement().getEffort()!=null){
			playerVelocity(player);
		}
		player.getHead().setRotation(player.getMovement().getHeadTurn());
		player.setRotation(player.getMovement().getTurn());
		sendMessage(player);
	}
	
	/**
	 * Adds a string message to the messageBoard only if the current player has no pending messages
	 * @param message - the string to be posted
	 */
	void sendMessage(Player player){
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
	 * Checks for a boundary collision based on an agent, their radius
	 * Adds the collision to the passed collisionlist
	 * @param agent - the agent to check for collision
	 * @param radius - the radius of the agent
	 * @param collisionList - the list of collisions to add any collisions to
	 */
	void checkBoundaryCollision(MovingAgent agent, int radius, List<Collision> collisionList){
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
	void checkCollisions(){
		
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
					
					//If the ball has no current player, the ball has collided with the player
					if(ball.getLastPlayer()!=null){
						if((!(ball.getPlayer()!=null))&&(!(ball.getLastPlayer().equals(player1)))){
							Collision collision = new Collision(player1, ball);
							collisionList.add(collision);
						}
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
	
	void ballPhysics(Ball ball){
		if(ball.getPlayer()!=null){
			ball.setVelocity(ball.getPlayer().getVelocity());
		} else {
			// Vector which will modify the boids velocity vector
			Vector3d velocity = Utils.getVector(ball.getMovement().getEffort(), ball.getPositionVector());
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
			}
			// Update the position of the boid
			velocity.scale(timeScale);
			ball.setVelocity(velocity);
		}
	
	}
		
	
	void playerVelocity(Player player){
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
		}
		// Update the position of the boid
		player.getVelocity().scale(timeScale);
		player.getHead().setVelocity(player.getVelocity());
		
	}
	
	void ballHandling(Player player){
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
	/**
	 * Handles positional and angular changes when balls desired angle does not equal current angle
	 * When player holds ball, desiredangle is set through the physics engine. 
	 * These angular changes (such as player body angle changes) can also affect the position of the ball.
	 * This method handles such changes. 
	 */
	void ballAngularManipulation(Ball ball){
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
			Iterator<Object> iter = context.getObjects(Player.class).iterator();
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
		
	public class Collision{
		
		private MovingAgent agent1;
		private MovingAgent agent2;
		
		Collision(MovingAgent agent1, MovingAgent agent2){
			this.agent1 = agent1;
			this.agent2 = agent2;
			twoMovingParts();
		}
		
		Collision(MovingAgent agent1){
			this.agent1 = agent1;
			oneMovingPart();
		}
		
		void oneMovingPart(){
			agent1.getVelocity().set(agent1.getVelocity().x*-1, agent1.getVelocity().y*-1,0.0);
		}
		
		void twoMovingParts(){
			//newVelX = (firstBall.speed.x * (firstBall.getMass() – secondBall.getMass()) + (2 * secondBall.getMass() * secondBall.speed.x)) / (firstBall.getMass() + secondBall.getMass());
			double player1X = (agent1.getVelocity().x * (agent1.getMass() - agent2.getMass()) + (2 * agent2.getMass() * agent2.getVelocity().x)) / (agent1.getMass() + agent2.getMass());
			double player1Y = (agent1.getVelocity().y * (agent1.getMass() - agent2.getMass()) + (2 * agent2.getMass() * agent2.getVelocity().y)) / (agent1.getMass() + agent2.getMass());
			double player2X = (agent2.getVelocity().x * (agent2.getMass() - agent1.getMass()) + (2 * agent1.getMass() * agent1.getVelocity().x)) / (agent1.getMass() + agent2.getMass());
			double player2Y = (agent2.getVelocity().y * (agent2.getMass() - agent1.getMass()) + (2 * agent1.getMass() * agent1.getVelocity().y)) / (agent1.getMass() + agent2.getMass());
			agent1.getVelocity().set(player1X, player1Y, 0.0);
			agent2.getVelocity().set(player2X, player2Y, 0.0);
		}
		
		MovingAgent getAgent1(){
			return agent1;
		}
		
		MovingAgent getAgent2(){
			return agent2;
		}
	}
}