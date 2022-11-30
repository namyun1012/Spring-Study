

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ControllerConfig {
	
	@Bean
	public HelloController hellonController() {
		return new HelloController();
	}
}
