package org.xiaoli.xiaolicommonredis.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.xiaoli.xiaolicommondomain.constants.CommonConstants;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
@AutoConfigureBefore(name = "org.redisson.spring.starter.RedissonAutoConfigurationV2")
public class RedisConfig {


    //完成对于RedisTemplate的配置
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

//      对于Key进行序列化
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());


//     对Value进行序列化
        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = createJacksonSerializer();
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();

        return redisTemplate;

    }

    private GenericJackson2JsonRedisSerializer createJacksonSerializer() {

        ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
//              在反序列的时候，当Json数据中存在Java对象类中没有定义的属性时，若设置true ，则Jackson会抛出异常~，改为false，则不会
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//              序列化时，若为true，将日期类型的数据转换为时间戳~~
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//              如果一个类没有成员变量，如果设置为true的话，并且序列化的话的，会直接抛出异常~~
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
//              在反序列化的时候，若type类型中有未定义的类型~~若设置为true，则会抛出异常~
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
//              这是如果Map中有以日期类型为键，若设置为true，会把其设置为时间戳的形式
                .configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false)
//              这是使Json注解生效，如果设置为true,注解则生效；反之，则失效~~
                .configure(MapperFeature.USE_ANNOTATIONS, false)

                .addModule(new JavaTimeModule())
//                这是序列化LocalDateTime和LocalDate的配置
                .addModule(new SimpleModule()
                        .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(CommonConstants.STANDARD_FORMAT)))
                        .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(CommonConstants.STANDARD_FORMAT)))
                )
//              统一日期格式
                .defaultDateFormat(new SimpleDateFormat(CommonConstants.STANDARD_FORMAT)) // TODO 魔法值需要统一管理并加上有效注释
//              只针对非空的值进行序列化~~
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build();
        return  new GenericJackson2JsonRedisSerializer(OBJECT_MAPPER);
    }
}
