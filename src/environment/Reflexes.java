package environment;


/**
 * Gets intentions from the brain and updates the playerMovement object
 * Reflexes get information from the brain for use by the model
 * reflexes are mainly concerned with brain intentions 
 * @author user
 *
 */

public final class Reflexes {
	
	private Reflexes(){
	}	
	
	public static void init(Player player){
		player.getMovement().setHeadTurn(getHeadTurn(player));
		player.getMovement().setMessage(getMessage(player));
		player.getMovement().setTurn(getTurn(player));
		player.getMovement().setMoveDirection(getMoveDirection(player));
		player.getMovement().setMoveEnergy(getMoveEnergy(player));
		player.getMovement().setPassEffort(getPassDirection(player), getPassEnergy(player));
		player.getMovement().setArmsTurn(getArmsTurn(player));
	}
	
	public static void step(Player player){
		player.getMovement().setHeadTurn(getHeadTurn(player));
		player.getMovement().setMessage(getMessage(player));
		player.getMovement().setTurn(getTurn(player));
		player.getMovement().setMoveDirection(getMoveDirection(player));
		player.getMovement().setMoveEnergy(getMoveEnergy(player));
		player.getMovement().setPassEffort(getPassDirection(player), getPassEnergy(player));
		player.getMovement().setArmsTurn(getArmsTurn(player));
	}

	//-------------REAL GETTERS-------------------------//
	
	private static String getMessage(Player player){
		return player.getBrain().getNewMessage();
	}
	private static double getPassEnergy(Player player){
		return player.getBrain().getPassEnergy();
	}
	private static double getPassDirection(Player player){
		return player.getBrain().getPassDirection();
	}
	private static double getMoveEnergy(Player player){
		return player.getBrain().getMoveEnergy();
	}
	private static double getMoveDirection(Player player){
		return player.getBrain().getMoveDirection();
	}	
	private static double getTurn(Player player){
		return player.getBrain().getTurn();
	}	
	private static double getHeadTurn(Player player){
		return player.getBrain().getHeadTurn();
	}
	private static double getArmsTurn(Player player){
		return player.getBrain().getArmsTurn();
	}
}
