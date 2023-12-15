package com.example.demo.repository;

import com.example.demo.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    @Query("Select a from Action a order by a.id desc")
    List<Action> getAllAction();

    @Query(value = "select count(*) from tbl_action where thiet_bi = 'Đèn 1' and trang_thai = 'bật'", nativeQuery = true)
    int countDen1Bat();

    @Query(value = "select count(*) from tbl_action where thiet_bi = 'Đèn 1' and trang_thai = 'tắt';", nativeQuery = true)
    int countDen1Tat();

    @Query(value = "select count(*) from tbl_action where thiet_bi = 'Đèn 2' and trang_thai = 'bật';", nativeQuery = true)
    int countDen2Bat();

    @Query(value = "select count(*) from tbl_action where thiet_bi = 'Đèn 2' and trang_thai = 'tắt';", nativeQuery = true)
    int countDen2Tat();
}
