package thread.Design.strategy;

import thread.Design.strategy.role.King;
import thread.Design.strategy.weapon.SwordBehavior;

/**
 * 策略模式
 * 各个角色或功能做封装
 * https://blog.csdn.net/onewby/article/details/78868959
 * 多个类（题意中角色）只区别在表现行为（使用武器）不同，可以使用Strategy模式
 */
public class Strategy {

    public static void main(String[] args) {
        King king = new King();
        king.display();
        king.weaponWay();
        System.out.println("更换武器^");
        king.setWeapon(new SwordBehavior());
        king.weaponWay();
    }
}
