package thread.Design.strategy.weapon;

public class AxeBehavior implements WeaponBehavior{
    @Override
    public void useWeaPon() {
        System.out.println("我使用斧头");
    }
}