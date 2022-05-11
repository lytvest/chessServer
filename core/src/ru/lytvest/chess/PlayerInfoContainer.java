package ru.lytvest.chess;


import com.badlogic.gdx.scenes.scene2d.Group;

public class PlayerInfoContainer extends Group {
    TimeContainer me;
    TimeContainer enemy;

    public PlayerInfoContainer(String meName, String enemyName, int startTime){

        me = new TimeContainer(meName, startTime, false);
        enemy = new TimeContainer(enemyName, startTime, true);
        addActor(me);
        addActor(enemy);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        me.setBounds(0,0, getWidth(), getHeight() / 2);
        enemy.setBounds(0,getHeight() / 2, getWidth(), getHeight() / 2);
    }

    public void enemyActive(){
        me.setActive(false);
        enemy.setActive(true);
    }
    public void meActive(){
        me.setActive(true);
        enemy.setActive(false);
    }
    public void updateTime(int meTime, int enemyTime){
        me.updateTime(meTime);
        enemy.updateTime(enemyTime);
    }
    public void stop(){
        me.setActive(false);
        enemy.setActive(false);
    }
}
