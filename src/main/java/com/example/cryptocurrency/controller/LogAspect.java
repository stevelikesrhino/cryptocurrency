package com.example.cryptocurrency.controller;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    @Around("execution(public * com.example.cryptocurrency.controller.*.*(..))")
    public Object around(ProceedingJoinPoint p ){
        System.out.println("around - function name is "+ p.getSignature().getName());
        System.out.println("around - arguments with " + Arrays.toString(p.getArgs()));

        long a = System.currentTimeMillis();
        Object res = null;
        try {
            res = p.proceed();
        }catch (Throwable ex){
            throw new RuntimeException(ex);
        }
        System.out.println("around - function takes " + (System.currentTimeMillis() - a) + " ms"); // testing performance
        return res;
    }

}
