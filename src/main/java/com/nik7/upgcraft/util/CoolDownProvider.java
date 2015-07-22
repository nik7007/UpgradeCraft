package com.nik7.upgcraft.util;


public interface CoolDownProvider {

    void setTransferCoolDown(int transferCoolDown);

    boolean checkTransferCoolDown();

    int getMaxCoolDown();

}
