package com.psp.quartzscheduler.controller;

import com.psp.quartzscheduler.model.JobInfo;
import com.psp.quartzscheduler.service.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

    private final SchedulerService service;

    @Autowired
    public SchedulerController(SchedulerService service) {
        this.service = service;
    }

    @PostMapping("/schedule/{identity}")
    public void runHelloWorldJob(@PathVariable String identity) throws ParseException {
        service.schedule(identity);
    }

    @GetMapping
    public List<JobInfo> getAllRunningTimers() {
        return service.getInfo();
    }

    @GetMapping("/{identity}")
    public JobInfo getRunningTimer(@PathVariable String identity) {
        return service.getInfo(identity);
    }

    @DeleteMapping("/{identity}")
    public Boolean deleteTimer(@PathVariable String identity) {
        return service.deleteTimer(identity);
    }
}
