package studio.rockpile.server.analyze.job.step;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.entity.StepProperty;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;
import studio.rockpile.server.analyze.util.SpringContextUtil;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemChunkReaderBuilder {

    public ItemReader<Map<String, Object>> jdbcPagingReaderBuild(
            StepMetaInfo stepInfo, String jobEnvBean) throws Exception {
        Integer chunkSize = ChunkStepConstructor.DEFAULT_FETCH_CHUNK_SIZE;
        String dataSetName = "DefaultDataset";

        JdbcPagingItemReader<Map<String, Object>> itemReader = new JdbcPagingItemReader<>();
        DataSource dataSource = SpringContextUtil.getBean(DataSource.class);
        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(chunkSize);
        RowMapper<Map<String, Object>> rowMapper = (rs, rowNum) -> {
            Map<String, Object> raw = new HashMap<>();
            raw.put(PublicDomainDef.RAWS_INDEX_KEY, dataSetName);
            raw.put("id", rs.getLong("id"));
            raw.put("name", rs.getString("name"));
            raw.put("type", rs.getInt("type"));
            raw.put("balance", rs.getBigDecimal("balance"));
            raw.put("update_time", rs.getTimestamp("update_time"));
            return raw;
        };
        itemReader.setRowMapper(rowMapper);

        MySqlPagingQueryProvider provider = new MySqlPagingQueryProvider();
        List<StepProperty> properties = stepInfo.getProperties();
        for (StepProperty property : properties) {
            String code = property.getCode();
            switch (code) {
                case "SelectClause":
                    provider.setSelectClause(property.getContent());
                    break;
                case "FromClause":
                    provider.setFromClause(property.getContent());
                    break;
                case "WhereClause":
                    provider.setWhereClause(property.getContent());
                    break;
                default:
                    break;
            }
        }
        // 指定查询数据的排序字段
        Map<String, Order> sort = new HashMap<>(1); /*设置HashMap初始大小=1，按id单字段排序*/
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);
        itemReader.setQueryProvider(provider);
        itemReader.afterPropertiesSet();
        return itemReader;
    }
}
