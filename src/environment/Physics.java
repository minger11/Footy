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

	Context context;
	Vector3d desiredVelocity;
	Vector3d currentPosition;
	Vector3d desiredArticleVelocity;
	Vector3d velocity;
	SimpleAgent agent;
	double timeScale = .1;
	double acceleration = 1;
	double maxForwardSpeed = 7;
	double maxBackwardSpeed = 5;
	double maxSideWaySpeed = 4;
	double ballMaxSpeed = 14;
	Parameters params = RunEnvironment.getInstance().getParameters();
	
	/**
	 * The constructor for Player physics
	 * @param agent
	 * @param currentPosition
	 * @param desiredPosition
	 * @param desiredBallPosition
	 * @param velocity
	 */
	Physics(SimpleAgent agent, Vector3d currentPosition, Vector3d desiredPosition, Vector3d desiredBallPosition, Vector3d velocity){
		this.desiredArticleVelocity = desiredBallPosition;
		this.desiredVelocity = desiredPosition;
		this.currentPosition = currentPosition;		
		this.velocity = velocity;
		this.agent = agent;
	}
	
	/**
	 * The constructor for Ball physics
	 * @param agent
	 * @param currentPosition
	 * @param desiredPosition
	 * @param velocity
	 */
	Physics(SimpleAgent agent, Vector3d currentPosition, Vector3d desiredPosition, Vector3d velocity){
		this.desiredVelocity = desiredPosition;
		this.currentPosition = currentPosition;		
		this.velocity = velocity;
		this.agent = agent;
	}
	
	/*
	 * The constructor for general simulation physics
	 */
	Physics(Context context){
		this.context = context;
	}
	
	/**
	 * Checks for a boundary collision based on an agent, their radius
	 * Adds the collision to the passed collisionlist
	 * @param agent - the agent to check for collision
	 * @param radius - the radius of the agent
	 * @param collisionList - the list of collisions to add any collisions to
	 */
	public void checkBoundaryCollision(MovingAgent agent, int radius, List<Collision> collisionList){
		if(agent.positionVector.y+radius>=(Integer)params.getValue("display_height")||
				agent.positionVector.y-radius<=0||
						agent.positionVector.x+radius>=(Integer)params.getValue("display_width")||
								agent.positionVector.x-radius<=0){
			
			Collision coll = new Collision(agent);
			collisionList.add(coll);
		}
	}
	
	/**
	 * Checks for collisions in the model
	 */
	public void checkCollisions(){
		
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
				vector.sub(player1.positionVector, ball.positionVector);
				double distance = Math.abs(vector.length());
				
				//If the distance is too short a collision has occurred
				if(distance<((Integer)params.getValue("body_radius")+(Integer)params.getValue("ball_radius"))){
					
					//If the ball has no current player, the ball has collided with the player
					if(ball.getLastPlayer()!=null){
						if((!(ball.player!=null))&&(!(ball.getLastPlayer().equals(player1)))){
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
						vector.sub(player1.positionVector, player2.positionVector);
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

	
	protected Vector3d getUpdatedVelocity(){
		if(agent instanceof Player){
			playerPhysics();
		}
		if(agent instanceof Ball){
			ballPhysics((Ball)agent);
		}
		return velocity;
	}
	
	protected void ballPhysics(Ball ball){
		if(ball.player!=null){
			velocity = ball.player.velocity;
		} else {
			// Vector which will modify the boids velocity vector
			Vector3d velocityUpdate = new Vector3d();   
			//Represents the difference between the desired and current positions
			velocityUpdate.sub(desiredVelocity, currentPosition);

			//double predAcceleration = (Double)param.getValue("predAcceleration");
			//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
			
			//velocityUpdate.scale(acceleration * timeScale);

			// Apply the update to the velocity
			//velocity.add(velocityUpdate);
			velocity = velocityUpdate;
			// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
			if (velocity.length() > ballMaxSpeed ){
				velocity.normalize();
				velocity.scale(ballMaxSpeed);
			}
			// Update the position of the boid
			velocity.scale(timeScale);
		}
	
	}
		
	protected void playerPhysics(){
		ballHandling();
		if(desiredVelocity!=null){
			playerVelocity();
		}
	}
		
	
	protected void playerVelocity(){
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = new Vector3d();   
		//Represents the difference between the desired and current positions
		//velocityUpdate.sub(desiredPosition, currentPosition);

		//NEWW
		velocityUpdate = desiredVelocity;
		
		//double predAcceleration = (Double)param.getValue("predAcceleration");
		//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
		
		velocityUpdate.scale(acceleration * timeScale);

		// Apply the update to the velocity
		velocity.add(velocityUpdate);

		// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
		if (velocity.length() > maxForwardSpeed ){
			velocity.normalize();
			velocity.scale(maxForwardSpeed);
		}
		// Update the position of the boid
		velocity.scale(timeScale);
		
	}
	
	protected void ballHandling(){
		Iterator<Object> it = agent.context.getObjects(Ball.class).iterator();
		Ball ball;
		if(it.hasNext()){
			ball = (Ball)it.next();
				Vector3d vectorToBall = new Vector3d();
				vectorToBall.sub(ball.positionVector, ((Player)agent).positionVector);
				//Player has ball
				if((Math.abs(vectorToBall.length())<=((Integer)agent.params.getValue("body_radius")+(Integer)agent.params.getValue("ball_radius")))){
					if((!(ball.player!=null))||(ball.player.equals(agent))){
							//Player wants to carry ball, balls player is current player
							if(desiredArticleVelocity.equals(desiredVelocity)){
								ball.setPlayer((Player)agent);
								ball.players.add((Player)agent);
								ball.movement.setDesiredPosition(desiredArticleVelocity);
								ball.movement.setDesiredAngle(((Player) agent).rotation);
							} 
							//Player wants to move the ball, balls player is now null
							else {
								ball.setPlayer(null);
								ball.movement.setDesiredPosition(desiredArticleVelocity);
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
		public void ballAngularManipulation(){
			Ball ball = ((Ball) agent);
			if(ball.player!=null){
				if(ball.movement.desiredAngle!=ball.rotation){
					double angleA = ball.movement.desiredAngle;
					Vector3d distance = new Vector3d();
					distance.sub(ball.positionVector, ball.player.positionVector);
					//Distance between player and ball
					double side = distance.length();
					//Determines the x and y changes that need to be made (from the players current point)
					//To put ball at new angle
					double theta = angleA;
					double x = side*Math.cos(theta);
					double y = side*Math.sin(theta);
					Vector3d angleChange = new Vector3d(ball.player.positionVector.x+x,ball.player.positionVector.y+y,0.0);
					ball.positionVector = angleChange;
					ball.rotation = ball.movement.desiredAngle;
				}
			} else {
				Iterator<Object> iter = ball.context.getObjects(Player.class).iterator();
				while(iter.hasNext()){
					Player player = (Player) iter.next();
					Vector3d vectorToBall = new Vector3d();
					vectorToBall.sub(ball.positionVector, player.positionVector);
					double playerBodyRadius = ((Integer)agent.params.getValue("body_radius"));
					//Player has ball
					if(vectorToBall.length()<=playerBodyRadius){
						double moveValue = playerBodyRadius - vectorToBall.length();
						vectorToBall.normalize();
						vectorToBall.scale(moveValue);
						ball.positionVector.add(vectorToBall);	
						Vector3d playerVector = new Vector3d();
						playerVector.set(player.velocity);
						playerVector.normalize();
						playerVector.scale(playerBodyRadius);
						ball.movement.desiredAngle = player.positionVector.angle(playerVector)-player.positionVector.angle(ball.positionVector);
						ball.rotation = ball.movement.desiredAngle;
						}
				}
			}
		}
		
	public class Collision{
		
		MovingAgent agent1;
		MovingAgent agent2;
		
		Collision(MovingAgent agent1, MovingAgent agent2){
			this.agent1 = agent1;
			this.agent2 = agent2;
			twoMovingParts();
		}
		
		Collision(MovingAgent agent1){
			this.agent1 = agent1;
			oneMovingPart();
		}
		
		public void oneMovingPart(){
			agent1.velocity.set(agent1.velocity.x*-1, agent1.velocity.y*-1,0.0);
		}
		
		public void twoMovingParts(){
			//newVelX = (firstBall.speed.x * (firstBall.mass – secondBall.mass) + (2 * secondBall.mass * secondBall.speed.x)) / (firstBall.mass + secondBall.mass);
			double player1X = (agent1.velocity.x * (agent1.mass - agent2.mass) + (2 * agent2.mass * agent2.velocity.x)) / (agent1.mass + agent2.mass);
			double player1Y = (agent1.velocity.y * (agent1.mass - agent2.mass) + (2 * agent2.mass * agent2.velocity.y)) / (agent1.mass + agent2.mass);
			double player2X = (agent2.velocity.x * (agent2.mass - agent1.mass) + (2 * agent1.mass * agent1.velocity.x)) / (agent1.mass + agent2.mass);
			double player2Y = (agent2.velocity.y * (agent2.mass - agent1.mass) + (2 * agent1.mass * agent1.velocity.y)) / (agent1.mass + agent2.mass);
			agent1.velocity.set(player1X, player1Y, 0.0);
			agent2.velocity.set(player2X, player2Y, 0.0);
		}
		
		public MovingAgent getAgent1(){
			return agent1;
		}
		
		public MovingAgent getAgent2(){
			return agent2;
		}
	}
}