package game.assets.items.item;

public interface CanAttack {
    int getDamage();
    int getAttack_speed();

    void setDamage(int val);
    void setAttack_speed(int val);
}
