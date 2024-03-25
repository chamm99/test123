package com.example.demo.satto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class TimeTableResponseDTO {

    @Builder
    @Getter
    public static class lectDetail {

        private String sbjDivcls;
        private String sbjNo;
        private String name;
        private String time;


        public static lectDetail from(Lect lect) {
            return lectDetail.builder()
                    .sbjDivcls(lect.getSbjDivcls())
                    .sbjNo(lect.getSbjNo())
                    .time(lect.getLectTimeRoom())
                    .name(lect.getSbjNm())
                    .build();
        }

        public static List<lectDetail> from(List<Lect> lectList) {
            return lectList.stream().map(lect -> lectDetail.from(lect)).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "lectDetail{" +
                    "sbjDivcls='" + sbjDivcls + '\'' +
                    ", sbjNo='" + sbjNo + '\'' +
                    ", name='" + name + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }

    @Builder
    @Getter
    public static class timeTable {
        private List<lectDetail> timetable;
        private String totalTime;

        public static String calculateTotalTime(List<lectDetail> timetable) {
            String tt = "";
            for (lectDetail lecture : timetable) {
                tt += lecture.getTime();
            }
            return tt;
        }


        public static timeTable from(List<lectDetail> lectList) {
            return timeTable.builder()
                    .timetable(lectList)
                    .totalTime(calculateTotalTime(lectList))
                    .build();
        }
        public static List<timeTable> of(List<List<lectDetail>> lectList) {
            return lectList.stream().map(lectDetail -> timeTable.from(lectDetail)).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "timeTable{" +
                    "timetable=" + timetable +
                    ", totalTime='" + totalTime + '\'' +
                    '}';
        }
    }

    }
