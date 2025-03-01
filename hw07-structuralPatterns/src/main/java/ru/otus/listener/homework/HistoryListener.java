package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.listener.homework.serializer.MessageCopySerializer;
import ru.otus.listener.homework.serializer.MessageSerializer;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Object> map = new HashMap<>();

    private final MessageSerializer serializer;

    public HistoryListener(MessageSerializer serializer) {
        this.serializer = serializer;
    }

    public HistoryListener() {
        this.serializer = new MessageCopySerializer();
    }

    @Override
    public void onUpdated(Message msg) {

        long id = msg.getId();
        var serialized = serializer.serialize(msg);
        this.map.put(id, serialized);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        var obj = map.get(id);
        if (obj == null) {
            return Optional.empty();
        } else {
            var msg = serializer.deserialize(obj);
            return Optional.of(msg);
        }
    }
}
