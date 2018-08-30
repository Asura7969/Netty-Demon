package thread.Design.strategy.role;

import thread.Design.strategy.weapon.SwordBehavior;

public class Knight extends Role{
    public Knight(){
        weapon=new SwordBehavior();
    }
    @Override
    public void display() {
        System.out.println("我是骑士");
    }
}