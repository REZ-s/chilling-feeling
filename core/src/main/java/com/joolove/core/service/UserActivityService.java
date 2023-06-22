package com.joolove.core.service;

import com.joolove.core.domain.EActivityCode;
import com.joolove.core.domain.ETargetCode;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.UserActivityElements;
import com.joolove.core.repository.query.UserActivityRepository;
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
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    @Transactional
    public void addUserActivity(UserActivityLog userActivityLog) {
        userActivityRepository.save(userActivityLog);
    }

    public List<UserActivityElements> findUserActivityListByUsername(String username, ETargetCode target) {
        int page = 0;
        int size = 5;

        return userActivityRepository.findByUsername(username, target.name(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

    public List<UserActivityElements> findUserActivityListByGoodsName(String goodsName, ETargetCode target) {
        int page = 0;
        int size = 5;

        return userActivityRepository.findByGoodsName(goodsName, target.name(),
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

        return userActivityRepository.findGoodsListBestViews(
                EActivityCode.SEARCH.name(), EActivityCode.CLICK.name(), ETargetCode.GOODS.name(), requestedDays,
                PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

}
