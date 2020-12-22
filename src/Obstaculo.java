import java.io.*;
import java.util.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
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
public class Obstaculo {
    private double resX;
    //private Image[] imagenes;  
    private int im;
    private Image imagen;
    
    private Image fondo;
    private ArrayList <int[]> puntosParedes;
    private ArrayList <int[]> puntosPisos;
    private ArrayList <int[]> puntosTechos;
    private ArrayList <int[]> escaleras;
    private double posX;
    
    private ArrayList<Shape> poligonosPisos;
    private ArrayList<Shape> poligonosParedes;
    private ArrayList<Shape> poligonosTechos;
    
    private double space;

    public Obstaculo (double posX, int im, double space, double resX) throws FileNotFoundException{ //hacer calculo de puntos en txt previo al inicio del juego
        this.resX = resX;
        this.posX = posX;
        this.im = im;
        this.space = space;
        //System.out.println(this.space);
        //this.imagenes = new Image[] {new Image("image/obstaculos/0" + Math.round(resX) + ".png"), new Image("image/obstaculos/1" + Math.round(resX)+ ".png")};
        this.imagen = new Image("image/obstaculos/" + this.im + Math.round(resX) + ".png");
        //ArrayList<ArrayList<int[]>> puntos = puntos();
        /*
        this.puntosParedes = puntos.get(0);
        this.puntosPisos = puntos.get(1);
        this.puntosTechos = puntos.get(2);*/
        File obs = new File("src/poligonosObstaculos/"+ this.im + Math.round(this.resX) + ".ob");
        
        leer(obs);
        this.poligonosPisos = new ArrayList<>(3);
        this.poligonosParedes = new ArrayList<>(3);
        this.poligonosTechos = new ArrayList<>(3);
    }
    
    private void leer(File f) throws FileNotFoundException{
        Scanner sc = new Scanner(f);
        ArrayList[] poligonos = new ArrayList[3];
        for(int i = 0; i < 3; i++){
            poligonos[i] = new ArrayList<int[]>();
        }
        for(int i = 0; i < 3; i++){
            try{
                while(sc.hasNext()){
                   String s = sc.next();
                   if(s == "n") break;
                   else{
                       int[] arr = new int[4];
                       //System.out.println(s);
                       arr[0] = Integer.valueOf(s);

                       for(int j = 1; j < 4; j++){
                           arr[j] = sc.nextInt();
                       }
                       poligonos[i].add(arr);
                   }
                }
            }catch(NumberFormatException ex){}

        }
        puntosParedes = poligonos[0];
        puntosPisos = poligonos[1];
        puntosTechos = poligonos[2];
        
        
    }
    /*
    private void puntos(){
        ArrayList<int[]> puntosParedes = new ArrayList<>();
        ArrayList<int[]> puntosPisos = new ArrayList<>();
        ArrayList<int[]> puntosTechos = new ArrayList<>();
        ArrayList< Pair <Integer, Integer> > hechos = new ArrayList<>();
        for(int i  = 0; i < imagen.getHeight(); i++){
            int inicio  = 0, fin = (int)imagen.getWidth(), larInicio = -1, larFin = -1;
            boolean color = false;
            for(int j = 0; j < imagen.getWidth(); j++){
                if(hechos.contains(new Pair<>(j,i))) break;
                color = imagen.getPixelReader().getArgb(j,i)  != 0;
                if(color){
                    //punto[0] = j; punto[1] = i;
                    hechos.add(new Pair<>(j, i));
                    
                    inicio = j;
                    
                    //lado
                    boolean color2;
                    for(int k=inicio + 1; k<imagen.getWidth(); k++){
                        hechos.add(new Pair<>(k, i));
                        color2 = imagen.getPixelReader().getArgb(k,i) != 0;
                        if(!color2){
                            fin = k-9;
                            break;
                        }
                    }
                    
                    //abajo incio
                    for(int l=i+1; l<imagen.getHeight(); l++){
                        //punto[0] = inicio; punto[1] = l;
                        hechos.add(new Pair<>(inicio,l));
                        color2 = imagen.getPixelReader().getArgb(inicio,l)  != 0;
                        if(!color2){
                            larInicio = l-i-1;
                            break;
                        }
                    }
                    
                    //abajo fin
                    for(int l=i+1; l<imagen.getHeight(); l++){
                        hechos.add(new Pair<>(fin,l));
                        color2 = imagen.getPixelReader().getArgb(fin,l)  != 0;
                        if(!color2){
                            larFin = l-i-1;
                            break;
                        }
                    }
                    int[] paredInicio = {(int)inicio,(int)i+8,(int)8,(int)larInicio-8};
                    int[] paredFin = {(int)fin,(int)i+8,(int)8,(int)larFin-8};
                    
                    int[] piso = {(int)inicio+1,(int)i,(int)fin-inicio+8-2,(int)8};
                    int[]techo = {(int)inicio + 8,(int)(i+larInicio-8),(int)fin-inicio+8-16,8};
                    
                    puntosParedes.add(paredInicio);
                    puntosParedes.add(paredFin);
                    puntosPisos.add(piso);
                    puntosTechos.add(techo);
                }
            }
            
        }
        ArrayList<ArrayList<int[]>> puntos = new ArrayList<>(3);
        this.puntosParedes = puntosParedes;
        this.puntosPisos = puntosPisos;
        this.puntosTechos = puntosTechos;
    }
    */

    public double getSpace() {
        return space;
    }
    
    public void cambiarPoligonos(GraphicsContext lapiz){
        this.poligonosParedes = null; this.poligonosParedes = new ArrayList<>();
        this.poligonosPisos= null; this.poligonosPisos = new ArrayList<>();
        this.poligonosTechos = null; this.poligonosTechos = new ArrayList<>();
        
        for(int i=0; i<this.puntosParedes.size(); i++){
            setPared(i,this.puntosParedes.get(i),lapiz);
        }
        for(int j=0; j<this.puntosPisos.size(); j++){
            setPiso(j,this.puntosPisos.get(j),lapiz);
        }
        for(int k=0; k<this.puntosTechos.size(); k++){
            setTecho(k,this.puntosTechos.get(k),lapiz);
        }
    }
    
    public void setPared(int i, int[] arr, GraphicsContext lapiz){
        Shape s = new Rectangle(arr[0] + this.posX, arr[1], arr[2], arr[3]);
        this.poligonosParedes.add(i,s);
        lapiz.setFill(Color.RED);
        //lapiz.fillRect(arr[0] + this.posX, arr[1], arr[2], arr[3]);
    }

    public void setPiso(int i, int[] arr,GraphicsContext lapiz){
        Shape s = new Rectangle(arr[0] + this.posX, arr[1], arr[2], arr[3]);
        this.poligonosPisos.add(i,s);
        lapiz.setFill(Color.BLUE);
        //lapiz.fillRect(arr[0]+this.posX, arr[1], arr[2], arr[3]);
    }
    
    public void setTecho(int i, int[] arr,GraphicsContext lapiz){
        Shape s = new Rectangle(arr[0] + this.posX, arr[1], arr[2], arr[3]);
        this.poligonosTechos.add(i,s);
        lapiz.setFill(Color.YELLOW);
        //lapiz.fillRect(arr[0]+this.posX, arr[1], arr[2], arr[3]);
    }
    
    public ArrayList<Shape> getPoligonosParedes() {
        return poligonosParedes;
    }

    public ArrayList<Shape> getPoligonosPisos() {
        return poligonosPisos;
    }    

    public ArrayList<Shape> getPoligonosTechos() {
        return poligonosTechos;
    }
    
    public Image getImagen() {
        return imagen; 
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }
    
    
}
