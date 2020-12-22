
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
public class Corazon {
    private Image imagen;
    private double posX;
    private double posY;
    private Shape poligono;
    private double resX;

    public Corazon(double posX, double posY, double resX) {
        this.resX = resX;
        this.imagen = new Image("image/corazon.png");
        this.posX = posX;
        this.posY = posY;
        //this.poligono = new Circle(imagen.getHeight()/2, this.posX+imagen.getWidth()/2, this.posY+imagen.getHeight()/2);
        this.poligono = new Rectangle(this.posX, this.posY, this.imagen.getWidth(), this.imagen.getHeight());
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
        actualizarPoligono();
    }

    public void setPosY(double posY) {
        this.posY = posY;
        actualizarPoligono();
    }

    public void actualizarPoligono() {
        //this.poligono = new Circle(imagen.getHeight()/2, this.posX+imagen.getWidth()/2, this.posY+imagen.getHeight()/2);
        this.poligono = new Rectangle(this.posX, this.posY, this.imagen.getWidth(), this.imagen.getHeight());
    }

    public Image getImagen() {
        return imagen;
    }

    public Shape getPoligono() {
        return poligono;
    }
}
