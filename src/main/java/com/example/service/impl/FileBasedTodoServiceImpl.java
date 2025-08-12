package com.example.service.impl;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.model.Todo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Profile(value = "file")
public class FileBasedTodoServiceImpl extends AbstractTodoService {

    Logger logger = LoggerFactory.getLogger(FileBasedTodoServiceImpl.class);
    private final ObjectMapper objectMapper;
    private File dataFile = new File("todos.json");

    public FileBasedTodoServiceImpl() {
        super();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onDataChanged() {
        saveToFile();
    }

    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, todos);
            System.out.println("[Todo] Saved to: " + dataFile.getAbsolutePath());
        } catch (Exception e) {
            logger.error("Failed to save todos: ", e);
        }
    }

    @Override
    public Map<Long, Todo> loadTodos() {
        try {
            if (dataFile.exists() && dataFile.length() > 0) {
                todos = objectMapper.readValue(dataFile, new TypeReference<Map<Long, Todo>>() {
                });
                idCounter.set(todos.keySet().stream().max(Long::compareTo).orElse(0L) + 1);
                return todos;
            }
        } catch (Exception e) {
            logger.error("Failed to load todos: ", e);
        }
        return new ConcurrentHashMap<>();
    }
}
