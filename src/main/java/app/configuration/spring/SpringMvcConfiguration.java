package app.configuration.spring;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.mustache.MustacheViewResolver;
import org.springframework.web.servlet.view.mustache.java.MustacheJTemplateFactory;

@SuppressWarnings("checkstyle:CommentsIndentation")
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "app.controllers"
})
public class SpringMvcConfiguration implements WebMvcConfigurer {

    private static final Logger LOGGER = Logger.getLogger(SpringMvcConfiguration.class);

    @Autowired
    private ResourceLoader resourceLoader;

    @Bean
    public ViewResolver viewResolver() {
        MustacheViewResolver mustacheViewResolver = new MustacheViewResolver();
        mustacheViewResolver.setPrefix("/WEB-INF/views/");
        mustacheViewResolver.setSuffix(".mustache");
        mustacheViewResolver.setCache(false);

        MustacheJTemplateFactory templateFactory = new MustacheJTemplateFactory();
        templateFactory.setResourceLoader(resourceLoader);
        mustacheViewResolver.setTemplateFactory(templateFactory);
        return mustacheViewResolver;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {
        viewControllerRegistry.addViewController("/").setViewName("index");
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000);
        return multipartResolver;
    }

/*    @Bean(name = "constantsDom")
    public Document getDocumentBean() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File file = new File("/home/kolmogorov/Java_Practice/bcrew/doc_turnover/src/main/java/app/configuration/constants/constants.xml");
        Document document = null;
        try {
            document = builder.parse(file);
        } catch (SAXException | IOException e) {
            LOGGER.error("BUILDER PARSE ERROR" + e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            throw new Exception(e.getMessage());
        }
        document.getDocumentElement().normalize();
        return document;
    }*/
}
