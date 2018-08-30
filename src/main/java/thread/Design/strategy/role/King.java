package thread.Design.strategy.role;

import thread.Design.strategy.weapon.AxeBehavior;

public class King extends Role{
    public King(){
        weapon=new AxeBehavior();
    }
    @Override
    public void display() {
        System.out.println("我是国王");
    }
}