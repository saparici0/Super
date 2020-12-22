
import java.io.*;
import java.util.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
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
public class Personaje {
    private double resX;
    private double resY;
    private double posX;
    private double posY;
    private int monedas;
    private int vida;
    
    //private Image imagen;
    
    private String[] personajes = {"guy", "taco","rambo", "chuck"};
    
    private int[] per = new int[personajes.length];
    
    private Image[][] imagenes = new Image[100][100];
    private Image imageAccesorio;
    
    private int[][] adquisicion = new int[3][personajes.length];
    private int[][] valores = new int[3][personajes.length];
    private int[] valoresAc = new int[6];
    private int[] resistenciaCascos = new int[4];
    private int[] curVidaCasco = new int[4];
    
    private int[] accesorios = new int[6];
    
    private int[][] nFrames = {
        {//legs
            1,1,3,3,1,1,3
        },{//torsos
            1,1,1,1,3
        },{//heads
            1,1,1,1,1
        }
    };
    
    private Image[] imagen;
    private int[] numFrames;
    private Shape[] poligono;
    private Shape poligono2;
    
    private int[] index;
    private ArrayList<Bala> balas;
    
    private ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>> puntos; //Array List. niveles 1. por cada lado 2. por cada frame 3. por cada imagen 3.por parte 4. todas las partes
    
    //[tDer, tIzq, sentidoDer, disparo, escalera, subiendo, cayendo, tArr, tAba]
    private boolean[] estado;
    
    public Personaje(double posX, double posY, double resX, double resY) {
        this.estado = new boolean[9];
        this.index = new int[3]; this.index[0] = 0; this.index[1] = 0; this.index[2] = 0;
        this.numFrames = new int[3]; this.numFrames[0] = 1; this.numFrames[1] = 1; this.numFrames[2] = 1;
        
 
        this.poligono = new Shape[3];
        
        this.posX = posX;
        this.posY = posY;
        this.resX = resX;
        this.resY = resY;
        
        this.vida = 6;
        
        this.estado[2] = true;
        this.estado[5] = false;
        this.estado[6] = false;
        
        this.puntos = new ArrayList<>();
        
        per[0] = 0; per[1] = 0; per[2] = 0;
        actualizarPuntosPoligonos();
        this.balas = new ArrayList<>();
        this.resistenciaCascos = new int[]{5, 3, 2, 1};
        this.curVidaCasco = new int[]{5, 3, 2, 1};
        
    }

    public int[] getCurVidaCasco() {
        return curVidaCasco;
    }

    public void setCurVidaCasco(int[] curVidaCasco) {
        this.curVidaCasco = curVidaCasco;
    }
    
    
    public int[] getResistenciaCascos() {
        return resistenciaCascos;
    }

    public void setResistenciaCascos(int[] resistenciaCascos) {
        this.resistenciaCascos = resistenciaCascos;
    }
    
    
    
    public void addBala(Bala b){
        this.balas.add(b);
    }

    public ArrayList<Bala> getBalas() {
        return balas;
    }
    
    public void cambiarEstado(boolean[] estado){
        this.estado = estado;
        
        if(this.estado[5]){//subiendo
            //this.numFrames = 1;
            if(this.estado[3]){//dispara
               if(this.estado[2]) setIndex(0,0,0); //sentidoDer
               else setIndex(1,1,1); // sentidoIzq
            } else {
                if(this.estado[2]) setIndex(0,0,0);//sentidoDer
                else setIndex(1,1,1); //sentidoIzq
            }
        }
        
        else if(this.estado[6]){//cayendo
            //this.numFrames = 1;
            if(this.estado[3]){//dispara
               if(this.estado[2]) setIndex(0,0,0); //sentidoDer
               else setIndex(1,1,1); //sentidoIzq
            } else {
                if(this.estado[2]) setIndex(0,0,0); //sentidoDer
                else setIndex(1,1,1); //sentidoIzq
            }
        }
        
        else if(this.estado[4] && !this.estado[3]){//escalera y no disparo
            setIndex(6,4,4);
        }
        
        else if(this.estado[3]){//dispara
            if(!this.estado[4]){//NO escalera
                if(this.estado[0]){//tDer
                    setIndex(2,0,0);
                }
                else if(this.estado[1]){//tIzq
                    setIndex(3,1,1);
                } 
                else {//quieto
                    if(this.estado[2]) setIndex(0,0,0); //sentidoDer
                    else setIndex(1,1,1); //sentidoIzq
                }
            }
            else{ //escalera
                if(this.estado[2]) setIndex(4,2,2); //sentidoDer
                else setIndex(5,3,3); //sentidoIzq
            }
        }
        
        else if(this.estado[0]){//tDer
            setIndex(2,0,0);
        } 
        
        else if(this.estado[1]){//tIzq
            setIndex(3,1,1);
        } 
        
        else{//quieto
            if(this.estado[2]) setIndex(0,0,0);
            else setIndex(1,1,1);
        }
        
        this.numFrames[0] = this.nFrames[0][this.index[0]]; this.numFrames[1] = this.nFrames[1][this.index[1]]; this.numFrames[2] = this.nFrames[2][this.index[2]]; //cambio de numFrames
        this.imagen[0] = this.imagenes[0][this.index[0]]; this.imagen[1] = this.imagenes[1][this.index[1]]; this.imagen[2] = this.imagenes[2][this.index[2]]; //cambio de imagen
        
        
    }
    
