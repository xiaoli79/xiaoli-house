package org.xiaoli.xiaolichatservice.service.impl;

import cn.hutool.core.lang.func.Func;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoli.xiaoliadminapi.appUser.domain.dto.AppUserDTO;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;
import org.xiaoli.xiaoliadminapi.appUser.feign.AppUserFeignClient;
import org.xiaoli.xiaolichatservice.domain.dto.SessionAddReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionGetReqDTO;
import org.xiaoli.xiaolichatservice.domain.dto.SessionStatusDetailDTO;
import org.xiaoli.xiaolichatservice.domain.vo.MessageVO;
import org.xiaoli.xiaolichatservice.domain.vo.SessionAddResVO;

import org.xiaoli.xiaolichatservice.domain.vo.SessionGetResVO;
import org.xiaoli.xiaolichatservice.entity.Session;
import org.xiaoli.xiaolichatservice.mapper.SessionMapper;
import org.xiaoli.xiaolichatservice.service.ChatCacheService;
import org.xiaoli.xiaolichatservice.service.ISessionService;
import org.xiaoli.xiaolicommoncore.utils.BeanCopyUtil;
import org.xiaoli.xiaolicommondomain.domain.R;
import org.xiaoli.xiaolicommondomain.domain.ResultCode;
import org.xiaoli.xiaolicommondomain.exception.ServiceException;
import org.xiaoli.xiaolicommonsecurity.service.TokenService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SessionServiceImpl implements ISessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private ChatCacheService chatCacheService;

    @Autowired
    private AppUserFeignClient appUserFeignClient;

    @Autowired
    private TokenService tokenService;

    /**
     * 新建会话
     * @param sessionAddReqDTO
     * @return
     */
    @Override
    public SessionAddResVO add(SessionAddReqDTO sessionAddReqDTO) {


        //1.排序俩用户ID
        Long loginUserId = tokenService.getLoginUser().getUserId();
        Long userId1 = sessionAddReqDTO.getUserId1();
        Long userId2 = sessionAddReqDTO.getUserId2();

        boolean isSwapped = userId1 > userId2;
        if(isSwapped){
            Long temp = userId1;
            userId1 = userId2;
            userId2 = temp;
        }


        //校验会话是否存在
        Session session = sessionMapper.selectOne(
                new LambdaQueryWrapper<Session>()
                        .eq(Session::getUserId1, userId1)
                        .eq(Session::getUserId2, userId2));

        if(null != session){
            //存在：查询缓存并返回
            SessionStatusDetailDTO sessionDTO = chatCacheService.getSessionDTOByCache(session.getId());
            assert null != sessionDTO;
            SessionAddResVO sessionAddResVO = new SessionAddResVO();
            sessionAddResVO.setSessionId(session.getId());
            sessionAddResVO.setLoginUser(sessionDTO.getFromUser(loginUserId).getUser().convertToVO());
            sessionAddResVO.setOtherUser(sessionDTO.getToUser(loginUserId).getUser().convertToVO());
            return sessionAddResVO;

        }

        //不存在
        //新建会话sql
        session = new Session();
        session.setUserId1(userId1);
        session.setUserId2(userId2);
        sessionMapper.insert(session);

        //添加会话详情缓存redis
        //查询用户信息
        R<List<AppUserVO>> r = appUserFeignClient.list(Arrays.asList(userId1, userId2));

        if(null == r || r.getCode() != ResultCode.SUCCESS.getCode() || null == r.getData()){

            log.error("新增会话时，用户未查询到，新增失败! userId1:{}, userId2:{}", userId1, userId2);
            throw new ServiceException("新增会话时，用户未查询到！");
        }


        //结果转换
                Map<Long, AppUserDTO> userMap = r.getData().stream()
                .map(appUserVO -> {
                    AppUserDTO appUserDTO = new AppUserDTO();
                    BeanCopyUtil.copyProperties(appUserVO, appUserDTO);
                    return appUserDTO;
                }).collect(Collectors.toMap(AppUserDTO::getUserId, Function.identity()));


        SessionStatusDetailDTO sessionStatusDetailDTO = new  SessionStatusDetailDTO();

        sessionStatusDetailDTO.setSessionId(session.getId());
        SessionStatusDetailDTO.UserInfo userInfo1 = new SessionStatusDetailDTO.UserInfo();
        userInfo1.setUser(userMap.get(userId1));
        sessionStatusDetailDTO.setUser1(userInfo1);
        SessionStatusDetailDTO.UserInfo userInfo2 = new SessionStatusDetailDTO.UserInfo();
        userInfo2.setUser(userMap.get(userId2));
        sessionStatusDetailDTO.setUser2(userInfo2);
        chatCacheService.cahceSessionDTO(session.getId(),sessionStatusDetailDTO);

        //构造并返回
        SessionAddResVO resVO = new SessionAddResVO();
        resVO.setSessionId(session.getId());
        resVO.setLoginUser(
                sessionStatusDetailDTO.getFromUser(loginUserId).getUser().convertToVO());
        resVO.setOtherUser(
                sessionStatusDetailDTO.getToUser(loginUserId).getUser().convertToVO());
        return resVO;
    }


    /**
     * 查询咨询会话
     * @param sessionGetReqDTO
     * @return
     */
    @Override
    public SessionGetResVO get(SessionGetReqDTO sessionGetReqDTO) {


        SessionGetResVO resVO = new SessionGetResVO();

        Long userId1 = sessionGetReqDTO.getUserId1();
        Long userId2 = sessionGetReqDTO.getUserId2();

        boolean isSwapped = userId1 > userId2;
        if(isSwapped){
            Long temp = userId1;
            userId1 = userId2;
            userId2 = temp;
        }

        //校验会话是否存在
        Session session = sessionMapper.selectOne(
                new LambdaQueryWrapper<Session>()
                        .eq(Session::getUserId1, userId1)
                        .eq(Session::getUserId2, userId2));

        //不存在，返回空
        if(null == session){
            return resVO;
        }

        //存在，查询缓存
        SessionStatusDetailDTO sessionDTO = chatCacheService.getSessionDTOByCache(session.getId());
        if(null == sessionDTO){
            throw new ServiceException("聊天会话不一致");
        }

        resVO.setSessionId(session.getId());
        if(null != sessionDTO.getLastMessageDTO()){
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(sessionDTO.getLastMessageDTO(), messageVO);
            resVO.setLastMessageVO(messageVO);
        }
        if(null != sessionDTO.getLastSessionTime()){
            resVO.setLastSessionTime(sessionDTO.getLastSessionTime());
        }

        // 为浏览数：当前登录用户未浏览对方用户的消息数，存在自己的用户信息中
        Long loginUserId = tokenService.getLoginUser().getUserId();
        resVO.setNotVisitedCount(sessionDTO.getFromUser(loginUserId).getNotVisitedCount());
        resVO.setOtherUser(sessionDTO.getToUser(loginUserId).getUser().convertToVO());
        return resVO;
    }
}
