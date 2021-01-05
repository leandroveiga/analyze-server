package studio.rockpile.server.analyze.protocol;

import studio.rockpile.server.analyze.constant.StepStatusEnum;
import studio.rockpile.server.analyze.entity.DeciderMeta;
import studio.rockpile.server.analyze.entity.StepMeta;
import studio.rockpile.server.analyze.entity.StepProperty;

import java.util.List;

public class StepMetaInfo {
    private StepMeta step;
    private StepStatusEnum status;
    private DeciderMeta decider;
    private List<Long> previousLinks;
    private List<Long> nextLinks;
    private List<StepProperty> properties;

    @Override
    public String toString() {
        return "StepMetaInfo{" +
                "step=" + step +
                ", status=" + status +
                ", decider=" + decider +
                ", previousLinks=" + previousLinks +
                ", nextLinks=" + nextLinks +
                ", properties=" + properties +
                '}';
    }

    public StepMeta getStep() {
        return step;
    }

    public void setStep(StepMeta step) {
        this.step = step;
    }

    public StepStatusEnum getStatus() {
        return status;
    }

    public void setStatus(StepStatusEnum status) {
        this.status = status;
    }

    public DeciderMeta getDecider() {
        return decider;
    }

    public void setDecider(DeciderMeta decider) {
        this.decider = decider;
    }

    public List<Long> getPreviousLinks() {
        return previousLinks;
    }

    public void setPreviousLinks(List<Long> previousLinks) {
        this.previousLinks = previousLinks;
    }

    public List<Long> getNextLinks() {
        return nextLinks;
    }

    public void setNextLinks(List<Long> nextLinks) {
        this.nextLinks = nextLinks;
    }

    public List<StepProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<StepProperty> properties) {
        this.properties = properties;
    }
}
