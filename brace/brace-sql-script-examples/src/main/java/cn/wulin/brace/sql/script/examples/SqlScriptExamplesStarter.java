package cn.wulin.brace.sql.script.examples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan(basePackages= {"cn.wulin.brace.sql.script.examples"})
@EnableTransactionManagement
public class SqlScriptExamplesStarter {

	public static void main(String[] args) {
        try {
			SpringApplication.run(SqlScriptExamplesStarter.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
