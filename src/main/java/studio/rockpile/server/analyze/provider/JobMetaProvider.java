package studio.rockpile.server.analyze.provider;

import studio.rockpile.server.analyze.entity.JobMeta;
import com.baomidou.mybatisplus.extension.service.IService;
import studio.rockpile.server.analyze.protocol.JobMetaInfo;

/**
 * <p>
 * 作业元数据 服务类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
public interface JobMetaProvider extends IService<JobMeta> {
    JobMetaInfo getMetaInfo(Long jobId) throws Exception;
}
