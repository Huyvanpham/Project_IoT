package com.example.demo.service;

import com.example.demo.dto.SearchDto;
import com.example.demo.model.Action;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActionService {
    List<Action> getAllActions();
    void getByCondition(SearchDto searchDto);
    Action saveAction(JsonObject jsonObject);
}
