package BoardGame;


import BoardGame.model.Tiles;
import BoardGame.model.Rectangle;
import BoardGame.model.SpriteSheet;
import BoardGame.model.Sprite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Game extends JFrame implements Runnable {

    //colorID
    public static int alpha = 0xFFFF00DC;

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    private BufferedImage testImage;
    
    private Sprite testSprite;
    private SpriteSheet sheet;
    private Rectangle textRectangle = new Rectangle(30, 30, 100, 100);
    
    private Tiles tiles;

    public Game() {
        
        //Make our program shutdown when we exit out.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the position and size of our frame.
        setBounds(0, 0, 1000, 800);

        //Put our frame in the center of the screen.
        setLocationRelativeTo(null);

        //Add our graphics compoent
        add(canvas);

        //Make our frame visible.
        setVisible(true);

        //Create our object for buffer strategy.
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());
        
        // load Assets
        BufferedImage sheetImage = loadImage("Tiles1.png");
        sheet = new SpriteSheet(sheetImage);
        sheet.loadSprites(16, 16);
        
        tiles = new Tiles(new File("src/assets/tiles.txt"), sheet);
        //testImage = loadImage("GrassTile.png");
        //testSprite = sheet.getSprite(4, 1);
        //textRectangle.generateGraphics(3, 1234);
    }

    public void update() {

    }

    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        super.paint(graphics);
        
        tiles.renderTile(2, renderer, 0, 0, 3, 3);
        //renderer.renderSprite(testSprite, 0, 0, 5, 5);
        //renderer.renderRectangle(textRectangle, 1, 1);
        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    private BufferedImage loadImage(String path) {
        try {
            BufferedImage loadedImage = ImageIO.read(Game.class.getResource(path));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

    }

    public void run() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        int i = 0;
        int x = 0;

        long lastTime = System.nanoTime(); //long 2^63
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per second
        double changeInSeconds = 0;

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1) {
                update();
                changeInSeconds = 0;
            }
            render();
            lastTime = now;
        }

    }

    public static void main(String[] args) {
        Game game = new Game();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }

}