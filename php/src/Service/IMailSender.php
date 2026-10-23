<?php

namespace App\Service;

interface IMailSender {

	/** @throws MailCannotBeSentException */
	function send(string $mail, string $message);
}
