package org.fxtravel.fxspringboot.service.inter;

public interface GoodService {
    boolean checkAndGet(int id, int count);

    void putBack(int id, int count);
}
