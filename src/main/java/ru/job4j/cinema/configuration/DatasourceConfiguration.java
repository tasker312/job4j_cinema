package ru.job4j.cinema.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Component
public class DatasourceConfiguration {

    @Bean
    public DataSource dataSource(
            @Value("${datasource.url}") String url,
            @Value("${datasource.username}") String username,
            @Value("${datasource.password}") String password
    ) {
        return new BasicDataSource() {
            {
                setUrl(url);
                setUsername(username);
                setPassword(password);
            }
        };

    }

    @Bean
    public Sql2o sql2o(DataSource dataSource) {
        return new Sql2o(dataSource);
    }

}
