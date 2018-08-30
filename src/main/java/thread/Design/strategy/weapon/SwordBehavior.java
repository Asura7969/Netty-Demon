package thread.Design.strategy.weapon;

public class SwordBehavior implements WeaponBehavior{
    @Override
    public void useWeaPon() {
        System.out.println("我使用匕首");
    }
}