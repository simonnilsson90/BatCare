
package com.simme.lektion_5_java_ee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration  // Allows Spring to find this config
@EnableWebMvc
public class AppWebConfig implements WebMvcConfigurer {


    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }



    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/test").setViewName("test");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/logout").setViewName("logout");
        registry.addViewController("/register").setViewName("register");
        registry.addViewController("/admin-page").setViewName("admin-page");
      //  registry.addViewController("/gadgets").setViewName("gadgets");

    }

    // Allows CSS, JS, HTML, IMAGES to be displayed
    // TODO - Is the 'resources' really necessary?
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**", "/static/**")
                .addResourceLocations("classpath:/static/", "classpath:/resources/");
    }
}