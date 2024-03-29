package com.example.demo.sattoTest;

import com.example.demo.satto.LectRepository;
import com.example.demo.satto.LectService;
import com.example.demo.satto.TimeTableResponseDTO;
import com.example.demo.satto.dto3;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceTest {
    @Mock
    LectRepository lectRepository;

    @Autowired
    LectService lectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        lectService = new LectService(lectRepository); // 직접 의존성 주입
    }

    @Test
    @DisplayName("강의 조합 생성 테스트")
    public void majorTest() {
        dto3 dto = new dto3();
        dto.setName1("컴퓨터네트워크");
        dto.setName2("소프트웨어공학");
        dto.setName3("운영체제");
        List<TimeTableResponseDTO.timeTable> majorTimeTable = lectService.createMajorTimeTableByName(dto);
        List<TimeTableResponseDTO.timeTable> timeTable = lectService.findBigGonggang(majorTimeTable);

    }

    @Test
    @DisplayName("하루에 한 시간 감지 테스트")
    public void filterTest() {
        dto3 dto = new dto3();
        dto.setName1("컴퓨터네트워크");
        dto.setName2("소프트웨어공학");
        dto.setName3("운영체제");
        List<TimeTableResponseDTO.timeTable> majorTimeTable = lectService.createMajorTimeTableByName(dto);
        List<TimeTableResponseDTO.timeTable> timeTable = lectService.findBigGonggang(majorTimeTable);
        List<TimeTableResponseDTO.timeTable> timeTables = lectService.filtering2(timeTable);
        for (TimeTableResponseDTO.timeTable table : timeTables) {
            System.out.println("table = " + table.getTotalTime());
        }
    }
}
