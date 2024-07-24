package org.geonetwork;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Application context provider.
 *
 * <p>Can be use to retrieve bean from a non spring context. eg. Saxon function.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

  @Getter private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
