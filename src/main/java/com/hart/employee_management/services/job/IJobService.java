package com.hart.employee_management.services.job;

import com.hart.employee_management.model.Job;

import java.util.List;

public interface IJobService {
    List<Job> getAllJobs();
    Job findJobByTitle(String title);
    Job findJobById(Long id);
    Job createJob(Job job);
    Job updateJob(Job job, Long jobId);
    void deleteJob(Long id);
}
