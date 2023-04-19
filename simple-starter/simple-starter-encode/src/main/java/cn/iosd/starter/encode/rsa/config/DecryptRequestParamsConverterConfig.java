package cn.iosd.starter.encode.rsa.config;

import cn.iosd.starter.encode.rsa.annotation.DecryptRequestParamsResolve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 配置项注入映射资源
 *
 * @author ok1996
 */
@Configuration
@ConditionalOnProperty(name = "simple.encode.rsa.secureParams.enabled", havingValue = "true", matchIfMissing = true)
public class DecryptRequestParamsConverterConfig implements WebMvcConfigurer {

    @Autowired
    private DecryptRequestParamsResolve customArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(customArgumentResolver);
    }
}