package front;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
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
    private int frame = 0;
    private int moveDir = 0;

    private boolean mine;

    GraphicSoldier(back.Soldier s, boolean mine) {
        this.soldier = s;
        this.mine = mine;

        this.sprite = new Image("graphics/soldiers/" + s.getID() + ".png");

    }

    public back.Soldier getSoldier() {
        return soldier;
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

    void drawMyself(Point p, GraphicsContext gc, back.Player player1) {

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
                if(soldier.getOwner().equals(player1)) ySprite = SPRITESIZE; else ySprite = 0;
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

        if(soldier instanceof back.Hero) {
            DropShadow borderGlow= new DropShadow();
            borderGlow.setOffsetY(0f);
            borderGlow.setOffsetX(0f);
            if(mine)
                borderGlow.setColor(Color.rgb(27, 255, 108,0.9));
            else
                borderGlow.setColor(Color.rgb(255,0,0,0.9));

            borderGlow.setWidth(70);
            borderGlow.setHeight(70);

            gc.setEffect(borderGlow);
        } else
            gc.setEffect(null);

        gc.drawImage(sprite, xSprite, ySprite,SPRITESIZE,SPRITESIZE,xCoord,yCoord - 10,CELLWIDTH,CELLHEIGHT);

        gc.setLineWidth(4);
        gc.setStroke(Color.grayRgb(25, 0.5922));
        gc.strokeLine(15 + xCoord, 95 + yCoord, 85 + xCoord, 95 + yCoord);
        if(!mine) gc.setStroke(Color.rgb(255,0,0,0.9)); else gc.setStroke(Color.rgb(27, 255, 108,0.9));
        gc.strokeLine(15 + xCoord, 95 + yCoord, 15 + soldier.getHealthPercent() + xCoord, 95 + yCoord);

    }

}
