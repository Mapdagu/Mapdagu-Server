package com.project.mapdagu.domain.friend.repository;

import com.project.mapdagu.domain.friend.entity.Friend;
import com.project.mapdagu.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    Optional<Friend> findByMemberAndFriend(Member member, Member friend);
}
