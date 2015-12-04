package environment;

/**
 * A class to simulate collisions.  
 * @author user
 */

public class Collision{
	
	/**
	 * The first (possibly only) agent involved in the collision.
	 */
	private MovingAgent agent1;
	/**
	 * The second agent involved in the collision.
	 */
	private MovingAgent agent2;
	
	/**
	 * Constructor for dual agent collisions
	 * @param agent1
	 * @param agent2
	 */
	Collision(MovingAgent agent1, MovingAgent agent2){
		this.agent1 = agent1;
		this.agent2 = agent2;
		dualAgentCollision();
	}
	
	/**
	 * Constructor for single agent collisions
	 * @param agent1
	 */
	Collision(MovingAgent agent1){
		this.agent1 = agent1;
		singleAgentCollision();
	}
	
	/**
	 * Simulates the changing velocity in a single agent collision. Updates the velocity and immediately moves the agent to ensure it does not get stuck..
	 */
	void singleAgentCollision(){
		
		//Reverse the velocities
		agent1.getVelocity().set(agent1.getVelocity().x*-1, agent1.getVelocity().y*-1,0.0);
		
		//immediately move by this velocity - this is to ensure that the agent does not get stuck 
		//(as the upcoming collision energy loss can cause the reverse displacement to be less than the original collision overlap)
		agent1.getPositionVector().add(agent1.getVelocity());
		
		//reduce the velocity to model energy loss during the collision
		agent1.getVelocity().scale(Params.collisionEnergy);
		
		//If the agent is a ball, set the collided value to be true
		if(agent1 instanceof Ball){
			((Ball)agent1).getMovement().setCollided(true);
		}
	}
	
	/**
	 * Simulates the changing velocities in a dual agent collision. Updates the velocities and immediately moves agents to ensure they do not get stuck to each other.
	 */
	void dualAgentCollision(){
		
		//Calculate the new velocities using a simple collision formula
		double player1X = (agent1.getVelocity().x * (agent1.getMass() - agent2.getMass()) + (2 * agent2.getMass() * agent2.getVelocity().x)) / (agent1.getMass() + agent2.getMass());
		double player1Y = (agent1.getVelocity().y * (agent1.getMass() - agent2.getMass()) + (2 * agent2.getMass() * agent2.getVelocity().y)) / (agent1.getMass() + agent2.getMass());
		double player2X = (agent2.getVelocity().x * (agent2.getMass() - agent1.getMass()) + (2 * agent1.getMass() * agent1.getVelocity().x)) / (agent1.getMass() + agent2.getMass());
		double player2Y = (agent2.getVelocity().y * (agent2.getMass() - agent1.getMass()) + (2 * agent1.getMass() * agent1.getVelocity().y)) / (agent1.getMass() + agent2.getMass());
		
		//set the new velocities
		agent1.getVelocity().set(player1X, player1Y, 0.0);
		agent2.getVelocity().set(player2X, player2Y, 0.0);
		
		//immediately move by these velocitie - this is to ensure that the agents do not get stuck 
		//(as the upcoming collision energy loss can cause the reverse displacement to be less than the original collision overlap)
		agent1.getPositionVector().add(agent1.getVelocity());
		agent2.getPositionVector().add(agent2.getVelocity());
		
		//significantly reduce the velocity to model energy loss during the collision
		agent1.getVelocity().scale(Params.collisionEnergy);
		agent2.getVelocity().scale(Params.collisionEnergy);
		
		//If the agent is a ball, set the collided value to be true
		if(agent1 instanceof Ball){
			((Ball)agent1).getMovement().setCollided(true);
		} 
		if(agent2 instanceof Ball){
			((Ball)agent2).getMovement().setCollided(true);
		}
	}
	
	/**
	 * Simple getter
	 * @return the first agent involved in the collision
	 */
	MovingAgent getAgent1(){
		return agent1;
	}
	
	/**
	 * Simple getter
	 * @return the second agent involved in the collision
	 */
	MovingAgent getAgent2(){
		return agent2;
	}
}
