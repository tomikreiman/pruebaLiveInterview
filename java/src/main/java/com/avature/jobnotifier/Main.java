package com.avature.jobnotifier;

import com.avature.jobnotifier.config.Container;
import com.avature.jobnotifier.config.Dependencies;
import com.avature.jobnotifier.config.Routes;
import com.avature.jobnotifier.scripts.GenerateOpenApi;
import com.avature.jobnotifier.scripts.InitDb;
import io.javalin.Javalin;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        String dbPath = System.getenv().getOrDefault("DB_PATH", "data/jobs.db");
        InitDb.run(dbPath);
        GenerateOpenApi.run("public/openapi.yaml");

        Container container = new Container();
        Dependencies.configure(container);

        Javalin app = Javalin.create();
        Routes.register(app, container);

        int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "7000"));
        app.start(port);
    }
}
