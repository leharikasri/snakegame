import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener,KeyListener{
    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize=25;
    //snake
    Tile snakehead;
    //snakebody
    ArrayList<Tile> snakeBody;
    //food
    Tile food;
    //to place food randomly
    Random random;
    //game logic
    Timer gameLoop;
    //tomove
    int velocityX;
    int velocityY;
    //game over condition
    boolean gameover=false;
    
    // Constructor
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        //game panel to key pressing connection
        addKeyListener(this);
        setFocusable(true);

        snakehead=new Tile(5,5);
        snakeBody=new ArrayList<Tile>();

        food=new Tile(10,10);
        random=new Random();
        placeFood();

        velocityX=0;
        velocityY=1;//to gp downword

        gameLoop=new Timer(100,this);
        gameLoop.start();

    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        //grid
        for(int i=0;i<boardWidth/tileSize;i++){
            //x1,x2,y1,y2
            g.drawLine(i*tileSize,0,i*tileSize,boardHeight);
            g.drawLine(0,i*tileSize,boardWidth,i*tileSize);
        }
        //food
        g.setColor(Color.red);
        g.fill3DRect(food.x*tileSize,food.y*tileSize,tileSize,tileSize,true);
        //snake
        g.setColor(Color.green);
        g.fill3DRect(snakehead.x*tileSize,snakehead.y*tileSize,tileSize,tileSize,true);

        //snake body
        for(int i=0; i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize,true);
        }
        //score
        g.setFont( new Font("Arial",Font.PLAIN,16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("GAME OVER:"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
        else{
            g.drawString("SCORE:"+String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }

    public void placeFood(){
        food.x=random.nextInt(boardWidth/tileSize);//600/25=24
        food.y=random.nextInt(boardHeight/tileSize);

    }
    
    public void move(){
        //eatfood
        if (collision(snakehead,food)){
            snakeBody.add(new Tile(food.x,food.y));
            placeFood();
        }
        // move snake body
        for (int i= snakeBody.size()-1;i>=0;i--){
            Tile snakePart=snakeBody.get(i);
            if(i==0){
                snakePart.x= snakehead.x;
                snakePart.y= snakehead.y;
            }
            else{
                Tile prevsnakePart=snakeBody.get(i-1);
                snakePart.x= prevsnakePart.x;
                snakePart.y= prevsnakePart.y;
            }
        }
        // move snakehead
        snakehead.x+=velocityX;
        snakehead.y+=velocityY;
        //gameover condition
        for(int i=0; i<snakeBody.size();i++){
            Tile snakePart=snakeBody.get(i);
            //collide with snake head
            if(collision(snakehead,snakePart)){
                gameover=true;
            }
        }
            //if snake hits walls
        if(snakehead.x*tileSize<0 || snakehead.x*tileSize> boardWidth ||
            snakehead.y*tileSize<0 || snakehead.y*tileSize> boardHeight){
            gameover=true;
        }
}
    public boolean collision(Tile tile1,Tile tile2){
        return tile1.x==tile2.x && tile1.y==tile2.y;
    }

        
    
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();//it calls draw again and again
        if(gameover){
            gameLoop.stop();
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_UP && velocityY!=1){//WE PRESS UP KEY
            velocityX=0;
            velocityY=-1; 
        }
        else if (e.getKeyCode()==KeyEvent.VK_DOWN && velocityY!=-1) {
            velocityX=0;
            velocityY=1;
        }
        else if (e.getKeyCode()==KeyEvent.VK_LEFT && velocityX!=1){
            velocityX=-1;
            velocityY=0;
        }
        else if (e.getKeyCode()==KeyEvent.VK_RIGHT && velocityX!=-1){
            velocityX=1;
            velocityY=0;
        }
    }
    //we donotneed
    @Override
    public void keyTyped(KeyEvent e) {}
    
    
    @Override
    public void keyReleased(KeyEvent e) {}
}


