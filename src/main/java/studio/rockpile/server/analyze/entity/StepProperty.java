package studio.rockpile.server.analyze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 步骤属性
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@TableName("anlz_step_property")
public class StepProperty implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 步骤ID
     */
    private Long stepId;

    /**
     * 属性编码
     */
    private String code;

    /**
     * 序列
     */
    private Integer seqNo;

    /**
     * 属性内容
     */
    private String content;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "StepProperty{" +
        "id=" + id +
        ", stepId=" + stepId +
        ", code=" + code +
        ", seqNo=" + seqNo +
        ", content=" + content +
        "}";
    }
}
