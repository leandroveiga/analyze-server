package studio.rockpile.server.analyze.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import studio.rockpile.server.analyze.job.cache.FlowDataCacheHolder;

import java.util.HashMap;
import java.util.Map;

public class BatchJobEnvironment {
    private Long workerId = null;
    private Job job = null;
    private Map<String, ItemReader<Map<String, Object>>> chunkReaderSet = new HashMap<>(4);
    private Map<String, ItemProcessor<Map<String, Object>, Map<String, Object>>> chunkProcessorSet = new HashMap<>(4);
    private Map<String, ItemWriter<Map<String, Object>>> chunkWriterSet = new HashMap<>(4);
    private Map<String, Step> stepSet = new HashMap<>();
    private Map<String, JobExecutionDecider> deciderSet = new HashMap<>();
    private FlowDataCacheHolder rawsCache = new FlowDataCacheHolder();

    @Override
    public String toString() {
        return "BatchJobEnvironment{" +
                "workerId=" + workerId +
                ", job=" + job +
                ", chunkReaderSet=" + chunkReaderSet +
                ", chunkProcessorSet=" + chunkProcessorSet +
                ", chunkWriterSet=" + chunkWriterSet +
                ", stepSet=" + stepSet +
                ", deciderSet=" + deciderSet +
                ", rawsCache=" + rawsCache +
                '}';
    }

    public Long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Map<String, ItemReader<Map<String, Object>>> getChunkReaderSet() {
        return chunkReaderSet;
    }

    public void setChunkReaderSet(Map<String, ItemReader<Map<String, Object>>> chunkReaderSet) {
        this.chunkReaderSet = chunkReaderSet;
    }

    public Map<String, ItemProcessor<Map<String, Object>, Map<String, Object>>> getChunkProcessorSet() {
        return chunkProcessorSet;
    }

    public void setChunkProcessorSet(Map<String, ItemProcessor<Map<String, Object>, Map<String, Object>>> chunkProcessorSet) {
        this.chunkProcessorSet = chunkProcessorSet;
    }

    public Map<String, ItemWriter<Map<String, Object>>> getChunkWriterSet() {
        return chunkWriterSet;
    }

    public void setChunkWriterSet(Map<String, ItemWriter<Map<String, Object>>> chunkWriterSet) {
        this.chunkWriterSet = chunkWriterSet;
    }

    public Map<String, Step> getStepSet() {
        return stepSet;
    }

    public void setStepSet(Map<String, Step> stepSet) {
        this.stepSet = stepSet;
    }

    public Map<String, JobExecutionDecider> getDeciderSet() {
        return deciderSet;
    }

    public void setDeciderSet(Map<String, JobExecutionDecider> deciderSet) {
        this.deciderSet = deciderSet;
    }

    public FlowDataCacheHolder getRawsCache() {
        return rawsCache;
    }

    public void setRawsCache(FlowDataCacheHolder rawsCache) {
        this.rawsCache = rawsCache;
    }
}
