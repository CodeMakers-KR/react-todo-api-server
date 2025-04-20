package com.example.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimingAspect {
	
	private Logger logger = LoggerFactory.getLogger(TimingAspect.class);
	
	@Pointcut("execution(public * com.example.demo..service.*ServiceImpl.*(..))")
	private void aroundTarget() {}
	
	/**
	 * 원래 실행될 메소드의 전, 후에 공통코드를 실행한다.
	 * @param pjp 원래 실행될 클래스와 메소드 정보
	 * @return 원래 실행될 메소드의 반환 값
	 * @throws Throwable 
	 */
	@Around("aroundTarget()") // 원래 실행될 메소드의 전, 후, Exception 상황시 모두 실행.
	public Object timingAdvice(ProceedingJoinPoint pjp) throws Throwable {
		Object result = null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		try {
			result = pjp.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			stopWatch.stop();
			
			String classPath = pjp.getTarget().getClass().getName();
			String methodName = pjp.getSignature().getName();
			logger.debug("{}.{} 걸린시간: {}밀리초", classPath, methodName, stopWatch.getLastTaskTimeMillis());
		}
		
		return result;
	}
}

