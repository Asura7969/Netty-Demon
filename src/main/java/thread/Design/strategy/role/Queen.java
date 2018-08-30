package thread.Design.strategy.role;

import thread.Design.strategy.weapon.KnifeBehavior;

public class Queen extends Role{
    public Queen(){
        weapon=new KnifeBehavior();
    }
    @Override
    public void display() {
        System.out.println("我是皇后");
    }
}