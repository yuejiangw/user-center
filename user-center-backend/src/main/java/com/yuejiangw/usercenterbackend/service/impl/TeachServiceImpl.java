package com.yuejiangw.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuejiangw.usercenterbackend.common.UserUtils;
import com.yuejiangw.usercenterbackend.model.domain.Teach;
import com.yuejiangw.usercenterbackend.model.domain.User;
import com.yuejiangw.usercenterbackend.service.TeachService;
import com.yuejiangw.usercenterbackend.mapper.TeachMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
* @author yuejiangwu
* @description 针对表【teach】的数据库操作Service实现
* @createDate 2024-09-15 13:35:51
*/
@Service
public class TeachServiceImpl extends ServiceImpl<TeachMapper, Teach> implements TeachService {

    @Resource
    final private TeachMapper teachMapper;

    public TeachServiceImpl(final TeachMapper teachMapper) {
        this.teachMapper = teachMapper;
    }

    @Override
    public void teach(final long studentId, final long planId, final HttpServletRequest request) {
        final User currentUser = UserUtils.getCurrentUser(request);

        // build a Teach object
        final Teach teach = Teach.builder()
                .teacherId(currentUser.getId())
                .studentId(studentId)
                .planId(planId)
                .build();

        this.save(teach);
    }

    @Override
    public void notTeach(final long planId, final HttpServletRequest request) {
        final User currentUser = UserUtils.getCurrentUser(request);
        teachMapper.deleteById(planId);
    }
}
