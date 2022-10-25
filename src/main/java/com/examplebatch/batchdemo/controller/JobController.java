package com.examplebatch.batchdemo.controller;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;


@Profile("!worker")
@RequestMapping("/job")
@Controller
public class JobController {

    private final JobOperator jobOperator;

    public JobController(JobOperator jobOperator) {
        this.jobOperator = jobOperator;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public long launch(@RequestParam Integer minId, @RequestParam Integer maxId) throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {
        return this.jobOperator.start("job", String.format("minId=%s, maxId=%s,date=%s", minId, maxId, System.currentTimeMillis()));
    }

    @GetMapping("/health")
    public String health() {
        return "HELLOOOOO";
    }
}
