package ru.otus.listener.homework.serializer;

import ru.otus.model.Message;

public interface MessageSerializer {
    Object serialize(Message message) throws SerializableException;

    Message deserialize(Object obj) throws SerializableException;
}
