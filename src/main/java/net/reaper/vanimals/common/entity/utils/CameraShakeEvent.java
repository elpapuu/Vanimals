package net.reaper.vanimals.common.entity.utils;

import jdk.jfr.Event;
import net.minecraft.world.entity.player.Player;

public class CameraShakeEvent extends Event {
    private final Player player;

    public CameraShakeEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}