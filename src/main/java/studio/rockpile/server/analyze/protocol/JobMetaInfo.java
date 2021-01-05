package studio.rockpile.server.analyze.protocol;

import studio.rockpile.server.analyze.entity.JobMeta;
import studio.rockpile.server.analyze.entity.JobNamedParam;
import studio.rockpile.server.analyze.entity.StepLinkMeta;

import java.util.List;
import java.util.Map;

public class JobMetaInfo {
    private JobMeta job;
    private List<JobNamedParam> namedParams;
    private List<StepMetaInfo> steps;
    private Map<Long, StepLinkMeta> linkHash;

    @Override
    public String toString() {
        return "JobMetaInfo{" +
                "job=" + job +
                ", namedParams=" + namedParams +
                ", steps=" + steps +
                ", linkHash=" + linkHash +
                '}';
    }

    public JobMeta getJob() {
        return job;
    }

    public void setJob(JobMeta job) {
        this.job = job;
    }

    public List<JobNamedParam> getNamedParams() {
        return namedParams;
    }

    public void setNamedParams(List<JobNamedParam> namedParams) {
        this.namedParams = namedParams;
    }

    public List<StepMetaInfo> getSteps() {
        return steps;
    }

    public void setSteps(List<StepMetaInfo> steps) {
        this.steps = steps;
    }

    public Map<Long, StepLinkMeta> getLinkHash() {
        return linkHash;
    }

    public void setLinkHash(Map<Long, StepLinkMeta> linkHash) {
        this.linkHash = linkHash;
    }
}
