package environment;


/**
 * head is used to setup the head
 * @author user
 *
 */

public class Head extends MovingAgent{

	private Player player;
	
	Head(double x, double y, Player player){
		super(x, y);	
		this.player = player;
	}

	Player getPlayer(){
		return player;
	}
}
