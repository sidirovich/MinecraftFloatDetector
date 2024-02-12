package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;

public class RenderDebugWorkspace {
    public static void main(String[] args) throws AWTException, IOException, InterruptedException {
        Thread.sleep(5000);
        renderWorkspaceWithBlackBackground();
    }
    static void renderWorkspaceWithBlackBackground () throws AWTException, IOException {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int screenWidth = graphicsDevice.getDisplayMode().getWidth();
        int screenHeight = graphicsDevice.getDisplayMode().getHeight();
        Robot robot = new Robot(graphicsDevice);
        Rectangle rect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        MultiResolutionImage multiResolutionScreenshot = robot.createMultiResolutionScreenCapture(rect);
        Image screenshot = multiResolutionScreenshot.getResolutionVariant(screenWidth, screenHeight);
        int workspaceStartDrawHeightPoint = screenHeight / 2 - Main.workspaceHeight / 2;
        int workspaceStartDrawWidthPoint = screenWidth / 2 - Main.workspaceWidth / 2;
        BufferedImage bufferedImage = new BufferedImage(screenshot.getWidth(null), screenshot.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(screenshot, 0, 0, null);

        for (int x = 0; x < Main.workspaceWidth; x++) {
            for (int y = 0; y < Main.workspaceHeight; y++) {
                bufferedImage.setRGB(workspaceStartDrawWidthPoint + x, workspaceStartDrawHeightPoint + y, new Color(0, 0, 0).getRGB());
            }
        }

        File outputFile = new File("debug.png");
        ImageIO.write(bufferedImage, "png", outputFile);
    }
}
