class SalaryStatsService:
    def compute_stats(self, jobs: list) -> dict:
        return {
            "total_jobs": len(jobs),
            "average_salary": self._average_salary(jobs),
            "median_salary": self._median_salary(jobs),
            "count_by_tag": self._count_by_tag(jobs),
            "salary_bands": self._salary_bands(jobs),
        }

    def _average_salary(self, jobs: list) -> int:
        total = 0
        for job in jobs:
            total += job["salary"]

        return int(total / len(jobs))

    def _median_salary(self, jobs: list) -> int:
        salaries = []
        for job in jobs:
            salaries.append(job["salary"])

        n = len(salaries)
        for i in range(n):
            min_index = i
            for j in range(i + 1, n):
                if salaries[j] < salaries[min_index]:
                    min_index = j
            salaries[i], salaries[min_index] = salaries[min_index], salaries[i]

        return int(salaries[n // 2])

    def _count_by_tag(self, jobs: list) -> dict:
        counts = {}
        for job in jobs:
            tag = job["tags"]
            if tag not in counts:
                counts[tag] = 0
            counts[tag] += 1

        return counts

    def _salary_bands(self, jobs: list) -> list:
        low = 0
        mid = 0
        high = 0
        for job in jobs:
            salary = job["salary"]
            if 0 <= salary <= 50000:
                low += 1
            if 50000 <= salary <= 100000:
                mid += 1
            if salary >= 100000:
                high += 1

        return [
            {"range": "0-50000", "count": low},
            {"range": "50000-100000", "count": mid},
            {"range": "100000+", "count": high},
        ]
