package environment;

/**
 * Gets intentions from the brain and updates the respective playerMovement object
 * Reflexes get information from the brain for use by the model
 * @author user
 *
 */

public final class Reflexes {
	
	private Reflexes(){
	}	
	
	/**
	 * Gets the various intentions from the players brain and stores them inside the player's movement object
	 * @param player
	 */
	public static void react(Player player){
		player.getMovement().setHeadTurn(player.getBrain().getHeadTurn());
		player.getMovement().setMessage(player.getBrain().getNewMessage());
		player.getMovement().setTurn(player.getBrain().getTurn());
		player.getMovement().setMoveDirection(player.getBrain().getMoveDirection());
		player.getMovement().setMoveEnergy(player.getBrain().getMoveEnergy());
		player.getMovement().setPassDirection(player.getBrain().getPassDirection());
		player.getMovement().setPassEnergy(player.getBrain().getPassEnergy());
		player.getMovement().setArmsTurn(player.getBrain().getArmsTurn());
	}
}
