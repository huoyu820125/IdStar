/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.huoyu820125.idstar.paxos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 决策者
 * @author SunQian
 * @version 2.0
 */
public class Acceptor<T> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public Acceptor() {
        mMaxSerialNum = 0;
        mLastAcceptValue = new ProposeData();
        mLastAcceptValue.setSerialNum(0);
        mLastAcceptValue.setValue(null);
    }

    /**
     * 同意/拒绝下阶段会接受提议
     *  同意时，承诺不再同意编号小于mMaxSerialNum的提议，也不再接受编号小于mMaxSerialNum的提议
     * @author: SunQian
     * @param serialNum   提案
     * @param lastAcceptValue   提案
     * @return 接受return ture,决绝return false
     */
    public boolean propose(int serialNum, ProposeData<T> lastAcceptValue) {
        mLock.lock();
        try {
            if (0 >= serialNum) {
                mLock.unlock();
                return false;
            }
            if (mMaxSerialNum >= serialNum) {
                mLock.unlock();
                log.info("拒绝拉票:期望报酬={}, 当前报酬{}", mMaxSerialNum, serialNum);
                return false;
            }
            log.info("接受拉票:期望报酬={}, 当前报酬{}", mMaxSerialNum, serialNum);
            mMaxSerialNum = serialNum;
            lastAcceptValue.setSerialNum(mLastAcceptValue.serialNum());
            lastAcceptValue.setValue(mLastAcceptValue.value());
        }
        finally {
            mLock.unlock();
        }

        return true;
    }

    /**
     * 接受/拒绝提案
     *  只接受编号>=mMaxSerialNum的提案，并记录
     * @author: SunQian
     * @param value   提案
     * @return 接受return ture,决绝return false
     */
    public boolean accept(ProposeData<T> value) {
        mLock.lock();
        try {
            if (0 >= value.serialNum()) {
                mLock.unlock();
                return false;
            }
            if (mMaxSerialNum > value.serialNum()) {
                mLock.unlock();
                log.info("拒绝提案:期望报酬={}, 当前报酬{}", mMaxSerialNum, value.serialNum());
                return false;
            }
            log.info("接受提案:期望报酬={}, 当前报酬{}", mMaxSerialNum, value.serialNum());
            mLastAcceptValue.setSerialNum(value.serialNum());
            mLastAcceptValue.setValue(value.value());
        } finally {
            mLock.unlock();
        }
        return true;
    }
    private ProposeData<T> mLastAcceptValue;//最后接受的提案
    private int mMaxSerialNum;//Propose提交的最大流水号
    private Lock mLock = new ReentrantLock();
}
