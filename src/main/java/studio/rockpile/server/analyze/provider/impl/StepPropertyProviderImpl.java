package studio.rockpile.server.analyze.provider.impl;

import studio.rockpile.server.analyze.entity.StepProperty;
import studio.rockpile.server.analyze.dao.StepPropertyMapper;
import studio.rockpile.server.analyze.provider.StepPropertyProvider;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 步骤属性 服务实现类
 * </p>
 *
 * @author rockpile
 * @since 2021-01-04
 */
@Service
public class StepPropertyProviderImpl extends ServiceImpl<StepPropertyMapper, StepProperty> implements StepPropertyProvider {

}
