package environment;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;

/**
 * Builds the model
 * @author user
 *
 */

public class ModelBuilder implements ContextBuilder<Object> {
	
	private Context<Object> context;

	/**
	 * Creates the context, init's variables, fills context with projections, objects, etc before returning the context
	 */
	@Override
	public Context<Object> build(Context<Object> context) {
		createContext(context);
		createSimulation();
		createField();
		createPlayers();
		createBalls();
		return this.context;
	}
	
	/**
	 * Creates the context and sets the context id to Footy (should match context in XML file)
	*/
	public void createContext(Context<Object> context) {
		this.context = context;
		this.context.setId("Footy");
	}
	
	/**
	 * Sets up the space and parameters to be used by the simulation
	 */
	public void createSimulation(){
		
		//Creates the space which the simulation will be played on
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, 
				new SimpleCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.StrictBorders(),
				Params.displayWidth, Params.displayHeight);	
		
		//Calls the make Params method, which renders the params class ready for use in the simulation
		Params.makeParams(context, space);
	}
	
	/**
	 * Creates the players
	 */
	public void createPlayers() {
		createWesterners();
		createEasterners();
	}
	
	/**
	 * Creates the balls and adds them to the field at the position nominated in the parameters
	 */
	public void createBalls(){
		
		// iterate through the count
		for(int i = 0; i < Params.getBallCount(); i++) {
			new Ball(Params.getBallStartX(), Params.getBallStartY());
		}
	}
	
	/**
	 * Creates the westerners
	*/
	public void createWesterners() {
		
		//iterate through the count
		for (int i = 0; i < Params.getWesternerCount(); i++) {
			
			//set a variable for the y axis
			double y;
			
			//if the count is 1, y is the starting y
			if(Params.getWesternerCount()==1){
				y = Params.getWesternerStartY();
				
				//else distribute evenly along the y axis
			} else {
				y = Params.fieldInset+((i+1)*((Params.displayHeight-(2*Params.fieldInset))/(Params.getWesternerCount()+1)));
			}
			
			//create a new attacker for each iteration
			new Westerner(Params.getWesternerStartX()+(Params.getmaxStagger()*RandomHelper.nextDoubleFromTo(-1.0,1.0)), y, i+1);
		}
	}
	
	/**
	 * Creates the easterners
	*/
	public void createEasterners() {
		
		//iterate through the count
		for (int i = 0; i < Params.getEasternerCount(); i++) {
			
			//set a variable for the y axis
			double y;
			
			//create v formation
			int x = Params.getEasternerCount()/2;
			int setback = Math.abs(i-x);
			
			//if the count is 1, y is the starting y
			if(Params.getEasternerCount()==1){
				y = Params.getEasternerStartY();
				
				//else distribute evenly along the y axis
			} else {
				y = Params.fieldInset+((i+1)*((Params.displayHeight-(2*Params.fieldInset))/(Params.getEasternerCount()+1)));
			}
			
			//create a new attacker for each iteration
			new Easterner(Params.getEasternerStartX()+(setback*Params.getmaxStagger()*RandomHelper.nextDoubleFromTo(0.0,1.0)), y, i+1);
		}

	}

	/**
	 * Creates the field agent and places it in the middle of the screen
	 */
	public void createField() {
		new Field(Params.displayWidth/2,Params.displayHeight/2);
		createBoundary();
	}
	
	/**
	 * creates the boundary of the simulated field - 1 tryline and three sidelines
	 */
	public void createBoundary() {
		createSidelines();
		createTrylines();
	}
	
	/**
	 * creates sideline points at each point along the upper, lower and right edges of the display
	 */
	public void createSidelines() {
		//create horizontal sidelines
		for (double i = Params.westernTryline; i <= Params.easternTryline; i=i+Params.boundaryFrequency) {
			
			//create upper and lower sidepoints
			new SidePoint(i, Params.southernSideline);
			new SidePoint(i, Params.northernSideline);
		}
	}
	
	/**
	 * creates the east and west trylines
	 */
	public void createTrylines() {
		for (double i = Params.southernSideline; i <= Params.northernSideline; i=i+Params.boundaryFrequency) {
			//create a new trypoint for each iteration
			new WestTryPoint(Params.westernTryline, i);
		}
		
		//create opposing tryline
		for (double i = Params.southernSideline; i <= Params.northernSideline; i=i+Params.boundaryFrequency) {
			//create sidepoint
			new EastTryPoint(Params.easternTryline, i);
		}
	}

}