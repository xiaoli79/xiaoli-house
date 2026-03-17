package org.xiaoli.xiaolichatservice.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor


//注意@Data
//不包含@AllArgsConstructor和@NoArgsConstructor
public enum  WebSocketDataTypeEnum {

    //这个需要全参的构造方法来接收其值
    TEXT("text","文本消息"),   //这个就可以叫做枚举对象
    HEART_BEAT("heart_beat","心跳检测"),
    CHAT("chat","聊天信息");

    //有final的话必须要把值给初始化好
    private final String type;
    private final String desc;

    public static WebSocketDataTypeEnum getByType(String type){
        for(WebSocketDataTypeEnum typeEnum:WebSocketDataTypeEnum.values()){
            if(typeEnum.getType().equalsIgnoreCase(type)){
                return typeEnum;
            }
        }
        return null;
    }

    //1.枚举中常见的内置方法
    //name() 返回枚举常量的名字
    //System.out.println(WebSocketDataTypeEnum.TEXT.name());
    //TEXT

    //2.ordinal() 返回枚举常量在定义时的顺序，从0开始
    //System.out.println(WebSocketDataTypeEnum.TEXT.ordinal());   输出 1
    //System.out.println(WebSocketDataTypeEnum.HEART_BEAT.ordinal());   输出 2
    //System.out.println(WebSocketDataTypeEnum.CHAT.ordinal());    输出  3

    //3.values()   返回当前枚举的所有枚举值数组
    //for (WebSocketDataTypeEnum item : WebSocketDataTypeEnum.values()) {
    //    System.out.println(item);
    //}
    //输出  TEXT , HEART_BEAT , CHAT

    //4.valueof(String name)   根据枚举常量名来获取对应的枚举对象
    //WebSocketDataTypeEnum e = WebSocketDataTypeEnum.valueOf("TEXT");
    //System.out.println(e);   输出 TEXT对象

}
