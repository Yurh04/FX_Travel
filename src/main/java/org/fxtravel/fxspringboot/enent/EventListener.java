package org.fxtravel.fxspringboot.enent;

@FunctionalInterface
public interface EventListener<T> {
    void onEvent(T eventData);
}
