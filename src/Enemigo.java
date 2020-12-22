
import static java.lang.Math.*;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
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
public class Enemigo {
    private Image[] imagenes;
    private Image imagen;
    private boolean der;
    private double posX;
    private double posY;
    private int vida;
    private Shape poligono;
    private int frec;
    private ArrayList<Bala> balas;
    private int enemigo;
    private int velBala;
    private double resX;

    public Enemigo(int enemigo, double posX, double posY, int vida, double resX) {
        this.resX = resX;
        this.enemigo = enemigo;
        this.der = false;
        this.posX = posX;
        this.posY = posY;        
        this.imagenes = new Image[] {new Image("image/enemies/enemigo" + this.enemigo + "R.png"), new Image("image/enemies/enemigo" + this.enemigo + "L.png")};
        this.imagen = this.imagenes[(this.der) ? 1:0];
        this.poligono = new Rectangle(this.posX, this.posY, this.imagen.getWidth()*(resX/1600), this.imagen.getHeight()*(resX/1600));
        this.balas = new ArrayList<>();
        this.vida = vida;
        this.frec = 2;
        this.velBala = 8 + (frec % 8)/5;
        
    }
    
    public void addBala(Bala b){
        b.setDamage(1 + (vida/3)%3);
        this.balas.add(b);        
    }
    
    public ArrayList<Bala> getBalas() {
        return balas;
    }
    
    public void cambiarPoligono(){
        this.poligono = new Rectangle(this.posX, this.posY, this.imagen.getWidth()*(resX/1600), this.imagen.getHeight()*(resX/1600));
    }
    
    
    public Image getImagen() {
        return imagen;
    }

    public boolean isDer() {
        return der;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public int getVida() {
        return vida;
    }

    public Shape getPoligono() {
        return poligono;
    }

    public int getFrec() {
        return frec;
    }
    
    public int getVelBala() {
        return velBala;
    }

    public void setVelBala(int velBala) {
        this.velBala = velBala;
    }
    

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public void setDer(boolean der) {
        this.der = der;
        this.imagen = this.imagenes[(this.der) ? 1:0];
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public void setVida(int damage) {
        this.vida -= damage;
    }

    public void setPoligono(Shape poligono) {
        this.poligono = poligono;
    }

    public void setFrec(int frec) {
        this.frec = frec;
        this.velBala = 8 + (frec % 8)/5;
    }   
    
}