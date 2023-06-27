package com.joolove.core.repository.query;

import com.joolove.core.domain.EActivityCode;
import com.joolove.core.domain.ETargetCode;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityElements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivityLog, UUID> {

    @Query("select new com.joolove.core.dto.query.UserActivityElements(" +
            "u.user.username, u.targetCode, u.targetName, u.activityCode, u.activityDescription) " +
            "from UserActivityLog u " +
            "where u.user.username = ?1 " +
            "and u.targetCode = ?2")
    List<UserActivityElements> findByUsername(String username, ETargetCode targetCode, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.UserActivityElements(" +
            "u.user.username, u.targetCode, u.targetName, u.activityCode, u.activityDescription) " +
            "from UserActivityLog u " +
            "where u.targetName = ?1 " +
            "and u.targetCode = ?2")
    List<UserActivityElements> findByGoodsName(String goodsName, ETargetCode targetCode, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsStats gs " +
            "inner join GoodsDetails gd " +
            "on gd.goods.id = gs.goods.id " +
            "inner join UserActivityLog u " +
            "on u.targetName = gd.name " +
            "and (u.activityCode = ?1 or u.activityCode = ?2) " +
            "and u.targetCode = ?3")
    List<IGoodsView> findGoodsListBestViews(EActivityCode activityCode1, EActivityCode activityCode2,
                                            ETargetCode targetCode, LocalDateTime days, Pageable pageable);

}
