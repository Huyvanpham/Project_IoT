package com.example.demo.controller;

import com.example.demo.dto.SearchDto;
import com.example.demo.model.Action;
import com.example.demo.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/all-action")
public class ActionController {
    private final ActionService actionService;

    @Autowired
    public ActionController(ActionService actionService)
    {
        this.actionService = actionService;
    }

    @GetMapping()
    public String getACtionView(@RequestParam(name = "start-date", required = false) String startDate,
                                @RequestParam(name = "end-date", required = false) String endDate,
                                @RequestParam(name = "page", required = false) Integer pageIndex,
                                @RequestParam(name = "order-date", required = false) String ordDate,
                                Model model)
    {
        System.out.println(pageIndex);

        SearchDto searchDto = SearchDto.builder()
                             .startDate(startDate)
                             .endDate(endDate)
                             .pageIndex(pageIndex)
                             .orderDate(ordDate)
                             .build();

        this.actionService.getByCondition(searchDto);
        model.addAttribute("searchDto", searchDto);
        return "action";
    }
}
