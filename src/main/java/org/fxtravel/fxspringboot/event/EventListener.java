package org.fxtravel.fxspringboot.event;

@FunctionalInterface
public interface EventListener<T> {
    void onEvent(T eventData);
}
