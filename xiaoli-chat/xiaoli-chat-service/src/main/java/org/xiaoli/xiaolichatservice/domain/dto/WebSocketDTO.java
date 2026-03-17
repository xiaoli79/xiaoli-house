package org.xiaoli.xiaolichatservice.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketDTO<T>{

    private String type;

    private T data;


}
