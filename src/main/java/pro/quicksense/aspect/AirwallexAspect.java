package pro.quicksense.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pro.quicksense.service.PaymentService;

@Aspect
@Component
public class AirwallexAspect {

    /**
     * Intercept all Airwallex requests by authentication
     */
    @Around("@annotation(pro.quicksense.annotation.AirwallexRequest)")
    public Object authAirwallexRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        PaymentService paymentService = (PaymentService) joinPoint.getTarget();
        paymentService.authByAirwallex();
        return joinPoint.proceed();
    }
}
