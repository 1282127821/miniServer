package scheduler.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import scheduler.constant.CronConst;
import scheduler.constant.JobGroupEnum;
import scheduler.job.common.server.OneHourQuartzJob;
import scheduler.job.model.JobEntry;
import utils.snow.IdUtil;

/**
 * 定时调度服务
 *
 * @author : ddv
 * @since : 2019/7/8 下午11:05
 */
@Component
public class QuartzService {
    private static final Logger logger = LoggerFactory.getLogger(QuartzService.class);
    /**
     * jobId -- jobDetail 所有的修正都是通过 删除-添加
     */
    private static Map<Long, JobDetail> jobDetailMap = new ConcurrentHashMap<>();

    private Scheduler scheduler;

    // FIXME quartz监听是真的🐔 居然没有后置回调 现在周期性buff清除逻辑先写在buff逻辑里吧
    public void init() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            initOneHourJob();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    // 整点任务
    private void initOneHourJob() {
        String groupName = JobGroupEnum.PUBLIC_COMMON.name();
        String jobName = IdUtil.getLongId() + "";
        String triggerName = IdUtil.getLongId() + "";

        JobDetail jobDetail = JobBuilder.newJob(OneHourQuartzJob.class).withIdentity(jobName, groupName).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, groupName)
            .withSchedule(cronSchedule(CronConst.ONE_HOUR)).startNow().forJob(jobDetail.getKey()).build();

        addJob(jobDetail, trigger);
        logger.info("初始化整点任务...");
    }

    public void removeJob(JobDetail jobDetail) {
        try {
            scheduler.deleteJob(jobDetail.getKey());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addJob(JobDetail jobDetail, Trigger trigger) {
        logger.info("新的调度作业[{}]", jobDetail.getKey().getGroup());
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            logger.error("提交调度任务异常");
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void scheduleJob(JobEntry jobEntry) {
        addJob(jobEntry.getJobDetail(), jobEntry.getTrigger());
    }
}
