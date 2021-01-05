package studio.rockpile.server.analyze;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing /* 开启spring batch批处理 */
@MapperScan("studio.rockpile.server.analyze.**.dao*")
public class AnalyzeServer {
    private static final Logger logger = LoggerFactory.getLogger(AnalyzeServer.class);

    public static void main(String[] args) {
        SpringApplication.run(AnalyzeServer.class, args);
        logger.info("====== 启动成功 ======");
    }
}
