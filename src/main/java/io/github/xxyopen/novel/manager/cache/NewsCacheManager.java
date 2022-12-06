package io.github.xxyopen.novel.manager.cache;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.xxyopen.novel.core.constant.CacheConsts;
import io.github.xxyopen.novel.core.constant.DatabaseConsts;
import io.github.xxyopen.novel.dao.entity.NewsInfo;
import io.github.xxyopen.novel.dao.mapper.NewsInfoMapper;
import io.github.xxyopen.novel.dto.resp.NewsInfoRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsCacheManager {

    private final NewsInfoMapper newsInfoMapper;

    @Cacheable(cacheManager = CacheConsts.CAFFEINE_CACHE_MANAGER, value = CacheConsts.LATEST_NEWS_CACHE_NAME)
    public List<NewsInfoRespDto> listLatestNews() {
        QueryWrapper<NewsInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_2.getSql());
        return newsInfoMapper.selectList(queryWrapper).stream().map(v ->NewsInfoRespDto.builder()
                .id(v.getId())
                .categoryId(v.getCategoryId())
                .categoryName(v.getCategoryName())
                .title(v.getTitle())
                .sourceName(v.getSourceName())
                .updateTime(v.getUpdateTime())
                .build()).toList();
    }
}
