package com.example.demo.satto;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LectController {

    private final LectService lectService;


    @GetMapping
    public List<TimeTableResponseDTO.lectDetail> getLectByMajorAndYear(@RequestParam String major, @RequestParam int year){

        List<TimeTableResponseDTO.lectDetail> result = lectService.getLectByMajorAndYear(major, year);
        return result;

    }

    @GetMapping("/majorTimeTable")
    public List<List<TimeTableResponseDTO.lectDetail>> getMajorTimeTable(@RequestBody TimeTableCreateDTO createDTO){

        return lectService.createMajorTimeTable(createDTO);

    }

    @GetMapping("/timeTable")
    public List<List<TimeTableResponseDTO.lectDetail>> getTimeTable(@RequestBody TimeTableCreateDTO createDTO){

        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTable(createDTO);
        return lectService.createTimeTable(createDTO, majorTimaTable);

    }
    @GetMapping("/test")
    public List<List<TimeTableResponseDTO.lectDetail>> test(@RequestBody dto3 dto){

        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTableName(dto.getName1(), dto.getName2(), dto.getName3());
        return lectService.createTimeTable(majorTimaTable);

    }
    @GetMapping("/recommendedLibLects/timeTable")
    public List<List<TimeTableResponseDTO.lectDetail>> recommendLibLects(@RequestBody TimeTableCreateDTO createDTO,
                                                                         @RequestParam int libCount){
        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTable(createDTO);
        return lectService.recommendLibLectures(majorTimaTable, libCount);
    }

    @GetMapping("/recommendedLibLects/test")
    public List<List<TimeTableResponseDTO.lectDetail>> recommendLibLects(@RequestBody dto3 dto3,
                                                                         @RequestParam int libCount){
        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTableName(dto3.getName1(), dto3.getName2(), dto3.getName3());
        return lectService.recommendLibLectures(majorTimaTable, libCount);
    }
    @GetMapping("/recommendedLibLects/test4")
    public List<List<TimeTableResponseDTO.lectDetail>> recommendLibLects4(@RequestBody dto4 dto4,
                                                                         @RequestParam int libCount){
        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTableName4(dto4.getName1(), dto4.getName2(), dto4.getName3(), dto4.getName4());
        return lectService.recommendLibLectures(majorTimaTable, libCount);
    }

    @GetMapping("/recommendedLibLects/test5")
    public List<List<TimeTableResponseDTO.lectDetail>> recommendLibLects5(@RequestBody dto5 dto5,
                                                                         @RequestParam int libCount){
        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTableName5(dto5.getName1(), dto5.getName2(), dto5.getName3(), dto5.getName4(), dto5.getName5());
        return lectService.recommendLibLectures(majorTimaTable, libCount);
    }

    @GetMapping("/findBigGG")
    public List<TimeTableResponseDTO.timeTable> searchBigGG(@RequestBody dto3 dto5){
        List<TimeTableResponseDTO.timeTable> majorTimaTable = lectService.createMajorTimeTableByName(dto5);
        List<TimeTableResponseDTO.timeTable> filtered = lectService.findBigGonggang(majorTimaTable);
        return lectService.filterOneLect(lectService.findWholeGG(filtered));
    }
}
