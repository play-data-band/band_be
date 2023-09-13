package com.example.community.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "communityMember_id")
    private CommunityMember communityMember;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    private String useYn;
    private String memberImg;
    private String memberName;

}
