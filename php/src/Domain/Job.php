<?php

namespace App\Domain;

class Job implements IJob {
	private $name;
	private $tags;
	private $salary;

	function __construct(string $name, array $tags, int $salary) {
		$this->name = $name;
		$this->tags = $tags;
		$this->salary = $salary;
	}

	function getTags(): array {
		return $this->tags; 
	}

	function getName(): string {
		return $this->name;
	}

	function getSalary(): int {
		return $this->salary;
	}
}
