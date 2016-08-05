package com.floriantoenjes.instateam.converter;

import com.floriantoenjes.instateam.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class StringRoleConverter implements Converter<String, Role> {

    @Override
    public Role convert(String source) {
        Role role = new Role();
        int id = Integer.parseInt(source);
        role.setId(id);
        return role;
    }

    @Bean
    public ConversionService getConversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        Set<Converter> converters = new HashSet<>();
        converters.add(new StringRoleConverter());
        bean.setConverters(converters);
        return bean.getObject();
    }
}


