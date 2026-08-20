package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
@Configuration
@ComponentScan("ru.bsuedu.cad.lab")
public class AppConfig {
}