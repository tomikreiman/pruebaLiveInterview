<?php

namespace App\Domain;

class Person implements IPerson {
	private $name;
	private $personalTag;
	private $mail;
	private $phone;

	function __construct(
		string $name, 
		string $personalTag, 
		?string $mail, 
		?string $phone
	) {
		$this->name = $name;
		$this->personalTag = $personalTag;
		$this->mail = $mail;
		$this->phone = $phone;
	}

	function getPersonalTag(): string {
		return $this->personalTag;
	}

	function getPhone(): ?string {
		return $this->phone;
	}

	function getMail(): ?string {
		return $this->mail;
	}
}
