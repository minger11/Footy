package environment;

import javax.vecmath.Vector3d;

/**
 * An object created by the reflexes that represents the desires of the brain
 * @author user
 *
 */

public class PlayerMovement{
		
	/**
	 * Current player
	 */
	private Player player;
	/**
	 * Energy with which to move
	 */
	private double moveEnergy;
	/**
	 * Energy with which to pass
	 */
	private double passEnergy;
	/**
	 * Angle to move to (relative to the head)(in radians)
	 */
	private double moveDirection;
	/**
	 * Angle to pass to (relative to the head)(in radians)
	 */
	private double passDirection;
	/**
	 * Angle to turn the head by (relative to the head)(in radians)
	 */
	private double absoluteHeadTurn;	
	/**
	 * Angle to turn arms by (relative to the arms)(in radians)
	 */
	private double armsTurn;
	/**
	 * Angle to turn the player by (relative to the player)(in radians)
	 */
	private double turn;
	/**
	 * Message to say
	 */
	private String message;
	
	PlayerMovement(Player player){
		this.player = player;
	}
	
	//------------SETTERS-------------------------------------------------------------//	
	/**
	 * Simple setter
	 * @param angle
	 */
	void setTurn(double angle){
		turn=angle;
	}
	/**
	 * Simple setter
	 * @param angle
	 */
	void setHeadTurn(double angle){
		absoluteHeadTurn = angle;
	}
	/**
	 * Simple setter
	 * @param angle
	 */
	void setPassDirection(double angle){
		passDirection = angle;
	}	
	/**
	 * Simple setter
	 * @param energy
	 */
	void setPassEnergy(double energy){
		passEnergy = energy;
	}
	/**
	 * Simple setter
	 * @param x
	 */
	void setMoveEnergy(double x){
		moveEnergy = x;
	}
	/**
	 * Simple setter
	 * @param x
	 */
	void setMoveDirection(double x){
		moveDirection = x;
	}
	/**
	 * Simple setter
	 * @param angle
	 */
	void setArmsTurn(double angle){
		armsTurn = angle;
	}
	/**
	 * Simple setter
	 * @param x
	 */
	void setMessage(String x){
		message = x;
	}	
	
	//------------GETTERS-------------------------------------------------//	
	/**
	 * @return the message string to be spoken
	 */
	String getMessage(){
		return message;
	}
	double getMoveEnergy(){
		return moveEnergy;
	}
	/**
	 * @return the relative angle of the movement
	 */
	double getMoveDirection(){
		return moveDirection;
	}
	/**
	 * @return the relative head turn angle
	 */
	double getRelativeHeadTurn(){
		return absoluteHeadTurn;
	}
	/**
	 * @return the relative arms turn angle
	 */
	double getRelativeArmsTurn(){
		return armsTurn;
	}
	/**
	 * @return the relative turn angle
	 */
	double getRelativeTurn(){
		return turn;
	}	
	/**
	 * @return the absolute angle of the head turn angle
	 */
	double getAbsoluteHeadTurn(){
		double absoluteAngle = Utils.RelativeToAbsolute(getRelativeHeadTurn()+getRelativeTurn(), player.getHead().getRotation());
		return absoluteAngle;
	}
	/**
	 * @return the absolute angle of the arms turn angle
	 */
	double getAbsoluteArmsTurn(){
		double absoluteAngle = Utils.RelativeToAbsolute(getRelativeArmsTurn()+getRelativeTurn(), player.getArms().getRotation());
		return absoluteAngle;
	}
	/**
	 * @return the absolute angle of the turn angle
	 */
	double getAbsoluteTurn(){
		double absoluteAngle = Utils.RelativeToAbsolute(turn, player.getRotation());
		return absoluteAngle;
	}
	/**
	 * @return the absolute vector of the passEffort
	 */
	Vector3d getPassEffort(){
		double absoluteDirection = Utils.RelativeToAbsolute(passDirection, player.getHead().getRotation());
		return Utils.getVector(absoluteDirection, passEnergy);
	}
	/**
	 * @return the absolute vector of the effort
	 */
	Vector3d getEffort(){
		double absoluteDirection = Utils.RelativeToAbsolute(moveDirection, player.getHead().getRotation());
		return Utils.getVector(absoluteDirection, moveEnergy);
	}
	
}


