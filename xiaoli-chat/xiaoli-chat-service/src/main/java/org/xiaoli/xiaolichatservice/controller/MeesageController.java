package org.xiaoli.xiaolichatservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xiaoli.xiaolichatservice.domain.dto.MessageListReqDTO;
import org.xiaoli.xiaolichatservice.domain.vo.MessageVO;
import org.xiaoli.xiaolichatservice.service.IMessageService;
import org.xiaoli.xiaolicommondomain.domain.R;

import java.util.List;


@RestController
@RequestMapping("/message")
public class MeesageController {


    @Autowired
    private IMessageService messageService;


    /**
     * 查询聊天信息列表
     * @param messageListReqDTO
     * @return
     */
    @PostMapping("/list")
    public R<List<MessageVO>> list(@Validated @RequestBody MessageListReqDTO messageListReqDTO) {
        return R.ok(messageService.list(messageListReqDTO));
    }















}
