package studio.rockpile.server.analyze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 步骤元数据
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@TableName("anlz_step_meta")
public class StepMeta implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 步骤标题
     */
    private String stepTitle;

    /**
     * 步骤编码
     */
    private String stepCode;

    /**
     * 作业ID
     */
    private Long jobId;

    /**
     * 处理节点类型
     */
    private String segNodeType;

    private Integer rectangularX;

    private Integer rectangularY;

    /**
     * 决策器ID
     */
    private Long deciderId;

    /**
     * 步骤类型：1起始 9结束 2接续
     */
    private Integer stepType;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public void setStepTitle(String stepTitle) {
        this.stepTitle = stepTitle;
    }

    public String getStepCode() {
        return stepCode;
    }

    public void setStepCode(String stepCode) {
        this.stepCode = stepCode;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getSegNodeType() {
        return segNodeType;
    }

    public void setSegNodeType(String segNodeType) {
        this.segNodeType = segNodeType;
    }

    public Integer getRectangularX() {
        return rectangularX;
    }

    public void setRectangularX(Integer rectangularX) {
        this.rectangularX = rectangularX;
    }

    public Integer getRectangularY() {
        return rectangularY;
    }

    public void setRectangularY(Integer rectangularY) {
        this.rectangularY = rectangularY;
    }

    public Long getDeciderId() {
        return deciderId;
    }

    public void setDeciderId(Long deciderId) {
        this.deciderId = deciderId;
    }

    public Integer getStepType() {
        return stepType;
    }

    public void setStepType(Integer stepType) {
        this.stepType = stepType;
    }

    @Override
    public String toString() {
        return "StepMeta{" +
        "id=" + id +
        ", stepTitle=" + stepTitle +
        ", stepCode=" + stepCode +
        ", jobId=" + jobId +
        ", segNodeType=" + segNodeType +
        ", rectangularX=" + rectangularX +
        ", rectangularY=" + rectangularY +
        ", deciderId=" + deciderId +
        ", stepType=" + stepType +
        "}";
    }
}
