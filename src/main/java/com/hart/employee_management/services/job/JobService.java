package com.hart.employee_management.services.job;

import com.hart.employee_management.exception.CustomException;
import com.hart.employee_management.model.Job;
import com.hart.employee_management.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService implements IJobService {

    private final JobRepository jobRepository;

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job findJobByTitle(String title) {
        Job job = jobRepository.findByTitle(title);
        if (job == null) {
            throw new CustomException("Job not found");
        }
        return job;
    }

    @Override
    public Job findJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new CustomException("Job not found!"));
    }

    @Override
    public Job createJob(Job job) {
        if (jobRepository.existsByTitle(job.getTitle())) {
            throw new CustomException("Job with the title: " + job.getTitle() + " already exist");
        }
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Job job, Long jobId) {
        Job existingJob = findJobById(jobId);
        existingJob.setTitle(job.getTitle());
        return jobRepository.save(existingJob);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new CustomException("Job not found!"));
        jobRepository.deleteById(job.getId());
    }
}
