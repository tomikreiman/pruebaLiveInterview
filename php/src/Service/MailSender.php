<?php

namespace App\Service;

use Psr\Log\LoggerInterface;

class MailSender implements IMailSender {
    private LoggerInterface $logger;

    public function __construct(LoggerInterface $logger) {
        $this->logger = $logger;
    }

	/** @throws MailCannotBeSentException */
	function send(string $mail, string $message) {
		$this->logger->info("Sending email to: {$mail}", ['message' => $message]);
	}
}
