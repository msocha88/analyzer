package pl.msocha.analyzer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AnalyzerConfiguration {


    @Bean
    ExecutorService analyzerExecutorService() {
        return Executors.newCachedThreadPool();
    }

}
