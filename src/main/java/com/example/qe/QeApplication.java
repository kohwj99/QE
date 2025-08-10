package com.example.qe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    org.javers.spring.boot.sql.JaversSqlAutoConfiguration.class
})
public class QeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QeApplication.class, args);
	}

}
