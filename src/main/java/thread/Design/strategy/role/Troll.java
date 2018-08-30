package thread.Design.strategy.role;

import thread.Design.strategy.weapon.SwordBehavior;

public class Troll extends Role{
    public Troll(){
        weapon=new SwordBehavior();
    }
    @Override
    public void display() {
        System.out.println("我是妖怪");
    }
}