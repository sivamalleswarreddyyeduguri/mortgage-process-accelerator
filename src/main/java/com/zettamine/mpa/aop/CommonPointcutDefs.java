package com.zettamine.mpa.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class CommonPointcutDefs {
	
	@Pointcut("execution(* com.zettamine.mpa.*.*.*.*(..))")
	public void escrowLogging() {}

}
