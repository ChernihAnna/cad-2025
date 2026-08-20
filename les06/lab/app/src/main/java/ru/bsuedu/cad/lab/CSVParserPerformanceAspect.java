package ru.bsuedu.cad.lab;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CSVParserPerformanceAspect {

    @Around("execution(* ru.bsuedu.cad.lab.CSVParser.parse(..))")
    public Object measureParsingTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.nanoTime();

        Object result = joinPoint.proceed();

        long end = System.nanoTime();

        long elapsed = end - start;

        System.out.println(
                "Время парсинга CSV: " + elapsed + " нс"
        );

        return result;
    }
}