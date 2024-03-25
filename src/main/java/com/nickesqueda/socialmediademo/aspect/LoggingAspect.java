package com.nickesqueda.socialmediademo.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
  @Pointcut(
      "execution(* com.nickesqueda.socialmediademo..*(..)) && !execution(* com.nickesqueda.socialmediademo.security.JwtAuthFilter.*(..))")
  public void allPackagesAllMethods() {}

  @Pointcut("execution(* com.nickesqueda.socialmediademo.controller.*.*(..))")
  public void allControllerMethods() {}

  @Pointcut("execution(* com.nickesqueda.socialmediademo.service.*.*(..))")
  public void allServiceMethods() {}

  @Before("allControllerMethods()")
  public void logMethodCallsInfoMode(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] methodArgs = joinPoint.getArgs();
    log.info("Calling {} with arguments: {}", methodName, methodArgs);
  }

  @Before("allPackagesAllMethods()")
  public void logMethodCallsTraceMode(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] methodArgs = joinPoint.getArgs();
    log.trace("Calling {} with arguments: {}", methodName, methodArgs);
  }

  @AfterReturning(pointcut = "allControllerMethods() || allServiceMethods()", returning = "returnValue")
  public void logMethodReturnInfoMode(JoinPoint joinPoint, Object returnValue) {
    String methodName = joinPoint.getSignature().toShortString();
    String logMessage = "Returning from {} with value: {}";
    if (methodName.toUpperCase().contains("CONTROLLER")) {
      logMessage = "Returning successful response from {} with value: {}";
    }
    log.info(logMessage, methodName, returnValue);
  }

  @AfterReturning(pointcut = "allPackagesAllMethods()", returning = "returnValue")
  public void logMethodReturnDebugMode(JoinPoint joinPoint, Object returnValue) {
    String methodName = joinPoint.getSignature().toShortString();
    log.debug("Returning from {} with value: {}", methodName, returnValue);
  }

  @AfterThrowing(pointcut = "allPackagesAllMethods()", throwing = "e")
  public void logMethodException(JoinPoint joinPoint, Exception e) {
    String methodName = joinPoint.getSignature().toShortString();
    Object[] methodArgs = joinPoint.getArgs();
    log.error("Exception in {} :: {}", methodName, e.toString());
    log.debug("Exception in {} :: Method args: {} :: ", methodName, methodArgs, e);
  }
}
