package com.chen.auth.server.scheduler;/**
 * Created by Administrator on 2019/9/9.
 */


import com.chen.auth.model.mapper.AuthTokenMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 通用的定时任务调度
 * @Author:chen (SteadyJack)
 * @Date: 2019/9/9 23:00
 **/

@Component
@EnableAsync
public class CommonScheduler {

    //打印日志
    private static final Logger log= LoggerFactory.getLogger(CommonScheduler.class);

   //注入tokenDao
    @Autowired
    private AuthTokenMapper authTokenMapper;

    //剔除(删除)已经失效的token  cron = ：建议一个月执行一次
    @Scheduled(cron = "0 0 0 28 * ?") //每个月的 28 日 00:00:00 运行
    @Async("taskExecutor")
    public void  deleteInvalidateToken(){
        log.info("剔除(删除)已经失效的token 定时任务已开始运行");
        try {
            //1.删除is_active = 0的数据
            authTokenMapper.deleteUnactiveToken();
            //2.也可以交给运维-自动抽取出那些失效的token,进行转移(et)
        } catch (Exception e) {
            //fillInStackTrace每次执行的时候，会清空原来的栈内的trace信息。然后在当前的调用位置处重新建立trace信息,提高效率。
            log.error("--剔除(删除)已经失效的token-- 发生异常：",e.fillInStackTrace());
        }
    }
}















