package com.example.community.domain.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Table(name = "Schedules")
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String scheduleName;
    private LocalDateTime scheduleTime;
    private String meetingPlace;
    private String price;
    private int maxParticipation;
    private int participant;
    private String interest;
    private String memberName;
    private String memberImage;

    public void setParticipant(int participant) {
        if (participant < 0) {
            this.participant = 0;
        } else {
            this.participant = participant;
        }
    }

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

}
