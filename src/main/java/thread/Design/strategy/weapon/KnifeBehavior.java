package thread.Design.strategy.weapon;

public class KnifeBehavior implements WeaponBehavior{
    @Override
    public void useWeaPon() {
        System.out.println("我使用宝剑");
    }
}