package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.awt.*;

import static front.Board.CELLHEIGHT;
import static front.Board.CELLWIDTH;

public class GraphicSoldier {
    private static final int ANIMFPS = 6;
    private static final int SPRITESIZE = 100;

    private back.Soldier soldier;
    private Image sprite;
    private ImageView imageView;
    private int frame = 0;
    private int moveDir = 0;

    private boolean mine;

    GraphicSoldier(back.Soldier s, boolean mine) {
        this.soldier = s;
        this.mine = mine;
        //this.sprite = new Image("graphics/soldiers/" + soldier.GetID + ".png");
        if(mine) this.sprite = new Image("graphics/soldiers/darkknight.png");
        if(!mine) this.sprite = new Image("graphics/soldiers/fausto.png");
    }

    void move(Point dir) {
        if(dir.x == 0 && dir.y == 0) {
            moveDir = 0;
        } else if (dir.x == -1 && dir.y == 0) {
            moveDir = 1;
        } else if (dir.x == -1 && dir.y == 1) {
            moveDir = 2;
        } else if (dir.x == 0 && dir.y == 1) {
            moveDir = 3;
        } else if (dir.x == 1 && dir.y == 1) {
            moveDir = 4;
        } else if (dir.x == 1 && dir.y == 0) {
            moveDir = 5;
        } else if (dir.x == 1 && dir.y == -1) {
            moveDir = 6;
        } else if (dir.x == 0 && dir.y == -1) {
            moveDir = 7;
        } else if (dir.x == -1 && dir.y == -1) {
            moveDir = 8;
        }

        frame = 0;
    }

    void stop() {
        frame = 0;
        moveDir = 0;
    }

    void drawMyself(Point p, GraphicsContext gc) {
        frame++;
        if (frame > ANIMFPS) {
            stop();
        }

        int xCoord = 0;
        int yCoord = 0;
        int xSprite = 100 * (frame - 1);
        int ySprite = 0;

        switch(moveDir) {
            case 0:
                xCoord = p.y*CELLWIDTH;
                yCoord = p.x*CELLHEIGHT;
                xSprite = 0;
                if(mine) ySprite = SPRITESIZE;
                if(!mine) ySprite = 0;
                break;
            case 1:
                xCoord = p.y*CELLWIDTH;
                yCoord = (p.x + 1) * CELLHEIGHT - (CELLHEIGHT*frame)/ANIMFPS;
                ySprite = SPRITESIZE;
                break;
            case 2:
                xCoord = (p.y - 1) * CELLWIDTH + (CELLWIDTH*frame)/ANIMFPS;
                yCoord = (p.x + 1) * CELLHEIGHT - (CELLHEIGHT*frame)/ANIMFPS;
                ySprite = SPRITESIZE;
                break;
            case 3:
                xCoord = (p.y - 1) * CELLWIDTH + (CELLWIDTH*frame)/ANIMFPS;
                yCoord = p.x*CELLHEIGHT;
                ySprite = 3*SPRITESIZE;
                break;
            case 4:
                xCoord = (p.y - 1) * CELLWIDTH + (CELLWIDTH*frame)/ANIMFPS;
                yCoord = (p.x - 1) * CELLHEIGHT + (CELLHEIGHT*frame)/ANIMFPS;
                ySprite = 0;
                break;
            case 5:
                xCoord = p.y*CELLWIDTH;
                yCoord = (p.x - 1) * CELLHEIGHT + (CELLHEIGHT*frame)/ANIMFPS;
                ySprite = 0;
                break;
            case 6:
                xCoord = (p.y + 1) * CELLWIDTH - (CELLWIDTH*frame)/ANIMFPS;
                yCoord = (p.x - 1) * CELLHEIGHT + (CELLHEIGHT*frame)/ANIMFPS;
                ySprite = 0;
                break;
            case 7:
                xCoord = (p.y + 1) * CELLWIDTH - (CELLWIDTH*frame)/ANIMFPS;
                yCoord = p.x*CELLHEIGHT;
                ySprite = 2*SPRITESIZE;
                break;
            case 8:
                xCoord = (p.y + 1) * CELLWIDTH - (CELLWIDTH*frame)/ANIMFPS;
                yCoord = (p.x + 1) * CELLHEIGHT - (CELLHEIGHT*frame)/ANIMFPS;
                ySprite = SPRITESIZE;
        }

        gc.drawImage(sprite, xSprite, ySprite,SPRITESIZE,SPRITESIZE,xCoord,yCoord - 10,CELLWIDTH,CELLHEIGHT);

        gc.setEffect(null);

        gc.setLineWidth(4);
        gc.setStroke(javafx.scene.paint.Color.GREY);
        gc.strokeLine(15 + xCoord, 95 + yCoord, 85 + xCoord, 95 + yCoord);
        gc.setStroke(Color.RED);
        gc.strokeLine(15 + xCoord, 95 + yCoord, 65 + xCoord, 95 + yCoord);
    }

}
