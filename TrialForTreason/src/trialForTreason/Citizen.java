/**
 * 
 */
package trialForTreason;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;

/**
 * @author nick
 *
 */
public class Citizen {
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private int energy, startingEnergy;
	Map<Double, Double> up_cascade_list;
	double signal_accuracy;

	public Citizen(ContinuousSpace<Object> space, Grid<Object> grid, int energy, Map<Double, Double> up_cascade_list, double signal_accuracy ) {
		this.space = space;
		this.grid = grid;
		this.energy = startingEnergy = energy;
		this.up_cascade_list = up_cascade_list;
		this.signal_accuracy = signal_accuracy;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {  
		if (energy < 5) {
			handShake();
			if (seeing_monuments(this.signal_accuracy, this.up_cascade_list) == true) {
				System.out.println("I'll secure the city due to historic cascade");
			}
			}
		}
	
	
	public void handShake() {
//		Context context = ContextUtils.getContext(this);
		System.out.println("hello, I am a citizen");
	}
	
	public Boolean seeing_monuments(double signal_accuracy, Map<Double, Double> up_cascade_list) {
		return (up_cascade_list.get(signal_accuracy) > 0.6) ;	
	}
		
	}


