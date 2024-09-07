package com.sks.gateway;

import com.sks.gateway.auth.RequestRouteMatcher;
import org.springframework.stereotype.Component;

@Component
public class RestrictedRoutesConfig {
    public RequestRouteMatcher getRestrictedRoutes() {
        return RequestRouteMatcher.builder()
                .get("/fridge/*")
                .put("/fridge/*")
                .delete("/fridge/*/*")
                .post("/recipes")
                .put("/recipes/*")
                .delete("/recipes/*")
                .get("/surveys/*")
                .put("/surveys/*")
                .delete("/surveys/*")
                .post("/surveys")
                .get("/surveys/my")
                .put("/surveys/*/vote/*")
                .build();
    }
}