    public ArrayList<ArrayList<ArrayList<Double>>> puntos (Image imagen, int numFrames, double resX, double resY){
        double tamFrame = imagen.getWidth()/numFrames;
        ArrayList<ArrayList<ArrayList<Double>>> puntos = new ArrayList<>(numFrames);
        //System.out.println("");
        for(int posF=1; posF<=numFrames; posF++){
            ArrayList<ArrayList<Double>> xy = new ArrayList<>(2);
            ArrayList<Double> izqXY = new ArrayList<>();
            for(int i=(int)imagen.getHeight()-1; i>=0; i-=10){ //lado izquierdo de la imagen. Se puede cambiar el 10 para mayor precision, ideal 1.
                for(int j=0; j<tamFrame; j+=10){
                    if(imagen.getPixelReader().getArgb(j+((int)tamFrame*(posF-1)), i) != 0){
                        izqXY.add((double)j*(resX/1600)/*-tamFrame*(posF-1)*/);
                        if(i <= 12){
                            izqXY.add((double)i*(resX/1600)+2);
                        }
                        else izqXY.add((double)i*(resX/1600));
                        break;
                    }
                }
            }
            xy.add(izqXY);
            ArrayList<Double> derXY = new ArrayList<>();
            for(int i=9; i<imagen.getHeight(); i+=10){ //lado derecho de la imagen. Se puede cambiar el 10 para mayor precision, ideal 1.
                for(int j=(int)tamFrame-1; j>=0; j-=10){
                    if(imagen.getPixelReader().getArgb(j+((int)tamFrame*(posF-1)), i) != 0){
                        derXY.add((double)j*(resX/1600)/*-tamFrame*(posF-1)*/); 
                        if(i  <= 12){
                            derXY.add((double)i*(resX/1600)+2);
                        }
                        else derXY.add((double)i*(resX/1600));
                        break;
                    }
                }
            }
            xy.add(derXY);
            puntos.add(xy);
        }
        return puntos;
    }
    
    public void cambiarPoligono(int[] frame){ //moidificar funcion eliminando el proceso de dibujar
        int tam = 0; 
        // optimizacion, eliminacion de variables
        for(int part=0; part<3; part++){
            int imagen = this.index[part];
            int frame2 = frame[part];
            for(int lado=0; lado<2; lado++){
                tam += this.puntos.get(part).get(imagen).get(frame2).get(lado).size();
            }
        }
        
        double[] xy = new double[tam];
        double[] x = new double[tam/2];
        double[] y = new double[tam/2];
        
        int posCoor = 0;
        
        for(int part=0; part<3; part++){ //lado izq
            int imagen = this.index[part];
            int frame2 = frame[part];
            int lado = 0;
            for(int coor = 0; coor<this.puntos.get(part).get(imagen).get(frame2).get(lado).size(); coor++){
                if(coor%2 == 0){
                    xy[posCoor] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posX;
                    x[posCoor/2] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posX;
                    //System.out.print("[" + this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posX + ",");
                } else {
                    double resta = 0;
                    for(int r=0; r<=part; r++){
                        resta += this.imagen[r].getHeight()*(resX/1600);
                    } if(part == 2){
                        resta -= this.imagen[1].getHeight()/3*(resX/1600);
                        if(coor == this.puntos.get(part).get(imagen).get(frame2).get(lado).size() - 1) resta += 9;
                    }
                    xy[posCoor] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posY - resta;//this.imagen[part].getHeight()*(resX/1600);
                    y[(posCoor-1)/2] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posY - resta;//this.imagen[part].getHeight()*(resX/1600);
                    //System.out.print( this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posY - resta + "]");
                }
                posCoor++;
            }
        }
        
        for(int part=2; part>=0; part--){ //lado der
            int imagen = this.index[part];
            int frame2 = frame[part];
            int lado = 1;
            for(int coor = 0; coor<this.puntos.get(part).get(imagen).get(frame2).get(lado).size(); coor++){
                if(coor%2 == 0){
                    xy[posCoor] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posX;
                    x[posCoor/2] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posX;
                } else {
                    double resta = 0;
                    for(int r=0; r<=part; r++){
                        resta += this.imagen[r].getHeight()*(resX/1600);
                    } if(part == 2) {
                        resta -= this.imagen[1].getHeight()/3*(resX/1600);
                        if(coor == 1) resta+=9;
                    }
                    xy[posCoor] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posY - resta;//this.imagen[part].getHeight()*(resX/1600);
                    y[(posCoor-1)/2] = this.puntos.get(part).get(imagen).get(frame2).get(lado).get(coor) + posY - resta;//this.imagen[part].getHeight()*(resX/1600);
                }
                posCoor++;
            }
        }
        
        this.poligono2 = new Polygon(xy);
    }
    
