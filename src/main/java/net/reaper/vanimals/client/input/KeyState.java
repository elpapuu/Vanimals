package net.reaper.vanimals.client.input;

public class KeyState {
    public boolean isPressed;
    public boolean wasPressed;
    private int lastPressedTick;
    public int currentTick;
    public boolean justReleased;
    private int pressCount = 0;
    private boolean doublePressHandled = false;

    public void setPressed(boolean pIsPressed) {
        if (!pIsPressed && this.isPressed) {
            this.justReleased = true;
        }
        if (pIsPressed && !this.isPressed) {
            if (this.currentTick - this.lastPressedTick <= 10) {
                this.pressCount++;
            } else {
                this.pressCount = 1;
                this.doublePressHandled = false;
            }
            this.lastPressedTick = this.currentTick;
        }
        this.wasPressed = this.isPressed;
        this.isPressed = pIsPressed;
    }

    public boolean isDoublePressed() {
        if (this.pressCount >= 2 && (this.currentTick - this.lastPressedTick <= 10)) {
            if (!this.doublePressHandled) {
                this.doublePressHandled = true;
                return true;
            }
        }
        return false;
    }
}
