package studio.rockpile.server.analyze.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 作业元数据
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@TableName("anlz_job_meta")
public class JobMeta implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 作业标题
     */
    private String jobTitle;

    /**
     * 作业编码
     */
    private String jobCode;

    /**
     * 租户id
     */
    private Long tenancyId;

    /**
     * 发布状态：0在建 1已发布
     */
    private Integer publishStatus;

    /**
     * 更新时间
     */
    private Date updateTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public Long getTenancyId() {
        return tenancyId;
    }

    public void setTenancyId(Long tenancyId) {
        this.tenancyId = tenancyId;
    }

    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "JobMeta{" +
        "id=" + id +
        ", jobTitle=" + jobTitle +
        ", jobCode=" + jobCode +
        ", tenancyId=" + tenancyId +
        ", publishStatus=" + publishStatus +
        ", updateTime=" + updateTime +
        "}";
    }
}
