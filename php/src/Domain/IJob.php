<?php

namespace App\Domain;

interface IJob {
	function getName(): string;
	function getTags(): array;
	function getSalary(): int;
}
