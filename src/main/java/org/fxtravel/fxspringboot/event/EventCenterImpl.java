package org.fxtravel.fxspringboot.event;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class EventCenterImpl implements EventCenter {
    private final Map<EventType, Set<EventListener<?>>> listeners = new ConcurrentHashMap<>();
    private final Executor asyncExecutor = Executors.newFixedThreadPool(4);

    @Override
    public <T> void subscribe(EventType eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> ConcurrentHashMap.newKeySet())
                .add(listener);
    }

    @Override
    public <T> void unsubscribe(EventType eventType, EventListener<T> listener) {
        Set<EventListener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    @Override
    public <T> void publish(EventType eventType, T data) {
        Set<EventListener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                @SuppressWarnings("unchecked")
                EventListener<T> typedListener = (EventListener<T>) listener;
                try {
                    typedListener.onEvent(data);
                } catch (Exception e) {
                    // 记录错误但不要中断其他监听器
                    System.err.println("Error handling event: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public <T> void publishAsync(EventType eventType, T data) {
        asyncExecutor.execute(() -> publish(eventType, data));
    }
}