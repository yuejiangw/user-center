package com.yuejiangw.usercenterbackend.service;

import com.yuejiangw.usercenterbackend.model.domain.Teach;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author yuejiangwu
* @description 针对表【teach】的数据库操作Service
* @createDate 2024-09-15 13:35:51
*/
public interface TeachService extends IService<Teach> {
    void teach(final long studentId, final long planId, final HttpServletRequest request);

    void notTeach(final long planId, final HttpServletRequest request);
}
