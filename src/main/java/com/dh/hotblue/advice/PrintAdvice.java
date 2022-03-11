package com.dh.hotblue.advice;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class PrintAdvice {

	@Around("execution(* com.pick.*.controller..*.*(..))")
	@SuppressWarnings("rawtypes")
	public Object logAction(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Class clazz = joinPoint.getTarget().getClass();
		Object result = null;
		try {
			result = joinPoint.proceed(joinPoint.getArgs());
			return result;
		} finally {
			log.info(getRequestUrl(joinPoint, clazz));
			log.info("parameters" + params(joinPoint));
			log.info("response: " + ((result==null)?"null":result.toString()));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String getRequestUrl(JoinPoint joinPoint, Class clazz) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
		String baseUrl = (requestMapping != null) ? requestMapping.value()[0] : "";

		String url = Stream
				.of(GetMapping.class, PutMapping.class, PostMapping.class, PatchMapping.class, DeleteMapping.class,
						RequestMapping.class)
				.filter(mappingClass -> method.isAnnotationPresent(mappingClass))
				.map(mappingClass -> getUrl(method, mappingClass, baseUrl)).findFirst().orElse(null);
		return url;
	}

	private String getUrl(Method method, Class<? extends Annotation> annotationClass, String baseUrl) {
		Annotation annotation = method.getAnnotation(annotationClass);
		String[] value;
		String httpMethod = null;
		try {
			value = (String[]) annotationClass.getMethod("value").invoke(annotation);
			httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase();
		} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			return null;
		}
		return String.format("%s %s%s", httpMethod, baseUrl, value.length > 0 ? value[0] : "");
	}

	@SuppressWarnings("rawtypes")
	private Map params(JoinPoint joinPoint) {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		String[] parameterNames = codeSignature.getParameterNames();
		Object[] args = joinPoint.getArgs();
		Map<String, Object> params = new HashMap<>();
		if (parameterNames != null) {
			for (int i = 0; i < parameterNames.length; i++) {
				if(parameterNames[i].contains("request") || parameterNames[i].contains("response")) continue;
				params.put(parameterNames[i], args[i]);
			}
		}
		return params;
	}
}
