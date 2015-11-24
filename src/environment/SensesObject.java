package environment;

import repast.simphony.space.continuous.NdPoint;

/**
 * PlayerObject to be used for sending players from the senses to internal projections in the brain
 * Separates a Player from his dynamically changing position
 * The resulting player object contains the player and a static NdPoint
 * It is built this way to ensure that the brain cannot access dynamically changing values in the Players real positional NdPoint
 * @author user
 *
 */
public class SensesObject{
	
		double x;
		double y;
		NdPoint staticPosition;
		SimpleAgent simpleAgent;
		double agentAngle;
		boolean withinDepth = false;
		boolean withinSide = false;
		
		/**
		 * Creates a new playerObject with a static NdPoint that cannot change with the player
		 * By first breaking the NdPoint parameter into doubles, we can then create a new point that wont move with the player
		 * @param t
		 * @param p
		 */
		SensesObject(SimpleAgent agent, NdPoint point){
			simpleAgent = agent;
			NdPoint dynamicPoint = point;
			double xPosition = dynamicPoint.getX();
			double yPosition = dynamicPoint.getY();
			staticPosition = new NdPoint(xPosition,yPosition);
			x = staticPosition.getX();
			y = staticPosition.getY();
			withinDepth = true;
		}
		
		SensesObject(SimpleAgent agent, Double angle){
			simpleAgent = agent;
			agentAngle = angle;
		}
		
		public NdPoint getPosition(){
			return staticPosition;
		}
		
		public SimpleAgent getSimpleAgent(){
			return simpleAgent;
		}
		
		public double getX(){
			return x;
		}
		
		public boolean isWithinDepth(){
			return withinDepth;
		}
		
		public double getY(){
			return y;
		}
		
		public double getAgentAngle(){
			return agentAngle;
		}
}