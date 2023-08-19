package com.joolove.core.repository.query;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityLogElements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, UUID> {

    @Transactional
    @Modifying
    @Query("update UserActivityLog ual " +
            "set ual.updatedDate = ?4 " +
            "where ual.deviceId = ?1 " +
            "and ual.targetName = ?2 " +
            "and ual.targetCode = ?3")
    void updateByDeviceIdAndTargetNameAndTargetCode(UUID deviceId, String targetName, UserActivityLog.ETargetCode targetCode, LocalDateTime nowDate);

    @Transactional
    @Modifying
    @Query("delete from UserActivityLog ual " +
            "where ual.deviceId = ?1 " +
            "and ual.targetName = ?2 " +
            "and ual.targetCode = ?3")
    void deleteByDeviceIdAndTargetNameAndTargetCode(UUID deviceId, String targetName, UserActivityLog.ETargetCode targetCode);

    boolean existsByDeviceIdAndTargetNameAndTargetCode(UUID deviceId, String targetName, UserActivityLog.ETargetCode targetCode);

    @Query("select new com.joolove.core.dto.query.UserActivityLogElements(" +
            "ual.deviceId, ual.username, ual.targetCode, ual.targetName, ual.activityCode, ual.activityDescription) " +
            "from UserActivityLog ual " +
            "where ual.deviceId = ?1 " +
            "and ual.targetCode = ?2")
    List<UserActivityLogElements> findByDeviceIdAndTargetCode(UUID deviceId, UserActivityLog.ETargetCode targetCode, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.UserActivityLogElements(" +
            "ual.deviceId, ual.username, ual.targetCode, ual.targetName, ual.activityCode, ual.activityDescription) " +
            "from UserActivityLog ual " +
            "where ual.username = ?1 " +
            "and ual.targetCode = ?2")
    List<UserActivityLogElements> findByUsernameAndTargetCode(String username, UserActivityLog.ETargetCode targetCode, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.UserActivityLogElements(" +
            "ual.deviceId, ual.username, ual.targetCode, ual.targetName, ual.activityCode, ual.activityDescription) " +
            "from UserActivityLog ual " +
            "where ual.targetName = ?1 " +
            "and ual.targetCode = ?2")
    List<UserActivityLogElements> findByGoodsNameAndTargetCode(String goodsName, UserActivityLog.ETargetCode targetCode, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.UserActivityLogElements(" +
            "ual.deviceId, ual.username, ual.targetCode, ual.targetName, ual.activityCode, ual.activityDescription) " +
            "from UserActivityLog ual " +
            "where ual.updatedDate > ?1 " +
            "and ual.activityCode = 'SEARCH' " +
            "and ual.targetCode = 'GOODS' " +
            "group by ual.deviceId, ual.username, ual.targetCode, ual.targetName, ual.activityCode, ual.activityDescription " +
            "order by count(ual.targetName) desc ")
    List<UserActivityLogElements> findGoodsListBestViewsBySearchPart1(LocalDateTime days, Pageable pageable);

    @Query(value = "select gd.name as name, gd.type as type, gd.sub_type as subType, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and match(gd.name) against(concat('*', ?1, '*') in boolean mode)", nativeQuery = true)
    List<IGoodsView> findGoodsListBestViewsBySearchPart2(String targetNames, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsStats gs " +
            "inner join GoodsDetails gd " +
            "on gs.goods.id = gd.goods.id " +
            "inner join UserActivityLog ual " +
            "on gd.name = ual.targetName " +
            "and ual.updatedDate > ?1 " +
            "and ual.activityCode = 'CLICK' " +
            "and ual.targetCode = 'GOODS' " +
            "group by gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount " +
            "order by count(ual.targetName) desc ")
    List<IGoodsView> findGoodsListBestViewsByClick(LocalDateTime days, Pageable pageable);
}
