package environment;

import javax.vecmath.Vector3d;

/**
 * Gets intentions from the brain and updates the playerMovement object
 * Reflexes get information from the brain for use by the model
 * reflexes are mainly concerned with brain intentions 
 * @author user
 *
 */

public class Reflexes {
	
	private Player player;
	
	Reflexes(Player player){
		this.player = player;
	}	
	
	void init(){
		player.getMovement().setHeadTurn(getHeadTurn());
		player.getMovement().setMessage(getMessage());
		player.getMovement().setBodyTurn(getTurn());
		player.getMovement().setEffort(getMoveDirection(), getMoveEnergy());
		player.getMovement().setPassEffort(getPassDirection(), getPassEnergy());
		player.getMovement().setArmsTurn(getArmsTurn());
	}
	
	void step(){
		player.getMovement().setHeadTurn(getHeadTurn());
		player.getMovement().setMessage(getMessage());
		player.getMovement().setBodyTurn(getTurn());
		player.getMovement().setEffort(getMoveDirection(), getMoveEnergy());
		player.getMovement().setPassEffort(getPassDirection(), getPassEnergy());
		player.getMovement().setArmsTurn(getArmsTurn());
	}

	//-------------REAL GETTERS-------------------------//
	
	String getMessage(){
		return player.getBrain().getNewMessage();
	}
	
	double getPassEnergy(){
		return player.getBrain().getPassEnergy();
	}
	double getPassDirection(){
		return player.getBrain().getPassDirection();
	}
	double getMoveEnergy(){
		return player.getBrain().getMoveEnergy();
	}
	double getMoveDirection(){
		return player.getBrain().getMoveDirection();
	}	
	double getTurn(){
		return player.getBrain().getTurn();
	}	
	protected double getHeadTurn(){
		return player.getBrain().getHeadTurn();
	}
	protected double getArmsTurn(){
		return player.getBrain().getArmsTurn();
	}
}
