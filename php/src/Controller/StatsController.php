<?php

namespace App\Controller;

use App\Dao\IJobRepository;
use App\Service\SalaryStatsService;
use OpenApi\Attributes as OA;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class StatsController {
    private IJobRepository $jobRepository;
    private SalaryStatsService $statsService;

    public function __construct(IJobRepository $jobRepository, SalaryStatsService $statsService) {
        $this->jobRepository = $jobRepository;
        $this->statsService = $statsService;
    }

    #[OA\Get(
        path: '/api/jobs/stats',
        summary: 'Salary and tag statistics across all jobs',
        responses: [
            new OA\Response(
                response: 200,
                description: 'Aggregated job statistics',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(property: 'total_jobs', type: 'integer'),
                        new OA\Property(property: 'average_salary', type: 'integer'),
                        new OA\Property(property: 'median_salary', type: 'integer'),
                        new OA\Property(
                            property: 'count_by_tag',
                            type: 'object',
                            additionalProperties: new OA\AdditionalProperties(type: 'integer')
                        ),
                        new OA\Property(
                            property: 'salary_bands',
                            type: 'array',
                            items: new OA\Items(
                                properties: [
                                    new OA\Property(property: 'range', type: 'string'),
                                    new OA\Property(property: 'count', type: 'integer'),
                                ],
                                type: 'object'
                            )
                        ),
                    ]
                )
            ),
        ]
    )]
    public function getStats(Request $request, Response $response): Response {
        $jobs = $this->jobRepository->getAll();
        $stats = $this->statsService->computeStats($jobs);

        $response->getBody()->write(json_encode($stats));

        return $response->withHeader('Content-Type', 'application/json');
    }
}
