package com.example.demo.satto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LectService {

    private final LectRepository lectRepository;

    static int majorCount;
    static int count;

    public List<String> getAllTime() {
        return lectRepository.findAlltime();
    }

    public List<String> getAllName() {
        return lectRepository.findAllName();
    }

    public List<TimeTableResponseDTO.lectDetail> getLectByMajorAndYear(String major, int year) {

        List<Lect> lects = lectRepository.findLectByMajorAndYear(major, year);
        List<TimeTableResponseDTO.lectDetail> result = TimeTableResponseDTO.lectDetail.from(lects);
        return result;
    }

//    public TimeTableResponseDTO createMajorTT(TimeTableCreateDTO createDTO){
//        List<Lect> majorLects = lectRepository.findLectByMajorAndYear(createDTO.getMajorName(), createDTO.getYear());
//        List<String> lectNumber = new ArrayList<>();
//        List<String> lectTime = new ArrayList<>();
//
//    }

    public static boolean isNotTimeConflict(List<TimeTableResponseDTO.lectDetail> lectList,
                                         TimeTableResponseDTO.lectDetail lect) {

        String[] lectTimeSegments = lect.getTime().split(" "); // 한 번만 분리
        for (TimeTableResponseDTO.lectDetail existingLect : lectList) {
            for (String segment : lectTimeSegments) {
                if (existingLect.getTime().contains(segment)) {
                    return false; // 충돌 발견 시 즉시 반환
                }
            }
        }
        return true; // 충돌 없음

    }

    public static boolean isNotLectNumberConfilct(List<TimeTableResponseDTO.lectDetail> lectList,
                                               TimeTableResponseDTO.lectDetail lect) {

        for (TimeTableResponseDTO.lectDetail existingLect : lectList) {
            if (existingLect.getSbjNo().equals(lect.getSbjNo())) {
                return false; // 같은 강의 번호 발견 시 즉시 반환
            }
        }
        return true; // 충돌 없음
    }

    public static boolean isNotConflict(List<TimeTableResponseDTO.lectDetail> currentLect, TimeTableResponseDTO.lectDetail lect) {
        return currentLect.isEmpty() || (isNotTimeConflict(currentLect, lect) && isNotLectNumberConfilct(currentLect, lect));
    }

    public List<List<TimeTableResponseDTO.lectDetail>> createMajorTimeTable(TimeTableCreateDTO createDTO) {

        //3학년 1학기 4전공 선택 기준
        List<Lect> majorLectList = lectRepository.findLectByMajorAndYear(createDTO.getMajorName(), createDTO.getYear());
        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);

        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, createDTO.getMajorCount());

        System.out.println("전공 강의 조합 개수 : " + majorCount);

        return timeTable;
    }
    public List<List<TimeTableResponseDTO.lectDetail>> createMajorTimeTableName(String name1, String name2, String name3) {

        //3학년 1학기 4전공 선택 기준
//        List<Lect> majorLectList = lectRepository.findLectByMajorAndYear(createDTO.getMajorName(), createDTO.getYear());
        List<Lect> majorLectList = lectRepository.findLectByName(name1,name2,name3);
        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);

        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, 3);

        System.out.println("전공 강의 조합 개수 : " + majorCount);

        return timeTable;
    }

    public List<List<TimeTableResponseDTO.lectDetail>> createTimeTable(TimeTableCreateDTO createDTO,
                                                                       List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable){

//        List<Lect> majorLectList = lectRepository.findLectByMajorAndYear(createDTO.getMajorName(), createDTO.getYear());
        List<Lect> entireLect = lectRepository.findLectByCmpDivNm("교선");
//        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);
        List<TimeTableResponseDTO.lectDetail> entireLectDetailList = TimeTableResponseDTO.lectDetail.from(entireLect);

//        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
//        List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

//        generateCombinations(majorLectDetailList, 0, lectDetailList, majorTimeTable, createDTO.getMajorCount());

        for(int i = 0; i < majorTimeTable.size(); i++){
            generateCombinations1(entireLectDetailList, 0, majorTimeTable.get(i), timeTable, 6);

        }
        System.out.println("교선 강의 개수(교양과인성, 사회봉사 제외) : " + entireLect.size());
        System.out.println("전공 강의 조합 개수 : " + majorCount);
        System.out.println("전체 강의 조합 개수 : " + count);


        return timeTable;
    }
    public List<List<TimeTableResponseDTO.lectDetail>> createTimeTable(
                                                                       List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable){

//        List<Lect> majorLectList = lectRepository.findLectByMajorAndYear(createDTO.getMajorName(), createDTO.getYear());
        List<Lect> entireLect = lectRepository.findLectByCmpDivNm("교선");
//        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);
        List<TimeTableResponseDTO.lectDetail> entireLectDetailList = TimeTableResponseDTO.lectDetail.from(entireLect);

//        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
//        List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

//        generateCombinations(majorLectDetailList, 0, lectDetailList, majorTimeTable, createDTO.getMajorCount());

        for(int i = 0; i < majorTimeTable.size(); i++){
            generateCombinations1(entireLectDetailList, 0, majorTimeTable.get(i), timeTable, 6);

        }
        System.out.println("교선 강의 개수(교양과인성, 사회봉사 제외) : " + entireLect.size());
        System.out.println("전공 강의 조합 개수 : " + majorCount);
        System.out.println("전체 강의 조합 개수 : " + count);


        return timeTable;
    }

    //시간표 조합 알고리즘
    private static void generateCombinations(List<TimeTableResponseDTO.lectDetail> majorLectures,
                                             int start,
                                             List<TimeTableResponseDTO.lectDetail> current,
                                             List<List<TimeTableResponseDTO.lectDetail>> result,
                                             int targetSize
                                             ) {
        if (current.size() == targetSize) {
            result.add((new ArrayList<>(current)));
            majorCount++;
            return;
        }

        for (int i = start; i < majorLectures.size(); i++) {
            TimeTableResponseDTO.lectDetail nextMajorLecture = majorLectures.get(i);
            if (isNotConflict(current, nextMajorLecture)) {
                current.add(nextMajorLecture);
                generateCombinations(majorLectures, i + 1, current, result, targetSize);
                current.remove(current.size() - 1);
            }
        }
    }
    private static void generateCombinations1(List<TimeTableResponseDTO.lectDetail> majorLectures,
                                             int start,
                                             List<TimeTableResponseDTO.lectDetail> current,
                                             List<List<TimeTableResponseDTO.lectDetail>> result,
                                             int targetSize
                                             ) {
        if (current.size() == targetSize) {
            result.add((new ArrayList<>(current)));
            count++;
            return;
        }

        for (int i = start; i < majorLectures.size(); i++) {
            TimeTableResponseDTO.lectDetail nextMajorLecture = majorLectures.get(i);
            if (isNotConflict(current, nextMajorLecture)) {
                current.add(nextMajorLecture);
                generateCombinations1(majorLectures, i + 1, current, result, targetSize);
                current.remove(current.size() - 1);
            }
        }
    }
