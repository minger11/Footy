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
	
	Utils utils = new Utils();
	
	//Current Player
	private Player player;
	
	//Absolute effort vectors
	private Vector3d passEffort;
	private Vector3d effort;
	
	//Absolute rotational angles (radians not degrees)
	private double headTurn;
	private double armsTurn;
	private double turn;
	
	private String message;
	
	PlayerMovement(Player player){
		this.player = player;
		effort = new Vector3d();
		passEffort = new Vector3d();
	}
	
	//------------SETTERS-------------------------------------------------------------//	
	
	void setBodyTurn(double angle){
		double absoluteAngle = utils.RelativeToAbsolute(angle, player.getHead().getRotation());
		turn=absoluteAngle;
	}
	
	void setHeadTurn(double angle){
		double absoluteAngle = utils.RelativeToAbsolute(angle, player.getHead().getRotation());
		headTurn = absoluteAngle;
	}
		
	void setPassEffort(double direction, double distance){
		double absoluteDirection = utils.RelativeToAbsolute(direction, player.getHead().getRotation());
		passEffort = utils.getVector(absoluteDirection, distance);
	}
	
	void setEffort(double direction, double distance){
		double absoluteDirection = utils.RelativeToAbsolute(direction, player.getHead().getRotation());
		effort = utils.getVector(absoluteDirection, distance);
	}
	
	void setArmsTurn(double angle){
		double absoluteAngle = utils.RelativeToAbsolute(angle, player.getHead().getRotation());
		armsTurn = absoluteAngle;
	}
	
	void setMessage(String x){
		message = x;
	}	
	
	//------------GETTERS-------------------------------------------------//	
	
	double getHeadTurn(){
		return headTurn;
	}
		
	Vector3d getPassEffort(){
		return passEffort;
	}
	
	double getArmsTurn(){
		return armsTurn;
	}
	
	String getMessage(){
		return message;
	}

	double getTurn(){
		return turn;
	}
	
	Vector3d getEffort(){
		return effort;
	}
	
}


