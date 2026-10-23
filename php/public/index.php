<?php

use Slim\Factory\AppFactory;
use DI\Container;

require __DIR__ . '/../vendor/autoload.php';

// Dependencies
$container = new Container();
$dependencies = require __DIR__ . '/../config/dependencies.php';
$dependencies($container);

AppFactory::setContainer($container);

// Create app
$app = AppFactory::create();

// Register routes
$routes = require __DIR__ . '/../config/routes.php';
$routes($app);

// Run app
$app->run();
