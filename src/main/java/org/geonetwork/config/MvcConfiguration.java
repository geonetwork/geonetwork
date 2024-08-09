package org.geonetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** MVC configuration.
 *
 */
@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/home").setViewName("home");
    registry.addViewController("/").setViewName("home");
    registry.addViewController("/login").setViewName("login");
  }

  //  @Override
  //  public void addResourceHandlers(ResourceHandlerRegistry registry) {
  //    registry
  //        .addResourceHandler("/**")
  //        .addResourceLocations("classpath:/static/")
  //        .resourceChain(true)
  //        .addResolver(
  //            new PathResourceResolver() {
  //              @Override
  //              protected Resource getResource(String resourcePath, Resource location)
  //                  throws IOException {
  //                Resource requestedResource = location.createRelative(resourcePath);
  //
  //                return requestedResource.exists() && requestedResource.isReadable()
  //                    ? requestedResource
  //                    : new ClassPathResource("/static/index.html");
  //              }
  //            });
  //  }
}
