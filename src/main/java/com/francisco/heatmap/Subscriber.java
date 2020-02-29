package com.francisco.heatmap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class Subscriber {
	
	private Map<String, Object> dataTemp = new HashMap<String, Object>();
	
	public Map<String, Object> getDataTemp() {
		return dataTemp;
	}

	@PostConstruct
	public void init() {
		Runnable run = () -> {
			mqttReader();
		};
		new Thread(run).start();
	}
	
	public void mqttReader() {

		try {
			String topic = "aula/temperatura";
			String broker = "tcp://192.168.0.101:1883";
			String clientId = "Java Client";
			MemoryPersistence persistence = new MemoryPersistence();

			MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			System.out.println("Connecting to broker: "+broker);
			sampleClient.connect(connOpts);
			System.out.println("Connected");
			
			ObjectMapper mapper = new ObjectMapper();
			
			sampleClient.subscribe(topic, new IMqttMessageListener() {
				
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					try {
						Map<String, Object> jsonMap = mapper.readValue(message.getPayload(), Map.class);
						String key = String.format("%s:%s", jsonMap.get("x"), jsonMap.get("y"));
						Map<String, Object> valueTemp = new HashMap<String, Object>();
						valueTemp.put("x", (int)jsonMap.get("x") * 50 + 40);
						valueTemp.put("y", (int)jsonMap.get("y") * 70 + 40);
						valueTemp.put("value", jsonMap.get("temp"));
						dataTemp.put(key, valueTemp);
						System.out.println(key);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
        


	}

}
