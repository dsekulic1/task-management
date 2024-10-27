package com.example.taskmanagement.config;

import com.example.taskmanagement.controller.exception.InvalidDateFormatException;
import com.example.taskmanagement.repository.entity.Category;
import com.example.taskmanagement.repository.entity.Priority;
import com.example.taskmanagement.repository.entity.Status;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ModelMapperConfiguration {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        modelMapper.addConverter(new AbstractConverter<String, Date>() {
            @Override
            protected Date convert(String source) {
                try {
                    return source != null ? dateFormat.parse(source) : null;
                } catch (ParseException e) {
                    throw new InvalidDateFormatException("Invalid date format, expected yyyy-MM-dd");
                }
            }
        });

        modelMapper.addConverter(new AbstractConverter<Date, String>() {
            @Override
            protected String convert(Date source) {
                return source != null ? dateFormat.format(source) : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<String, Priority>() {
            @Override
            protected Priority convert(String source) {
                return source != null ? Priority.valueOf(source.toUpperCase()) : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<Priority, String>() {
            @Override
            protected String convert(Priority source) {
                return source != null ? source.name() : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<String, Status>() {
            @Override
            protected Status convert(String source) {
                return source != null ? Status.valueOf(source.toUpperCase()) : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<Status, String>() {
            @Override
            protected String convert(Status source) {
                return source != null ? source.name() : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<String, Category>() {
            @Override
            protected Category convert(String source) {
                return source != null ? Category.valueOf(source.toUpperCase()) : null;
            }
        });

        modelMapper.addConverter(new AbstractConverter<Category, String>() {
            @Override
            protected String convert(Category source) {
                return source != null ? source.name() : null;
            }
        });
        return modelMapper;
    }
}