package com.gw.ui.config;

import com.gw.common.http.filter.LoggingFilter;
import com.gw.grpc.common.CorrelationIdInterceptor;
import com.gw.grpc.common.MetricsInterceptor;
import com.gw.user.client.UserGrpcClient;
import com.gw.user.client.UserGrpcClientConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
@EnableAutoConfiguration
@ConfigurationPropertiesScan(value = {"com.gw.ui.config", "com.gw.common.config"})
public class AppConfig implements WebFluxConfigurer {
    @Autowired
    private LoggingFilter loggingFilter;

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/",
            "classpath:/resources/",
            "classpath:/static/",
            "classpath:/public/",
            "classpath:/META-INF/resources/webjars/"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/static/login.html") final Resource indexHtml) {
        return route(GET("/"), request -> {
            var stringWriter = new StringWriter();
            try {
                try (var inputStream = indexHtml.getInputStream();
                     var inputStreamReader = new InputStreamReader(inputStream)) {
                    inputStreamReader.transferTo(stringWriter);
                }
            } catch (IOException e) {
                throw new BeanInitializationException(e.getMessage());
            }
            return ok().contentType(MediaType.TEXT_HTML).bodyValue(stringWriter.toString());
        });
    }

    @Bean
    public UserGrpcClient userGrpcClient(UserGrpcConfig userGrpcConfig,
                                         CorrelationIdInterceptor correlationIdInterceptor,
                                         MetricsInterceptor metricsInterceptor,
                                         CircuitBreakerRegistry circuitBreakerRegistry,
                                         MeterRegistry meterRegistry) {
        return new UserGrpcClient(
                new UserGrpcClientConfig(userGrpcConfig.host(), userGrpcConfig.port(),
                        userGrpcConfig.timeoutInMs(), userGrpcConfig.circuitBreaker()),
                List.of(correlationIdInterceptor, metricsInterceptor),
                circuitBreakerRegistry, meterRegistry);
    }
}
