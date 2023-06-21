package com.joolove.core.repository.query;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.request.UserActivityElements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivityLog, UUID> {

    @Query("select new com.joolove.core.dto.request.UserActivityElements(" +
            "?1, u.targetCode, u.activityCode, u.activityDescription) " +
            "from UserActivityLog u " +
            "where u.user.username = ?1" +
            "and u.targetCode = ?2")
    List<UserActivityElements> findByUsername(String username, String targetName, Pageable pageable);

}
