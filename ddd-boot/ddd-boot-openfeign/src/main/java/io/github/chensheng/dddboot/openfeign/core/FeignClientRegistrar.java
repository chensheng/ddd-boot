package io.github.chensheng.dddboot.openfeign.core;

import io.github.chensheng.dddboot.openfeign.annotation.FeignClient;
import io.github.chensheng.dddboot.openfeign.config.OpenFeignAutoConfiguration;
import io.github.chensheng.dddboot.openfeign.config.OpenFeignProperties;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FeignClientRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(FeignClient.class);
        scanner.addIncludeFilter(annotationTypeFilter);

        Set<String> basePackages = resolveBasePackages();
        if (CollectionUtil.isEmpty(basePackages)) {
            return;
        }

        for (String basePackage : basePackages) {
            this.doScan(registry, basePackage, scanner);
        }
    }

    private void doScan(BeanDefinitionRegistry registry, String basePackage, ClassPathScanningCandidateComponentProvider scanner) {
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents(basePackage);

        for (BeanDefinition candidateComponent : candidateComponents) {
            if (!(candidateComponent instanceof AnnotatedBeanDefinition)) {
                continue;
            }

            AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
            AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
            Assert.isTrue(annotationMetadata.isInterface(), "@FeignClient can only be specified on an interface");

            Map<String, Object> attributes = annotationMetadata.getAnnotationAttributes(FeignClient.class.getCanonicalName());
            String beanUnderlyingClassName = beanDefinition.getBeanClassName();
            Class<?> beanUnderlyingClass = null;
            try {
                beanUnderlyingClass = ClassUtils.getClass(beanUnderlyingClassName);
            } catch (ClassNotFoundException e) {
                throw  new IllegalStateException("Could not find class [" + beanUnderlyingClassName + "]", e);
            }

            String beanName = generateBeanName(annotationMetadata, attributes);
            registerFeignClient(registry, beanName, beanUnderlyingClass, attributes);
        }
    }

    private void registerFeignClient(BeanDefinitionRegistry registry, String beanName, Class<?> beanUnderlyingClass, Map<String, Object> attributes){
        BeanDefinitionBuilder bdBuilder = BeanDefinitionBuilder.genericBeanDefinition(FeignClientFactoryBean.class);
        bdBuilder.addPropertyValue("type", beanUnderlyingClass);
        bdBuilder.addPropertyValue("url", getUrl(attributes));
        bdBuilder.addPropertyValue("encoderType", getSingleType(attributes, "encoder"));
        bdBuilder.addPropertyValue("decoderType", getSingleType(attributes, "decoder"));
        bdBuilder.addPropertyValue("errorDecoderType", getSingleType(attributes, "errorDecoder"));
        bdBuilder.addPropertyValue("interceptorTypes", getMultiTypes(attributes, "interceptors"));
        bdBuilder.addPropertyValue("retryerTypes", getMultiTypes(attributes, "retryer"));
        bdBuilder.addPropertyValue("contractTypes", getMultiTypes(attributes, "contract"));
        bdBuilder.addPropertyReference("client", OpenFeignAutoConfiguration.DEFAULT_FEIGN_CLIENT_BEAN_NAME);

        BeanDefinition beanDefinition = bdBuilder.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getUrl(Map<String, Object> attributes) {
        String url = (String) attributes.get("url");
        return this.environment.resolvePlaceholders(url);
    }

    private Class<?> getSingleType(Map<String, Object> attributes, String attributeName) {
        Object singleType = attributes.get(attributeName);
        if (singleType == null) {
            return null;
        }

        return (Class<?>) singleType;
    }

    private Class<?>[] getMultiTypes(Map<String, Object> attributes, String attributeName) {
        Object multiTypes = attributes.get(attributeName);
        if (multiTypes == null) {
            return null;
        }

        return (Class<?>[]) multiTypes;
    }

    private String generateBeanName(AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        if (attributes != null && attributes.containsKey("name") && TextUtil.isNotEmpty((String) attributes.get("name"))) {
            return ((String) attributes.get("name")).trim();
        }

        return annotationMetadata.getClassName();
    }

    private Set<String> resolveBasePackages() {
        Set<String> basePackages = new HashSet<String>();

        String pkg = this.environment.getProperty(OpenFeignProperties.PROP_BASE_PACKAGE);

        if (TextUtil.isNotEmpty(pkg)) {
            basePackages.add(pkg.trim());
        }
        return basePackages;
    }

    private ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }
}
