import os
import sys

# Add the project root to the Python path so absolute imports work
project_root = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
if project_root not in sys.path:
    sys.path.insert(0, project_root)

from flask import Flask

from config.dependencies import Container, configure_dependencies
from config.routes import register_routes


def create_app() -> Flask:
    app = Flask(__name__, root_path=project_root)
    app.config["JSON_SORT_KEYS"] = False

    container = Container()
    configure_dependencies(container)
    register_routes(app, container)

    return app


app = create_app()

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
