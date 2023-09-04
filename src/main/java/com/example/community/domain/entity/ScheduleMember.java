package com.example.community.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class ScheduleMember {
    @Id @GeneratedValue
    private Long id;
    private Long memberId;
    private Long scheduleId;
    private String memberImg;
    private String memberName;
}
