package thread.Design.strategy.weapon;

public class BowAndArrowBehavior implements WeaponBehavior{
    @Override
    public void useWeaPon() {
        System.out.println("我使用弓箭");
    }

}