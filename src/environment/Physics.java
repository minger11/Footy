package environment;

import java.util.Iterator;

import javax.vecmath.Vector3d;

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

	Vector3d desiredPosition;
	Vector3d currentPosition;
	Vector3d desiredBallPosition;
	Vector3d velocity;
	SimpleAgent agent;
	double timeScale = .1;
	double acceleration = 1;
	double maxSpeed = 7;
	
	Physics(SimpleAgent agent, Vector3d currentPosition, Vector3d desiredPosition, Vector3d desiredBallPosition, Vector3d velocity){
		this.desiredBallPosition = desiredBallPosition;
		this.desiredPosition = desiredPosition;
		this.currentPosition = currentPosition;		
		this.velocity = velocity;
		this.agent = agent;
	}
	
	Physics(SimpleAgent agent, Vector3d currentPosition, Vector3d desiredPosition, Vector3d velocity){
		this.desiredPosition = desiredPosition;
		this.currentPosition = currentPosition;		
		this.velocity = velocity;
		this.agent = agent;
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
			velocity = ball.player.movement.velocity;
		} else {
			velocity.set(0,0,0);
		}
	
	}
	

	
	protected void playerPhysics(){
		ballHandling();
		// Vector which will modify the boids velocity vector
		Vector3d velocityUpdate = new Vector3d();   
		//Represents the difference between the desired and current positions
		velocityUpdate.sub(desiredPosition, currentPosition);

		//double predAcceleration = (Double)param.getValue("predAcceleration");
		//double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
		
		velocityUpdate.scale(acceleration * timeScale);

		// Apply the update to the velocity
		velocity.add(velocityUpdate);

		// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
		if (velocity.length() > maxSpeed ){
			velocity.normalize();
			velocity.scale(maxSpeed);
		}
		// Update the position of the boid
		velocity.scale(timeScale);
	}
		
	protected void ballHandling(){
		Iterator<Object> it = agent.context.getObjects(Ball.class).iterator();
		Ball ball;
		if(it.hasNext()){
			ball = (Ball)it.next();
			//Player has ball
			if(ball.currentPosition.equals(agent.currentPosition)){
				//Player wants to carry ball, balls player is current player
				if(desiredBallPosition.equals(desiredPosition)){
					ball.setPlayer((Player)agent);
					ball.movement.setDesiredPosition(desiredBallPosition);
				} 
				//Player wants to move the ball, balls player is now null
				else {
					ball.setPlayer(null);
					ball.movement.setDesiredPosition(desiredBallPosition);
				}
			}			
		}
	}

	/**
	 * Predator grabs a random target prey and attempts to eat it.
	 * 
	 * Adopted from the C# Swarm model by Daniel Greenheck: 
	 *   http://greenhecktech.com/2014/04/03/throwback-thursday-swarm-intelligence/
	 * 
	 * @author Eric Tatara
	 * 
	 
	public class Predator extends Boid {
		
		private Prey target;         // The current target
		private Vector3d attackVector = new Vector3d();    // Vector to the prey
	  private ContinuousSpace space;
		
		
		 * Predator initialization.
		 
		//@ScheduledMethod(start=0)
		public void init(){
			Context context = ContextUtils.getContext(this);
			space = (ContinuousSpace)context.getProjection("Space");
		
			NdPoint q = space.getLocation(this);
			lastPosition = new Vector3d(q.getX(), q.getY(), q.getZ());
		}
		
		//@ScheduledMethod(start=1, interval=1)
		public void update(){
			Parameters param = RunEnvironment.getInstance().getParameters();
			Context context = ContextUtils.getContext(this);
			
			// A smaller time scale results in smoother movement, but over a shorter
			//  distance.  Reduce to speed up simluation speed.
			double timeScale = (Double)param.getValue("timeScale");
			
			// Vector which will modify the boids velocity vector
			Vector3d velocityUpdate = new Vector3d();     

			// If we have no target or we killed this one, acquire a new one
			
			// How close the predator has to be to the prey to kill it
			double killRadius = (Double)param.getValue("killRadius");
			
			if( target == null || attackVector.lengthSquared() < killRadius * killRadius){
				target = (Prey)context.getRandomObjects(Prey.class, 1).iterator().next();
			}
			
			if(target != null){
				attackVector.sub(target.getLastPosition(), lastPosition);
				velocityUpdate.add(attackVector);
			}

			// Update the velocity of the boid
			// Modify our velocity update vector to take into account acceleration over time
			double preyAcceleration = (Double)param.getValue("preyAcceleration");
			double predAcceleration = (Double)param.getValue("predAcceleration");
			double predMaxSpeed = (Double)param.getValue("predMaxSpeed");
			
			velocityUpdate.scale(predAcceleration * preyAcceleration * timeScale);

			// Apply the update to the velocity
			velocity.add(velocityUpdate);

			// If our velocity vector exceeds the max speed, throttle it back to the MAX_SPEED
			if (velocity.length() > predMaxSpeed ){
				velocity.normalize();
				velocity.scale(predMaxSpeed);
			}

			// Update the position of the boid
			velocity.scale(timeScale);
			lastPosition.add(velocity);
			space.moveByDisplacement(this, velocity.x, velocity.y, velocity.z);
		}
		*/
	
}
