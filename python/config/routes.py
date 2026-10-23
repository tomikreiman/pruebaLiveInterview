from flask import Flask, Response, send_from_directory

from config.dependencies import Container


def register_routes(app: Flask, container: Container) -> None:
    home_controller = container.get("home_controller")
    job_controller = container.get("job_controller")

    @app.get("/")
    def index():
        return home_controller.index()

    @app.get("/openapi.yaml")
    def openapi():
        return send_from_directory(app.root_path + "/public", "openapi.yaml")

    @app.get("/docs/<path:path>")
    def docs(path):
        return send_from_directory(app.root_path + "/public/docs", path)

    @app.post("/api/jobs")
    def create_job():
        return job_controller.create_job()
