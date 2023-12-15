package com.example.demo.dto;

import com.example.demo.model.Data;
import com.example.demo.model.LedStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDto {
    private Long id;
    private Float temperature;
    private Float humidity;
    private Float light;
    private String time;
    private Float doBui;
    private LedStatus ledStatus;
    private int[] countThietBiBatTat;
    public DataDto(Data data)
    {
        if(!ObjectUtils.isEmpty(data))
        {
            this.id = data.getId();
            this.temperature = data.getTemperature();
            this.humidity = data.getHumidity();
            this.light = data.getLight();
            this.doBui = data.getDoBui();
            this.time = data.getTime().toString();
        }
    }
}
