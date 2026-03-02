package org.xiaoli.xiaolicommoncore.utils.domain;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class TestUser1 {


    public String name;

    public Integer age;


    public String sex;

    public String QQ;

    public String Wechat;

    private Date date;

    private Map<Date,String> map;

//  处理特殊的拷贝场景~~
    public TestUserVO convertToVO(){
        TestUserVO testUserVO = new TestUserVO();
        BeanUtils.copyProperties(this, testUserVO);
        testUserVO.setContactInfo(this.Wechat + "--" + this.QQ);
        return testUserVO;
    }

//  对列表的拷贝进行处理~~
    public static List<TestUserVO> convertToVOList(List<TestUser1> list){
        if(list == null || list.isEmpty()){
            return null;
        }

        List<TestUserVO> testUserVOList = new ArrayList<>(list.size());
//      对这个列表进行遍历，进而对每一个元素进行一个Bean拷贝
        for(TestUser1 testUser1 : list){
            TestUserVO testUserVO = new TestUserVO();
//          实现单个拷贝
            BeanUtils.copyProperties(testUser1, testUserVO);
//          处理特殊情况
            testUserVO.setContactInfo(testUser1.getWechat() + "--" + testUser1.getQQ());
            testUserVOList.add(testUserVO);
        }
        return testUserVOList;
    }







}
