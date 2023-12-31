package com.example.community.controller;

import com.example.community.common.RestResult;
import com.example.community.domain.dto.AttendanceRequestDto;
import com.example.community.domain.request.ScheduleRequest;
import com.example.community.domain.response.ScheduleResponse;
import com.example.community.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping("{communityId}")
    public void save(@PathVariable("communityId") Long communityId,
            @RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.save(communityId, scheduleRequest);
    }

    @GetMapping("/community/{id}")
    public List<ScheduleResponse> findAll(@PathVariable("id") Long communityId) {
        List<ScheduleResponse> all = scheduleService.findAll(communityId);
        return all;
    }

    @GetMapping
    public List<ScheduleResponse> findAllSchedule() {
        return scheduleService.findAllSchedule();
    }

    //http://localhost:8080/api/v1/schedule/upcoming-by-interest?interest=사교/인맥&interest=공예/만들기
    @GetMapping("/upcoming-by-interest")
    public List<ScheduleResponse> getScheduleByInterest(@RequestParam List<String> interest) {
        List<ScheduleResponse> upcomingSchedulesByInterest = scheduleService.findUpcomingSchedulesByInterest(interest);
        System.out.println(upcomingSchedulesByInterest);
        return upcomingSchedulesByInterest;
    }

    @GetMapping("{id}")
    public ScheduleResponse findById(@PathVariable("id") Long id) {
        return scheduleService.findById(id);
    }

    @DeleteMapping("/{scheduleId}")
    public void deleteSchedule(@PathVariable("scheduleId") Long scheduleId) {
        scheduleService.deleteByScheduleId(scheduleId);
    }

    @PutMapping("/{communityId}/{scheduleId}")
    public void updateSchedule(@PathVariable("communityId") Long communityId,
                               @PathVariable("scheduleId") Long scheduleId,
                               @RequestBody ScheduleRequest scheduleRequest) {
        scheduleService.updateSchedule(communityId, scheduleId, scheduleRequest);
    }

    @PostMapping("/attendance")
    public ResponseEntity<RestResult<Object>> toggleAttendance(
            @RequestBody AttendanceRequestDto attendanceRequestDto) {
        return scheduleService.toggleAttendance(attendanceRequestDto);
    }
}
