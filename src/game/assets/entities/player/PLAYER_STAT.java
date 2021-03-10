package game.assets.entities.player;

public enum PLAYER_STAT {
    move_speed("Movement speed"),
    rate_of_fire("Rate of fire"),
    damage("Damage"),
    dash_speed("Dash speed"),
    dash_duration("Dash duration"),
    dash_cooldown("Dash cooldown"),
    health("Max health");

    public String displayName;

    PLAYER_STAT(String displayName) {
        this.displayName = displayName;
    }
}
