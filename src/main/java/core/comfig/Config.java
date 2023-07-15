package core.comfig;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import core.annotation.Bean;
import core.annotation.ComponentScan;
import core.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"core", "next"})
public class Config {
	@Bean
	public DataSource dataSource() {
		System.out.println("test");
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("org.h2.Driver");
		ds.setUrl("jdbc:h2:~/jwp-basic;AUTO_SERVER=TRUE");
		ds.setUsername("sa");
		ds.setPassword("");
		return ds;
	}
}