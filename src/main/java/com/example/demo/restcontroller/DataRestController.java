package com.example.demo.restcontroller;

import com.example.demo.dto.SearchDataDto;
import com.example.demo.model.LedStatus;
import com.example.demo.service.DataService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class DataRestController {
    private final DataService dataService;
    private LedStatus ledStatus;

    @Autowired
    public DataRestController(DataService dataService,
                              @Qualifier("ledStatus") LedStatus ledStatus)
    {
        this.dataService = dataService;
        this.ledStatus = ledStatus;
    }

    @RequestMapping()
    public ResponseEntity<Object> get7LastestResult()
    {
        return ResponseEntity.ok(this.dataService.get7LastestResult(ledStatus));
    }

    @PostMapping("/get-by-condition")
    public ResponseEntity<Object> getByCondition(@RequestBody String searchDataJson)
    {
        SearchDataDto searchDataDto = (new Gson()).fromJson(searchDataJson, SearchDataDto.class);
        this.dataService.getByCondition(searchDataDto);
        if (!searchDataDto.isValid())
            return ResponseEntity.badRequest().body(searchDataDto.getMessages());
        return ResponseEntity.ok(searchDataDto);
    }
}
