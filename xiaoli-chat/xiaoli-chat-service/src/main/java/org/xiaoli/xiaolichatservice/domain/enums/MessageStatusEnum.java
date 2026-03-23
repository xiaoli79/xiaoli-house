package org.xiaoli.xiaolichatservice.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageStatusEnum {


    MESSAGE_UNREAD(0, "未读"),
    MESSAGE_READ(1, "已读"),

    MESSAGE_NOT_VISITED(10, "未浏览"),
    MESSAGE_VISITED(11, "已浏览");


    private final Integer code;

    private final String info;


    public static MessageStatusEnum getByCode(Integer code) {
        for (MessageStatusEnum messageStatusEnum : MessageStatusEnum.values()) {
            if (messageStatusEnum.getCode().equals(code)) {
                return messageStatusEnum;
            }
        }
        return null;
    }

}







