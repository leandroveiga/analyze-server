package studio.rockpile.server.analyze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 命名参数元数据
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@TableName("anlz_job_named_param")
public class JobNamedParam implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 作业ID
     */
    private Long jobId;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数编码
     */
    private String paramCode;

    /**
     * 数据类型：string:字符 int:整形 date:日期(yyyy-MM-dd) time:时间(yyyy-MM-dd HH:mm:ss) utc_time:时间(UTC) long:长整形 double:浮点
     */
    private String dataType;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 是否必须：0否 1是
     */
    private Boolean isRequire;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Boolean getRequire() {
        return isRequire;
    }

    public void setRequire(Boolean isRequire) {
        this.isRequire = isRequire;
    }

    @Override
    public String toString() {
        return "JobNamedParam{" +
        "id=" + id +
        ", jobId=" + jobId +
        ", paramName=" + paramName +
        ", paramCode=" + paramCode +
        ", dataType=" + dataType +
        ", defaultValue=" + defaultValue +
        ", isRequire=" + isRequire +
        "}";
    }
}
