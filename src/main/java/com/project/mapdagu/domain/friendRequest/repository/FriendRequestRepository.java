package com.project.mapdagu.domain.friendRequest.repository;

import com.project.mapdagu.domain.friendRequest.entity.FriendRequest;
import com.project.mapdagu.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByFromMemberAndToMember(Member fromMember, Member toMember);

    @Query("select f from FriendRequest f " +
            " join fetch f.fromMember " +
            " where f.toMember.id =:toMemberId")
    Slice<FriendRequest> findAllByToMemberQuery(@Param("toMemberId") Long toMemberId, Pageable pageable);
}
