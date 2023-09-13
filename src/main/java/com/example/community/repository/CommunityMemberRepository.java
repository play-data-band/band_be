package com.example.community.repository;

import com.example.community.domain.entity.Community;
import com.example.community.domain.entity.CommunityMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityMemberRepository extends CustomCommunityMemberRepository, JpaRepository<CommunityMember, Long> {
    List<CommunityMember> findAllByCommunityId(Long communityId);

    List<CommunityMember> findAllByMemberId(Long memberId);

    @Modifying
    @Query("DELETE from CommunityMember c " +
            "where c.communityId = :communityId and " +
            "c.memberId =:memberId ")
    void deleteByMemberIdAndCommunityId(@Param("memberId") Long memberId,@Param("communityId") Long communityId);

    Optional<CommunityMember> findByCommunityIdAndMemberId(Long communityId, Long memberId);
}
