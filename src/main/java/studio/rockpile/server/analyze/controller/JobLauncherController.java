package studio.rockpile.server.analyze.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.constant.DataTypeEnum;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.entity.JobNamedParam;
import studio.rockpile.server.analyze.job.BatchJobConstructor;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;
import studio.rockpile.server.analyze.job.JobParamConstructor;
import studio.rockpile.server.analyze.job.example.JobMetaInfoExample;
import studio.rockpile.server.analyze.protocol.CommonResult;
import studio.rockpile.server.analyze.protocol.JobMetaInfo;
import studio.rockpile.server.analyze.protocol.JobExecuteRequest;
import studio.rockpile.server.analyze.util.SpringContextUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobLauncher")
public class JobLauncherController {
    private static final Logger logger = LoggerFactory.getLogger(JobLauncherController.class);

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private BatchJobConstructor jobConstructor;
    @Autowired
    private JobParamConstructor paramConstructor;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    // http://127.0.0.1:50306/analyze/jobLauncher/demo/jobMetaInfo
    @RequestMapping(value = "/demo/jobMetaInfo", method = RequestMethod.GET)
    public CommonResult<?> demoJobMetaInfo() {
        try {
            JobMetaInfo jobMetaInfo = JobMetaInfoExample.getMetaInfo();
            return CommonResult.success(jobMetaInfo);
        } catch (Exception e) {
            logger.error("服务处理异常：", e);
            return CommonResult.failed("服务处理异常：" + e.getMessage());
        }
    }

    // http://127.0.0.1:50306/analyze/jobLauncher/exec/job
    @RequestMapping(value = "/exec/job", method = RequestMethod.POST)
    public CommonResult<?> execBatchJob(@RequestBody JobExecuteRequest execReq) throws Exception {
        try {
            // 从redis缓存中获取JobMetaInfo，缓存没有读取数据库
            // JobMetaInfo jobMetaInfo = jobMetaProvider.getMetaInfo(execReq.getJobId());
            // 测试数据模拟调用
            JobMetaInfo jobMetaInfo = JobMetaInfoExample.getMetaInfo();
            String jobEnvBean = jobConstructor.build(jobMetaInfo);

            Map<String, Object> args = execReq.getArgs();
            List<JobNamedParam> namedParams = jobMetaInfo.getNamedParams();
            JobParameters params = paramConstructor.build(namedParams, args);

            BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
            JobExecution execution = jobLauncher.run(jobEnv.getJob(), params);
            long instanceId = execution.getJobInstance().getInstanceId();
            BatchStatus status = execution.getStatus();
            if (BatchStatus.FAILED.equals(status)) {
                return CommonResult.failed("批处理作业执行失败，instance_id=" + instanceId);
            } else {
                return CommonResult.success("批处理作业执行成功，instance_id=" + instanceId);
            }
        } catch (Exception e) {
            logger.error("batch job ({}) processing fail：{}", "demoBatchJob", e);
            return CommonResult.failed("job processing fail：" + e.getMessage());
        }

    }

    // http://127.0.0.1:50306/analyze/jobLauncher/batch/demo/exec?message=rockpile
    @RequestMapping(value = "/batch/demo/exec", method = RequestMethod.GET)
    public CommonResult<?> execDemoBatchJob(@RequestParam(value = "message", required = true) String message) {
        try {
            Date launchTime = Calendar.getInstance().getTime();
            JobParametersBuilder builder = new JobParametersBuilder();
            // JobParameter : { Object parameter, ParameterType parameterType }
            builder.addParameter(PublicDomainDef.JOB_LAUNCH_TIME_PARAM_KEY, new JobParameter(launchTime));
            builder.addParameter(PublicDomainDef.JOB_LAUNCH_DAY_PARAM_KEY, new JobParameter(Long.valueOf(formatter.format(launchTime))));
            builder.addParameter("message", new JobParameter(message));
            JobParameters params = builder.toJobParameters();

            // 启动任务，并把参数传递给任务
            JobExecution execution = jobLauncher.run(SpringContextUtil.getBean("demoBatchJob", Job.class), params);
            long instanceId = execution.getJobInstance().getInstanceId();
            BatchStatus status = execution.getStatus();
            if (BatchStatus.FAILED.equals(status)) {
                return CommonResult.failed("批处理作业执行失败，instance_id=" + instanceId);
            } else {
                return CommonResult.success("批处理作业执行成功，instance_id=" + instanceId);
            }
        } catch (Exception e) {
            logger.error("batch job ({}) processing fail：{}", "demoBatchJob", e);
            return CommonResult.failed("job processing fail：" + e.getMessage());
        }
    }
}
