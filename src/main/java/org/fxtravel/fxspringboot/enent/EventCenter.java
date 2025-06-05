package org.fxtravel.fxspringboot.enent;

public interface EventCenter {
    /**
     * 订阅事件
     * @param eventType 事件类型
     * @param listener 监听器
     * @param <T> 事件数据类型
     */
    <T> void subscribe(EventType eventType, EventListener<T> listener);

    /**
     * 取消订阅
     * @param eventType 事件类型
     * @param listener 监听器
     * @param <T> 事件数据类型
     */
    <T> void unsubscribe(EventType eventType, EventListener<T> listener);

    /**
     * 发布事件
     * @param eventType 事件类型
     * @param data 事件数据
     * @param <T> 事件数据类型
     */
    <T> void publish(EventType eventType, T data);

    /**
     * 异步发布事件
     * @param eventType 事件类型
     * @param data 事件数据
     * @param <T> 事件数据类型
     */
    <T> void publishAsync(EventType eventType, T data);
}
