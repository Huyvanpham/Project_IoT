package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class SearchDataDto extends SearchDto<DataDto>
{
    private String temperatureValue;
    private String humidityValue;
    private String lightValue;
    private String doBuiValue;
}
