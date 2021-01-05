package studio.rockpile.server.analyze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 连接元数据
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@TableName("anlz_step_link_meta")
public class StepLinkMeta implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 起始步骤ID
     */
    private Long stepFromId;

    /**
     * 截止步骤ID
     */
    private Long stepToId;

    /**
     * 决策结果
     */
    private String deciderResult;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStepFromId() {
        return stepFromId;
    }

    public void setStepFromId(Long stepFromId) {
        this.stepFromId = stepFromId;
    }

    public Long getStepToId() {
        return stepToId;
    }

    public void setStepToId(Long stepToId) {
        this.stepToId = stepToId;
    }

    public String getDeciderResult() {
        return deciderResult;
    }

    public void setDeciderResult(String deciderResult) {
        this.deciderResult = deciderResult;
    }

    @Override
    public String toString() {
        return "StepLinkMeta{" +
        "id=" + id +
        ", stepFromId=" + stepFromId +
        ", stepToId=" + stepToId +
        ", deciderResult=" + deciderResult +
        "}";
    }
}
