package com.psp.quartzscheduler.utils;

import com.psp.quartzscheduler.model.JobData;
import com.psp.quartzscheduler.model.JobInfo;
import org.quartz.*;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;

public final class SchedulerUtils {
    private SchedulerUtils() {}

    public static JobDetail buildJobDetail(final Class jobClass, final JobData jobData) {
        final JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(jobData.getIdentity(), jobData);
        return JobBuilder
                .newJob(jobClass)
                .withIdentity(jobData.getIdentity())
                .setJobData(jobDataMap)
                .build();
    }

    public static Trigger buildTrigger(final JobData jobData) {

        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobData.getIdentity())
                .withSchedule(cronSchedule(jobData.getCronExpression()))
                .build();
    }

    public static JobInfo buildJobInfo(JobData jobData) {
        return JobInfo.builder()
                .identity(jobData.getIdentity())
                .cronExpression(jobData.getCronExpression().getCronExpression())
                .callbackData(jobData.getCallbackData())
                .nextTrigger(jobData.getCronExpression().getNextValidTimeAfter(new Date()))
                .timeZone(jobData.getCronExpression().getTimeZone())
                .build();
    }
}
