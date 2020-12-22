
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Simón
 */
public class Bala {
    private double posX;
    private double posY;
    private boolean dir;
    private Image imagen;
    private double resX;
    private int damage;
    private int tipo;
    private double vel;
    private Image[][] imagenes = {{new Image("image/balas/1r.png"), new Image("image/accesorios/ac4L.png"), new Image("image/accesorios/ac5L.png")},
                                 {new Image("image/balas/1.png"), new Image("image/accesorios/ac4.png"), new Image("image/accesorios/ac5.png")}}
                                 ;
    
    private int[] damages = {1, 1, 3};
    private double[] vels = new double[3];
    private Shape poligono;

    public Bala(double posX, double posY, boolean dir, double resX, int tipo, double vel) {
        this.posX = posX;
        this.posY = posY;
        this.dir = dir;
        this.tipo = tipo;
        this.damage = damages[tipo];
        vels[0] = vel; vels[1] = 3*vel; vels[2] = vel;
        this.vel = vels[tipo];
        this.imagen = this.imagenes[this.dir ? 1:0][this.tipo];
        this.poligono = new Rectangle(this.posX, this.posY, this.resX/80, this.resX/160);
    }
    
    public void setPosX(double posX) {
        this.posX = posX;
        this.poligono = new Rectangle(this.posX, this.posY, this.resX/50, this.resX/160);
    }

    public int getDamage() {
        return damage;
    }
    
    public void setDamage(int damage) {
        this.damage = damage;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public boolean isDir() {
        return dir;
    }

    public Image getImagen() {
        return imagen;
    }
    
    public Shape getPoligono(){
       return this.poligono;
    }

    public double getVel() {
        return vel;
    }
}
