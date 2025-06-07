package org.fxtravel.fxspringboot.service.impl.trainseat;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.fxtravel.fxspringboot.event.EventCenter;
import org.fxtravel.fxspringboot.event.EventType;
import org.fxtravel.fxspringboot.event.data.PaymentInfo;
import org.fxtravel.fxspringboot.mapper.TrainSeat.TrainMapper;
import org.fxtravel.fxspringboot.mapper.TrainSeat.TrainSeatMapper;
import org.fxtravel.fxspringboot.pojo.dto.train.TrainSearchResult;
import org.fxtravel.fxspringboot.pojo.entities.Train;
import org.fxtravel.fxspringboot.pojo.entities.TrainSeat;
import org.fxtravel.fxspringboot.service.inter.TrainSeatService;
import org.fxtravel.fxspringboot.utils.SeatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrainSeatServiceImpl implements TrainSeatService {
    @Autowired
    TrainMapper trainMapper;
    @Autowired
    TrainSeatMapper trainSeatMapper;

    @Autowired
    private EventCenter eventCenter;

    @PostConstruct
    public void init() {
        // 注册回调，确保在服务启动时就注册
        eventCenter.subscribe(EventType.TM_STATUS_CHANGED, this::handlePaymentStatusChange);
    }

    @PreDestroy
    public void destroy() {
        // 服务关闭时注销回调
        eventCenter.unsubscribe(EventType.TM_STATUS_CHANGED, this::handlePaymentStatusChange);
    }

    // 处理支付状态变更的回调方法
    private void handlePaymentStatusChange(PaymentInfo info) {
        switch (info.getNewStatus()){
            case FAILED:
            case REFUNDED:
                putBack(info.getGoodId(), info.getQuantity(), info.getData());
                break;
        }
    }

    @Override
    public Train getTrainById(Integer trainId) {
        return trainMapper.selectById(trainId);
    }

    @Override
    public List<TrainSearchResult> findByRouteAndTimeOrderByTime(String fromStation, String toStation, LocalDate departureDate) {
        LocalDateTime startOfDay = departureDate.atStartOfDay();
        LocalDateTime endOfDay = departureDate.atTime(23, 59, 59);

        List<Train> trains = trainMapper.findByRouteAndTimeOrderByTime(fromStation, toStation, startOfDay, endOfDay);

        return getResults(trains);
    }

    @Override
    public List<TrainSearchResult> findByRouteAndTimeOrderByDuration(String fromStation, String toStation, LocalDate departureDate) {
        LocalDateTime startOfDay = departureDate.atStartOfDay();
        LocalDateTime endOfDay = departureDate.atTime(23, 59, 59);

        List<Train> trains = trainMapper.findByRouteAndTimeOrderByDuration(fromStation, toStation, startOfDay, endOfDay);

        return getResults(trains);
    }

    private List<TrainSearchResult> getResults(List<Train> trains) {
        if (trains.isEmpty()) {
            return null;
        }

        // 转换为搜索结果
        List<TrainSearchResult> results = new ArrayList<>();

        for (Train train : trains) {
            // 获取该列车的所有座位信息
            List<TrainSeat> seatList = trainSeatMapper.findByTrain(train.getId());
            // 若列车没有设置座次，则不显示该列车
            if (seatList.isEmpty()) {
                continue;
            }
            // 创建搜索结果对象
            TrainSearchResult result = new TrainSearchResult();
            result.setTrain(train);
            result.setTrainseats(seatList);
            results.add(result);
        }

        return results;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public boolean checkAndGet(int id, int count, Object seatNumberRef) {
        if (!(seatNumberRef instanceof String[] seatNumber)) {
            System.err.println(seatNumberRef.getClass());
            return false;
        }

        TrainSeat obj = trainSeatMapper.selectById(id);
        if (obj == null || obj.getRemain() < 1) {
            return false;
        }
        Integer index = SeatUtil.nextAvailable(obj.getSeatAllocation());
        seatNumber[0] = SeatUtil.idx2number(index);

        trainSeatMapper.deduct(id, index);
        return true;
    }

    @Override
    public void putBack(int id, int count, Object seatNumber) {
        if (!(seatNumber instanceof String)) {
            System.err.println(seatNumber.getClass());
            return;
        }
        trainSeatMapper.add(id, SeatUtil.number2idx((String) seatNumber));
    }
}
