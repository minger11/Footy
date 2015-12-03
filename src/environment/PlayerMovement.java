package environment;

import javax.vecmath.Vector3d;

/**
 * Movement is concerned with taking the reflex desires, 
 * running them through the appropriate physics methods 
 * and updating the true movements in the model
 * @author user
 *
 */

public class PlayerMovement{
		
	//Current Player
	private Player player;
	
	//Absolute effort vectors
	private Vector3d passEffort;
	private double moveDirection;
	private double moveEnergy;
	
	//Absolute rotational angles (radians not degrees)
	private double headTurn;
	private double armsTurn;
	private double turn;
	
	private String message;
	
	PlayerMovement(Player player){
		this.player = player;
		passEffort = new Vector3d();
	}
	
	//------------SETTERS-------------------------------------------------------------//	
	
	void setTurn(double angle){
		turn=angle;
	}
	
	void setHeadTurn(double angle){
		headTurn = angle;
	}
		
	void setPassEffort(double direction, double distance){
		double absoluteDirection = Utils.RelativeToAbsolute(direction, player.getHead().getRotation());
		passEffort = Utils.getVector(absoluteDirection, distance);
	}
	void setMoveEnergy(double x){
		moveEnergy = x;
	}
	void setMoveDirection(double x){
		moveDirection = x;
	}
	
	void setArmsTurn(double angle){
		armsTurn = angle;
	}
	
	void setMessage(String x){
		message = x;
	}	
	
	//------------GETTERS-------------------------------------------------//	
	
	double getRelativeHeadTurn(){
		return headTurn;
	}
	
	double getRelativeArmsTurn(){
		return armsTurn;
	}
	
	double getRelativeTurn(){
		return turn;
	}
	
	//--------------------------------------------------------------
	
	double getHeadTurn(){
		double absoluteAngle = Utils.RelativeToAbsolute(headTurn, player.getHead().getRotation());
		return absoluteAngle;
	}

	
	double getArmsTurn(){
		double absoluteAngle = Utils.RelativeToAbsolute(armsTurn, player.getArms().getRotation());
		return absoluteAngle;
	}
	
	String getMessage(){
		return message;
	}

	double getTurn(){
		double absoluteAngle = Utils.RelativeToAbsolute(turn, player.getRotation());
		return absoluteAngle;
	}
	double getMoveEnergy(){
		return moveEnergy;
	}
	double getMoveDirection(){
		return moveDirection;
	}
	
	Vector3d getPassEffort(){
		return passEffort;
	}
	
	Vector3d getEffort(){
		double absoluteDirection = Utils.RelativeToAbsolute(moveDirection, player.getHead().getRotation());
		return Utils.getVector(absoluteDirection, moveEnergy);
	}
	
}


