package environment;

import javax.vecmath.Vector3d;

/**
 * used for sending players from the senses to the brain - Breaks NdPoints into Vectors
 * Separates a Player from his dynamically changing position
 * It is built this way to ensure that the brain cannot access dynamically changing values in the Players real positional NdPoint
 * @author user
 *
 */
public class SensesObject{
	
	private Object object;
	
	/**
	 * Indicates whether the object is within the players depth vision
	 */
	private boolean withinDepth = false;
	/**
	 * The rotation of the object player's head rotation (in radians)(relative to the player's head).
	 * Only available if object is a player and is in depth.
	 */
	private Double headRotation;
	/**
	 * The rotation of the object player's body rotation (in radians)(relative to the player's head).
	 * Only available if object is a player and is in depth.
	 */
	private Double bodyRotation;
	/**
	 * The rotation of the object player's arms rotation (in radians)(relative to the player's head).
	 * Only available if object is a player and is in depth.
	 */
	private Double armsRotation;
	/**
	 * The angle of the object player's velocity (relative to the players head)(in radians)
	 */
	private Double moveDirection;
	/**
	 * The length of the object player's velocity vector.
	 */
	private Double moveVelocity;
	/**
	 * The number of the object player.
	 */
	private Integer number;
	/**
	 * The distance to the object. Only available if the object is in depth.
	 */
	private Double distance;
	
	/**
	 * The angle to the object (relative to the players head). Not set for official messages.
	 */
	private Double relativeAngle;
	
		
	/**
	 * Creates a new sensesObject with a static Vector3d that cannot change with the player
	 * By first breaking the NdPoint parameter into doubles, we can then create a new point that wont move with the player
	 * @param object - agent 
	 * @param vector
	 * @param player
	 * @param withinDepth
	 */
	SensesObject(Object object, Vector3d vector, Player player, boolean withinDepth){
		
		this.object = object;
		this.withinDepth = withinDepth;
		
		//Set the relative angle to the object
		relativeAngle = Utils.absoluteToRelative(Utils.getAngle(vector), player.getHead().getRotation());
		
		//If the object is within depth, set the distance to the object
		if(this.withinDepth){
			distance = vector.length();
			
			//Set the various variables if the object is a player
			if(object instanceof Player){
				headRotation = Utils.absoluteToRelative(((Player)object).getHead().getRotation(), player.getHead().getRotation());
				bodyRotation = Utils.absoluteToRelative(((Player)object).getRotation(), player.getHead().getRotation());
				armsRotation = Utils.absoluteToRelative(((Player)object).getArms().getRotation(), player.getHead().getRotation());
				number = ((Player)object).getNumber();
				moveDirection = Utils.absoluteToRelative(Utils.getAngle(((Player)object).getVelocity()), player.getHead().getRotation());
				moveVelocity = ((Player)object).getVelocity().length();
			}
		}
	}
	
	/**
	 * Creates a new senses object
	 * @param object - the agent being viewed
	 * @param angle - the relative angle from the current player and the agent
	 */
	SensesObject(Object object, Player player, Double angle){
	
		this.object = object;	
		withinDepth = false;
		relativeAngle = Utils.absoluteToRelative(angle, player.getHead().getRotation());
	}
	
	/**
	 * Creates a new senses object
	 * @param object - the agent being viewed
	 * @param angle - the relative angle from the current player and the agent
	 */
	SensesObject(Object object){
		this.object = object;
	}
	
	
	/**
	 * Simple getter
	 * @return the distance to the object
	 */
	public Double getDistance(){
		return distance;
	}
	
	/**
	 * Simple getter
	 * @return the object
	 */
	public Object getObject(){
		return object;
	}
	
	/**
	 * Simple getter
	 * @return the withinDepth boolean - indicating whether the object is within depth vision
	 */
	public boolean isWithinDepth(){
		return withinDepth;
	}
	
	/**
	 * Simple getter
	 * @return the angle to the object (relative to the current player's head)(in radians)
	 */
	public Double getRelativeAngle(){
		return relativeAngle;
	}
	
	/**
	 * Simple getter
	 * @return the angle of the object player's body (relative to the current player's head)(in radians)
	 */
	public Double getRelativeBodyRotation(){
		return bodyRotation;
	}
	
	/**
	 * Simple getter
	 * @return the angle to the object player's head (relative to the current player's head)(in radians)
	 */
	public Double getRelativeHeadRotation(){
		return headRotation;
	}
	
	/**
	 * Simple getter
	 * @return the angle to the object player's arms (relative to the current player's head)(in radians)
	 */
	public Double getRelativeArmsRotation(){
		return armsRotation;
	}
	
	/**
	 * Simple getter
	 * @return the length of the object players velocity vector
	 */
	public Double getMoveVelocity(){
		return moveVelocity;
	}
	
	/**
	 * Simple getter
	 * @return the angle of the object player's velocity vector (relative to the current player's head)(in radians)
	 */
	public Double getMoveDirection(){
		return moveDirection;
	}
	
	/**
	 * Simple getter
	 * @return the number of the object player
	 */
	public Integer getNumber(){
		return number;
	}
}