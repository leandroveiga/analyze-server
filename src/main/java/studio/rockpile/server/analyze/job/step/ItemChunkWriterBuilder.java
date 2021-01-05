package studio.rockpile.server.analyze.job.step;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;
import studio.rockpile.server.analyze.job.cache.GenericDataEntity;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ItemChunkWriterBuilder {

    public ItemWriter<Map<String, Object>> build(StepMetaInfo stepInfo, String jobEnvBean) {
        ItemWriter<Map<String, Object>> writer = items -> {
            BatchJobEnvironment environment = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);

            Map<String, GenericDataEntity> rawsHash = environment.getRawsCache().getRawsHash();
            for (Map<String, Object> item : items) {
                String rawKey = item.get(PublicDomainDef.RAWS_INDEX_KEY).toString();
                if (rawsHash.containsKey(rawKey)) {
                    GenericDataEntity raws = rawsHash.get(rawKey);
                    List<Map<String, Object>> dataSet = raws.getDataSet();
                    if (dataSet == null) {
                        dataSet = new ArrayList<>();
                        dataSet.add(item);
                        raws.setDataSet(dataSet);
                    } else {
                        dataSet.add(item);
                    }
                } else {
                    GenericDataEntity raws = new GenericDataEntity();
                    List<Map<String, Object>> dataSet = new ArrayList<>();
                    dataSet.add(item);
                    raws.setDataSet(dataSet);
                    rawsHash.put(rawKey, raws);
                }
            }
        };
        return writer;
    }
}
