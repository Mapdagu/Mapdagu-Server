package com.project.mapdagu.domain.friendRequest.repository;

import com.project.mapdagu.domain.friendRequest.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
}
