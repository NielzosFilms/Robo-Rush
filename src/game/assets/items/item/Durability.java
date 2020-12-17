package game.assets.items.item;

public interface Durability {
    int getDurability();
    void setDurability();
    void subDurability(int amount);
    void addDurability(int amount);
}
