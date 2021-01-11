package studio.rockpile.server.analyze.job.step;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component(value = "itemUpperCaseProcessor")
public class ItemUpperCaseProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {
    private static final Logger logger = LoggerFactory.getLogger(ItemUpperCaseProcessor.class);

    @Override
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        logger.debug("item step upper case processor : {}", item);
        String name = item.get("name").toString();
        item.put("name", name.toUpperCase());
        return item;
    }
}
