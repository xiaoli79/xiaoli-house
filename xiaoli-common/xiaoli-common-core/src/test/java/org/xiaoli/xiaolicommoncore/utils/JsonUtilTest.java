package org.xiaoli.xiaolicommoncore.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.xiaoli.xiaolicommoncore.utils.domain.*;

import java.util.*;


class JsonUtilTest {


//  序列化
    @Test
    void obj2String() {
        TestClass testClass = new TestClass();
        testClass.setName("test");
        testClass.setAge(123);
        testClass.setId(123);
        System.out.println(JsonUtil.obj2String(testClass));
    }


//  序列化 格式化~~
    @Test
    void obj2StringPretty() {
        TestClass testClass = new TestClass();
        testClass.setName("test");
        testClass.setAge(123);
        testClass.setId(123);
        System.out.println(JsonUtil.obj2StringPretty(testClass));
    }

//  反序列化
    @Test
    void string2Obj() {
        String str = "{\"id\":123,\"name\":\"test\",\"age\":123}";
        TestClass testClass = JsonUtil.string2Obj(str, TestClass.class);
        System.out.println(testClass);

    }

//  对列表进行泛型擦除~~
    @Test
    void string2List() {

//        TestClass testClass = new TestClass();
//        testClass.setName("test");
//        testClass.setAge(123);
//        testClass.setId(123);
//        List<TestClass> list = new ArrayList<>();
//        list.add(testClass);
//        System.out.println(JsonUtil.obj2String(list));

//      这是未解决泛型擦除的问题~~
        List list1 = JsonUtil.string2Obj("[{\"id\":123,\"name\":\"test\",\"age\":123}]", List.class);
        System.out.println(list1);

//      反序列化~~将Json字符串，转变成对象~~
        List<TestClass> lists = JsonUtil.string2List("[{\"id\":123,\"name\":\"test\",\"age\":123}]", TestClass.class);
        System.out.println(lists);
    }


    @Test
    void string2Map() {
//        TestClass testClass = new TestClass();
//        testClass.setName("test");
//        testClass.setAge(123);
//        testClass.setId(123);
//        LinkedHashMap<String, TestClass> map = new LinkedHashMap<>();
//        map.put("test", testClass);
////      先获取Json字符串~~
//        System.out.println(JsonUtil.obj2String(map));

//      将Json字符串反序列成对象~~
        Map<String, TestClass> stringTestClassMap = JsonUtil.string2Map("{\"test\":{\"id\":123,\"name\":\"test\",\"age\":123}}", TestClass.class);
        System.out.println(stringTestClassMap);


    }

    @Test
    void testString2Obj() {

//        TestClass testClass = new TestClass();
//        testClass.setName("test");
//        testClass.setAge(123);
//        testClass.setId(123);
//        List<Map<String,TestClass>> list = new ArrayList<>();
//        Map<String,TestClass> map = new HashMap<>();
//        map.put("test", testClass);
//        list.add(map);
//        System.out.println(JsonUtil.obj2String(list));
        List<Map<String, TestClass>> maps = JsonUtil.string2Obj("[{\"test\":{\"id\":123,\"name\":\"test\",\"age\":123}}]", new TypeReference<List<Map<String, TestClass>>>() {
        });
        System.out.println(maps);
    }


    @Test
    public void jsonConfigTest1(){
//        TestUser user = new TestUser();
//        user.setName("test");
//        System.out.println(JsonUtil.obj2String(user));
////      当有未知的参数时候，通过配置，进而让他不会抛出异常！！
//        String jsonStr = "{\"name\":\"test\",\"age\":123}";
//
//        System.out.println(JsonUtil.string2Obj(jsonStr, TestUser.class));

//        TestUser testUser = new TestUser();
//        testUser.setName("test");
//        testUser.setDate(new Date());
//        System.out.println(JsonUtil.obj2String(testUser));

//        TestUser testUser = new TestUser();
//        Map<Date,String> map = new LinkedHashMap<>();
//        Date date = new Date();
//        map.put(date,"111");
//        testUser.setMap(map);
//        testUser.setName("test");
//        System.out.println(JsonUtil.obj2String(testUser));

    }
    @Test
    public void jsonConfigTest2() {

//        TestAnimal testAnimal = new TestAnimal();
//        System.out.println(JsonUtil.obj2String(testAnimal));
//
//        TestDog testDog = new TestDog();
//        testDog.setName("testDog");
//        testDog.setAge(123);
//        System.out.println(JsonUtil.obj2String(testDog));
//
//        TestCat cat = new TestCat();
//        cat.setName("testCat");
//        cat.setSex("母");
//        System.out.println(JsonUtil.obj2String(cat));

        String dogJson = "{\"type\":\"dog\",\"name\":\"testDog\",\"age\":123}";
        String catJson = "{\"type\":\"cat\",\"sex\":\"母\"}";
        String birdJson = "{\"type\":\"bird\",\"sex\":\"母\"}";


        System.out.println(JsonUtil.string2Obj(dogJson, TestAnimal.class));
        System.out.println(JsonUtil.string2Obj(catJson, TestAnimal.class));
        System.out.println(JsonUtil.string2Obj(birdJson, TestAnimal.class));





    }





}