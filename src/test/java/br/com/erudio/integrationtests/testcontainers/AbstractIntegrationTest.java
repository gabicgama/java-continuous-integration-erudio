package br.com.erudio.integrationtests.testcontainers;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

// Forma moderna de testcontainer com a spring boot

@Testcontainers
public abstract class AbstractIntegrationTest {

	@Container
	@ServiceConnection
	static final MySQLContainer mysql = new MySQLContainer("mysql:8.4");
}



// Forma de integração manual do testcontainer com a spring boot

//import java.util.Map;
//import java.util.stream.Stream;
//
//import org.springframework.context.ApplicationContextInitializer;
//import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.core.env.ConfigurableEnvironment;
//import org.springframework.core.env.MapPropertySource;
//import org.springframework.test.context.ContextConfiguration;
//import org.testcontainers.lifecycle.Startables;
//import org.testcontainers.mysql.MySQLContainer;

//@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
//public class AbstractIntegrationTest {
//
//    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//
//
//        static final MySQLContainer mysql = new MySQLContainer("mysql:8.4");
//        
//        private static void startContainers() {
//            Startables.deepStart(Stream.of(mysql)).join();
//        }
//        
//        private static Map<String, String> createConnectionConfiguration(){
//            return Map.of(
//                "spring.datasource.url", mysql.getJdbcUrl(),
//                "spring.datasource.username", mysql.getUsername(),
//                "spring.datasource.password", mysql.getPassword());
//        }
//        
//        @Override
//        @SuppressWarnings({ "rawtypes", "unchecked" })
//        public void initialize(ConfigurableApplicationContext applicationContext) {
//            startContainers();
//            ConfigurableEnvironment environment = applicationContext.getEnvironment();
//            MapPropertySource testcontaines =
//                new MapPropertySource(
//                    "testcontainers",
//                    (Map) createConnectionConfiguration());
//            
//            environment.getPropertySources().addFirst(testcontaines);
//        }
//        
//    }
//}