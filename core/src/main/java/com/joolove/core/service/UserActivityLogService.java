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
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserActivityLogService {

    private final UserActivityLogRepository userActivityLogRepository;

    @Transactional
    public void addUserActivityLog(UserActivityLog userActivityLog) {
        userActivityLogRepository.save(userActivityLog);
    }

    public List<UserActivityLogElements> findUserActivityListByUsername(String username, UserActivityLog.ETargetCode target) {
        int page = 0;
        int size = 5;

        return userActivityLogRepository.findByUsername(username, target,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

    public List<UserActivityLogElements> findUserActivityListByGoodsName(String goodsName, UserActivityLog.ETargetCode target) {
        int page = 0;
        int size = 5;

        return userActivityLogRepository.findByGoodsName(goodsName, target,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

    public List<IGoodsView> findBestViewsUserActivityListDefault() {
        return findBestViewsUserActivityList(0, 10, LocalDateTime.now().minusDays(7));
    }

    // 인기 상품 조회용
    public List<IGoodsView> findBestViewsUserActivityList(Integer page, Integer size, LocalDateTime days) {
        int defaultPage = 0;
        int defaultSize = 10;
        LocalDateTime defaultDays = LocalDateTime.now().minusDays(7);
        int requestedPage = page != null ? page : defaultPage;
        int requestedSize = size != null ? size : defaultSize;
        LocalDateTime requestedDays = days != null ? days : defaultDays;

        return userActivityLogRepository.findGoodsListBestViews(
                UserActivityLog.EActivityCode.SEARCH, UserActivityLog.EActivityCode.CLICK, UserActivityLog.ETargetCode.GOODS, requestedDays,
                PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

}
