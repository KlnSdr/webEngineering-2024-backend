package com.sks.gateway.common;

import java.util.ArrayList;
import java.util.List;

public class RequestRouteMatcher {
    private final List<String> getRoutes;
    private final List<String> postRoutes;
    private final List<String> putRoutes;
    private final List<String> deleteRoutes;

    public RequestRouteMatcher(List<String> getRoutes, List<String> postRoutes, List<String> putRoutes, List<String> deleteRoutes) {
        this.getRoutes = getRoutes;
        this.postRoutes = postRoutes;
        this.putRoutes = putRoutes;
        this.deleteRoutes = deleteRoutes;
    }

    public boolean isRestrictedRoute(String path, String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> getRoutes.stream().anyMatch(path::matches);
            case "POST" -> postRoutes.stream().anyMatch(path::matches);
            case "PUT" -> putRoutes.stream().anyMatch(path::matches);
            case "DELETE" -> deleteRoutes.stream().anyMatch(path::matches);
            default -> false;
        };
    }

    public static RestrictedRoutesBuilder builder() {
        return new RestrictedRoutesBuilder();
    }

    public static class RestrictedRoutesBuilder {
        private static final String PATTERN_STAR = "[^/]+";
        private final List<String> getRoutes = new ArrayList<>();
        private final List<String> postRoutes = new ArrayList<>();
        private final List<String> putRoutes = new ArrayList<>();
        private final List<String> deleteRoutes = new ArrayList<>();

        public RestrictedRoutesBuilder get(String path) {
            getRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder post(String path) {
            postRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder put(String path) {
            putRoutes.add(path);
            return this;
        }

        public RestrictedRoutesBuilder delete(String path) {
            deleteRoutes.add(path);
            return this;
        }

        public RequestRouteMatcher build() {
            getRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            postRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            putRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            deleteRoutes.replaceAll(route -> route.replace("*", PATTERN_STAR));
            return new RequestRouteMatcher(getRoutes, postRoutes, putRoutes, deleteRoutes);
        }
    }
}
