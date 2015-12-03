package environment;

import javax.vecmath.Vector3d;

/**
 * used for sending players from the senses to the brain - Breaks NdPoints into Vectors
 * Separates a Player from his dynamically changing position
 * The resulting player object contains the player and a static Vector3d
 * It is built this way to ensure that the brain cannot access dynamically changing values in the Players real positional NdPoint
 * @author user
 *
 */
public class SensesObject{
	
	private Object simpleAgent;
	
		//Within depthvision
	private boolean withinDepth = false;
			
		//--------withinDepth = TRUE------------------------------------------//		
		//For agents that are within depth
		
		//For agents that are players and who are within depth
	private double headRotation;
	private double bodyRotation;
			
		//--------WITHINDEPTH = FALSE------------------------------------------//		
		//For agents that are not within depth and whose distance is not know
	private double relativeAngle;
	private double distance;
		
		/**
		 * Creates a new sensesObject with a static Vector3d that cannot change with the player
		 * By first breaking the NdPoint parameter into doubles, we can then create a new point that wont move with the player
		 * @param t
		 * @param p
		 */
		SensesObject(Object agent, Vector3d vector, Player player, boolean withinDepth){
			
			simpleAgent = agent;
			
			this.withinDepth = withinDepth;
			
			relativeAngle = Utils.absoluteToRelative(Utils.getAngle(vector), player.getHead().getRotation());
			
			if(this.withinDepth){
				distance = vector.length();
				
				
				if(simpleAgent instanceof Player){
					headRotation = Utils.absoluteToRelative(((Player)simpleAgent).getHead().getRotation(), player.getHead().getRotation());
					bodyRotation = Utils.absoluteToRelative(((Player)simpleAgent).getRotation(), player.getHead().getRotation());
				}
			}
		}
		
		/**
		 * Creates a new 
		 * @param agent - the agent being viewed
		 * @param angle - the relative angle from the current player and the agent
		 */
		SensesObject(Object agent, Player player, Double angle){
		
			simpleAgent = agent;
				
			withinDepth = false;
			relativeAngle = Utils.absoluteToRelative(angle, player.getHead().getRotation());
		}
		
		SensesObject(Object agent){
			simpleAgent = agent;
		}
		
		
		//---------------SETTERS AND GETTERS -----------------------//
		public double getDistance(){
			return distance;
		}
			
		public Object getSimpleAgent(){
			return simpleAgent;
		}
			
		public boolean isWithinDepth(){
			return withinDepth;
		}

		public double getRelativeAngle(){
			return relativeAngle;
		}
		
		public double getRelativeBodyRotation(){
			return bodyRotation;
		}
		
		public double getRelativeHeadRotation(){
			return headRotation;
		}
}