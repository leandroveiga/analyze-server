package studio.rockpile.server.analyze.provider.impl;

import org.springframework.beans.factory.annotation.Autowired;
import studio.rockpile.server.analyze.entity.JobMeta;
import studio.rockpile.server.analyze.dao.JobMetaMapper;
import studio.rockpile.server.analyze.protocol.JobMetaInfo;
import studio.rockpile.server.analyze.provider.JobMetaProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业元数据 服务实现类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@Service
public class JobMetaProviderImpl extends ServiceImpl<JobMetaMapper, JobMeta> implements JobMetaProvider {
    @Autowired
    private JobMetaMapper jobMetaMapper;

    @Override
    public JobMetaInfo getMetaInfo(Long jobId) throws Exception {
        return null;
    }
}
