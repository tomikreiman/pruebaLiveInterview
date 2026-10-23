class ScheduleService:
    def max_non_overlapping(self, jobs: list) -> list:
        intervals = []
        for job in jobs:
            intervals.append({
                "name": job["name"],
                "start_hour": int(job["start_hour"]),
                "end_hour": int(job["end_hour"]),
            })

        n = len(intervals)
        for i in range(n):
            for j in range(n - 1):
                if intervals[j]["start_hour"] > intervals[j + 1]["start_hour"]:
                    intervals[j], intervals[j + 1] = intervals[j + 1], intervals[j]

        selected = []
        last_end = 0
        for interval in intervals:
            if interval["start_hour"] > last_end:
                selected.append(interval)
                last_end = interval["end_hour"]

        return selected
