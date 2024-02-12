package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main {
    static int workspaceHeight = 400;
    static int workspaceWidth = 200;
    static int frameHeight = 400;
    static int frameWidth = 400;

    public static void main(String[] args) {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        JFrame frame = new JFrame("MinecraftFloatDetector");
        frame.setVisible(true);
        frame.setSize(frameWidth, frameHeight);
        frame.setBackground(Color.darkGray);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(graphicsDevice.getDisplayMode().getWidth() / 2 - frameWidth / 2, graphicsDevice.getDisplayMode().getHeight() / 2 - frameHeight / 2);

        JButton buttonStart = new JButton("Start macros");
        frame.getContentPane().add(buttonStart);
        frame.pack();

        buttonStart.addActionListener(e -> {
            System.out.println("handle actionPerformed");
            new Thread(() -> {
                try {
                    loopAnalysis();
                } catch (AWTException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }).start();
        });
    }

    /*
     * Когда нажат buttonStart, закидываем удочку (Нажатие ЛКМ), запускаем цикл отслеживания screenImageAnalysis пока он не будет равным FALSE,
     * если screenImageAnalysis => true Нажимаем Ловим рыбу, Закидываем удочку (Нажатие ЛКМ, timeout 400ms, Нажатие ЛКМ),
     * Повторяем цикл отслеживания screenImageAnalysis
     * */
    static void loopAnalysis() throws AWTException, InterruptedException {
        System.out.println("Start loopAnalysis");
        /*
        *   if === 1 Set next step
        *   it === 0 Stop function and break loop
        * */
        int typeStep = 1;
        /*
         *   if === true Рыба не обнаружена
         *   it === false Рыба клюнула дергаем поплавок
         * */
        boolean typeOfAnalysisStep = true;
        // Начинаем ловить рыбу
        while (typeStep == 1) {
            // Закидываем удочку
            System.out.println("Ожидаем 3 секунды");
            Thread.sleep(2000);
            System.out.println("Закидываем удочку");
            pressLMB();
            // Ждем всплытия поплавка
            Thread.sleep(2000);
            System.out.println("всплытия поплавка");
            // Запускаем анализ изображения на наличие поплавка
            System.out.println("Запускаем анализ изображения на наличие поплавка");
            while (typeOfAnalysisStep) {
                long startTime = System.currentTimeMillis();
                boolean resultOfImageAnalysis = screenImageAnalysis();
                long endTime = System.currentTimeMillis();
                long timeElapsed = endTime - startTime;
                System.out.println("resultOfImageAnalysis result: " + timeElapsed + "ms" + " - " + resultOfImageAnalysis);
                if (!resultOfImageAnalysis) {
                    // Поплавок не обнаружен ловим рыбу
                    System.out.println("Поплавок не обнаружен ловим рыбу");
                    // Если ловить моментально есть шанс что не поймается, надо чуть чуть подождать
                    Thread.sleep(50);
                    pressLMB();
                    // Завершаем цикл анализа изображения
                    break;
                }
            }
        }
    }

    static void pressLMB () throws AWTException {
        Robot robot = new Robot();
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    static boolean screenImageAnalysis () throws AWTException {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = graphicsDevice.getDisplayMode().getWidth();
        int screenHeight = graphicsDevice.getDisplayMode().getHeight();
        Robot robot = new Robot(graphicsDevice);
        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        MultiResolutionImage multiResolutionScreenshot = robot.createMultiResolutionScreenCapture(rect);
        Image screenshot = multiResolutionScreenshot.getResolutionVariant(screenWidth, screenHeight);
        int workspaceStartDrawHeightPoint = screenHeight / 2 - workspaceHeight / 2 - 100;
        int workspaceStartDrawWidthPoint = screenWidth / 2 - workspaceWidth / 2;
        BufferedImage bufferedImage = new BufferedImage(screenshot.getWidth(null), screenshot.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(screenshot, 0, 0, null);
        return hasRedPixel(bufferedImage, workspaceStartDrawHeightPoint, workspaceStartDrawWidthPoint);
    }

    static boolean hasRedPixel (BufferedImage bufferedImage, int heightStartPoint, int widthStartPoint) {
        for (int x = 0; x < workspaceWidth; x++) {
            for (int y = 0; y < workspaceHeight; y++)
            {
                int pixelId = bufferedImage.getRGB(widthStartPoint + x, heightStartPoint + y);
                ColorModel colorModel = bufferedImage.getColorModel();
                int blueColorCount = colorModel.getBlue(pixelId);
                int redColorCount = colorModel.getRed(pixelId);
                int greenColorCount = colorModel.getGreen(pixelId);
                if (redColorCount > 140 && greenColorCount < 40 && blueColorCount < 40) {
//                    System.out.println("redColorCount: " + redColorCount);
//                    System.out.println("greenColorCount: " + greenColorCount);
//                    System.out.println("blueColorCount: " + blueColorCount);
                    return true;
                }
            }
        }
//        if (isRed) {
//            for (int x = 0; x < workspaceWidth; x++) {
//                for (int y = 0; y < workspaceHeight; y++)
//                {
//                    bufferedImage.setRGB(widthStartPoint + x,heightStartPoint + y, new Color(0, 0, 0).getRGB());
//                }
//            }
//            File outputFile = new File("./debug_images/" + System.currentTimeMillis() + ".png");
//            ImageIO.write(bufferedImage, "png", outputFile);
//        }
        return false;
    }
}