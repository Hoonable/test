package org.example.newsfeed.aop;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "ReqeustAop")
@Aspect
@Component
public class ReqeustAop {
    @Pointcut("execution(* org.example.newsfeed.controller..*(..))")
    private void forAllController(){}

    @After("forAllController()")
    public void afterAllController(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("Request URL: " + request.getRequestURL() + ", HTTP Method: " + request.getMethod());
    }
}
