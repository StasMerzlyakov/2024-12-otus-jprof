package ru.otus.listener.homework.serializer;

import java.util.ArrayList;
import java.util.Collections;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

/**
 * Серилизация сообщения путем простого копирования полей.
 * Можно придумать варианты с сериализацией в json, xml, БД, массив байт и прочее.
 */
public class MessageCopySerializer implements MessageSerializer {
    @Override
    public Object serialize(Message message) throws SerializableException {
        return copyOfMessage(message);
    }

    @Override
    public Message deserialize(Object obj) throws SerializableException {
        if (obj instanceof Message m) {
            return m;
        }
        throw new SerializableException("object is not instance of Message");
    }

    private Object copyOfMessage(Message message) {
        if (message == null) {
            return null;
        }

        var field13 = copyOfObjectForMessage(message.getField13());
        return message.toBuilder().field13(field13).build();
    }

    private ObjectForMessage copyOfObjectForMessage(ObjectForMessage field) {
        if (field == null) {
            return null;
        }

        var newField = new ObjectForMessage();

        var data = field.getData();
        if (data != null) {
            var newData = new ArrayList<>(data);
            Collections.copy(newData, data);
            newField.setData(newData);
        }

        return newField;
    }
}
