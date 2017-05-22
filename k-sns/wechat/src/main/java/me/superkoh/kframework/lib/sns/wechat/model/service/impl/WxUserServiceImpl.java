package me.superkoh.kframework.lib.sns.wechat.model.service.impl;

import me.superkoh.kframework.lib.sns.wechat.model.domain.WxUser;
import me.superkoh.kframework.lib.sns.wechat.model.mapper.WxUserMapper;
import me.superkoh.kframework.lib.sns.wechat.model.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by KOH on 2017/5/22.
 * <p>
 * k-framework
 */
@Service
public class WxUserServiceImpl implements WxUserService {
    @Autowired
    private WxUserMapper wxUserMapper;

    @Override
    public WxUser getUserByUnionId(String unionId) {
        return wxUserMapper.selectByUnionId(unionId);
    }
}
