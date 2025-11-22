package com.yd.price.workflow.metrics;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MicrometerDaoAspect {
    private static final double[] PERCENTILES = {0.5, 0.95, 0.99};
    private final MeterRegistry registry;

    public MicrometerDaoAspect(final MeterRegistry registry) {
        this.registry = registry;
    }

      @Around("""
            execution(public * com.yd.price.workflow.db.repository.*.*(..))
            && within(com.yd.price.workflow.db.repository.Dao+)
            """)
    public Object processMicrometerMonitoring(ProceedingJoinPoint joinPoint) throws Throwable {
        String exceptionClass = TimedAspect.DEFAULT_EXCEPTION_TAG_VALUE;
        final Timer.Sample sample = Timer.start(registry);
        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            exceptionClass = ex.getClass().getSimpleName();
            throw ex;
        } finally {
            sample.stop(initTimer(joinPoint, exceptionClass));
        }
    }

    private Timer initTimer(ProceedingJoinPoint joinPoint, String exceptionClass) {
        final Object thisObject = joinPoint.getThis();
        if (thisObject == null) {
            throw new RuntimeException("The executable object is currently unavailable. " +
                    "Possible issue with incorrect pointcut configuration.");
        }

        final Class<?> repositoryInterface = thisObject.getClass().getInterfaces()[0];
        final String shortClassName = repositoryInterface.getSimpleName();
        final String methodName = joinPoint.getSignature().getName();

        return Timer.builder(shortClassName + '.' + methodName)
                .tag("class", repositoryInterface.getName())
                .tag("method", methodName)
                .tag(TimedAspect.EXCEPTION_TAG, exceptionClass)
                .publishPercentiles(PERCENTILES)
                .register(registry);
    }
}
