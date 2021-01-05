package studio.rockpile.server.analyze.provider.impl;

import studio.rockpile.server.analyze.entity.DeciderMeta;
import studio.rockpile.server.analyze.dao.DeciderMetaMapper;
import studio.rockpile.server.analyze.provider.DeciderMetaProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 决策器元数据 服务实现类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@Service
public class DeciderMetaProviderImpl extends ServiceImpl<DeciderMetaMapper, DeciderMeta> implements DeciderMetaProvider {

}
