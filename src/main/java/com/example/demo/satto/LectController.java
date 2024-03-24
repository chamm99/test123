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
    public List<List<TimeTableResponseDTO.lectDetail>> test(@RequestBody dto dto){

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
    public List<List<TimeTableResponseDTO.lectDetail>> recommendLibLects(@RequestBody dto dto,
                                                                         @RequestParam int libCount){
        List<List<TimeTableResponseDTO.lectDetail>> majorTimaTable = lectService.createMajorTimeTableName(dto.getName1(), dto.getName2(), dto.getName3());
        return lectService.recommendLibLectures(majorTimaTable, libCount);
    }
}
