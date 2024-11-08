package net.reaper.vanimals.client.input;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

public class InputStateManager {
    private static final InputStateManager INSTANCE = new InputStateManager();
    private final Map<InputKey, KeyState> keyStates;

    private InputStateManager() {
        this.keyStates = new EnumMap<>(InputKey.class);
        for (InputKey key : InputKey.values()) {
            this.keyStates.put(key, new KeyState());
        }
    }

    public static InputStateManager getInstance() {
        return INSTANCE;
    }

    public void updateKeyState(InputKey pKey, boolean pIsPressed) {
        Optional.ofNullable(this.keyStates.get(pKey)).ifPresent(state -> state.setPressed(pIsPressed));
    }

    public void update() {
        this.keyStates.values().forEach(keyState -> {
            ++keyState.currentTick;
        });
    }

    public boolean isKeyPress(InputKey pKey, KeyPressType pPressType) {
        KeyState keyState = this.keyStates.get(pKey);
        if (keyState != null) {
            return pPressType.matches(keyState);
        }
        return false;
    }
}
