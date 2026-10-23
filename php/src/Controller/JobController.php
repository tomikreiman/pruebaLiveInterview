<?php

namespace App\Controller;

use App\Service\JobService;
use App\Service\JobNotifier;
use App\Domain\Job;
use OpenApi\Attributes as OA;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;
use InvalidArgumentException;
use PDOException;

#[OA\Info(
    version: '1.0.0',
    title: 'Job Notifier API',
    description: 'Simple PHP REST API for job notifications'
)]
#[OA\Server(
    url: 'http://localhost:8082',
    description: 'Local development server'
)]
class JobController {
    private JobService $jobService;
    private JobNotifier $jobNotifier;

    public function __construct(JobService $jobService, JobNotifier $jobNotifier) {
        $this->jobService = $jobService;
        $this->jobNotifier = $jobNotifier;
    }

    #[OA\Post(
        path: '/api/jobs',
        summary: 'Create a new job',
        requestBody: new OA\RequestBody(
            required: true,
            content: new OA\JsonContent(
                required: ['name'],
                properties: [
                    new OA\Property(property: 'name', type: 'string'),
                    new OA\Property(
                        property: 'tags',
                        type: 'array',
                        items: new OA\Items(type: 'string')
                    ),
                    new OA\Property(property: 'salary', type: 'integer'),
                ]
            )
        ),
        responses: [
            new OA\Response(
                response: 201,
                description: 'Job created successfully',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(property: 'success', type: 'boolean'),
                        new OA\Property(property: 'job_id', type: 'integer'),
                        new OA\Property(property: 'message', type: 'string'),
                    ]
                )
            ),
            new OA\Response(
                response: 400,
                description: 'Bad request (e.g. Title is required)',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(property: 'error', type: 'string'),
                    ]
                )
            ),
            new OA\Response(
                response: 500,
                description: 'Internal server error',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(property: 'error', type: 'string'),
                    ]
                )
            ),
        ]
    )]
    public function createJob(Request $request, Response $response): Response {
        $data = json_decode($request->getBody()->getContents(), true);

        try {
            if (!isset($data['name'])) {
                throw new InvalidArgumentException('Name is required');
            }

            $job = new Job(
                $data['name'],
                $data['tags'] ?? [],
                (int)($data['salary'] ?? 0)
            );

            $jobId = $this->jobService->createJob($job);

            $this->jobNotifier->notify($job);

            $response->getBody()->write(json_encode([
                'success' => true,
                'job_id' => $jobId,
                'message' => 'Job created successfully'
            ]));

            return $response->withStatus(201)->withHeader('Content-Type', 'application/json');
        } catch (InvalidArgumentException $e) {
            $response->getBody()->write(json_encode(['error' => $e->getMessage()]));
            return $response->withStatus(400)->withHeader('Content-Type', 'application/json');
        } catch (PDOException $e) {
            $response->getBody()->write(json_encode(['error' => 'Failed to create job: ' . $e->getMessage()]));
            return $response->withStatus(500)->withHeader('Content-Type', 'application/json');
        }
    }
}
