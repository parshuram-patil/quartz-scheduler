package com.psp.quartzscheduler.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.TimeZone;

@Data
@Builder()
public class JobInfo {
    private String identity;
    private String cronExpression;
    private String callbackData;
    private Date nextTrigger;
    private TimeZone timeZone;
}
