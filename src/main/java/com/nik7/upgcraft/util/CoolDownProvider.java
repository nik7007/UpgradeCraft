package com.nik7.upgcraft.util;


public interface CoolDownProvider {

    public void setTransferCoolDown(int transferCoolDown);

    public boolean checkTransferCoolDown();

}
