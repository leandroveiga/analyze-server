package studio.rockpile.server.analyze.provider.impl;

import studio.rockpile.server.analyze.entity.StepMeta;
import studio.rockpile.server.analyze.dao.StepMetaMapper;
import studio.rockpile.server.analyze.provider.StepMetaProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 步骤元数据 服务实现类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@Service
public class StepMetaProviderImpl extends ServiceImpl<StepMetaMapper, StepMeta> implements StepMetaProvider {

}
