package com.zettamine.mpa.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LoggingAspect {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
    
    @Before("com.zettamine.mpa.aop.CommonPointcutDefs.escrowLogging()")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
    	String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsTypes = getArgsTypes(args);
        LOGGER.info(String.format("Starting Execution of - \"%s.%s()\" with parameters: %s", className, methodName, argsTypes));
    }

    @After("com.zettamine.mpa.aop.CommonPointcutDefs.escrowLogging()")
    public void logAfterMethodCall(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String argsTypes = getArgsTypes(args);
        LOGGER.info(String.format("After the Execution of - \"%s.%s()\" with parameters: %s", className, methodName, argsTypes));
    }

    
    @AfterThrowing(pointcut = "com.zettamine.mpa.aop.CommonPointcutDefs.escrowLogging()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.error(String.format("Exception thrown in method \"%s.%s()\": %s", className, methodName, exception.getMessage()), exception);
    }
    
    private static String getArgsTypes(Object[] args) {
    	StringBuilder argTypes = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                argTypes.append(arg.getClass().getSimpleName()).append(", ");
            } else {
                argTypes.append("null").append(", ");
            }
        }
        if (argTypes.length() > 0) {
            argTypes.setLength(argTypes.length() - 2);
        }
        return argTypes.toString();
    }
 
}
