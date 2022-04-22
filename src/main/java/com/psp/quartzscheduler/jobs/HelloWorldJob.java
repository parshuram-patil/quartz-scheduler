package com.psp.quartzscheduler.jobs;

import com.psp.quartzscheduler.model.JobData;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloWorldJob implements Job {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String identity = context.getJobDetail().getKey().getName();
        JobData jobData = (JobData) jobDataMap.get(identity);
        Date date = new Date();
        LOG.info("Now - {}", date);
        LOG.info("Job ID - {}", identity);
        LOG.info("Callback data - {}", jobData.getCallbackData());
        LOG.info("Next valid trigger - {}", jobData.getCronExpression().getNextValidTimeAfter(date));
        LOG.info("\n ---------------------------------------------------- \n\n");
    }
}
