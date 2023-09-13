package com.example.community.repository;

import com.example.community.domain.entity.Attendance;
import com.example.community.domain.entity.CommunityMember;
import com.example.community.domain.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByCommunityMemberAndSchedule(CommunityMember communityMember, Schedule schedule);
}
