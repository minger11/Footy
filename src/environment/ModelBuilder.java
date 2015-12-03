package environment;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;

public class ModelBuilder implements ContextBuilder<Object> {
	
	private Context<Object> context;
	private ContinuousSpace<Object> space;

	/**
	 * Creates the context, init's variables, fills context with projections, objects, etc before returning the context
	 */
	@Override
	public Context<Object> build(Context<Object> context) {
		createContext(context);
		createProjections();	
		createSimulation();
		createScheduler();
		createMessageBoard();
		createField();
		createMover();
		createPlayers();
		createBall();
		createBoundary();
		createReferee();
		return this.context;
	}
	
	/**
	 * Creates both the continuous space and grid style layouts for display
	 */
	public void createProjections() {
		//Creates the space
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", context, 
				new SimpleCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.StrictBorders(),
				Sim.displayWidth, Sim.displayHeight);				
	}
	
	public void createSimulation(){
		Sim.makeSim(context, space);
	}
	
	/**
	 * Creates the context and sets the context id to Footy (should match context in XML file)
	*/
	public void createContext(Context<Object> context) {
		this.context = context;
		this.context.setId("Footy");
	}
	
	/**
	 * Creates a scheduler to tell each class when and what to do
	 */
	public void createScheduler() {
		Scheduler sim = new Scheduler();
		context.add(sim);
	}
	
	/**
	 * Creates a referee to adjudicate the game
	 */
	public void createReferee(){
	}
	
	/**
	 * Creates a messageboard for audio messages to be sent
	 */
	public void createMessageBoard(){
	}
	
	/**
	 * Creates a mover that moves all movable objects at the end of each step and checks for collisions
	 */
	public void createMover(){
	}
	
	/**
	 * Initiates both create attackers and create defenders
	 */
	public void createPlayers() {
		createDefenders();
		createAttackers();
	}
	
	/**
	 * Creates the ball and adds it to the field at the position nominated in the parameters
	 */
	public void createBall(){
		new Ball(Sim.ballStartX, Sim.ballStartY);
	}
	
	/**
	 * Creates the defenders and positions them in the relevant spaces
	 * This should be relatively straight forward. We create a specified number of Defenders
	 * by looping through some creation code the specified number of times. 
	 * In this case we loop through the amount of times as specified in the parameters
	 * Note that the parameter’s value is returned as an Object so we need to cast it appropriately
	 * We add the new Defenders to context. 
	 * In adding them to the context we automatically add them to any projections associated with that context. 
	 * So in this case, the Defenders are added to the space and grid using their Adders as described above. 
	*/
	public void createDefenders() {
				
				// iterate through the count
				for(int i = 0; i < Sim.westernerCount; i++) {
					
					// Create a new defender for each iteration
					new Westerner(Sim.westernerStartX, Sim.westernerStartY, i+1);
				}
	}
	
	/**
	 * Creates the Attackers and positions them in the relevant spaces
	 * This should be relatively straight forward. We create a specified number of Attackers
	 * by looping through some creation code the specified number of times. 
	 * In this case we loop through the amount of times as specified in the parameters
	 * Note that the parameter’s value is returned as an Object so we need to cast it appropriately
	 * We add the new Attackers to context. 
	 * In adding them to the context we automatically add them to any projections associated with that context. 
	 * So in this case, the Attackers are added to the space and grid using their Adders as described above. 
	 * The Attackers are created with a random energy level from 4 to 10. 
	 * We use the RandomHelper to do this for us. 
	 * In general, all random number type operations should be done through the RandomHelper. 
	*/
	public void createAttackers() {
		
		//iterate through the count
		for (int i = 0; i < Sim.easternerCount; i++) {
			
			double y;
			if(Sim.easternerCount==1){
				y = Sim.easternerStartY;
			} else {
				y = Sim.fieldInset+((i+1)*(Sim.displayHeight/(Sim.easternerCount+1)));
			}
			
			//create a new attacker for each iteration
			new Easterner(Sim.easternerStartX, y, i+1);
		}

	}

	/**
	 * Creates the field agent and places it in the middle of the screen
	 */
	public void createField() {
		new Field(Sim.displayWidth/2,Sim.displayHeight/2);
	}
	
	/**
	 * creates the boundary of the simulated field - 1 tryline and three sidelines
	 */
	public void createBoundary() {
		createSidelines();
		createTryline();
	}
	
	/**
	 * creates sideline points at each point along the upper, lower and right edges of the display
	 */
	public void createSidelines() {
		
		double southernSideline = Sim.fieldInset;
		double northernSideline = Sim.displayHeight-Sim.fieldInset;
		double westernTryline = Sim.fieldInset+Sim.fieldIncrement;
		double easternTryline = Sim.displayWidth-Sim.fieldInset-Sim.fieldIncrement;
		
		//create horizontal sidelines
		for (double i = westernTryline; i <= easternTryline; i=i+10) {
			
			//create upper and lower sidepoints
			new SidePoint(i, southernSideline);
			new SidePoint(i, northernSideline);
		}
	}
	
	/**
	 * creates a new trypoint at each point along the left edge of the display
	 */
	public void createTryline() {
		double southernSideline = Sim.fieldInset;
		double northernSideline = Sim.displayHeight-Sim.fieldInset;
		double westernTryline = Sim.fieldInset+Sim.fieldIncrement;
		double easternTryline = Sim.displayWidth-Sim.fieldInset-Sim.fieldIncrement;
		for (double i = southernSideline; i <= northernSideline; i=i+10) {
			//create a new trypoint for each iteration
			new WestTryPoint(westernTryline, i);
		}
		
		//create opposing tryline (sideline for this instance)
		for (double i = southernSideline; i <= northernSideline; i=i+10) {
			//create sidepoint
			new EastTryPoint(easternTryline, i);
		}
	}

}