package com.aem.SampleProject.core.schedulers;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OSGI R6 Annotation Scheduler Example
 * 
 *
 *
 */
@Component(immediate = true, service = OSGIR6SchedulerExample.class)
@Designate(ocd = OSGIR6SchedulerExampleConfiguration.class)
public class OSGIR6SchedulerExample implements Runnable {

	@Reference
	private Scheduler scheduler;
	private int schedulerID;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Activate
	protected void activate(OSGIR6SchedulerExampleConfiguration config) {
		schedulerID = config.schedulerName().hashCode();
	}

	@Modified
	protected void modified(OSGIR6SchedulerExampleConfiguration config) {
		removeScheduler();
		schedulerID = config.schedulerName().hashCode(); // update schedulerID
		addScheduler(config);
	}

	@Deactivate
	protected void deactivate(OSGIR6SchedulerExampleConfiguration config) {
		removeScheduler();
	}

	/**
	 * Remove a scheduler based on the scheduler ID
	 */
	private void removeScheduler() {
		logger.debug("Removing Scheduler Job '{}'", schedulerID);
		scheduler.unschedule(String.valueOf(schedulerID));
	}

	/**
	 * Add a scheduler based on the scheduler ID
	 */
	private void addScheduler(OSGIR6SchedulerExampleConfiguration config) {
		if (config.serviceEnabled()) {
			ScheduleOptions sopts = scheduler.EXPR(config.schedulerExpression());
			sopts.name(String.valueOf(schedulerID));
			sopts.canRunConcurrently(false);
			scheduler.schedule(this, sopts);
			logger.debug("Scheduler added succesfully");
		} else {
			logger.debug("OSGIR6SchedulerExample is Disabled, no scheduler job created");
		}
	}

	@Override
	public void run() {
		logger.debug("Inside OSGIR6SchedulerExample run Method");
		logger.info("Inside OSGIR6SchedulerExample run Method - info");
	}

}
