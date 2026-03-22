package org.xiaoli.xiaolichatservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolichatservice.domain.dto.SessionAddReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionGetReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionListReqDTO;
import org.xiaoli.xiaolichatservice.domain.vo.SessionAddResVO;
import org.xiaoli.xiaolichatservice.domain.vo.SessionGetResVO;
import org.xiaoli.xiaolichatservice.service.ISessionService;
import org.xiaoli.xiaolicommondomain.domain.R;

import java.util.List;

@RestController
@RequestMapping("/session")
public class SessionController {
//

    @Autowired
    private ISessionService sessionService;


    /**
     * 新建会话
     * @param sessionAddReqDTO
     * @return
     */
    @PostMapping("/add")
    public R<SessionAddResVO> add(@Validated @RequestBody SessionAddReqDTO sessionAddReqDTO){
        return R.ok(sessionService.add(sessionAddReqDTO));
    }

    /**
     * 查询咨询会话
     * @param sessionGetReqDTO
     * @return
     */
    @PostMapping("/get")
    public R<SessionGetResVO> get(@Validated @RequestBody SessionGetReqDTO sessionGetReqDTO){
        return R.ok(sessionService.get(sessionGetReqDTO));
    }


    /**
     * 获取会话列表
     * @param sessionListReqDTO
     * @return
     */
    @PostMapping("/list")
    public R<List<SessionGetResVO>> list(@Validated @RequestBody SessionListReqDTO sessionListReqDTO){
        return R.ok(sessionService.list(sessionListReqDTO));
    }























}
