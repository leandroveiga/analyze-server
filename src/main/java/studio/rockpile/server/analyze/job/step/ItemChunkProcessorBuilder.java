package studio.rockpile.server.analyze.job.step;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ItemChunkProcessorBuilder {

    public ItemProcessor<Map<String, Object>, Map<String, Object>> build(
            StepMetaInfo stepInfo, String jobEnvBean) {
        ItemProcessor<Map<String, Object>, Map<String, Object>> processor
                = new ItemProcessor<Map<String, Object>, Map<String, Object>>() {
            @Override
            public Map<String, Object> process(Map<String, Object> item) throws Exception {
                System.out.println("item step filter processor : " + item);
                return item;
            }
        };
        return processor;
    }

    public CompositeItemProcessor compositeBuild(StepMetaInfo stepInfo, String jobEnvBean) {
        CompositeItemProcessor compositeProcessor = new CompositeItemProcessor();
        List<ItemProcessor<Map<String, Object>, Map<String, Object>>> processors = new ArrayList<>();
        processors.add(item -> {
            item.put("name", item.get("name").toString() + ".1");
            System.out.println("item step filter processor-1 : " + item);
            return item;
        });
        processors.add(item -> {
            item.put("name", item.get("name").toString() + ".2");
            System.out.println("item step filter processor-2 : " + item);
            return item;
        });
        compositeProcessor.setDelegates(processors);
        return compositeProcessor;
    }
}
