package thread.Design.strategy.role;

import thread.Design.strategy.weapon.WeaponBehavior;

public abstract class Role {
      WeaponBehavior weapon;
      public Role(){  
      }
      public void setWeapon(WeaponBehavior weapon) {
           this.weapon = weapon;
      }
      abstract void display();
      public void weaponWay(){
          weapon.useWeaPon();
      } 
}