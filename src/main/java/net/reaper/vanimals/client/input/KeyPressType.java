package net.reaper.vanimals.client.input;

public enum KeyPressType {
    HOLD {
        @Override
        public boolean matches(KeyState pKeyState) {
            return pKeyState.isPressed;
        }
    },
    PRESSED {
        @Override
        public boolean matches(KeyState pKeyState) {
            return pKeyState.isPressed && !pKeyState.wasPressed;
        }
    },
    RELEASED {
        @Override
        public boolean matches(KeyState pKeyState) {
            if (pKeyState.justReleased) {
                pKeyState.justReleased = false;
                return true;
            }
            return false;
        }
    },
    DOUBLE_PRESSED {
        @Override
        public boolean matches(KeyState pKeyState) {
            return pKeyState.isDoublePressed();
        }
    },
    NONE {
        @Override
        public boolean matches(KeyState pKeyState) {
            return !pKeyState.isPressed;
        }
    };

    public abstract boolean matches(KeyState pKeyState);
}
