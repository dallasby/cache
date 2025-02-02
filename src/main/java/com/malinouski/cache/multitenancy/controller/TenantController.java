package com.malinouski.cache.multitenancy.controller;

import com.malinouski.cache.multitenancy.context.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tenant")
public class TenantController {
    @GetMapping
    String getTenant() {
        return TenantContext.getCurrentTenant();
    }
}
