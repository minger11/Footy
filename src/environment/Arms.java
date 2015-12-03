package environment;


/**
 * head is used to setup the head
 * @author user
 *
 */

public class Arms extends MovingAgent{

	private Player player;
	
	Arms(double x, double y, Player player){
		super(x, y);	
		this.player = player;
	}

	Player getPlayer(){
		return player;
	}
}
