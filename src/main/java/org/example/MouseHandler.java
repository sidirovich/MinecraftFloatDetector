package org.example;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseListener;
import org.jnativehook.mouse.NativeMouseMotionListener;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MouseHandler implements NativeMouseInputListener {
    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
        System.out.println("mouse Press:" + nativeMouseEvent.getButton());
    }
    @Override
    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        return;
    }
    @Override
    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        return;
    }
    @Override
    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        return;
    }
    @Override
    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        return;
    }
    public static void main(String[] args) throws AWTException, NativeHookException {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        GlobalScreen.registerNativeHook();
        // GlobalScreen.dispatchEvent(new NativeInputEvent(GlobalScreen, NativeInputEvent.BUTTON1_MASK));
        MouseHandler mouseHandler = new MouseHandler();
        GlobalScreen.addNativeMouseListener((NativeMouseListener) mouseHandler);
//        GlobalScreen.addNativeMouseMotionListener((NativeMouseMotionListener) main);
    }
}
