package studio.rockpile.server.analyze.provider.impl;

import studio.rockpile.server.analyze.entity.JobNamedParam;
import studio.rockpile.server.analyze.dao.JobNamedParamMapper;
import studio.rockpile.server.analyze.provider.JobNamedParamProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 命名参数元数据 服务实现类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@Service
public class JobNamedParamProviderImpl extends ServiceImpl<JobNamedParamMapper, JobNamedParam> implements JobNamedParamProvider {

}
