package cn.iosd.base.config.api.service.impl;

import cn.iosd.base.config.api.domain.BaseConfigInfo;
import cn.iosd.base.config.api.feign.BaseConfigInfoFeign;
import cn.iosd.base.config.api.service.IBaseConfigService;
import cn.iosd.base.config.api.vo.BaseConfigVo;
import cn.iosd.base.config.api.vo.CodeValue;
import cn.iosd.base.config.api.vo.CodeValueListHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ok1996
 */
@Service
public class BaseConfigInfoServiceFeignImpl implements IBaseConfigService {
    @Autowired
    private BaseConfigInfoFeign baseConfigInfoFeign;

    @Override
    public BaseConfigInfo selectByKey(String key) {
        return baseConfigInfoFeign.selectByKey(key).dataOrThrowExceptionIfNotSuccess();
    }

    @Override
    public List<CodeValue<?>> selectListByKey(String key) {
        return baseConfigInfoFeign.selectListByKey(key).dataOrThrowExceptionIfNotSuccess();
    }

    @Override
    public int insert(BaseConfigVo baseConfigVo) {
        return baseConfigInfoFeign.insert(baseConfigVo).dataOrThrowExceptionIfNotSuccess();
    }

    @Override
    public int update(BaseConfigVo baseConfigVo) {
        return baseConfigInfoFeign.update(baseConfigVo).dataOrThrowExceptionIfNotSuccess();
    }

    @Override
    public List<CodeValueListHistory> selectListHistoryByKey(String key) {
        return baseConfigInfoFeign.selectListHistoryByKey(key).dataOrThrowExceptionIfNotSuccess();
    }
}
