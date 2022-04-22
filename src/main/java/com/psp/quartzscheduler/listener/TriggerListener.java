package com.psp.quartzscheduler.listener;

import com.psp.quartzscheduler.service.SchedulerService;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TriggerListener implements org.quartz.TriggerListener {
    private static final Logger LOG = LoggerFactory.getLogger(TriggerListener.class);

    private final SchedulerService schedulerService;

    public  TriggerListener(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public String getName() {
        return TriggerListener.class.getSimpleName();
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        this.schedulerService.storeJobs(context.getJobDetail());
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        LOG.error("Trigger Misfire - {}", trigger.getJobKey());
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, Trigger.CompletedExecutionInstruction triggerInstructionCode) {

    }
}
