package com.github.ypp.jwt;

import com.github.ypp.jwt.config.JwtProperties;
import com.github.ypp.jwt.core.AutoRefreshInterceptor;
import com.github.ypp.jwt.core.CheckInterceptor;
import com.github.ypp.jwt.core.JwtManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnBean(IJwtExceptionService.class)
@ComponentScan(basePackages = {"com.github.ypp.jwt"})
public class JwtAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(JwtAutoConfiguration.class);

    private final JwtProperties prop;

    private JwtManager manager;

    public JwtAutoConfiguration(JwtProperties prop) {
        this.prop = prop;
    }

    @PostConstruct
    public void afterConstruct() {
        logger.info("JWT configuration starts with properties:" + prop);
        if (prop.getMaxIdle() < prop.getMaxAlive()) {
            throw new IllegalArgumentException("maxIdleMinute must be larger than maxAliveMinute");
        }
    }

    @Bean
    public JwtManager JetManager() {
        return new JwtManager(prop);
    }

    @Autowired
    public void setJetManager(JwtManager manager) {
        this.manager = manager;
        JwtUtil.setManager(manager);
    }

    @Configuration
    public class JWTWebMvcConfigurer implements WebMvcConfigurer {

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new CheckInterceptor(manager)).addPathPatterns("/**");
            if (prop.isAutoRefresh())
                registry.addInterceptor(new AutoRefreshInterceptor(manager)).addPathPatterns("/**");
        }

    }
}
