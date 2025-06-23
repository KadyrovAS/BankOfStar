package star.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    @Bean(name = "rulesDataSourcePart01")
    public DataSource rulesDataSourcePart01(@Value("${application.rules.part01.url}") String rulesUrl){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(rulesUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }

    @Bean(name = "rulesDataSourcePart02")
    public DataSource rulesDataSourcePart02(@Value("${application.rules.part02.url}") String rulesUrl){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(rulesUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        return dataSource;
    }


    @Bean
    public DataSourceInitializer rulesDataSourceInitializerPart01(
            @Qualifier("rulesDataSourcePart01") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema-init_part01.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }

    @Bean
    public DataSourceInitializer rulesDataSourceInitializerPart02(
            @Qualifier("rulesDataSourcePart02") DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);

        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema-init_part02.sql"));

        initializer.setDatabasePopulator(populator);
        return initializer;
    }


    @Bean(name = "transactionsDataSource")
    public DataSource transactionsDataSource(@Value("${application.transactions-db.url}") String transactionsUrl) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(transactionsUrl);
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setReadOnly(true);
        return dataSource;
    }


    @Bean(name = "transactionsJdbcTemplate")
    public JdbcTemplate transactionsJdbcTemplate(
            @Qualifier("transactionsDataSource") DataSource dataSource
    ) {
        return new JdbcTemplate(dataSource);
    }


    @Bean(name = "rulesJdbcTemplatePart01")
    public JdbcTemplate rulesJdbcTemplatePart01(
            @Qualifier("rulesDataSourcePart01") DataSource dataSource
    ){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "rulesJdbcTemplatePart02")
    public JdbcTemplate rulesJdbcTemplatePart02(
            @Qualifier("rulesDataSourcePart02") DataSource dataSource
    ){
        return new JdbcTemplate(dataSource);
    }

}