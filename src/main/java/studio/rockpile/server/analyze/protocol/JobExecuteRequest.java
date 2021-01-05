package studio.rockpile.server.analyze.protocol;

import java.util.Map;

public class JobExecuteRequest {
    private Long jobId;
    private Map<String, Object> args;

    @Override
    public String toString() {
        return "JobScheduleRequest{" +
                "jobId=" + jobId +
                ", args=" + args +
                '}';
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public void setArgs(Map<String, Object> args) {
        this.args = args;
    }
}
