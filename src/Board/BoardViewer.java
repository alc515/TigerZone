package Board;

import Server.TileConvertor;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardViewer extends JFrame{
    public static final int SCREEN_WIDTH = 805;
    public static final int VIEW_WIDTH = 13; //Better if Odd
    public static final int REAL_BOARD_CENTER = 75;
    
    public static final int CENTER = VIEW_WIDTH/2;
    public static final int BOARD_SIZE = VIEW_WIDTH * VIEW_WIDTH;
    public static final int TILE_WIDTH = SCREEN_WIDTH/VIEW_WIDTH;

    
    JPanel mainPanel = new JPanel(new GridLayout(VIEW_WIDTH, VIEW_WIDTH));
    JLabel[] tile = new JLabel[BOARD_SIZE];
    public BoardViewer(){
        java.net.URL imgUrl = getClass().getResource("/UI/Graphics/hastile.png");
        if (imgUrl == null) {
        System.err.println("Couldn't find file");
    }
        for (int i = 0; i < BOARD_SIZE; i++) {
            tile[i] = new JLabel(new ImageIcon(imgUrl));
            mainPanel.add(tile[i]);
        }
    }
    
    public void view(){
        add(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel.repaint();
        mainPanel.setVisible(true);
        setSize(SCREEN_WIDTH, SCREEN_WIDTH);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public void addTile(Tile gameTile, int x, int y, int rotation) {
        x -= REAL_BOARD_CENTER;
        x += CENTER;
        y -= REAL_BOARD_CENTER;
        y = -y;
        y += CENTER;
        int space = x + (VIEW_WIDTH * y);
        switch (rotation) {
            case 90:
                gameTile.rotate();
                gameTile.rotate();
                gameTile.rotate();
                break;
            case 180:
                gameTile.rotate();
                gameTile.rotate();
                break;
            case 270:
                gameTile.rotate();
                break;
            default:
                break;
        }
        String rotationString = Integer.toString(rotation) + "/";
        String imageString = "/UI/Graphics/Tiles/" + rotationString + TileConvertor.tileToServerString(gameTile) + ".jpg";
        java.net.URL imgURL = getClass().getResource(imageString);
        tile[space].setIcon(new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(TILE_WIDTH, TILE_WIDTH, Image.SCALE_DEFAULT)));
        repaint();
    }
}
