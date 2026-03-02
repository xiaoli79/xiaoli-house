package org.xiaoli.xiaolifileservice.domain.vo;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignVO {

//  获取签名信息
    private String signature;


    private String host;


    private String pathPrefix;


    private String xOSSCredential;

    private String xOSSDate;

    private String policy;
}

