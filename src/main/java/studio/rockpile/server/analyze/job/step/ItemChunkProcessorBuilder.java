package studio.rockpile.server.analyze.job.step;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import java.util.Map;

@Component
public class ItemChunkProcessorBuilder {

    public ItemProcessor<Map<String, Object>, Map<String, Object>> build(
            StepMetaInfo stepInfo, String jobEnvBean) {
        ItemProcessor<Map<String, Object>, Map<String, Object>> processor
                = item -> {
            System.out.println("item step filter processor : " + item);
            return item;
        };
        return processor;
    }
}
