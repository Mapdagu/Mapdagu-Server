package com.project.mapdagu.domain.friend.repository;

import com.project.mapdagu.domain.friend.entity.Friend;
import com.project.mapdagu.domain.member.entity.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByMemberAndFriend(Member member, Member friend);

    @Query("select f.friend from Friend f " +
            " where f.member.id =:memberId ")
    List<Member> findAllFriendByMemberId(@Param("memberId") Long memberId);

    @Query("select f.member from Friend f " +
            " where f.friend.id =:friendId ")
    List<Member> findAllMemberByFriendId(@Param("friendId") Long friendId);

}