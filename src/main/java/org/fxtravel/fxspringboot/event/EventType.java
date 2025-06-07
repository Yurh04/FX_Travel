package org.fxtravel.fxspringboot.event;

/**
 * 事件类型标记接口
 */
public enum EventType {
    TM_CREATED,         // TrainMeal 创建
    TM_STATUS_CHANGED,  // TrainMeal 状态变化
    TT_CREATED,         // TrainTicket 创建
    TT_STATUS_CHANGED,  // TrainTicket 状态变化
    HT_CREATED,         // Hotel 创建
    HT_STATUS_CHANGED,  // Hotel 状态变化
}
