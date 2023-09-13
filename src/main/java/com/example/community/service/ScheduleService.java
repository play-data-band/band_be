package com.example.community.service;

import com.example.community.common.RestError;
import com.example.community.common.RestResult;
import com.example.community.domain.dto.AttendanceRequestDto;
import com.example.community.domain.dto.ScheduleMemberCountDto;
import com.example.community.domain.entity.Attendance;
import com.example.community.domain.entity.Community;
import com.example.community.domain.entity.CommunityMember;
import com.example.community.domain.entity.Schedule;
import com.example.community.domain.request.ScheduleRequest;
import com.example.community.domain.response.ScheduleResponse;
import com.example.community.repository.AttendanceRepository;
import com.example.community.repository.CommunityMemberRepository;
import com.example.community.repository.CommunityRepository;
import com.example.community.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CommunityRepository communityRepository;
    private final AttendanceRepository attendanceRepository;
    private final CommunityMemberRepository communityMemberRepository;

    public void save(Long communityId, ScheduleRequest request) {

        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        Schedule schedule = Schedule.builder()
                .scheduleName(request.getScheduleName())
                .scheduleTime(request.getScheduleTime())
                .meetingPlace(request.getMeetingPlace())
                .price(request.getPrice())
                .maxParticipation(request.getMaxParticipation())
                .community(community)
                .interest(request.getInterest())
                .build();
        scheduleRepository.save(schedule);
    }

    public List<ScheduleResponse> findAll(Long communityId) {
        Community community = new Community();
        community.setId(communityId);
        List<Schedule> scheduleList = scheduleRepository.findByCommunity(community);
        return scheduleList.stream().map(ScheduleResponse::new).collect(Collectors.toList());
    }

    public ScheduleResponse findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
        return new ScheduleResponse(schedule);
    }

    public List<ScheduleResponse> findAllSchedule() {
        List<Schedule> scheduleList = scheduleRepository.findAll();
        return scheduleList.stream().map(ScheduleResponse::new).collect(Collectors.toList());
    }

    public List<ScheduleResponse> findUpcomingSchedulesByInterest(List<String> interest) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Schedule> scheduleList = scheduleRepository.getScheduleByMain(interest, currentTime);
        return scheduleList.stream().map(ScheduleResponse::new).collect(Collectors.toList());
    }

    public void deleteByScheduleId(Long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    public void updateSchedule(Long communityId, Long scheduleId, ScheduleRequest request) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new RuntimeException("Community not found"));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getCommunity().equals(community)) {
            throw new RuntimeException("Mismatched Community and Schedule");
        }
        schedule.setScheduleName(request.getScheduleName());
        schedule.setScheduleTime(request.getScheduleTime());
        schedule.setMeetingPlace(request.getMeetingPlace());
        schedule.setPrice(request.getPrice());
        schedule.setMaxParticipation(request.getMaxParticipation());

        scheduleRepository.save(schedule);
    }

    //스케줄 참석/불참
    public ResponseEntity<RestResult<Object>> toggleAttendance(AttendanceRequestDto attendanceRequestDto) {
        Long memberId = attendanceRequestDto.getMemberId();
        Long communityId = attendanceRequestDto.getCommunityId();
        Long scheduleId = attendanceRequestDto.getScheduleId();
        String useYn = attendanceRequestDto.getUseYn();

        CommunityMember communityMember = communityMemberRepository.findByCommunityIdAndMemberId(communityId, memberId).orElseThrow(
                () -> new RuntimeException("CommunityMember not found!"));
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Schedule not found!"));

        Optional<Attendance> attendance = attendanceRepository.findByCommunityMemberAndSchedule(communityMember, schedule);

        if (schedule.getCommunity().getId() == communityMember.getCommunityId()) {
            if ("Y".equals(useYn) && !attendance.isPresent()) {
                //참석할 수 있는 최대 인원과 현재 참석한 인원 비교
                if (schedule.getParticipant() < schedule.getMaxParticipation()) {
                    //참석
                    Attendance newAttendance = new Attendance();
                    newAttendance.setCommunityMember(communityMember);
                    newAttendance.setSchedule(schedule);
                    newAttendance.setUseYn("Y");
                    attendanceRepository.save(newAttendance);

                    // 참석자 카운트 증가
                    schedule.setParticipant(schedule.getParticipant() + 1);
                    scheduleRepository.save(schedule);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new RestResult<>("BAD_REQUEST", new RestError("BAD_REQUEST", "일정 정원이 초과되었습니다.")));
                }
            } else if ("N".equals(useYn) && attendance.isPresent()) {
                //불참
                schedule.setParticipant(schedule.getParticipant() - 1);
                scheduleRepository.save(schedule);
                attendanceRepository.delete(attendance.get());
            }
        }
        return ResponseEntity.ok(new RestResult<>("success", "참석이 완료 되었습니다."));
    }
}


