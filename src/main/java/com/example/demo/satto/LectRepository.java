package com.example.demo.satto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectRepository extends JpaRepository<Lect,String> {

    @Query("SELECT t.lectTimeRoom FROM Lect t")
    List<String> findAlltime();

    @Query("SELECT t.sbjNm FROM Lect t")
    List<String> findAllName();

    @Query("select l from Lect l where l.estDeptInfo = :major and l.crsShyr = :year")
    List<Lect> findLectByMajorAndYear(@Param("major") String major, @Param("year") int year);

    @Query("select l.sbjNm from Lect l where l.estDeptInfo = :major and l.crsShyr = :year")
    List<String> findLectNMByMajorAndYear(@Param("major") String major, @Param("year") int year);

    @Query("select l from Lect l where l.sbjNm not like '%교양과인성%' and l.sbjNm not like '%사회봉사%' and l.cyberYn = 'N' and l.cmpDivNm = :cmpdivnm   ")
    List<Lect> findLectByCmpDivNm(@Param("cmpdivnm") String cmpdivnm);

    @Query("select l from Lect l where l.sbjNm not like '%교양과인성%' and l.sbjNm not like '%사회봉사%' and l.cmpDivNm = :cmpdivnm   ")
    List<Lect> findLectExceptGyoinSabong(@Param("cmpdivnm") String cmpdivnm);

    Lect findLectBySbjDivcls(String sbjDivcls);

    @Query("select  l from Lect l where l.sbjNm = :name or l.sbjNm = :name2 or l.sbjNm = :name3")
    List<Lect> findLectByName(@Param("name") String name, @Param("name2") String name2, @Param("name3") String name3);

    @Query("select  l from Lect l where l.sbjNm = :name or l.sbjNm = :name2 or l.sbjNm = :name3 or l.sbjNm = :name4")
    List<Lect> findLectByName4(@Param("name") String name, @Param("name2") String name2, @Param("name3") String name3, @Param("name4") String name4);

    @Query("select l from Lect l where l.sbjNm = :name or l.sbjNm = :name2 or l.sbjNm = :name3 or l.sbjNm = :name4 or l.sbjNm = :name5")
    List<Lect> findLectByName5(@Param("name") String name, @Param("name2") String name2, @Param("name3") String name3, @Param("name4") String name4, @Param("name5") String name5);

}
