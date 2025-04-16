package com.hart.employee_management.controller;

import com.hart.employee_management.model.Job;
import com.hart.employee_management.response.ApiResponse;
import com.hart.employee_management.services.job.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService jobService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllJobs() {
        try {
            List<Job> jobs = (List<Job>) jobService.getAllJobs();
            return ResponseEntity.ok(new ApiResponse(false, "Jobs retrieved", jobs));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    new ApiResponse(true, "Something went wrong, please try again.", null
            ));
        }
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse> findJobById(@PathVariable Long jobId) {
        try {
            Job job = jobService.findJobById(jobId);
            return ResponseEntity.ok(new ApiResponse(false, "Job retrieved", job));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createJob(@RequestBody Job job) {
        try {
            Job newJob = jobService.createJob(job);
            return ResponseEntity.ok(new ApiResponse(false, "Job created successfully", newJob));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @PutMapping("/update/{jobId}")
    public ResponseEntity<ApiResponse> updateJob (@RequestBody Job job, @PathVariable Long jobId) {
        try {
            Job updatedJob = jobService.updateJob(job, jobId);
            return ResponseEntity.ok(new ApiResponse(false, "Job updated successfully", updatedJob));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(true, e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{jobId}")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable Long jobId) {
        try {
            jobService.deleteJob(jobId);
            return ResponseEntity.ok(new ApiResponse(false, "Job deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse(true, e.getMessage(), null));
        }
    }
}
