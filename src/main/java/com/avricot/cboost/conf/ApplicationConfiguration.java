package com.avricot.cboost.conf;

import com.avricot.cboost.service.ApplicationContextHolder;
import com.avricot.cboost.utils.MsgHelper;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

@Configuration
@PropertySource({"classpath:/META-INF/cboost/cboost.properties"})
@ComponentScan(basePackages = {
        "com.avricot.cboost.utils",
        "com.avricot.cboost.service",
        "com.avricot.cboost.security"})
@Import(value = {
        AsyncConfiguration.class,
        CacheConfiguration.class})
@ImportResource("classpath:META-INF/spring/applicationContext-*.xml")
public class ApplicationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ApplicationConfiguration.class);

    @Inject
    private Environment env;
    @Value("es.cluster.name")
    private String clusterName;

    /**
     * Initializes cboost.
     * <p/>
     * Spring profiles can be configured with a system property -Dspring.profiles.active=your-active-profile
     * <p/>
     */
    @PostConstruct
    public void initApplication() throws IOException {

        log.debug("Looking for Spring profiles...");
        if (env.getActiveProfiles().length == 0) {
            log.debug("No Spring profile configured, running with default configuration");
        } else {
            for (String profile : env.getActiveProfiles()) {
                log.debug("Detected Spring profile : {}", profile);
            }
        }
    }

    @Bean(name = "messageSource")
    public MessageSource getMessageSource(){
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:messages/text");
        return source;
    }

    @Bean
    public ApplicationContextHolder setApplicationContextHolder(){
        return new ApplicationContextHolder();
    }

    @Bean
    public ElasticsearchTemplate setElasticsearchTemplate(){
        Node node = NodeBuilder.nodeBuilder().client(true).clusterName(clusterName).node();
        return new ElasticsearchTemplate(node.client());
    }

}
