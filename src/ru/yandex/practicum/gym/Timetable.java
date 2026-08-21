package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, List<TrainingSession>> scheduleByDay;

    public Timetable() {
        this.scheduleByDay = new HashMap<>();
    }

    public void addNewTrainingSession(TrainingSession session) {
        DayOfWeek day = session.getDayOfWeek();

        List<TrainingSession> sessionsForDay = scheduleByDay.get(day);

        if (sessionsForDay == null) {
            sessionsForDay = new ArrayList<>();
            scheduleByDay.put(day, sessionsForDay);
        }

        sessionsForDay.add(session);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        List<TrainingSession> sessionsForDay = scheduleByDay.get(dayOfWeek);

        if (sessionsForDay == null) {
            return new ArrayList<>();
        }

        sessionsForDay.sort(new Comparator<TrainingSession>() {
            @Override
            public int compare(TrainingSession o1, TrainingSession o2) {
                TimeOfDay t1 = o1.getTimeOfDay();
                TimeOfDay t2 = o2.getTimeOfDay();

                if (t1.getHours() < t2.getHours()) return -1;
                if (t1.getHours() > t2.getHours()) return 1;

                if (t1.getMinutes() < t2.getMinutes()) return -1;
                if (t1.getMinutes() > t2.getMinutes()) return 1;

                return 0;
            }
        });

        return sessionsForDay;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        List<TrainingSession> sessionsForDay = scheduleByDay.get(dayOfWeek);

        if (sessionsForDay == null) {
            return new ArrayList<>();
        }

        List<TrainingSession> result = new ArrayList<>();

        for (TrainingSession session : sessionsForDay) {
            TimeOfDay currentTime = session.getTimeOfDay();
            if (currentTime.getHours() == timeOfDay.getHours() &&
                    currentTime.getMinutes() == timeOfDay.getMinutes()) {
                result.add(session);
            }
        }

        return result;
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCountMap = new HashMap<>();

        for (List<TrainingSession> sessionsForDay : scheduleByDay.values()) {
            for (TrainingSession session : sessionsForDay) {
                Coach coach = session.getCoach();

                if (coachCountMap.containsKey(coach)) {
                    int currentCount = coachCountMap.get(coach);
                    coachCountMap.put(coach, currentCount + 1);
                } else {
                    coachCountMap.put(coach, 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCountMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings o1, CounterOfTrainings o2) {
                if (o1.getCount() < o2.getCount()) return 1;
                if (o1.getCount() > o2.getCount()) return -1;
                return 0;
            }
        });

        return result;
    }

    public static class CounterOfTrainings {
        private final Coach coach;
        private final int count;

        public CounterOfTrainings(Coach coach, int count) {
            this.coach = coach;
            this.count = count;
        }

        public Coach getCoach() {
            return coach;
        }

        public int getCount() {
            return count;
        }
    }
}