package brain;

/**
 * Stategy class for the player
 * Should have subclasses attack/defend 
 * Each subclass could have various subclasses ie different strategies of defence
 * @author user
 *
 */
public class Strategy {

	public void Defend(){
		//knowMyMark();
		//knowMyself();
		//knowBall();
		//makeDecision();
		//moveMofo();
	}
	
	public void Attack(){
		Brain.strafeSideToSide();
	}
	
}
