package org.xiaoli.xiaolichatservice.domain.vo;

import lombok.Data;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;

@Data
public class SessionGetResVO {

    /**
     * 会话Id
     */
    private Long sessionId;
    /**
     * 最后一条消息信息
     */
    private MessageVO lastMessageVO;
    /**
     * 最后会话时间
     */
    private Long lastSessionTime;
    /**
     * 消息未浏览数
     */
    private Integer notVisitedCount;
    /**
     * 对方信息
     */
    private AppUserVO otherUser;

}