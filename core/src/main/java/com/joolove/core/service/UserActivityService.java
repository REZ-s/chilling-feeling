package com.joolove.core.service;

import com.joolove.core.domain.EActivityCode;
import com.joolove.core.domain.ETargetCode;
import com.joolove.core.domain.log.UserActivityLog;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.request.UserActivityElements;
import com.joolove.core.repository.query.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 인기 상품 조회
    public List<IGoodsView> findBestViewsUserActivityList() {
        int page = 0;
        int size = 10;

        return userActivityRepository.findGoodsListBestViews(EActivityCode.SEARCH.name(), EActivityCode.CLICK.name(), ETargetCode.GOODS.name(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

}
