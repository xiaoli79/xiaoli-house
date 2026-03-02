package org.xiaoli.xiaolimstemplateservice.test;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.xiaoli.xiaolimstemplateservice.domain.ValidationUserReqDTO;

@RestController
@RequestMapping("/test/validated")
@Validated
public class TestValidatedController {


//  以RequestBody传递参数
    @PostMapping("/a1")
    public int a1 (@RequestBody @Validated ValidationUserReqDTO userDto){
        System.out.println(userDto);
        return 0;
    }

// 以RequestParam传递参数~
    @GetMapping("/a2")
    public int a2 (@Validated ValidationUserReqDTO userDto){
        System.out.println(userDto);
        return 0;
    }

//  平铺式写法~~
    @GetMapping("/a3")
    public int a3( @NotNull (message = "昵称不能为空") String name , @Min(value = 0, message = "id不能小于0岁") int id){
        return 0;
    }

//  以PathVariable
    @GetMapping("/a4/{id}/{name}")
    public int a4(@Min(value = 0, message = "id不能小于0岁") @PathVariable Integer id,@NotNull(message = "昵称不能为空")  @PathVariable String name){
        return 0;
    }
}