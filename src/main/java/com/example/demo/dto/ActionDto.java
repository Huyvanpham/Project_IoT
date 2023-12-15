package com.example.demo.dto;

import com.example.demo.model.Action;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActionDto {
    public Integer id;
    private String time;
    private String thietBi;
    private String trangThai;

    public ActionDto(Action action){
        if(action != null){
            this.id = action.getId();
            this.time = action.getTime().toString().split("\\.")[0];
            this.thietBi = action.getThietBi();
            this.trangThai = action.getTrangThai();
        }
    }
}
