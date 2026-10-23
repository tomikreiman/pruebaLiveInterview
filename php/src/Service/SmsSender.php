<?php

namespace App\Service;

use Psr\Log\LoggerInterface;

class SmsSender implements ISmsSender {
    private LoggerInterface $logger;

    public function __construct(LoggerInterface $logger) {
        $this->logger = $logger;
    }

	/** @throws SmsCannotBeSentException */
	function send(string $phone, string $message) {
		$this->logger->info("Sending SMS to: {$phone}", ['message' => $message]);
	}
}
