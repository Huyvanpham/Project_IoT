package com.example.demo.service;

import com.example.demo.model.Data;
import com.example.demo.dto.DataDto;
import com.example.demo.dto.SearchDataDto;
import com.example.demo.model.LedStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataService {
    List<DataDto> get7LastestResult(LedStatus ledStatus);
    List<DataDto> getAllResult();
    void getByCondition(SearchDataDto searchDataDto);
    void saveData(Data data);
}
