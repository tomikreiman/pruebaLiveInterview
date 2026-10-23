<?php

namespace App\Domain;

interface IPerson {
	function getPersonalTag(): string;
	function getPhone(): ?string;
	function getMail(): ?string;
}
