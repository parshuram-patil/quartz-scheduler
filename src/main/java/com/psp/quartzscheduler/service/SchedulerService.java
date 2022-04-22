package com.psp.quartzscheduler.service;

import com.psp.quartzscheduler.jobs.HelloWorldJob;
import com.psp.quartzscheduler.model.JobData;
import com.psp.quartzscheduler.model.JobInfo;
import com.psp.quartzscheduler.utils.SchedulerUtils;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SchedulerService {
    private static final Logger LOG = LoggerFactory.getLogger(SchedulerService.class);
    private final Scheduler scheduler;

    @Autowired
    public SchedulerService(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public <T extends Job> void schedule(String identity) throws ParseException {
        CronExpression cronExpression = new CronExpression("*/2 */2 * * * ?");
        JobData jobData = JobData.builder(identity, cronExpression)
                .callbackData("Hello World Job")
                .build();

        final JobDetail jobDetail = SchedulerUtils.buildJobDetail(HelloWorldJob.class, jobData);
        final Trigger trigger = SchedulerUtils.buildTrigger(jobData);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public List<JobInfo> getInfo() {
        try {
            Date now = new Date();
            return scheduler.getJobKeys(GroupMatcher.anyGroup())
                    .stream()
                    .map(jobKey -> {
                        try {
                            final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                            JobData jobData =  (JobData) jobDetail.getJobDataMap().get(jobKey.getName());
                            LOG.info("Next valid trigger getInfo() - {}", jobData.getCronExpression().getNextValidTimeAfter(now));
                            return SchedulerUtils.buildJobInfo(jobData);
                        } catch (final SchedulerException e) {
                            LOG.error(e.getMessage(), e);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (final SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return Collections.emptyList();
        }
    }

    public JobInfo getInfo(final String identity) {
        try {
            final JobDetail jobDetail = scheduler.getJobDetail(new JobKey(identity));
            if (jobDetail == null) {
                LOG.error("Failed to find scheduler with ID '{}'", identity);
                return null;
            }
            JobData jobData =  (JobData) jobDetail.getJobDataMap().get(identity);
            return SchedulerUtils.buildJobInfo(jobData);
        } catch (final SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public Boolean deleteTimer(final String identity) {
        try {
            return scheduler.deleteJob(new JobKey(identity));
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
            return false;
        }
    }

    @PostConstruct
    public void init() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @PreDestroy
    public void preDestroy() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
