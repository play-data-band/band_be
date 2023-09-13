package com.example.community.domain.dto;

import com.example.community.domain.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleMemberCountDto {
    private Long scheduleId;
    private int maxParticipation;
    private int participant;
    private String message;

}
