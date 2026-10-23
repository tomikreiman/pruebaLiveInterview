<?php

namespace App\Controller;

use App\Dao\IJobRepository;
use App\Service\ScheduleService;
use OpenApi\Attributes as OA;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class ScheduleController {
    private IJobRepository $jobRepository;
    private ScheduleService $scheduleService;

    public function __construct(IJobRepository $jobRepository, ScheduleService $scheduleService) {
        $this->jobRepository = $jobRepository;
        $this->scheduleService = $scheduleService;
    }

    #[OA\Get(
        path: '/api/jobs/schedule',
        summary: 'Maximum set of non-overlapping interview/event slots',
        description: 'Each job has an interview/event slot [start_hour, end_hour) in 24-hour '
            . 'format that occupies a single shared resource (one room/interviewer). Returns the '
            . 'largest set of jobs whose slots do not overlap.',
        responses: [
            new OA\Response(
                response: 200,
                description: 'The selected non-overlapping interview/event slots',
                content: new OA\JsonContent(
                    properties: [
                        new OA\Property(
                            property: 'selected_count',
                            type: 'integer',
                            description: 'Number of non-overlapping slots selected'
                        ),
                        new OA\Property(
                            property: 'selected',
                            type: 'array',
                            items: new OA\Items(
                                properties: [
                                    new OA\Property(property: 'name', type: 'string'),
                                    new OA\Property(
                                        property: 'start_hour',
                                        type: 'integer',
                                        description: 'Slot start hour, inclusive (0-23, 24h format)'
                                    ),
                                    new OA\Property(
                                        property: 'end_hour',
                                        type: 'integer',
                                        description: 'Slot end hour, exclusive (1-24, 24h format)'
                                    ),
                                ],
                                type: 'object'
                            )
                        ),
                    ]
                )
            ),
        ]
    )]
    public function getSchedule(Request $request, Response $response): Response {
        $jobs = $this->jobRepository->getAll();
        $selected = $this->scheduleService->maxNonOverlapping($jobs);

        $payload = [
            'selected_count' => count($selected),
            'selected' => $selected,
        ];

        $response->getBody()->write(json_encode($payload));

        return $response->withHeader('Content-Type', 'application/json');
    }
}
