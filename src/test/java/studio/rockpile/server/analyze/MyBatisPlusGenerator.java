package studio.rockpile.server.analyze;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.SQLException;

public class MyBatisPlusGenerator {

    public static void main(String[] args) throws SQLException {
        GlobalConfig globalConf = new GlobalConfig();
        globalConf.setActiveRecord(false);
        globalConf.setAuthor("rockpile");
        globalConf.setOutputDir("D:/workstation/temporary/mybatis-generator/src");
        // globalConf.setSwagger2(true);
        globalConf.setFileOverride(true);
        globalConf.setIdType(IdType.ASSIGN_ID); // 主键生成策略
        globalConf.setServiceName("%sProvider");
        globalConf.setServiceImplName("%sProviderImpl");
        globalConf.setEnableCache(false); // 是否开启二级缓存，只适合数据量小更新少的场景
        globalConf.setBaseResultMap(true);
        globalConf.setBaseColumnList(true);

        DataSourceConfig dsConf = new DataSourceConfig();
        dsConf.setDbType(DbType.MYSQL);
        dsConf.setDriverName("com.mysql.cj.jdbc.Driver");
        dsConf.setUrl(
                "jdbc:mysql://192.168.209.130:3301/mystudio?useUnicode=true&characterEncoding=UTF-8&useSSL=false&zeroDateTimeBehavior=convertToNull");
        dsConf.setUsername("rockpile");
        dsConf.setPassword("rockpile");
        dsConf.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换
            @Override
            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                if (fieldType.toLowerCase().contains("tinyint")) {
                    return DbColumnType.BOOLEAN;
                } else if (fieldType.toLowerCase().contains("datetime")) {
                    return DbColumnType.DATE;
                } else if (fieldType.toLowerCase().contains("double")) {
                    return DbColumnType.BIG_DECIMAL;
                } else if (fieldType.toLowerCase().contains("json")) {
                    return DbColumnType.STRING;
                } else {
                    return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
                }
            }
        });

        String[] tableNames = {"anlz_decider_meta"
                , "anlz_job_meta"
                , "anlz_job_named_param"
                , "anlz_step_link_meta"
                , "anlz_step_meta"
                , "anlz_step_property"};

        StrategyConfig strategyConf = new StrategyConfig();
        strategyConf.setNaming(NamingStrategy.underline_to_camel);
        strategyConf.setColumnNaming(NamingStrategy.underline_to_camel);
        strategyConf.setEntityLombokModel(false);
        strategyConf.setRestControllerStyle(true);
        strategyConf.setTablePrefix("anlz_");
        strategyConf.setInclude(tableNames);
        // strategyConf.setSuperEntityClass(BaseEntity.class.getName());
        // strategyConf.setSuperEntityColumns(new String[] { "id", "is_del" });
        strategyConf.setSuperMapperClass(BaseMapper.class.getName());
        strategyConf.setSuperServiceClass(IService.class.getName());
        strategyConf.setSuperServiceImplClass(ServiceImpl.class.getName());

        PackageConfig packageConf = new PackageConfig();
        packageConf.setParent("studio.rockpile.server");
        packageConf.setMapper("analyze.dao");
        packageConf.setService("analyze.provider");
        packageConf.setServiceImpl("analyze.provider.impl");
        packageConf.setController("analyze.controller");
        packageConf.setEntity("analyze.entity");
        packageConf.setXml("analyze.dao.mapper");

        AutoGenerator generator = new AutoGenerator();
        generator.setGlobalConfig(globalConf);
        generator.setDataSource(dsConf);
        generator.setStrategy(strategyConf);
        generator.setPackageInfo(packageConf);

        generator.execute();
    }
}
