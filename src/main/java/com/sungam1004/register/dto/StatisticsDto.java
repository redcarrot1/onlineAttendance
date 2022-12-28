package com.sungam1004.register.dto;

import com.sungam1004.register.domain.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StatisticsDto {

    public static final List<String> date =
            List.of("2023-01-01",
                    "2023-01-08",
                    "2023-01-15",
                    "2023-01-22",
                    "2023-01-29",
                    "2023-02-05",
                    "2023-02-12",
                    "2023-02-19",
                    "2023-02-26",
                    "2023-03-05",
                    "2023-03-12",
                    "2023-03-19",
                    "2023-03-26",
                    "2023-04-02",
                    "2023-04-09",
                    "2023-04-16",
                    "2023-04-23",
                    "2023-04-30",
                    "2023-05-07",
                    "2023-05-14",
                    "2023-05-21",
                    "2023-05-28",
                    "2023-06-04",
                    "2023-06-11",
                    "2023-06-18",
                    "2023-06-25",
                    "2023-07-02",
                    "2023-07-09",
                    "2023-07-16",
                    "2023-07-23",
                    "2023-07-30",
                    "2023-08-06",
                    "2023-08-13",
                    "2023-08-20",
                    "2023-08-27",
                    "2023-09-03",
                    "2023-09-10",
                    "2023-09-17",
                    "2023-09-24",
                    "2023-10-01",
                    "2023-10-08",
                    "2023-10-15",
                    "2023-10-22",
                    "2023-10-29",
                    "2023-11-05",
                    "2023-11-12",
                    "2023-11-19",
                    "2023-11-26",
                    "2023-12-03",
                    "2023-12-10",
                    "2023-12-17",
                    "2023-12-24",
                    "2023-12-31");

    private List<String> names = new ArrayList<>();
    private Map<String, Long> mapNameIndex = new HashMap<>();
    private List<List<LocalDateTime>> attendance = new ArrayList<>();

    public void setName(List<User> users) {
        List<LocalDateTime> dd = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) dd.add(null);

        for (User user : users) {
            names.add(user.getName());
            mapNameIndex.put(user.getName(), user.getId());
            attendance.add(dd);
        }
    }

    public void addAttendance(String name, LocalDateTime dateTime) {
        int index = names.size() == 1 ? 0 : mapNameIndex.get(name).intValue();
        String attendanceDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(dateTime);

        for (int i = 0; i < date.size(); i++) {
            if (Objects.equals(date.get(i), attendanceDate)) {
                attendance.get(index).set(i, dateTime);
                break;
            }
        }
    }

    public List<List<LocalDateTime>> getAttendance() {
        return attendance;
    }

    public List<String> getNames() {
        return names;
    }

}
