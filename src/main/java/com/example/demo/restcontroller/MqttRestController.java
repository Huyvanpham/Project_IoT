package com.example.demo.restcontroller;

import com.example.demo.mqtt.MqttGateway;
import com.example.demo.service.ActionService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mqtt/api")
public class MqttRestController
{
    @Autowired
    private ActionService actionService;
    private final MqttGateway mqttGateway;;

    @Autowired
    public MqttRestController(MqttGateway mqttGateway)
    {
        this.mqttGateway = mqttGateway;
    }

    @PostMapping("/led")
    public ResponseEntity<Object> publish(@RequestBody String ledControllMessage)
    {
        JsonObject jsonObject = new Gson().fromJson(ledControllMessage, JsonObject.class);
        //lưu sự kiện bấm nút này
        this.actionService.saveAction(jsonObject);
        this.mqttGateway.sendToMqtt(ledControllMessage, "ledControlInput");
        return ResponseEntity.ok("ok");
    }
}
