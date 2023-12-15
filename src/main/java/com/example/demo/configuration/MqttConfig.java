package com.example.demo.configuration;

import com.example.demo.model.Data;
import com.example.demo.model.LedStatus;
import com.example.demo.service.DataService;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.sql.Timestamp;
import java.util.Date;

@Configuration
public class MqttConfig
{
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory()    
    {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://192.168.71.104:1883"});
        options.setUserName("huyp");
        String password = "2002";
        options.setPassword(password.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel()
    {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound()
    {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn", mqttPahoClientFactory(), "#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler()
    {
        return new MessageHandler()
        {
            @Autowired
            @Qualifier("ledStatus")
            LedStatus ledStatus;
            @Autowired DataService dataService;
            @Override
            public void handleMessage(Message<?> message) throws MessagingException
            {
                Gson gson = new Gson();
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                if(topic.equals("dataSensorOutput"))
                {
                    System.out.println("nhận dữ liệu thành công");
                    Data data = gson.fromJson(message.getPayload().toString(), Data.class);
                    data.setTime(new Timestamp(new Date().getTime()));
                    this.dataService.saveData(data);
                }
                if(topic.equals("ledStatus"))
                {
                    LedStatus newLedstatus = gson.fromJson(message.getPayload().toString(), LedStatus.class);
                    ledStatus.setLed1(newLedstatus.getLed1());
                    ledStatus.setLed2(newLedstatus.getLed2());
                    ledStatus.setLed3(newLedstatus.getLed3());
                }
                System.out.println(message.getPayload());
            }
        };
    }

    @Bean
    public MessageChannel mqttOutBoundChannel()
    {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MessageHandler mqttOutBound()
    {
        System.out.println(123);
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("outTopic");
        return messageHandler;
    }

    @Bean(name = "ledStatus")
    public LedStatus getLedstatus()
    {
        return new LedStatus();
    }
}