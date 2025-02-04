package com.malinouski.cache.multitenancy.web.interceptor;

import com.malinouski.cache.multitenancy.context.TenantContext;
import com.malinouski.cache.multitenancy.resolvers.HttpHeaderTenantResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantInterceptor implements HandlerInterceptor {
    private final HttpHeaderTenantResolver tenantResolver;

    @Value("${multitenant.defaultTenant}")
    String defaultTenant;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String tenantId = tenantResolver.resolveTenantId(request);
        log.info("TenantInterceptor: Received header for tenantId: {}", tenantId);
        if (tenantId == null || tenantId.isEmpty()) {
            tenantId = defaultTenant;
            log.info("TenantInterceptor: TenantId header is missing. Using default tenant: {}", defaultTenant);
        } else {
            log.info("TenantInterceptor: Received header for tenantId: {}", tenantId);
        }

        TenantContext.setCurrentTenant(tenantId);
        log.info("TenantInterceptor: TenantContext set to: {}", TenantContext.getCurrentTenant());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        clear();
    }

    private void clear() {
        TenantContext.clear();
    }
}
