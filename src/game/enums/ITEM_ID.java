package game.enums;

public enum ITEM_ID {
    NULL("NULL"), key("Key"), power_up("Test Item");

    public String displayName;

    ITEM_ID(String displayName) {
        this.displayName = displayName;
    }
}
