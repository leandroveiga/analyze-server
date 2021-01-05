package studio.rockpile.server.analyze.provider.impl;

import studio.rockpile.server.analyze.entity.StepLinkMeta;
import studio.rockpile.server.analyze.dao.StepLinkMetaMapper;
import studio.rockpile.server.analyze.provider.StepLinkMetaProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 连接元数据 服务实现类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@Service
public class StepLinkMetaProviderImpl extends ServiceImpl<StepLinkMetaMapper, StepLinkMeta> implements StepLinkMetaProvider {

}
