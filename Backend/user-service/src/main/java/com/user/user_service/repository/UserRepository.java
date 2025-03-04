package com.user.user_service.repository;

import com.user.user_service.DTO.UserSummaryDTO;
import com.user.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserEmail(String email);

    @Query("SELECT new com.user.user_service.DTO.UserSummaryDTO(u.userId, u.userName, u.userProfilePhoto) FROM User u")
    List<UserSummaryDTO> findAllUsersWithProfile();

}
