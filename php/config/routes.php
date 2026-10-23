<?php

use Slim\App;
use App\Controller\HomeController;
use App\Controller\JobController;

return function (App $app) {
    // Landing page
    $app->get('/', [HomeController::class, 'index']);

    // Serve the generated OpenAPI spec (generated at container startup, not on host mount)
    $app->get('/openapi.yaml', function ($request, $response) {
        $file = __DIR__ . '/../public/openapi.yaml';
        if (!file_exists($file)) {
            return $response->withStatus(404);
        }
        $response->getBody()->write(file_get_contents($file));
        return $response->withHeader('Content-Type', 'application/yaml');
    });

    // API endpoint to save a new job
    $app->post('/api/jobs', [JobController::class, 'createJob']);
};
