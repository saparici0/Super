
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Simón
 */
public class Mapa {
    private Image piso;
    private Image fondo;
    private Image[] obstaculos = new Image[10];
    private int key;
    private int numObs;
    private int map;
    private double resX;
    private Random random;

    public Mapa(int key, int map, int numObs, double resX) {
        this.map = map;
        this.resX = resX;
        this.numObs = numObs;
        this.piso = new Image("/image/piso/piso" + this.map + Math.round(this.resX) + ".png");
        this.fondo = new Image("/image/fondos/fondo" + this.map + Math.round(this.resX) + ".png");
        this.key = key;
        this.random = new Random(key);
    }
    
    public int nextObsIndex(){
        return this.random.nextInt(this.numObs);
    }
    
    public Obstaculo nextObstaculo(double posX){
        try {
            return new Obstaculo(posX, nextObsIndex(), new Random().nextInt((int) resX/4) + resX/3, this.resX);
        } catch (FileNotFoundException ex) {
            
        }
        return null;
    }

    public Image getFondo() {
        return fondo;
    }

    public Image getPiso() {
        return piso;
    }

    public int getKey() {
        return key;
    }

    public int getNumObs() {
        return numObs;
    }

    public int getMap() {
        return map;
    }
}
