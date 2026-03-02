package org.xiaoli.xiaolicommoncore.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xiaoli.xiaolicommoncore.enums.RejectType;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程数
     * 不管有没有任务，至少有这个核心线程数待命去执行任务
     */
    @Value("${thread.pool-executor.corePoolSize:5}")
    private Integer corePoolSize;



    /**
     *
     * 最大线程数
     * 核心线程数+临时线程数不能超过这个最大线程数（在任务已经超过核心线程数的时候）
     */
    @Value("${thread.pool-executor.maxPoolSize:100}")
    private Integer maxPoolSize;



    /**
     * 阻塞队列大小
     * 当核心线程全都在忙时，新来的任务会进入队列排队。
     * 只有当【队列也排满了】（而不是任务数达到最大线程数），
     * 才会开始创建新的临时线程，直到达到 maxPoolSize。
     */
    @Value("${thread.pool-executor.queueCapacity:100}")
    private Integer queueCapacity;




    /**
     * 空闲存活时间
     * 临时线程超过keepAliveSeconds，这些线程就会被销毁
     */
    @Value("${thread.pool-executor.keepAliveSeconds:60}")
    private Integer keepAliveSeconds;




    /**
     * 线程名称
     * 线程名的前缀，用来标明相关的数据有
     */
    @Value("${thread.pool-executor.prefixName:thread-service-}")
    private String prefixName;




    /**
     * 拒绝策略
     * 根据拒绝策略的不同，执行不同的策略~~
     */
    @Value("${thread.pool-executor.rejectHandler:2}")
    private Integer rejectHandler;



    /**
     * 注册和配置线程池执行器
     *
     * @return 线程池执行器
     */
    @Bean("threadPoolTaskExecutor")
    public Executor getThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix(prefixName);
        //设置线程池关闭的时候 等待所有的任务完成后再继续销毁其他的bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //策略
        executor.setRejectedExecutionHandler(getRejectHandler());
        return executor;
    }


//  四种拒绝策略~~
//  AbortPolicy
//  程序会抛出RejectedExectionException异常,你需要手动处理这个异常，否则任务丢失且报错
//  CallerRunsPolicy
//  这是代码中的默认策略。任务不会丢失，而是由提交任务的那个线程自己去执行。这会变相减慢提交任务的速度，给线程池喘息的机会~~
//  DiscardOldestPolicy
//  队里最早的任务会被丢弃，然后尝试执行新任务。可能会丢失重要数据
//  DiscardPolicy
//  任务直接被丢弃，没有任何报错。只有在任务无关紧要时候才敢使用~~

    /**
     * 拒绝策略
     *
     * @return 拒绝策略处理器
     */
    public RejectedExecutionHandler getRejectHandler() {
        if (RejectType.AbortPolicy.getValue().equals(rejectHandler)) {
            return new ThreadPoolExecutor.AbortPolicy();
        } else if (RejectType.CallerRunsPolicy.getValue().equals(rejectHandler)) {
            return new ThreadPoolExecutor.CallerRunsPolicy();
        } else if (RejectType.DiscardOldestPolicy.getValue().equals(rejectHandler)) {
            return new ThreadPoolExecutor.DiscardOldestPolicy();
        } else {
            return new ThreadPoolExecutor.DiscardPolicy();
        }
    }
}
