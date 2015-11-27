package environment;

import javax.vecmath.Vector3d;
import repast.simphony.space.continuous.NdPoint;

/**
 * used for sending players from the senses to the brain - Breaks NdPoints into Vectors
 * Separates a Player from his dynamically changing position
 * The resulting player object contains the player and a static Vector3d
 * It is built this way to ensure that the brain cannot access dynamically changing values in the Players real positional NdPoint
 * @author user
 *
 */
public class SensesObject{
	
		Utils utils = new Utils();
		Object simpleAgent;
	
		//Within depthvision
		boolean withinDepth;
			
		//--------withinDepth = TRUE------------------------------------------//		
		//For agents that are within depth
		Vector3d relativeVector;
		
		//For agents that are players and who are within depth
		double headRotation;
		double bodyRotation;
			
		//--------WITHINDEPTH = FALSE------------------------------------------//		
		//For agents that are not within depth and whose distance is not know
		double relativeAngle;
		
		
		

		
		/**
		 * Creates a new sensesObject with a static Vector3d that cannot change with the player
		 * By first breaking the NdPoint parameter into doubles, we can then create a new point that wont move with the player
		 * @param t
		 * @param p
		 */
		SensesObject(Object agent, Vector3d vector, Player player, boolean withinDepth){
			
			simpleAgent = agent;
			
			this.withinDepth = withinDepth;
			
			relativeAngle = utils.absoluteToRelative(utils.getAngle(vector), player.head.rotation);
			
			if(this.withinDepth){
				relativeVector = vector;
				
				if(simpleAgent instanceof Player){
					headRotation = utils.absoluteToRelative(((Player)simpleAgent).head.rotation, player.head.rotation);
					bodyRotation = utils.absoluteToRelative(((Player)simpleAgent).rotation, player.head.rotation);
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
			relativeAngle = utils.absoluteToRelative(angle, player.head.rotation);
		}
		
		SensesObject(Object agent){
			simpleAgent = agent;
		}
		
		
		//---------------SETTERS AND GETTERS -----------------------//
		
		public Vector3d getRelativeVector(){
			return relativeVector;
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