package org.xiaoli.xiaolicommoncore.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.xiaoli.xiaolicommoncore.utils.domain.TestUser1;
import org.xiaoli.xiaolicommoncore.utils.domain.TestUserVO;

import java.util.ArrayList;
import java.util.List;

class BeanCopyUtilTest {

    @Test
    void copyList() {

        TestUser1 testUser3 = new TestUser1();
        testUser3.setName("testUser3");
        testUser3.setAge(123);
        testUser3.setWechat("wechat");
        testUser3.setQQ("123213");
        TestUserVO testUserVO = testUser3.convertToVO();
        System.out.println(testUserVO);

        TestUser1 testUser1 = new TestUser1();
        testUser1.setName("xiaoli");
        testUser1.setAge(18);
        testUser1.setSex("男");
        testUser1.setWechat("666");
        testUser1.setQQ("666");

        TestUser1 testUser2 = new TestUser1();
        testUser2.setName("xiaoli666");
        testUser2.setAge(19);
        testUser2.setSex("男");

        List<TestUser1> list = new ArrayList<>();
        list.add(testUser1);
        list.add(testUser2);
        System.out.println(list);

//      列表的Bean拷贝~~
        List<TestUserVO> testUserVOS = BeanCopyUtil.copyList(list, TestUserVO::new);
        System.out.println(testUserVOS);

        List<TestUserVO> testUserVOS1 = TestUser1.convertToVOList(list);
        System.out.println(testUserVOS1);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class SourcePoJo {
        private String username;
        private Long id;
    }

    @Data
    class TargetPoJo {
        private String username;
        private String id;
    }

    @Data
    class TargetPoJo2 {
        private String username;
        private Long id;
    }

//  属性类型不一致导致拷贝失败
    @Test
    void copyfaili() {
        SourcePoJo sourcePoJo = new SourcePoJo("bite", (long) 1000000);
        TargetPoJo targetPoJo = new TargetPoJo();
        BeanUtils.copyProperties(sourcePoJo, targetPoJo);
        System.out.println(targetPoJo);
    }

//  底层为反射 拷贝效率低
    @Test
    void copyslow() {
        long copyStartTime = System.currentTimeMillis();
        SourcePoJo source = new SourcePoJo("bite", 1000000L);
        TargetPoJo2 target = new TargetPoJo2();
        for (int i = 0; i < 10000; i++) {
            BeanUtils.copyProperties(source, target);
        }
        System.out.println("copy方式: " + (System.currentTimeMillis() - copyStartTime));

        long setStartTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            target.setUsername(source.getUsername());
            target.setId(source.getId());
        }
        System.out.println("set方式: " + (System.currentTimeMillis() - setStartTime));
    }

// 为浅拷贝
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Card{
        private String name;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Person{
        private String name;
        private Card card;
    }


    @Test
    void copyLight(){

        Person person = new Person("xiaoli",new Card("xiaoli"));
        Person target = new Person();
//      拷贝后的对象
        BeanUtils.copyProperties(person,target);
        System.out.println(target);
        person.getCard().setName("lixiao");
        System.out.println(target);


//      修改后的代码，用来解决浅拷贝的问题
        Card targetCard = new Card();
        BeanUtils.copyProperties(target.getCard(),targetCard);
        target.setCard(targetCard);
        person.getCard().setName("xiaoli");
        System.out.println(target);

    }



    // 4. 内部类数据无法成功拷贝
    @Data
    @ToString
    public class SourcePoJoInner {
        private String username;
        private Long id;
        public InnerClass innerClass;

        @Data
        @ToString
        @AllArgsConstructor
        public static class InnerClass {
            public String innerName;
        }
    }

    @Data
    @ToString
    public class TargetPoJoInner {
        private String username;
        private Long id;
        public InnerClass innerClass;

        @Data
        @ToString
        public static class InnerClass {
            public String innerName;
        }
    }

    @Test
    void copyInnerClass() {
        SourcePoJoInner sourcePoJo = new SourcePoJoInner();
        sourcePoJo.setUsername("bite");
        //这是给内部类进行赋值
        SourcePoJoInner.InnerClass innerClass = new SourcePoJoInner.InnerClass("sourceInner");
        sourcePoJo.innerClass = innerClass;
        System.out.println(sourcePoJo);

        TargetPoJoInner targetPoJo = new TargetPoJoInner();
        BeanUtils.copyProperties(sourcePoJo, targetPoJo);
        System.out.println(targetPoJo);

        //修改后的代码
        TargetPoJoInner targetPoJoInner = new TargetPoJoInner();
        TargetPoJoInner.InnerClass targetInnerClass = new TargetPoJoInner.InnerClass();
//      把这个有值 直接拷贝到targetInnerClass即可
        BeanUtils.copyProperties(innerClass, targetInnerClass);
//      为这个类进行赋值
        targetPoJoInner.innerClass = targetInnerClass;
//      即可成功完成打印
        System.out.println(targetPoJoInner);


    }


}