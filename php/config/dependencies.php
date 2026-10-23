<?php

use DI\Container;
use Psr\Container\ContainerInterface;
use Psr\Log\LoggerInterface;
use Monolog\Logger;
use Monolog\Handler\StreamHandler;
use App\Dao\JobRepository;
use App\Dao\IJobRepository;
use App\Dao\PersonRepository;
use App\Dao\IPersonRepository;
use App\Service\JobService;
use App\Controller\HomeController;
use App\Controller\JobController;
use App\Service\IMailSender;
use App\Service\MailSender;
use App\Service\ISmsSender;
use App\Service\SmsSender;
use App\Service\JobNotifier;

return function (Container $container) {
    $container->set(LoggerInterface::class, function() {
        $logger = new Logger('job-notifier');
        $logger->pushHandler(new StreamHandler('php://stdout', Logger::DEBUG));
        return $logger;
    });

    $container->set('PDO', function() {
        $dbPath = getenv('DB_PATH') ?: __DIR__ . '/../data/jobs.db';
        $dsn = 'sqlite:' . $dbPath;
        $pdo = new PDO($dsn);
        $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        return $pdo;
    });

    $container->set(IMailSender::class, function(ContainerInterface $c) {
        return new MailSender($c->get(LoggerInterface::class));
    });

    $container->set(ISmsSender::class, function(ContainerInterface $c) {
        return new SmsSender($c->get(LoggerInterface::class));
    });

    $container->set(IJobRepository::class, function(ContainerInterface $c) {
        return new JobRepository($c->get('PDO'));
    });

    $container->set(IPersonRepository::class, function(ContainerInterface $c) {
        return new PersonRepository($c->get('PDO'));
    });

    $container->set(JobNotifier::class, function(ContainerInterface $c) {
        return new JobNotifier($c->get(IPersonRepository::class), $c->get(IMailSender::class), $c->get(ISmsSender::class));
    });

    $container->set(JobService::class, function(ContainerInterface $c) {
        return new JobService($c->get(IJobRepository::class));
    });

    $container->set(JobController::class, function(ContainerInterface $c) {
        return new JobController($c->get(JobService::class), $c->get(JobNotifier::class));
    });

    $container->set(HomeController::class, function() {
        return new HomeController();
    });
};
