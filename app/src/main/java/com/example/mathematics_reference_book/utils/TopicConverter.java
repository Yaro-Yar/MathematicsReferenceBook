package com.example.mathematics_reference_book.utils;

import com.example.mathematics_reference_book.data.TopicEntity;
import com.example.mathematics_reference_book.models.Topic;
import java.util.ArrayList;
import java.util.List;

public class TopicConverter {

    // Преобразование Topic → TopicEntity (для сохранения в БД)
    public static TopicEntity toEntity(Topic topic) {
        return new TopicEntity(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getFormula(),
                topic.getTheory(),
                topic.getCategory(),
                topic.isFavorite(),
                topic.getDifficultyLevel(),
                topic.getUserNotes()
        );
    }

    // Преобразование TopicEntity → Topic (для работы в UI)
    public static Topic fromEntity(TopicEntity entity) {
        return new Topic.Builder(entity.getId(), entity.getTitle())
                .description(entity.getDescription())
                .formula(entity.getFormula())
                .theory(entity.getTheory())
                .category(entity.getCategory())
                .difficultyLevel(entity.getDifficultyLevel())
                .isFavorite(entity.isFavorite())
                .userNotes(entity.getUserNotes())
                .build();
    }

    // Преобразование списка TopicEntity → List<Topic>

    // Преобразование списка Topic → List<TopicEntity>
    public static List<TopicEntity> toEntityList(List<Topic> topics) {
        List<TopicEntity> entities = new ArrayList<>();
        for (Topic topic : topics) {
            entities.add(toEntity(topic));
        }
        return entities;
    }

    public static List<Topic> fromEntityList(List<TopicEntity> entities) {
        List<Topic> topics = new ArrayList<>();
        if (entities != null) {
            for (TopicEntity entity : entities) {
                if (entity != null) {
                    topics.add(fromEntity(entity));
                }
            }
        }
        return topics;
    }
}