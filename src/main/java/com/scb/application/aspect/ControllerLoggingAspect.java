package com.scb.application.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerPointcut() {
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut() {
    }

    @Around("restControllerPointcut() || controllerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        
        log.info("==> Request: {} {} (Controller: {}.{})",
                request.getMethod(),
                request.getRequestURI(),
                signature.getDeclaringType().getSimpleName(),
                signature.getName());
        
        if (joinPoint.getArgs().length > 0) {
            log.info("==> Method arguments: {}", Arrays.toString(joinPoint.getArgs()));
        }
        
        try {
            Object result = joinPoint.proceed();
            
            long executionTime = System.currentTimeMillis() - start;
            log.info("<== Response: {} {} completed in {}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    executionTime);
            
            return result;
        } catch (Exception e) {
            log.error("<== Exception in {}.{}: {}",
                    signature.getDeclaringType().getSimpleName(),
                    signature.getName(),
                    e.getMessage());
            throw e;
        }
    }
}