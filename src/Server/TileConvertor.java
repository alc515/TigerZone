package Server;

import Board.Tile;

public class TileConvertor {
    public static String tileToRotatedString(Tile tile, int rotation){
        if(rotation == 90){
            tile.rotate();
            tile.rotate();
            tile.rotate();
        }
        if(rotation == 180){
            tile.rotate();
            tile.rotate();
        }
        if(rotation == 270){
            tile.rotate();
        }
        return tileToServerString(tile);
    }
    
    public static String tileToServerString(Tile tile) {
        String result = "";
        result += featureIntToString(tile.t.getEdge());
        result += featureIntToString(tile.r.getEdge());
        result += featureIntToString(tile.b.getEdge());
        result += featureIntToString(tile.l.getEdge());
        result += featureIntToString(tile.getMiddle());
        return result;
    }
    
    public static Tile serverStringToTile(String serverString){
        int middleEdge = 0;
            switch (serverString) {
                case "TJTJ-":
                    middleEdge = 3;
                    break;
                case "LLJJ-":
                    middleEdge = 1;
                    break;
                case "TLTJ-":
                    middleEdge = 3;
                    break;
                case "TJTJD":
                    middleEdge = 3;
                    break;
                case "LJTJ-":
                    middleEdge = 3;
                    break;
                case "LJTJD":
                    middleEdge = 3;
                    break;
                default:
                    middleEdge = featureCharToInt(serverString.charAt(3));
                    break;
            }
        int top = featureCharToInt(serverString.charAt(0));
        int right = featureCharToInt(serverString.charAt(1));
        int bottom = featureCharToInt(serverString.charAt(2));
        int left = featureCharToInt(serverString.charAt(3));
        int middle = featureCharToInt(serverString.charAt(4));
        
        return new Tile(right, top, left, bottom, middleEdge, middle);     
    }
    
    public static String featureIntToString(int feature) {
        if (feature == 2)
            return "J";
        if (feature == 1)
            return "L";
        if (feature == 3)
            return "T";
        if (feature == 0)
            return "-";
        if (feature == 4)
            return "B";
        if (feature == 5)
            return "C";
        if (feature == 6)
            return "D";
        if (feature == 7)
            return "P";
        if (feature == 8)
            return "X";
        
       System.out.println("Invalid Feature Specified");
        return "E";
    }
    
    public static int featureCharToInt(char feature){
        if (feature == 'J')
            return 2;
        if (feature == 'L')
            return 1;
        if (feature == 'T')
            return 3;
        if (feature == '-')
            return 0;
        if (feature == 'B')
            return 4;
        if (feature == 'C')
            return 5;
        if (feature == 'D')
            return 6;
        if (feature == 'P')
            return 7;
        if (feature == 'X')
            return 8;
      
        System.out.println("Invalid Feature Specified");
        return -1;
        
    }
}