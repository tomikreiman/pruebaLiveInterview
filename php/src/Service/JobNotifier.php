<?php

namespace App\Service;

use App\Dao\IPersonRepository;
use App\Domain\IJob;
use App\Domain\IPerson;

class JobNotifier {
    private IPersonRepository $personRepository;
    private IMailSender $mailSender;
    private ISmsSender $smsSender;

    public function __construct(
        IPersonRepository $personRepository,
        IMailSender $mailSender,
        ISmsSender $smsSender
    ) {
        $this->personRepository = $personRepository;
        $this->mailSender = $mailSender;
        $this->smsSender = $smsSender;
    }

    public function notify(IJob $job): void {
        foreach ($this->personRepository->getAll() as $person) {
            if (!in_array($person->getPersonalTag(), $job->getTags())) {
                continue;
            }
            $this->notifyPerson($person, $job);
        }
    }

    private function notifyPerson(IPerson $person, IJob $job) {
        if ($person->getPhone()) {
            $this->smsSender->send($person->getPhone(), $this->getMessage($job));
        }
        if ($person->getMail()) {
            $this->mailSender->send($person->getMail(), $this->getMessage($job));
        }
    }

    private function getMessage(IJob $job): string {
        if ($job->getSalary() > 100000) {
            $salary = 100000 . '+';
        } else if ($job->getSalary() < 30000) {
            $salary = 30000 . '-';
        } else {
            $salary = $job->getSalary();
        }
        
        return 'There is a new alert for your subscription. Job ' 
            . $job->getName() 
            . ', with salary ' 
            . $salary 
            . '.';
    }
}
