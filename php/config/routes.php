<?php

use Slim\App;
use App\Controller\HomeController;
use App\Controller\JobController;
use App\Controller\StatsController;
use App\Controller\ScheduleController;

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

    // API endpoint for job salary/tag statistics
    $app->get('/api/jobs/stats', [StatsController::class, 'getStats']);

    // API endpoint for max non-overlapping notification-window schedule
    $app->get('/api/jobs/schedule', [ScheduleController::class, 'getSchedule']);
};