//    private static void generateCombinations(List<TimeTableResponseDTO.lectDetail> majorLectures,
//                                             int start,
//                                             List<TimeTableResponseDTO.lectDetail> current,
//                                             List<List<TimeTableResponseDTO.lectDetail>> result,
//                                             int targetSize
//                                             ) {
//        if (current.size() == targetSize) {
//            result.add((new ArrayList<>(current)));
//            majorCount++;
//            return;
//        }
//
//        for (int i = start; i < majorLectures.size(); i++) {
//            TimeTableResponseDTO.lectDetail nextLecture = majorLectures.get(i);
//            if (isConflict(current, nextLecture)) {
//                current.add(nextLecture);
//                generateCombinations(majorLectures, i + 1, current, result, targetSize);
//                current.remove(current.size() - 1);
//            }
//        }
//    }

    //DTO 아닌 ENTITY 기준 생성 코드
//    private static void generateCombinations(List<Lect> lectures,
//                                             int start,
//                                             List<Lect> current,
//                                             List<List<Lect>> result,
//                                             int targetSize
//                                             ) {
//        if (current.size() == targetSize) {
//            result.add((new ArrayList<>(current)));
//            count++;
//            return;
//        }
//
//        for (int i = start; i < lectures.size(); i++) {
//            Lect nextLecture = lectures.get(i);
//            if (!isConflict(current, nextLecture)) {
//                current.add(nextLecture);
//                generateCombinations(lectures, i + 1, current, result, targetSize);
//                current.remove(current.size() - 1);
//            }
//        }
//    }

}
