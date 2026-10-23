<?php

namespace App\Service;

interface ISmsSender {
	
	/** @throws SmsCannotBeSentException */
	function send(string $phone, string $message);
}
