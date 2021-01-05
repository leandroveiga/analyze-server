package studio.rockpile.server.analyze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 决策器元数据
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@TableName("anlz_decider_meta")
public class DeciderMeta implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 决策器标题
     */
    private String deciderTitle;

    /**
     * 决策器编码
     */
    private String deciderCode;

    /**
     * 决策表达式
     */
    private String expression;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeciderTitle() {
        return deciderTitle;
    }

    public void setDeciderTitle(String deciderTitle) {
        this.deciderTitle = deciderTitle;
    }

    public String getDeciderCode() {
        return deciderCode;
    }

    public void setDeciderCode(String deciderCode) {
        this.deciderCode = deciderCode;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "DeciderMeta{" +
        "id=" + id +
        ", deciderTitle=" + deciderTitle +
        ", deciderCode=" + deciderCode +
        ", expression=" + expression +
        "}";
    }
}