    public void actualizarPuntosPoligonos(){
        this.puntos = new ArrayList<>();
        this.actualizarIm();
        this.imagen = new Image[personajes.length]; 
        this.imagen[0] = this.imagenes[0][index[0]]; this.imagen[1] = this.imagenes[1][index[1]]; this.imagen[2] = this.imagenes[2][index[2]];
        
        for(int i=0; i<this.imagenes.length; i++){ //parte
            ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> im = new ArrayList<>();
            for(int j=0; j<this.imagenes[i].length; j++){//imagen
                im.add(puntos(this.imagenes[i][j],this.nFrames[i][j],this.resX, this.resY));
            }
            this.puntos.add(im);
        }
    }
    
    public int getVida() {
        return vida;
    }
    
    public void setVida(int val) {
        this.vida += val;
    }
    
    public int[] getPer() {
        return per;
    } 
    
    public int getPer(int i) {
        return per[i];
    }
    public void setPer(int i, int value) {
        per[i] = value;
        actualizarIm();
        imagen[i] = imagenes[0][0];
        actualizarPuntosPoligonos();
    }

    public String[] getPersonajes() {
        return personajes;
    }    
    
    public int[] getIndex() {
        return index;
    }

    public void setIndex(int a, int b, int c) {
        this.index[0] = a; this.index[1] = b; this.index[2] = c;
    }

    public Shape[] getPoligono() {
        return poligono;
    }

    public Shape getPoligono2() {
        return poligono2;
    }
    
    public boolean[] getEstado() {
        return estado;
    }
    
    public void setEstado(int i, boolean b){
        this.estado[i] = b;
    }
    
    public int[] getNumFrames() {
        return numFrames;
    }
    
    public Image[] getImagen() {
        return imagen;
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }
    
    public void addMoneda(){
        this.monedas++;
    }
    
    public void restarMoneda(int val){
        this.monedas -= val;
    }
    
    public void setMoneda(int val){
        this.monedas = val;
    }

    public int getMonedas() {
        return monedas;
    }
    
    public void setMonedas(int monedas){
        this.monedas=monedas;
    }
            
    
    public void actualizarIm(){
        this.imagenes = new Image[][]{
        {//legs
            new Image("image/" + this.personajes[per[0]] + "/leg/normalR.png"),
            new Image("image/" + this.personajes[per[0]] + "/leg/normalL.png"),
            new Image("image/" + this.personajes[per[0]] + "/leg/R.png"),
            new Image("image/" + this.personajes[per[0]] + "/leg/L.png"),
        },{//torsos
            new Image("image/" + this.personajes[per[1]] + "/torso/normalR.png"),
            new Image("image/" + this.personajes[per[1]] + "/torso/normalL.png"),
        },{//heads
            new Image("image/" + this.personajes[per[2]] + "/head/normalR.png"),
            new Image("image/" + this.personajes[per[2]] + "/head/normalL.png"),
        }};
    }
    
    public void actualizarImAc(int i){
        this.imageAccesorio = new Image("image/accesorios/ac"+i+".png");
    }

    public Image getImageAccesorio() {
        return imageAccesorio;
    }

    public void setImageAccesorio(Image imageAccesorio) {
        this.imageAccesorio = imageAccesorio;
    }
    
    
    public void setAdquisicion(int[][] adquisicon) {
        this.adquisicion = adquisicon;
    }

    public void setValores(int[][] valores) {
        this.valores = valores;
    }

    public int[][] getAdquisicion() {
        return adquisicion;
    }

    public int[][] getValores() {
        return valores;
    }

    public void setAccesorios(int[] accesorios) {
        this.accesorios = accesorios;
    }

    public int[] getAccesorios() {
        return accesorios;
    }

    public int[] getValoresAc() {
        return valoresAc;
    }

    public void setValoresAc(int[] valoresAc) {
        this.valoresAc = valoresAc;
    }
    
}
