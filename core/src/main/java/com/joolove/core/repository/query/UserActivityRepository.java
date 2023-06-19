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

    // 최근 5개를 그냥 가져오면 안되고, targetName이 goodsName인 경우에만 가져와야한다.
    @Query("select new com.joolove.core.dto.request.UserActivityElements(" +
            "?1, u.targetName, u.activityCode, u.activityDescription) " +
            "from UserActivityLog u " +
            "where u.user.username = ?1")
    List<UserActivityElements> findByUsername(String username, Pageable pageable);

}
