package org.fxtravel.fxspringboot.utils;

import org.fxtravel.fxspringboot.common.SeatType;

public class SeatUtil {
    public static Integer nextAvailable(byte[] seatAllocation) {
        if (seatAllocation == null) {
            return -1;
        }

        for (int i = 0; i < seatAllocation.length * 8; i++) {
            int byteIndex = i / 8;
            int bitIndex = i % 8;
            byte currentByte = seatAllocation[byteIndex];

            // 检查当前位是否为0（可用）
            if ((currentByte & (1 << bitIndex)) == 0) {
                return i;
            }
        }

        return -1; // 没有找到空闲座位
    }

    public static String idx2number(int index) {
        // 每20个座位为一组，使用同一个字母
        int group = index / 20;
        int seatInGroup = index % 20 + 1; // 转换为1-20
        // 确定起始字母
        char baseChar = getBaseChar();
        // 计算字母部分
        char seatChar = (char)(baseChar + group);
        // 格式化数字部分为两位数
        return String.format("%c%02d", seatChar, seatInGroup);
    }

    public static int number2idx(String seatNumber) {
        if (seatNumber == null || seatNumber.length() < 2) {
            return -1;
        }

        // 获取字母部分
        char seatChar = seatNumber.charAt(0);
        // 获取数字部分
        String numberStr = seatNumber.substring(1);

        try {
            int seatInGroup = Integer.parseInt(numberStr);
            if (seatInGroup < 1 || seatInGroup > 20) {
                return -1;
            }

            // 确定起始字母
            char baseChar = getBaseChar();
            // 计算组号
            int group = seatChar - baseChar;
            if (group < 0) {
                return -1;
            }

            // 计算索引
            return group * 20 + (seatInGroup - 1);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static char getBaseChar() {
        return 'A';
    }

    // 设置特定位为1
    public static byte[] setBit(byte[] bytes, int index) {
        if (bytes == null || index < 0 || index >= bytes.length * 8) {
            return bytes;
        }

        int byteIndex = index / 8;
        int bitIndex = index % 8;
        bytes[byteIndex] |= (byte) (1 << bitIndex);
        return bytes;
    }

    // 设置特定位为0
    public static byte[] clearBit(byte[] bytes, int index) {
        if (bytes == null || index < 0 || index >= bytes.length * 8) {
            return bytes;
        }

        int byteIndex = index / 8;
        int bitIndex = index % 8;
        bytes[byteIndex] &= (byte) ~(1 << bitIndex);
        return bytes;
    }
}