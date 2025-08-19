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

    private static final Logger logger = LoggerFactory.getLogger(FileBasedTodoServiceImpl.class);
    private final ObjectMapper objectMapper;
    private final File dataFile = new File(System.getProperty("todos.file.path", "todos.json"));

    public FileBasedTodoServiceImpl() {
        super();
        this.objectMapper = new ObjectMapper();
        setInitialData(loadTodos());
    }

    @Override
    public void onDataChanged() {
        saveToFile();
    }

    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, todos);
            logger.info("Todos saved to file successfully");
        } catch (Exception e) {
            logger.error("Failed to save todos: ", e);
        }
    }

    @Override
    public Map<Long, Todo> loadTodos() {
        try {
            if (dataFile.exists() && dataFile.length() > 0) {
                Map<Long, Todo> loadedTodos = objectMapper.readValue(dataFile, new TypeReference<Map<Long, Todo>>() {
                });
                idCounter.set(loadedTodos.keySet().stream().max(Long::compareTo).orElse(0L) + 1);
                return new ConcurrentHashMap<>(loadedTodos);
            }
        } catch (Exception e) {
            logger.error("Failed to load todos: ", e);
        }
        return new ConcurrentHashMap<>();
    }
}
