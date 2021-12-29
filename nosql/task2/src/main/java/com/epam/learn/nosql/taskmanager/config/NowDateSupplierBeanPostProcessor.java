package com.epam.learn.nosql.taskmanager.config;

import com.epam.learn.nosql.taskmanager.dao.NowDateSupplier;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Profile("test")
public class NowDateSupplierBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof NowDateSupplier) {
            ((NowDateSupplier) bean).setNowDateSupplier(
                    () -> ZonedDateTime.parse("2021-06-17T18:00:00+03:00")
            );
        }
        return bean;
    }
}
