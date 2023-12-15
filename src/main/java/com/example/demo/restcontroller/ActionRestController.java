package com.example.demo.restcontroller;

import com.example.demo.dto.SearchDto;
import com.example.demo.model.Action;
import com.example.demo.mqtt.MqttGateway;
import com.example.demo.service.ActionService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/action/")
public class ActionRestController {
    @Autowired
    private ActionService actionService;
    private final MqttGateway mqttGateway;;

    @Autowired
    public ActionRestController(MqttGateway mqttGateway) {
        this.mqttGateway = mqttGateway;
    }

    @PostMapping("/led")
    public ResponseEntity<Object> publish(@RequestBody String ledControllMessage)
    {
        System.out.println("Điều khiển led: " + ledControllMessage);
        JsonObject jsonObject = new Gson().fromJson(ledControllMessage, JsonObject.class);
        this.mqttGateway.sendToMqtt(ledControllMessage, "ledControlInput");
        this.actionService.saveAction(jsonObject);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/get-by-condition")
    public ResponseEntity<Object> getByCondition(@RequestBody String searchJson)
    {
        SearchDto<Action> searchDto = (new Gson()).fromJson(searchJson, SearchDto.class);
        this.actionService.getByCondition(searchDto);
        if(searchDto.isValid())
            return ResponseEntity.ok().body(searchDto);
        return ResponseEntity.badRequest().body(searchDto.getMessages());
    }
}
