package com.example.demo.satto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LectService {

    private final LectRepository lectRepository;

    static int majorCount;
    static int count;


    public List<TimeTableResponseDTO.lectDetail> getLectByMajorAndYear(String major, int year) {

        List<Lect> lects = lectRepository.findLectByMajorAndYear(major, year);
        return TimeTableResponseDTO.lectDetail.from(lects);
    }


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

        List<Lect> majorLectList = lectRepository.findLectByName(name1,name2,name3);
        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);

        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, 4);

        System.out.println("전공 강의 조합 개수 : " + majorCount);

        return timeTable;
    }
    public List<List<TimeTableResponseDTO.lectDetail>> createMajorTimeTableName4(String name1, String name2, String name3, String name4) {

        List<Lect> majorLectList = lectRepository.findLectByName4(name1,name2,name3,name4);
        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);

        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, 4);

        System.out.println("전공 강의 조합 개수 : " + majorCount);

        return timeTable;
    }
    public List<List<TimeTableResponseDTO.lectDetail>> createMajorTimeTableName5(String name1, String name2, String name3, String name4, String name5) {

        List<Lect> majorLectList = lectRepository.findLectByName5(name1,name2,name3,name4,name5);
        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);

        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, 5);

        System.out.println("전공 강의 조합 개수 : " + majorCount);

        return timeTable;
    }

    public List<List<TimeTableResponseDTO.lectDetail>> createTimeTable(TimeTableCreateDTO createDTO,
                                                                       List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable){

        List<Lect> entireLect = lectRepository.findLectByCmpDivNm("교선");
        List<TimeTableResponseDTO.lectDetail> entireLectDetailList = TimeTableResponseDTO.lectDetail.from(entireLect);

        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();


        for (List<TimeTableResponseDTO.lectDetail> lectDetails : majorTimeTable) {
            generateCombinations1(entireLectDetailList, 0, lectDetails, timeTable, 6);

        }
        System.out.println("교선 강의 개수(교양과인성, 사회봉사 제외) : " + entireLect.size());
        System.out.println("전공 강의 조합 개수 : " + majorCount);
        System.out.println("전체 강의 조합 개수 : " + count);


        return timeTable;
    }
    public List<List<TimeTableResponseDTO.lectDetail>> createTimeTable(
                                                                       List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable){

        List<Lect> entireLect = lectRepository.findLectByCmpDivNm("교선");
        List<TimeTableResponseDTO.lectDetail> entireLectDetailList = TimeTableResponseDTO.lectDetail.from(entireLect);

        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        for (List<TimeTableResponseDTO.lectDetail> lectDetails : majorTimeTable) {
            generateCombinations1(entireLectDetailList, 0, lectDetails, timeTable, 6);

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
    public List<List<TimeTableResponseDTO.lectDetail>> recommendLibLectures(List<List<TimeTableResponseDTO.lectDetail>> majorTimeTable, int libCount) {
        List<Lect> entireLect = lectRepository.findLectExceptGyoinSabong("교선");
        List<TimeTableResponseDTO.lectDetail> entireLectDetailList = TimeTableResponseDTO.lectDetail.from(entireLect);

        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        // 추천 교양 목록 생성
        Collections.shuffle(entireLectDetailList); // 교양 목록을 랜덤하게 섞음

        // 제한된 개수만큼 교양 목록을 선택
        List<TimeTableResponseDTO.lectDetail> recommendedLectures = entireLectDetailList.subList(0, Math.min(libCount, entireLectDetailList.size()));
        for (List<TimeTableResponseDTO.lectDetail> lectDetails : majorTimeTable) {
            generateCombinations1(recommendedLectures, 0, lectDetails, timeTable, 6);
        }
        System.out.println("교선 강의 개수(교양과인성, 사회봉사 제외) : " + recommendedLectures.size());
        System.out.println("전공 강의 조합 개수 : " + majorCount);
        System.out.println("전체 강의 조합 개수 : " + count);

        return timeTable;
    }
    public List<TimeTableResponseDTO.timeTable> optimizationTimeTable(List<TimeTableResponseDTO.timeTable> timeTables){

        List<Integer> toRemoveIndexes = new ArrayList<>();
        for(int i = 0; i < timeTables.size(); i++){

            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).getTotalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet이 없으면 생성
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                // 시간 추가
                scheduleMap.get(day).add(time);
            }

            boolean hasThreeHourGap = false;
            // 각 요일별로 시간대 확인
            for (TreeSet<Integer> times : scheduleMap.values()) {
                // 이전 시간대 저장
                Integer prevTime = null;
                for (Integer currentTime : times) {
                    if (prevTime != null && (currentTime - prevTime > 3)) {
                        // 3교시 이상 비어있으면 다음 시간표로 넘어감
                        hasThreeHourGap = true;
                        break;
                    }
                    prevTime = currentTime;
                }
                if (!hasThreeHourGap) {
                    break; // 현재 시간표에서 3교시 이상 비어있지 않는 부분을 찾았으면 더 이상 확인하지 않음
                }
            }
            if (hasThreeHourGap) {
                // 3교시 이상 비어있는 경우 리스트에서 제거
                toRemoveIndexes.add(i);
            }
        }
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }
    public List<TimeTableResponseDTO.timeTable> createFilteredTimeTable(List<TimeTableResponseDTO.timeTable> majorTimeTable){

        List<Lect> entireLect = lectRepository.findLectByCmpDivNm("교선");
        List<TimeTableResponseDTO.lectDetail> entireLectDetailList = TimeTableResponseDTO.lectDetail.from(entireLect);

        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        for(int i = 0; i < majorTimeTable.size(); i++){
            generateCombinations1(entireLectDetailList, 0, majorTimeTable.get(i).getTimetable(), timeTable, 4);
        }
        List<TimeTableResponseDTO.timeTable> timeTableList = TimeTableResponseDTO.timeTable.of(timeTable);
        filterMorningLect(findWholeGG(filterOneLect(optimizationTimeTable(timeTableList))));
        System.out.println("교선 강의 개수(교양과인성, 사회봉사, 성공학특강, 이러닝 제외) : " + entireLect.size());
        System.out.println("전공 강의 조합 개수 : " + majorCount);
        System.out.println("전체 강의 조합 개수 : " + timeTableList.size());

        return timeTableList;
    }
    public List<TimeTableResponseDTO.timeTable> createMajorTimeTableByName(dto3 dto) {

        //3학년 1학기 3전공 선택 기준
        List<Lect> majorLectList = lectRepository.findLectByName(dto.getName1(), dto.getName2(), dto.getName3());
        List<TimeTableResponseDTO.lectDetail> majorLectDetailList = TimeTableResponseDTO.lectDetail.from(majorLectList);

        List<TimeTableResponseDTO.lectDetail> lectDetailList = new ArrayList<>();
        List<List<TimeTableResponseDTO.lectDetail>> timeTable = new ArrayList<>();

        generateCombinations(majorLectDetailList, 0, lectDetailList, timeTable, 3);

        List<TimeTableResponseDTO.timeTable> timeTableList = TimeTableResponseDTO.timeTable.of(timeTable);
        System.out.println("전공 강의 조합 개수 : " + majorCount);

        return timeTableList;
    }

    //한 강의 있는 날 찾기
    public List<TimeTableResponseDTO.timeTable> filterOneLect(List<TimeTableResponseDTO.timeTable> timeTables) {
        // 제거하기 위한 인덱스 리스트
        List<Integer> toRemoveIndexes = new ArrayList<>();

        for (int i = 0; i < timeTables.size(); i++) {
            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).getTotalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet 생성 또는 시간 추가
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                scheduleMap.get(day).add(time);
            }

            // 하루에 강의가 1시간만 있는 경우를 찾기
            boolean hasOneHourClassOnly = scheduleMap.values().stream()
                    .anyMatch(times -> times.size() == 1); // 하루에 시간이 정확히 1개만 있는 경우 확인

            if (hasOneHourClassOnly) {
                // 하루에 강의가 1시간만 있는 경우 인덱스 저장
                toRemoveIndexes.add(i);
            }
        }

        // 뒤에서부터 제거하여 인덱스 변화를 방지
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }
    //1교시 포함 시간표 제거
    public List<TimeTableResponseDTO.timeTable> filterMorningLect(List<TimeTableResponseDTO.timeTable> timeTables) {
        List<Integer> toRemoveIndexes = new ArrayList<>();
        for (int i = 0; i < timeTables.size(); i++) {
            if(timeTables.get(i).getTotalTime().contains("1")) {
                toRemoveIndexes.add(i);
            }
        }

        // 뒤에서부터 제거하여 인덱스 변화를 방지
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }
    //통 공강 찾기
    public List<TimeTableResponseDTO.timeTable> findWholeGG(List<TimeTableResponseDTO.timeTable> timeTables) {
        // 제거하기 위한 인덱스 리스트
        List<Integer> toRemoveIndexes = new ArrayList<>();

        for (int i = 0; i < timeTables.size(); i++) {
            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).getTotalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet 생성 또는 시간 추가
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                scheduleMap.get(day).add(time);
            }

            // 모든 요일에 강의가 있는지 확인
            boolean hasNoEmptyDay = scheduleMap.keySet().size() == 5; // 월화수목금을 기준으로 5일 모두 강의가 있는지 확인

            if (hasNoEmptyDay) {
                // 모든 요일에 강의가 있어 공강이 없는 경우 인덱스 저장
                toRemoveIndexes.add(i);
            }
        }

        // 뒤에서부터 제거하여 인덱스 변화를 방지
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }
    public List<TimeTableResponseDTO.timeTable> find7HourKeepGoing(List<TimeTableResponseDTO.timeTable> timeTables){

        List<Integer> toRemoveIndexes = new ArrayList<>();
        for(int i = 0; i < timeTables.size(); i++){

            Map<Character, TreeSet<Integer>> scheduleMap = new HashMap<>();
            String[] parts = timeTables.get(i).getTotalTime().split(" ");
            for (String part : parts) {
                char day = part.charAt(0); // 요일
                int time = Integer.parseInt(part.substring(1)); // 시간

                // 해당 요일에 대한 TreeSet이 없으면 생성
                scheduleMap.putIfAbsent(day, new TreeSet<>());
                // 시간 추가
                scheduleMap.get(day).add(time);
            }

            boolean hasSevenHourContinuous = false;
            // 각 요일별로 시간대 확인
            for (TreeSet<Integer> times : scheduleMap.values()) {
                // 연속 시간 확인
                Integer firstTime = null;
                Integer lastTime = null;
                for (Integer currentTime : times) {
                    if (firstTime == null) {
                        firstTime = currentTime;
                        lastTime = currentTime;
                    } else if (currentTime - lastTime == 1) {
                        lastTime = currentTime;
                        // 연속되는 시간이 7시간 이상인지 확인
                        if (lastTime - firstTime >= 6) {
                            hasSevenHourContinuous = true;
                            break;
                        }
                    } else {
                        firstTime = currentTime;
                        lastTime = currentTime;
                    }
                }
                if (hasSevenHourContinuous) {
                    break; // 7시간 이상 연속되는 경우 찾았으면 더 이상 확인하지 않음
                }
            }
            if (hasSevenHourContinuous) {
                // 7시간 이상 연속된 경우 리스트에서 제거
                toRemoveIndexes.add(i);
            }
        }
        Collections.reverse(toRemoveIndexes);
        for (int index : toRemoveIndexes) {
            timeTables.remove(index);
        }

        return timeTables;
    }
}
