package studio.rockpile.server.analyze.job.step;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import studio.rockpile.server.analyze.config.DynamicBeanRegister;
import studio.rockpile.server.analyze.constant.PublicDomainDef;
import studio.rockpile.server.analyze.entity.StepProperty;
import studio.rockpile.server.analyze.job.BatchJobEnvironment;
import studio.rockpile.server.analyze.protocol.StepMetaInfo;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemChunkReaderBuilder {
    private final DataSource dataSource;

    public ItemChunkReaderBuilder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ItemReader<Map<String, Object>> jdbcCursorReaderBuild(
            StepMetaInfo stepInfo, String jobEnvBean) throws Exception {
        Integer chunkSize = ChunkStepConstructor.DEFAULT_FETCH_CHUNK_SIZE;
        String dataSetName = "DefaultDataset";

        BatchJobEnvironment jobEnv = DynamicBeanRegister.getBean(jobEnvBean, BatchJobEnvironment.class);
        Map<String, JobParameter> params = jobEnv.getRawsCache().getParams();
        System.out.println("... params : " + params);

        List<StepProperty> properties = stepInfo.getProperties();
        StringBuilder selectClause = new StringBuilder("select ");
        StringBuilder fromClause = new StringBuilder(" from ");
        StringBuilder whereClause = new StringBuilder(" where ");
        for (StepProperty property : properties) {
            String code = property.getCode();
            switch (code) {
                case "SelectClause":
                    selectClause.append(property.getContent());
                    break;
                case "FromClause":
                    fromClause.append(property.getContent());
                    break;
                case "WhereClause":
                    whereClause.append(property.getContent());
                    break;
                default:
                    break;
            }
        }
        String sql = selectClause.toString() + fromClause.toString() + whereClause.toString();

        JdbcCursorItemReader<Map<String, Object>> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setFetchSize(chunkSize);
        itemReader.setSql(sql);
        itemReader.setPreparedStatementSetter(new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, (Long) (params.get("start_id").getValue()));
                ps.setBigDecimal(2, BigDecimal.valueOf((Double) (params.get("balance").getValue())));
            }
        });

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
        itemReader.afterPropertiesSet();
        return itemReader;
    }

    public ItemReader<Map<String, Object>> jdbcPagingReaderBuild(
            StepMetaInfo stepInfo, String jobEnvBean) throws Exception {
        Integer chunkSize = ChunkStepConstructor.DEFAULT_FETCH_CHUNK_SIZE;
        String dataSetName = "DefaultDataset";

        JdbcPagingItemReader<Map<String, Object>> itemReader = new JdbcPagingItemReader<>();
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
