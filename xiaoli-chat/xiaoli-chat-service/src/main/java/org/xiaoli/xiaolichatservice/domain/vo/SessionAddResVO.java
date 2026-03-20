package org.xiaoli.xiaolichatservice.domain.vo;


import lombok.Data;
import org.xiaoli.xiaoliadminapi.appUser.domain.vo.AppUserVO;

@Data
public class SessionAddResVO {

    private Long sessionId;

    private AppUserVO loginUser;

    private AppUserVO otherUser;
}
