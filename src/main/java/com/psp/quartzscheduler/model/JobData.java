package com.psp.quartzscheduler.model;

import lombok.Builder;
import lombok.Data;
import org.quartz.CronExpression;

@Data
@Builder(toBuilder = true)
public class JobData {

    private String identity;
    private String callbackData;
    private CronExpression cronExpression;

    public static JobDataBuilder builder(String identity, CronExpression cronExpression) {
        return new JobDataBuilder()
                .identity(identity)
                .cronExpression(cronExpression);
    }
}
