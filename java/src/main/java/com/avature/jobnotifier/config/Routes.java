package com.avature.jobnotifier.config;

import com.avature.jobnotifier.controller.HomeController;
import com.avature.jobnotifier.controller.JobController;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Routes {
    private Routes() {
    }

    public static void register(Javalin app, Container container) {
        HomeController homeController = container.get("home_controller");
        JobController jobController = container.get("job_controller");

        app.get("/", homeController::index);

        app.get("/openapi.yaml", ctx -> {
            Path file = Path.of("public", "openapi.yaml");
            if (!Files.exists(file)) {
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            ctx.contentType("application/yaml").result(Files.readString(file));
        });

        app.get("/docs/{path}", ctx -> {
            Path file = Path.of("public", "docs", ctx.pathParam("path"));
            if (!Files.exists(file)) {
                ctx.status(HttpStatus.NOT_FOUND);
                return;
            }
            ctx.result(Files.readAllBytes(file));
        });

        app.post("/api/jobs", jobController::createJob);
    }
}
