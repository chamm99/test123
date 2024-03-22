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
    }

        @Builder
        @Getter
        public static class timeTable {
            private List<lectDetail> timetable;
            private int count;

            public static timeTable from(List<lectDetail> timetable) {
                return timeTable.builder()
                        .timetable(timetable)
                        .count(timetable.size())
                        .build();
            }


        }


    }
