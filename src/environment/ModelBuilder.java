package environment;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class ModelBuilder implements ContextBuilder<Object> {
	
	private Parameters params;
	private Context<Object> context;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;

	/**
	 * Creates the context, init's variables, fills context with projections, objects, etc before returning the context
	 */
	@Override
	public Context build(Context<Object> context) {
		createContext(context);
		createParameters();
		createProjections();
		createSimulation();
		createField();
		createPlayers();
		createBoundary();
		createReferee();
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
	 * Retrieves the parameters set by the user in the runtime environment
	 * Sets these parameters to the local parameters
	 */
	public void createParameters() {
		//takes the parameters from the runtime environment
		this.params = RunEnvironment.getInstance().getParameters();
	}
	
	public void createSimulation() {
		Scheduler sim = new Scheduler(context);
		context.add(sim);
	}
	
	public void createReferee(){
		Referee ref = new Referee(context);
		context.add(ref);
	}
	
	/** 
	 * Creates both the Grid space and the Continuous Space
	 * creates the ContinuousSpace
	 * and Grid projections. In both cases, we begin by getting a factory of the appropriate type
	 * and then use that factory to create the actual ContinuousSpace or Grid. Both factories
	 * take similar parameters:
	• the name of the grid or space
	• the context to associate the grid or space with
	• an Adder which determines where objects added to the grid or space will be initially
	located
	• a class that describes the borders of the grid or space. Borders determine the
	behavior of the space or grid at its edges. For example, WrapAroundBorders will
	wrap the borders, turning the space or grid into a torus. Other border types such as
	StrictBorders will enforce the border as a boundary across which agents cannot
	move.
	• the dimensions of the grid (50 x 50 for example).
	The GridFactory differs slightly in that it bundles the borders, adder, dimensions etc.
		into a GridBuilderParameters object. The GridBuilderParameters also takes a boolean
		value that determines whether more than one object is allowed to occupy a grid point
		location at a time. With this in mind then, the above code creates a ContinuousSpace
		named “space” and associates it with the passed in Context. Any object added to this
		space will be added at a random location via the RandomCartesianAdder. The borders of
		the space will wrap around forming a torus, set via the
		repast.simphony.space.continuous.WrapAroundBorders(). Lastly, the dimensions of
		the space will be 50 x 50. This code also creates a Grid called “grid” and associates it
		with the Context. The grid will also wrap and form a torus. Objects added to this grid
		will be added with the SimpleGridAdder which means that they are not given a location
		when added, but rather held in a kind of “parking lot” waiting to be manually added via
		one of the Grid’s methods. The true value specifies that multiple occupancy of a grid
		location is allowed. The grid’s dimensions will be 50 x 50. Note the we are using the
		SimpleGridAdder here so that we can manually set an agent’s Grid location to correspond
		to its ContinuousSpace location. We will do this later in the build method.
		*/
	public void createProjections() {
		//sets the height and width of the boxed environment at runtime
				int displayHeight = (Integer)params.getValue("display_height");
				int displayWidth = (Integer)params.getValue("display_width");
				
				ContinuousSpaceFactory spaceFactory = 
						ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
				space = 
						spaceFactory.createContinuousSpace("space", context, 
								new SimpleCartesianAdder<Object>(), 
								new repast.simphony.space.continuous.StrictBorders(),
								displayWidth, displayHeight);				
				GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
				grid = gridFactory.createGrid("grid", context,
						new GridBuilderParameters<Object>(new StrictBorders(),
						new SimpleGridAdder<Object>(),
						true, displayWidth, displayHeight));
	}
	
	public void createPlayers() {
		createDefenders();
		createAttackers();
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
		// Retrieve defender count from runtime environment
				int defenderCount = (Integer)params.getValue("defender_count");
				// iterate through the count
				for(int i = 0; i < defenderCount; i++) {
					// Retrieve the starting positions from the runtime environment
					int defenderStartX = (Integer)params.getValue("defender_start_x");
					int defenderStartY = (Integer)params.getValue("defender_start_y");
					// Create a new defender for each iteration
					new Defender(context, defenderStartX, defenderStartY, i+1);
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
		// Retrieve the attacker count from the runtime environment
		int attackerCount = (Integer)params.getValue("attacker_count");
		//iterate through the count
		for (int i = 0; i < attackerCount; i++) {
			// Retrieve the starting positions from the runtime environment
			int attackerStartX = (Integer)params.getValue("attacker_start_x");
			int attackerStartY = (Integer)params.getValue("attacker_start_y");
			//create a new attacker for each iteration
			new Attacker(context, attackerStartX, attackerStartY, i+1);
		}

	}

	public void createField() {
		int displayHeight = (Integer)params.getValue("display_height");
		int displayWidth = (Integer)params.getValue("display_width");
		new Field(context,displayWidth/2,displayHeight/2);
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
		int southernSideline = (Integer)params.getValue("fieldInset");
		int northernSideline = (Integer)params.getValue("display_height")-(Integer)params.getValue("fieldInset");
		int westernTryline = (Integer)params.getValue("fieldInset")+(Integer)params.getValue("fieldIncrement");
		int easternTryline = (Integer)params.getValue("display_width")-(Integer)params.getValue("fieldInset")-(Integer)params.getValue("fieldIncrement");
		//create horizontal sidelines
		for (int i = westernTryline; i < easternTryline; i++) {
			//create upper and lower sidepoints
			new SidePoint(context, i, southernSideline);
			new SidePoint(context, i, northernSideline-1);
		}
		//create opposing tryline (sideline for this instance)
		for (int i = southernSideline; i < northernSideline; i++) {
			//create sidepoint
			new SidePoint(context, easternTryline-1, i);
		}
	}
	
	/**
	 * creates a new trypoint at each point along the left edge of the display
	 */
	public void createTryline() {
		int southernSideline = (Integer)params.getValue("fieldInset");
		int northernSideline = (Integer)params.getValue("display_height")-(Integer)params.getValue("fieldInset");
		int easternTryline = (Integer)params.getValue("fieldInset")+(Integer)params.getValue("fieldIncrement");
		for (int i = southernSideline; i < northernSideline; i++) {
			//create a new attacker for each iteration
			new TryPoint(context, easternTryline, i);
		}
	}

}