package net.reaper.vanimals.client.input;

import org.lwjgl.glfw.GLFW;

public enum InputKey {
    ATTACK(GLFW.GLFW_MOUSE_BUTTON_1),
    USE(GLFW.GLFW_MOUSE_BUTTON_2),
    JUMP(GLFW.GLFW_KEY_SPACE),
    W(GLFW.GLFW_KEY_W),
    A(GLFW.GLFW_KEY_A),
    S(GLFW.GLFW_KEY_S),
    D(GLFW.GLFW_KEY_D),
    SHIFT(GLFW.GLFW_KEY_LEFT_SHIFT),
    CTRL(GLFW.GLFW_KEY_LEFT_CONTROL),
    ALT(GLFW.GLFW_KEY_LEFT_ALT),
    E(GLFW.GLFW_KEY_E),
    I(GLFW.GLFW_KEY_I),
    R(GLFW.GLFW_KEY_R),
    M(GLFW.GLFW_KEY_M),
    F(GLFW.GLFW_KEY_F),
    G(GLFW.GLFW_KEY_G),
    H(GLFW.GLFW_KEY_H),
    ONE(GLFW.GLFW_KEY_1),
    TWO(GLFW.GLFW_KEY_2),
    THREE(GLFW.GLFW_KEY_3),
    FOUR(GLFW.GLFW_KEY_4),
    FIVE(GLFW.GLFW_KEY_5),
    SIX(GLFW.GLFW_KEY_6),
    SEVEN(GLFW.GLFW_KEY_7),
    EIGHT(GLFW.GLFW_KEY_8),
    NINE(GLFW.GLFW_KEY_9),
    ZERO(GLFW.GLFW_KEY_0),
    Q(GLFW.GLFW_KEY_Q),
    T(GLFW.GLFW_KEY_T),
    Y(GLFW.GLFW_KEY_Y),
    U(GLFW.GLFW_KEY_U),
    O(GLFW.GLFW_KEY_O),
    P(GLFW.GLFW_KEY_P),
    Z(GLFW.GLFW_KEY_Z),
    X(GLFW.GLFW_KEY_X),
    C(GLFW.GLFW_KEY_C),
    V(GLFW.GLFW_KEY_V),
    B(GLFW.GLFW_KEY_B),
    N(GLFW.GLFW_KEY_N),
    K(GLFW.GLFW_KEY_K),
    L(GLFW.GLFW_KEY_L),
    J(GLFW.GLFW_KEY_J),
    TAB(GLFW.GLFW_KEY_TAB),
    ESC(GLFW.GLFW_KEY_ESCAPE),
    PAGE_UP(GLFW.GLFW_KEY_PAGE_UP),
    PAGE_DOWN(GLFW.GLFW_KEY_PAGE_DOWN),
    HOME(GLFW.GLFW_KEY_HOME),
    END(GLFW.GLFW_KEY_END),
    INSERT(GLFW.GLFW_KEY_INSERT),
    DELETE(GLFW.GLFW_KEY_DELETE),
    UP(GLFW.GLFW_KEY_UP),
    DOWN(GLFW.GLFW_KEY_DOWN),
    LEFT(GLFW.GLFW_KEY_LEFT),
    RIGHT(GLFW.GLFW_KEY_RIGHT),
    NUMPAD_0(GLFW.GLFW_KEY_KP_0),
    NUMPAD_1(GLFW.GLFW_KEY_KP_1),
    NUMPAD_2(GLFW.GLFW_KEY_KP_2),
    NUMPAD_3(GLFW.GLFW_KEY_KP_3),
    NUMPAD_4(GLFW.GLFW_KEY_KP_4),
    NUMPAD_5(GLFW.GLFW_KEY_KP_5),
    NUMPAD_6(GLFW.GLFW_KEY_KP_6),
    NUMPAD_7(GLFW.GLFW_KEY_KP_7),
    NUMPAD_8(GLFW.GLFW_KEY_KP_8),
    NUMPAD_9(GLFW.GLFW_KEY_KP_9),
    F1(GLFW.GLFW_KEY_F1),
    F2(GLFW.GLFW_KEY_F2),
    F3(GLFW.GLFW_KEY_F3),
    F4(GLFW.GLFW_KEY_F4),
    F5(GLFW.GLFW_KEY_F5),
    F6(GLFW.GLFW_KEY_F6),
    F7(GLFW.GLFW_KEY_F7),
    F8(GLFW.GLFW_KEY_F8),
    F9(GLFW.GLFW_KEY_F9),
    F10(GLFW.GLFW_KEY_F10),
    F11(GLFW.GLFW_KEY_F11),
    F12(GLFW.GLFW_KEY_F12),
    SEMICOLON(GLFW.GLFW_KEY_SEMICOLON),
    EQUAL(GLFW.GLFW_KEY_EQUAL),
    MINUS(GLFW.GLFW_KEY_MINUS),
    COMMA(GLFW.GLFW_KEY_COMMA),
    PERIOD(GLFW.GLFW_KEY_PERIOD),
    SLASH(GLFW.GLFW_KEY_SLASH),
    BACKSLASH(GLFW.GLFW_KEY_BACKSLASH),
    QUOTE(GLFW.GLFW_KEY_APOSTROPHE),
    SPACE(GLFW.GLFW_KEY_SPACE);

    private final int keyCode;

    InputKey(int pKeyCode) {
        this.keyCode = pKeyCode;
    }

    public static InputKey fromKeyCode(int pKeyCode) {
        for (InputKey key : values()) {
            if (key.keyCode == pKeyCode) {
                return key;
            }
        }
        return null;
    }
}