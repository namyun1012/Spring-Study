package chap08;

import org.apache.tomcat.jdbc.pool.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class AppCtx {

	
		@Bean(destroyMethod = "close")
		public DataSource dataSource() {
			DataSource ds = new DataSource();
			ds.setDriverClassName("com.mysql.jdbc.Driver");
			ds.setUrl("jdbc:mysql://localhost/spring5fs?useSSL=false&characterEncoding=utf8");
			//mysql 이 update 되면서 SSL을 사용해서 사용 안한다고 해줘야 함
			ds.setUsername("spring5");
			ds.setPassword("spring5");
			ds.setInitialSize(2);
			ds.setInitialSize(10);
			ds.setTestWhileIdle(true);;
			ds.setMinEvictableIdleTimeMillis(60000 * 3);
			ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
			return ds;
		}
		@Bean
		public PlatformTransactionManager transactionManager() {
			DataSourceTransactionManager tm = new DataSourceTransactionManager();
			tm.setDataSource(dataSource());
			return tm;
			
		}
		@Bean
		public MemberDao memberDao() {
			return new MemberDao(dataSource());
		}
		
	
		@Bean
		public ChangePasswordService changePwdSvc() {
			ChangePasswordService pwdSvc = new ChangePasswordService();
			pwdSvc.setMemberDao(memberDao());
			return pwdSvc;
		}
}
