package environment;

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
