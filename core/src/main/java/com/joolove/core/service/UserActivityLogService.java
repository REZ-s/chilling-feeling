package com.joolove.core.service;

import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityLogElements;
import com.joolove.core.repository.query.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserActivityLogService {
    private final UserActivityLogRepository userActivityLogRepository;

    // 같은 deviceId 에 같은 목적코드 같은 검색어가 들어오면, 기존 로그를 찾아서 updatedDate 를 갱신한다.
    @Transactional
    public void addUserActivityLog(UserActivityLog userActivityLog) {
        if (userActivityLogRepository.existsByDeviceIdAndTargetNameAndTargetCode(
                userActivityLog.getDeviceId(), userActivityLog.getTargetName(), UserActivityLog.ETargetCode.GOODS)) {
            userActivityLogRepository.updateByDeviceIdAndTargetNameAndTargetCode(
                    userActivityLog.getDeviceId(), userActivityLog.getTargetName(), UserActivityLog.ETargetCode.GOODS, LocalDateTime.now());
            return;
        }

        userActivityLogRepository.save(userActivityLog);
    }

    @Transactional
    public void removeUserActivityLog(UUID deviceId, String targetName, UserActivityLog.ETargetCode targetCode) {
        if (userActivityLogRepository.existsByDeviceIdAndTargetNameAndTargetCode(
                deviceId, targetName, UserActivityLog.ETargetCode.GOODS)) {
            userActivityLogRepository.deleteByDeviceIdAndTargetNameAndTargetCode(deviceId, targetName, UserActivityLog.ETargetCode.GOODS);
        }
    }

    public List<UserActivityLogElements> findUserActivityListByDeviceIdAndTargetCode(UUID deviceId, UserActivityLog.ETargetCode targetCode) {
        int page = 0;
        int size = 5;

        return userActivityLogRepository.findByDeviceIdAndTargetCode(deviceId, targetCode,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedDate")));
    }

    public List<UserActivityLogElements> findUserActivityListByUsernameAndTargetCode(String username, UserActivityLog.ETargetCode targetCode) {
        int page = 0;
        int size = 5;

        return userActivityLogRepository.findByUsernameAndTargetCode(username, targetCode,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedDate")));
    }

    public List<UserActivityLogElements> findUserActivityListByGoodsNameAndTargetCode(String goodsName, UserActivityLog.ETargetCode targetCode) {
        int page = 0;
        int size = 5;

        return userActivityLogRepository.findByGoodsNameAndTargetCode(goodsName, targetCode,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedDate")));
    }

    // 인기 상품 조회 (기본값)
    public List<IGoodsView> findBestViewsUserActivityListDefault() {
        return findBestViewsUserActivityList(0, 10, (short) 7);
    }

    // 인기 상품 조회
    public List<IGoodsView> findBestViewsUserActivityList(Integer page, Integer size, Short days) {
        int defaultPage = 0;
        int defaultSize = 10;
        LocalDateTime defaultDays = LocalDateTime.now().minusDays(7);
        int requestedPage = page != null ? page : defaultPage;
        int requestedSize = size != null ? size : defaultSize;
        LocalDateTime requestedDays = days != null ? LocalDateTime.now().minusDays(days) : defaultDays;

        // Search log 에서 name 은 정확한 상품명이 아닐 가능성이 높으므로, (1) 먼저 조회수가 많은 문자열을 가져와서 (2) 부분 문자열로 fts
        List<UserActivityLogElements> activityLogElementsList = userActivityLogRepository.findGoodsListBestViewsBySearchPart1(requestedDays,
                PageRequest.of(requestedPage, requestedSize));

        Set<String> targetNameSet = new HashSet<>();
        for (UserActivityLogElements e : activityLogElementsList) {
            targetNameSet.add(e.getTargetName());
        }
        String targetNames = String.join(" ", targetNameSet);

        List<IGoodsView> goodsViewList = userActivityLogRepository.findGoodsListBestViewsBySearchPart2(targetNames, PageRequest.of(requestedPage, requestedSize));

        // Click log 에서 name 은 직접 상품을 클릭했으므로, 정확한 이름 일치(equal) 조회
        goodsViewList.addAll(userActivityLogRepository.findGoodsListBestViewsByClick(requestedDays,
                PageRequest.of(requestedPage, requestedSize)));

        return goodsViewList;
    }

}
