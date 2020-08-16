package store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
  @Value("spring.datasource.url")
  private String url;

  @Value("spring.datasource.username")
  private String username;

  @Value("spring.datasource.password")
  private String password;

  @Value("spring.datasource.driver-class-name")
  private String driver;

  @Value("spring.jpa.properties.hibernate.hbm2ddl.auto")
  private String auto;

  @Value("spring.jpa.properties.hibernate.dialect")
  private String dialect;

  @Value("spring.jpa.show-sql")
  private String showSql;
}
