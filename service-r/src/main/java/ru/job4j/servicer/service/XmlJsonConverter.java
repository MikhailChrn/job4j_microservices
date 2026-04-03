package ru.job4j.servicer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

@Service
public class XmlJsonConverter {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final XmlMapper xmlMapper = new XmlMapper();

    public String xmlToJson(String xml) throws Exception {
        Object obj = xmlMapper.readValue(xml, Object.class);
        return jsonMapper.writeValueAsString(obj);
    }

    public String jsonToXml(String json) throws Exception {
        Object obj = jsonMapper.readValue(json, Object.class);
        return xmlMapper.writeValueAsString(obj);
    }
}
