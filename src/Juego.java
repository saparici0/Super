import com.sun.javafx.application.HostServicesDelegate;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.max;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


public class Juego extends AnimationTimer{
    //private  Scene escena;
    private GraphicsContext lapiz;
    private ArrayList<String> teclado;
    private double resX;
    private double resY;
    private Mapa mapa;
    private ArrayList<String> opciones;
    private int seleccion = 0;
    private boolean seleccionado;
    private Image fondo;
    private Image piso;
    private int[] secuencia;
    private int numero;
    private Personaje batman;
    private ArrayList <Enemigo> enemigos;
    private double movX ;
    private double movY;
    private double caida;
    private ArrayList<Obstaculo> obstaculos = null; 
    private long distance;
    private long distMax;
    private boolean choqueDer;
    private boolean choqueIzq;
    private ArrayList<Bala> balas = new ArrayList<>();
    private ArrayList<Moneda> monedas = new ArrayList<>();
    private ArrayList<Corazon> corazones = new ArrayList<>();
    private ArrayList<Moneda> monedasEn = new ArrayList<>();    
    private final Image monedaC = new Image("image/monedaC.png");
    private int monedasG;
    private boolean tecPresionadas[]=new boolean[7]; //esp, up, down, right, left, enter, escape
    private int tiendaSelec;
    private int YNpub;
    private boolean pub;
    private HostServicesDelegate hostServices;
    private File playerInfo = new File("src/info/player.super");
    private File customInfo = new File("src/info/custom.super");
    private File tempInfo= new File("src/info/temp.super"); 
    private ArrayList<String> opciones2;
    private String resolucion;//opcion inicial de la resolucion
    private String fondoSelec="CIUDAD";//opcion inicial del fondo
    private boolean seleccionado2;
    private int seleccion2=0;//seleccion menu de resolucion
    private boolean solicitudCompra = false;
    private int[] curPer = new int[3];
    private int curAc = 0;
    private String[] gris = {"", "", ""};
    
    public Juego(Scene escena, GraphicsContext lapiz, double resX, double resY) {        
        this.lapiz = lapiz;
        //this.escena = escena;
        this.resX = resX;
        this.resY = resY;
        
        this.secuencia = new int[3];
        this.distance = 0;
        
        //mapa
        this.mapa = new Mapa(new Random().nextInt(), 1, 22, this.resX);//key, map, numero de obstaculos disponibles, resX
        this.piso = mapa.getPiso();
        this.fondo = mapa.getFondo();
        
        this.monedas.add(new Moneda(400, this.resY - this.resY/15 - 50*1600/this.resX, this.resX));
        this.corazones.add(new Corazon(2.5*resX, this.resY - this.resY/15 - 50*1600/this.resX, this.resX));
        
        this.batman =  new Personaje(100,resY-resY/15 + 0.1 -((resX/4000)*40*(40+1)),this.resX, this.resY);//primer dibujo personaje, se cambia el 40 para cambiar la distancia el salto inicial
        
        readInfo();
        readInfoCustom();
        this.enemigos  = new ArrayList<>();
        this.enemigos.add(new Enemigo(0, resX*3/4 , resY  - resY/15 - 150*(resX/1600), 1, resX));
        int[] sec = {0,0,0};
        this.batman.cambiarPoligono(sec);
        
        this.obstaculos = new ArrayList<>();
        
        resolucion=Integer.toString((int)resX);
	opciones2=new ArrayList<>();//opciones de resolucion y fondo
        opciones2.add("RESOLUCION");
        opciones2.add("FONDO");
        
        try {
            this.obstaculos.add(new Obstaculo(1000,1,resX/3,resX));
        } catch (FileNotFoundException ex) {
            System.out.println();
        }
            
        teclado = new ArrayList<>();
        opciones = new ArrayList<>();
        
        opciones.add("NUEVO  JUEGO");
        opciones.add("CONTINUAR  JUEGO");
        opciones.add("TIENDA");
        opciones.add("OPCIONES");
        opciones.add("SALIR");
        
        //ARCADECLASSIC.TTF
        lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/15));
        lapiz.setTextAlign(TextAlignment.CENTER);
        lapiz.setFill(Color.WHITE);
        
        lapiz.setStroke(Color.CYAN);
        
        
        escena.setOnKeyPressed(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    if ( !teclado.contains(code) )
                        teclado.add( code );
                }
            });

        escena.setOnKeyReleased(
            new EventHandler<KeyEvent>()
            {
                public void handle(KeyEvent e)
                {
                    String code = e.getCode().toString();
                    teclado.remove( code );
                }
            });
        
    }
    
    @Override
    public void handle(long now){
        this.numero++;
        this.numero %= resX+1;
        movX %= resX*3;

        //limpieza
        lapiz.clearRect(0, 0, resX, resY);
        
        if(!seleccionado){
            lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/15));
            lapiz.setTextAlign(TextAlignment.CENTER);
            
            if (teclado.contains("UP")){
                this.tecPresionadas[1] = true;
            }else if(this.tecPresionadas[1]){
                this.tecPresionadas[1] = false;
                this.seleccion += 4;
                this.seleccion %= 5;
            }
            if (teclado.contains("DOWN")){
                this.tecPresionadas[2] = true;
            }else if(this.tecPresionadas[2]){
                this.tecPresionadas[2]=false;
                this.seleccion++;
                this.seleccion %= 5;
            }
            if (teclado.contains("ENTER")){
                this.tecPresionadas[5] = true;
                
            }else if(tecPresionadas[5]){
                this.seleccionado = true;  
                this.tecPresionadas[5] = false;
            }
            
            if(this.seleccion < 0) this.seleccion = opciones.size() - 1;

            this.seleccion %= opciones.size();

            for(int i=0; i<opciones.size(); i++){
                if (i==this.seleccion){
                    lapiz.setFill(Color.RED);
                    lapiz.fillRoundRect(resX/2 - (opciones.get(i).length()*0.6)*(resY/15)/2, resY/(opciones.size()+1)*(i+1) - resY/15 + resY/75, (opciones.get(i).length()*0.6)*(resY/15), resY/15,4*resY/75,4*resY/75);
                    lapiz.setFill(Color.WHITE);
                }
                lapiz.fillText(opciones.get(i),resX/2, resY/(opciones.size()+1)*(i+1));
            }
        } else if(this.seleccion == 0){
            this.movX = 0;
            this.batman =  new Personaje(100,resY-resY/15 + 0.1 - ((resX/4000)*40*(40+1)),this.resX, this.resY);//primer dibujo personaje, se cambia el 40 para cambiar la distancia el salto inicial
            this.batman.actualizarIm();
            readInfoCustom();
            readInfo();
            this.distance = 0;
            this.distMax = 0;
            this.seleccion = 1;
            reiniciarJuego();
        }else if(this.batman.getVida() <= 0 && seleccion == 1){                
            lapiz.clearRect(0, 0, resX, resY);
            lapiz.setFill(Color.WHITE);
            lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF", resX/20));
            lapiz.setTextAlign(TextAlignment.CENTER);
            lapiz.fillText("GAME  OVER", resX/2, resY/2);
            //puntaje
            lapiz.setFill(Color.WHITE);
            this.distMax  = max(this.distMax, this.distance);
            String points = (int)(this.distMax/100) + "";
            lapiz.fillText(points, this.resX/2, resX/24);
            if(teclado.contains("ESCAPE")) seleccionado = false;
        } else if (this.seleccion == 1 ) {
            if(teclado.contains("ESCAPE")) seleccionado = false;
            
            //----------------------------NIVEL DEL JUEGO----------------------------------------
            //mueve la secuencia de movimiento cada 8 loops.
            if(this.numero % 8 == 0){
                this.secuencia[0]++; this.secuencia[1]++; this.secuencia[2]++;
            }
            this.secuencia[0] %= this.batman.getNumFrames()[0];
            this.secuencia[1] %= this.batman.getNumFrames()[1];
            this.secuencia[2] %= this.batman.getNumFrames()[2];
            
            boolean[] estado = {false,false,batman.getEstado()[2],false,false,false,false,false,false};
            
            //dibujo del fondo
            lapiz.drawImage(fondo,movX,0,resX-movX,resY,0,0,resX-movX,resY);
            lapiz.drawImage(fondo,0,0,movX,resY,resX-movX,0,movX,resY);
            lapiz.drawImage(fondo,0,0,movX,resY,resX*2-movX,0,movX,resY);
            lapiz.drawImage(fondo,0,0,movX,resY,resX*3-movX,0,movX,resY); 

            //piso
            Shape superficie = new Rectangle(0,resY-resY/15,resX,resY/15);
            for(int i=0; i<resX; i+=piso.getWidth()){
                lapiz.drawImage(piso, movX, 0, piso.getWidth(), piso.getHeight(), i, resY - resY/15, piso.getWidth()-movX, resY/15);
            }
            for(double i=piso.getWidth()-movX; i<resX; i+=piso.getWidth()){
                lapiz.drawImage(piso, 0, 0, movX, piso.getHeight(), i, resY - resY/15, movX, resY/15);
            }
            
            //personaje
            this.batman.cambiarPoligono(this.secuencia); //CAMBIO
            
            double[] tamFrame = {this.batman.getImagen()[0].getWidth()/this.batman.getNumFrames()[0], this.batman.getImagen()[1].getWidth()/this.batman.getNumFrames()[1], this.batman.getImagen()[2].getWidth()/this.batman.getNumFrames()[2]};
            for(int part=0; part<3; part++){
                double resta = 0;
                for(int r=0; r<=part; r++){
                    resta += this.batman.getImagen()[r].getHeight()*(resX/1600);
                } if(part == 2) resta -= this.batman.getImagen()[1].getHeight()/3*(resX/1600);
                lapiz.drawImage(this.batman.getImagen()[part], this.secuencia[part]*tamFrame[part], 0, tamFrame[part], this.batman.getImagen()[part].getHeight(),batman.getPosX(),this.batman.getPosY()- resta,tamFrame[part]*(resX/1600),this.batman.getImagen()[part].getHeight()*(resX/1600));
            }
            //dibuja los accesorios
            for(int i = 0; i < 4; i++){
                if(this.batman.getAccesorios()[i] >= 1){
                    double resta = 160*(resX/1600);
                    if(this.batman.getEstado()[2])
                        lapiz.drawImage(new Image("image/accesorios/ac" + i + ".png"), batman.getPosX(),this.batman.getPosY()- resta, 130*(resX/1600), 160*(resX/1600));
                    else 
                        lapiz.drawImage(new Image("image/accesorios/ac" + i + "L.png"), batman.getPosX(),this.batman.getPosY()- resta, 130*(resX/1600), 160*(resX/1600));
                }
            }
            
            //enemigos
            //creacion
            while(enemigos.get(enemigos.size() - 1).getPosX() + 3*resX/2 < obstaculos.get(obstaculos.size() - 1).getPosX()) {
                int r = new Random().nextInt((int) resX/2);
                Enemigo e = new Enemigo(new Random().nextInt(10), enemigos.get(enemigos.size() - 1).getPosX() + resX + r, posY(enemigos.get(enemigos.size() - 1).getPosX() + resX + r) - 160*resX/1600, 1 + (int) this.distMax/10000, this.resX);
                e.setFrec(e.getFrec() + (int) this.distMax /100);
                enemigos.add(e);
            }
            
            //giro y verificacion de distancia con el jugador para eliminar o imprimir
            for(int e=0; e<this.enemigos.size(); e++){
                if(enemigos.get(e).getPosX() < batman.getPosX()) enemigos.get(e).setDer(true);
                else enemigos.get(e).setDer(false);
                if(numero / 60 % (1 + (int) 5/enemigos.get(e).getFrec()) == 0 && (double)numero/60.0 == (int)numero/60){ //dispara con la frecuencia del enemigo
                    Bala b = new Bala(enemigos.get(e).getPosX(), enemigos.get(e).getPosY() + 100*resX/1600, enemigos.get(e).getPosX() < batman.getPosX(), resX, 0, enemigos.get(e).getVelBala());
                    enemigos.get(e).addBala(b); //agrega una bala al enemigo
                }
                if(enemigos.get(e).getPosX() < batman.getPosX() - 2*resX - 100) this.enemigos.remove(e--); //elimina las enemigos si estan muy lejos del personaje
                else{
                    this.enemigos.get(e).cambiarPoligono();
                    lapiz.drawImage(this.enemigos.get(e).getImagen(),this.enemigos.get(e).getPosX(),this.enemigos.get(e).getPosY(), this.enemigos.get(e).getImagen().getWidth()*(resX/1600), this.enemigos.get(e).getImagen().getHeight()*(resX/1600));
                } 
            }
            
            //monedas
            //creacion
            while(monedas.get(monedas.size() - 1).getPosX() + 5*resX/2 < obstaculos.get(obstaculos.size() - 1).getPosX()) {
                monedas.add(new Moneda(monedas.get(monedas.size() - 1).getPosX() + resX + new Random().nextInt((int) resX/2), posY(monedas.get(monedas.size() - 1).getPosX() + resX) - monedas.get(0).getImagen().getHeight() - 10, this.resX));
            }
            if(distMax % 10000 == 0 && distMax != 0) {
                corazones.add(new Corazon(this.batman.getPosX() + resX + new Random().nextInt((int) resX), posY(this.batman.getPosX() + new Random().nextInt((int) resX)) - 39*resX/1600, this.resX));
            }
            
            //verificacion de colisiones, recoleccion, dibujo de monedas
            for(int m=0; m<this.monedas.size(); m++){
                if(monedas.get(m).getPosX() < batman.getPosX() - 2*resX - 100) this.monedas.remove(m--); //elimina las monedas si estan muy lejos del personaje
                else if(SVGPath.intersect(this.batman.getPoligono2(),monedas.get(m).getPoligono()).getBoundsInLocal().getWidth()!= -1){
                    this.batman.addMoneda(); this.monedas.remove(m--);
                    this.monedasG++;
                    try {
                        saveInfo(playerInfo);
                    } catch (FileNotFoundException ex) {
                        System.out.println();
                    }
                }
                else lapiz.drawImage(this.monedas.get(m).getImagen(),this.monedas.get(m).getPosX(),this.monedas.get(m).getPosY()); //dibujo de monedas
            }
            
            //verificacion de colisiones, recoleccion, dibujo de monedas de los enemigos
            for(int m=0; m<this.monedasEn.size(); m++){
                if(monedasEn.get(m).getPosX() < batman.getPosX() - 2*resX - 100) this.monedasEn.remove(m--); //elimina las monedasEn si estan muy lejos del personaje
                else if(SVGPath.intersect(this.batman.getPoligono2(),monedasEn.get(m).getPoligono()).getBoundsInLocal().getWidth()!= -1){
                    this.batman.addMoneda(); this.monedasEn.remove(m--);
                    this.monedasG++;
                    try {
                        saveInfo(playerInfo);
                    } catch (FileNotFoundException ex) {
                        System.out.println();
                    }
                }
                else lapiz.drawImage(this.monedasEn.get(m).getImagen(),this.monedasEn.get(m).getPosX(),this.monedasEn.get(m).getPosY()); //dibujo de monedas
            }
            
            //verificacion de colisiones, recoleccion, dibujo de Corazones
            for(int m=0; m<this.corazones.size(); m++){
                if(corazones.get(m).getPosX() < batman.getPosX() - 2*resX - 100) this.corazones.remove(m--); //elimina las corazones si estan muy lejos del personaje
                else if(SVGPath.intersect(this.batman.getPoligono2(),corazones.get(m).getPoligono()).getBoundsInLocal().getWidth()!= -1){
                    this.batman.setVida(1); this.corazones.remove(m--);
                    try {
                        saveInfo(playerInfo);
                    } catch (FileNotFoundException ex) {
                        System.out.println();
                    }
                }
                else lapiz.drawImage(this.corazones.get(m).getImagen(),this.corazones.get(m).getPosX(),this.corazones.get(m).getPosY()); //dibujo de corazones
            }
            
            //revisar colisiones piso base
            boolean impacto = false;
            boolean colision = false;
            boolean choque = false;
            double area = SVGPath.intersect(this.batman.getPoligono2(),superficie).getBoundsInLocal().getHeight(); //CAMBIO
            if(area != -1){
                colision = true;
                caida = 0;
                estado[6] = false; //cayendo = false
                batman.setPosY(batman.getPosY() - (int)area);
            }
            // ciclo pisos
            
            //creacion de obstaculos
            while (this.obstaculos.size() < 8) this.obstaculos.add(this.mapa.nextObstaculo(obstaculos.get(obstaculos.size() - 1).getPosX() + obstaculos.get(obstaculos.size() - 1).getImagen().getWidth() + obstaculos.get(obstaculos.size() - 1).getSpace()));
                
            // obstaculos
            for(int ob=0; ob<this.obstaculos.size(); ob++){
                if(this.obstaculos.get(ob).getPosX() < batman.getPosX() - 2*resX - 100) this.obstaculos.remove(ob); //elimina los obstculos si estan muy lejos del personaje
                else {
                    lapiz.drawImage(this.obstaculos.get(ob).getImagen(),0,0,this.obstaculos.get(ob).getImagen().getWidth(), this.obstaculos.get(ob).getImagen().getHeight(), this.obstaculos.get(ob).getPosX(),0,this.obstaculos.get(ob).getImagen().getWidth(),this.resY);
                    this.obstaculos.get(ob).cambiarPoligonos(lapiz);
                }
            }
            
            for(int ob=0; ob<this.obstaculos.size(); ob++){
                if(abs(this.obstaculos.get(ob).getPosX() - batman.getPosX()) < resX){
                    for(int k=0; k<this.obstaculos.get(ob).getPoligonosPisos().size(); k++){
                        area = SVGPath.intersect(this.batman.getPoligono2(),obstaculos.get(ob).getPoligonosPisos().get(k)).getBoundsInLocal().getHeight(); //CAMBIO
                        if(area != -1) {//revisa la altura del area de la interseccion entre batman y las superficies
                            colision = true;
                            caida = 0;
                            estado[6] = false; //cayendo = true
                            batman.setPosY(batman.getPosY() - (int)area);
                            break;
                        }
                    }

                    //ciclos techos
                    for(int k=0; k<this.obstaculos.get(ob).getPoligonosTechos().size(); k++){
                        area = SVGPath.intersect(this.batman.getPoligono2(),obstaculos.get(ob).getPoligonosTechos().get(k)).getBoundsInLocal().getHeight(); //CAMBIO
                        if(area != -1) {//revisa la altura del area de la interseccion entre batman y las superficies
                            impacto = true;
                            //caida = 0;
                            estado[6] = true; //cayendo = true
                            estado[5] = false; //subiendo = false
                            this.movY = 0;
                            break;
                        }
                    }

                    //ciclo choques horizontales
                    for(int k=0; k<obstaculos.get(ob).getPoligonosParedes().size(); k++){
                        //calcular una unica vez el area
                        area = SVGPath.intersect(this.batman.getPoligono2(),obstaculos.get(ob).getPoligonosParedes().get(k)).getBoundsInLocal().getWidth(); //CAMBIO
                        if(area != -1) {//revisa el ancho del area de la interseccion entre batman y las paredes
                            choque = true;
                            if(batman.getEstado()[2]) {
                                choqueDer = true;
                                choqueIzq = false;
                                this.batman.setPosX(batman.getPosX() - (int)area + 1);
                            }
                            else {
                                choqueIzq = true;
                                choqueDer = false;
                                this.batman.setPosX(batman.getPosX() + (int)area - 1);
                            }
                            break;
                        }                    
                    }
                }
                
                if(choque) break;
            }
             
            if(!choque){
                choqueDer = false;
                choqueIzq = false;
            }
            
            //revision balas, eliminacion si colisionan o salen de la pantalla.
            
            //Para las balas del personaje
            for(int i=0; i<batman.getBalas().size(); i++){
                if(batman.getBalas().get(i).getPosX() > this.resX || batman.getBalas().get(i).getPosX() < 0){
                    batman.getBalas().remove(i--);
                } else{
                    boolean b = true;
                    for(int j=0; j < this.obstaculos.size() && b; j++){//itera en los obstaculos
                        if(abs(this.batman.getBalas().get(i).getPosX() - this.obstaculos.get(j).getPosX()) < this.resX){
                            for( int k=0; k < this.obstaculos.get(j).getPoligonosParedes().size(); k++){ //los poligonos paredes del obstaculo
                                if(this.obstaculos.get(j).getPoligonosParedes().get(k).contains(this.batman.getBalas().get(i).getPosX(), this.batman.getBalas().get(i).getPosY()) ||
                                    this.obstaculos.get(j).getPoligonosParedes().get(k).contains(this.batman.getBalas().get(i).getPosX() + this.batman.getBalas().get(i).getImagen().getWidth(), this.batman.getBalas().get(i).getPosY())){
                                    batman.getBalas().remove(i--);
                                    b = false;
                                    break;
                                }
                            }
                        }
                        
                    }
                    if(b && this.batman.getBalas().size() > 0){
                        for(int e = 0; e < this.enemigos.size(); e++){//itera en los enemigos
                            if(abs(this.batman.getBalas().get(i).getPosX() - this.enemigos.get(e).getPosX()) < this.resX){
                                if(this.enemigos.get(e).getPoligono().contains(this.batman.getBalas().get(i).getPosX(), this.batman.getBalas().get(i).getPosY()) ||
                                    this.enemigos.get(e).getPoligono().contains(this.batman.getBalas().get(i).getPosX() + this.batman.getBalas().get(i).getImagen().getWidth(), this.batman.getBalas().get(i).getPosY())){
                                    this.enemigos.get(e).setVida(this.batman.getBalas().get(i).getDamage());
                                    this.batman.getBalas().remove(i--);
                                    if(this.enemigos.get(e).getVida() <= 0){
                                        this.monedasEn.add(new Moneda(this.enemigos.get(e).getPosX() + 50*resX/1600 - 30*resX/1600, this.enemigos.get(e).getPosY(), resX));
                                        this.enemigos.remove(e--);
                                    }
                                    break;
                                }
                            }

                        }
                    }                    
                }
            }
            //Para las balas de los enemigos
            
            boolean estoyVivo = true;
            for(int e = 0; e < enemigos.size(); e++){
                for(int i=0; i<enemigos.get(e).getBalas().size(); i++){
                    if(enemigos.get(e).getBalas().get(i).getPosX() > this.resX || enemigos.get(e).getBalas().get(i).getPosX() < 0){
                        enemigos.get(e).getBalas().remove(i--);
                    } else{
                        boolean b = true;
                        for(int j=0; j < this.obstaculos.size() && b; j++){//itera en los obstaculos
                            if(abs(this.enemigos.get(e).getBalas().get(i).getPosX() - this.obstaculos.get(j).getPosX()) < this.resX){
                                for( int k=0; k < this.obstaculos.get(j).getPoligonosParedes().size(); k++){ //los poligonos paredes del obstaculo
                                    if(this.obstaculos.get(j).getPoligonosParedes().get(k).contains(this.enemigos.get(e).getBalas().get(i).getPosX(), this.enemigos.get(e).getBalas().get(i).getPosY()) ||
                                        this.obstaculos.get(j).getPoligonosParedes().get(k).contains(this.enemigos.get(e).getBalas().get(i).getPosX() + this.enemigos.get(e).getBalas().get(i).getImagen().getWidth(), this.enemigos.get(e).getBalas().get(i).getPosY())){
                                        enemigos.get(e).getBalas().remove(i--);
                                        b = false;
                                        break;
                                    }
                                }
                            }
                            if(b && abs(this.enemigos.get(e).getBalas().get(i).getPosX() - this.batman.getPosX()) < this.resX){
                                if(this.batman.getPoligono2().contains(this.enemigos.get(e).getBalas().get(i).getPosX(), this.enemigos.get(e).getBalas().get(i).getPosY()) ||
                                    this.batman.getPoligono2().contains(this.enemigos.get(e).getBalas().get(i).getPosX() + this.enemigos.get(e).getBalas().get(i).getImagen().getWidth(), this.enemigos.get(e).getBalas().get(i).getPosY())){
                                    boolean recibeDamage = true;
                                    for(int ac = 3; ac>=0; ac--){
                                        if(this.batman.getAccesorios()[ac] >= 1){
                                            recibeDamage = false;
                                            
                                            this.batman.getCurVidaCasco()[ac] -= this.enemigos.get(e).getBalas().get(i).getDamage();
                                            if(this.batman.getCurVidaCasco()[ac] <= 0){
                                                this.batman.getCurVidaCasco()[ac] = this.batman.getResistenciaCascos()[ac];
                                                this.batman.getAccesorios()[ac]--;
                                            }
                                            try {
                                                saveInfoCustom(customInfo);
                                            } catch (FileNotFoundException ex) {
                                            }
                                            this.enemigos.get(e).getBalas().remove(i--);
                                            b = true;
                                            break;
                                        }
                                    }
                                    if(recibeDamage){
                                        this.batman.setVida(-this.enemigos.get(e).getBalas().get(i).getDamage());
                                        this.enemigos.get(e).getBalas().remove(i--);
                                        b = true;
                                    }                                    
                                    break;
                                }
                            }

                        }     
                    }
                }
            }
            
            //Dibuja las balas
            //Para las balas del personaje
            for(int i  = 0; i < this.batman.getBalas().size(); i++){
                lapiz.drawImage(this.batman.getBalas().get(i).getImagen(), this.batman.getBalas().get(i).getPosX() /*+ (this.batman.getBalas().get(i).isDir() ? 1 : 0)*110*resX/1600*/, this.batman.getBalas().get(i).getPosY());
                if(this.batman.getBalas().get(i).isDir()) this.batman.getBalas().get(i).setPosX(this.batman.getBalas().get(i).getPosX() + this.batman.getBalas().get(i).getVel()*resX/1600);
                else this.batman.getBalas().get(i).setPosX(this.batman.getBalas().get(i).getPosX() - this.batman.getBalas().get(i).getVel()*resX/1600);
            }
            
            //Para las balas de los enemigos
            for(int e = 0; e < enemigos.size(); e++){
                for(int i  = 0; i < this.enemigos.get(e).getBalas().size(); i++){
                    lapiz.drawImage(this.enemigos.get(e).getBalas().get(i).getImagen(), this.enemigos.get(e).getBalas().get(i).getPosX() + (this.enemigos.get(e).getBalas().get(i).isDir() ? 1 : 0)*110*resX/1600, this.enemigos.get(e).getBalas().get(i).getPosY());
                    if(this.enemigos.get(e).getBalas().get(i).isDir()) this.enemigos.get(e).getBalas().get(i).setPosX(this.enemigos.get(e).getBalas().get(i).getPosX() + this.enemigos.get(e).getBalas().get(i).getVel()*resX/1600);
                    else this.enemigos.get(e).getBalas().get(i).setPosX(this.enemigos.get(e).getBalas().get(i).getPosX() - this.enemigos.get(e).getBalas().get(i).getVel()*resX/1600);
                }
            }
            
            //La vida de nuestro personaje
            int corazones = 2;
            for(; corazones <= this.batman.getVida(); corazones += 2) lapiz.drawImage(new Image("image/corazon.png"), (100 + corazones*50)*resX/1600, 20*resX/1600);
            if(corazones - this.batman.getVida() == 1) lapiz.drawImage(new Image("image/mediocorazon.png"),  (100 + corazones*50)*resX/1600, 20*resX/1600);
            
            //cambio del estado del personaje por las flechas
            if (teclado.contains("RIGHT")){
                estado[0] = true;
                estado[2] = true;//sentidoDer
                if(this.batman.getPosX() < resX/2 - tamFrame[0]*(resX/1600) || movX < 0){
                    //movimiento horizontal
                    if(this.batman.getPosX() <= resX && !choqueDer) {
                       this.batman.setPosX(this.batman.getPosX()+ (resX/200));
                       this.distance += resX/200;
                    }
                } else{
                    movX += (resX/200);
                    this.distance += resX/200;
                    for(int ob=0; ob<this.obstaculos.size(); ob++) this.obstaculos.get(ob).setPosX(this.obstaculos.get(ob).getPosX() - resX/200);
                    for(int en=0; en<this.enemigos.size(); en++) this.enemigos.get(en).setPosX(this.enemigos.get(en).getPosX() - resX/200);
                    for(int m=0; m<this.monedas.size(); m++) this.monedas.get(m).setPosX(this.monedas.get(m).getPosX() - resX/200);
                    for(int m=0; m<this.monedasEn.size(); m++) this.monedasEn.get(m).setPosX(this.monedasEn.get(m).getPosX() - resX/200);
                    for(int m=0; m<this.corazones.size(); m++) this.corazones.get(m).setPosX(this.corazones.get(m).getPosX() - resX/200);
                    //for(int ba=0; ba<this.batman.getbatman.getBalas()().size(); ba++) this.batman.getbatman.getBalas()().get(ba).setPosX(this.batman.getbatman.getBalas()().get(ba).getPosX() - resX/200);
                    for(int b=0; b<this.batman.getBalas().size(); b++) if(!batman.getBalas().get(b).isDir()) this.batman.getBalas().get(b).setPosX(this.batman.getBalas().get(b).getPosX() - resX/200);                    
                    for(int i = 0; i < this.enemigos.size(); i++){
                        for(int b=0; b<this.enemigos.get(i).getBalas().size(); b++){
                            if(!enemigos.get(i).getBalas().get(b).isDir()){
                                this.enemigos.get(i).getBalas().get(b).setPosX(this.enemigos.get(i).getBalas().get(b).getPosX() - resX/200);
                            }
                        }
                    }
                    
                } 
            }
            if (teclado.contains("LEFT")){
                estado[1] = true;
                estado[2] = false;//sentidoIzq 
                //movimiento horizontal
                if(this.batman.getPosX() > resX/4 - tamFrame[0]*(resX/1600) || movX <= 0){
                    if(this.batman.getPosX() >= 0 && !choqueIzq) {
                        this.batman.setPosX(this.batman.getPosX()-(resX/200));
                        this.distance -= resX/200;
                    }
                } else if (movX >= (resX/200)) {
                    movX -= (resX/200);
                    this.distance -= (resX/200);
                    for(int ob=0; ob<this.obstaculos.size(); ob++) this.obstaculos.get(ob).setPosX(this.obstaculos.get(ob).getPosX() + resX/200);
                    for(int en=0; en<this.enemigos.size(); en++) this.enemigos.get(en).setPosX(this.enemigos.get(en).getPosX() + resX/200);
                    for(int m=0; m<this.monedas.size(); m++) this.monedas.get(m).setPosX(this.monedas.get(m).getPosX() + resX/200);
                    for(int m=0; m<this.monedasEn.size(); m++) this.monedasEn.get(m).setPosX(this.monedasEn.get(m).getPosX() + resX/200);
                    for(int m=0; m<this.corazones.size(); m++) this.corazones.get(m).setPosX(this.corazones.get(m).getPosX() + resX/200);
                    //for(int b=0; b<this.balas.size(); b++) if(balas.get(b).isDir()) this.balas.get(b).setPosX(this.balas.get(b).getPosX() - resX/200);
                }   
            }
            
            //disparo y creacion de balas
            if(teclado.contains("SPACE")){
                this.tecPresionadas[0] = true;
                estado[3] = true;
            } else {
                if(this.tecPresionadas[0]) {  
                    int tipo = 0;
                    if(this.batman.getAccesorios()[4] == 1){
                        if(this.batman.getAccesorios()[5] == 1) tipo = (int)(Math.random()*2) + 1;
                        else tipo = 1;
                    }
                    else if(this.batman.getAccesorios()[2] == 1){
                        tipo = (int)(Math.random()*1);
                        if(tipo == 1) tipo++;
                    }
                    if(estado[2]) {
                        int au = 8;
                        if(this.tecPresionadas[3] || this.tecPresionadas[4]) au += resX/200;
                        
                        batman.addBala(new Bala(this.batman.getPosX()+130*(resX/1600),this.batman.getPosY()-60*(resX/1600), estado[2], this.resX, tipo,au));
                    } else {
                        int au = 8;
                        if(this.tecPresionadas[3] || this.tecPresionadas[4]) au += resX/200;
                        batman.addBala(new Bala(this.batman.getPosX()-20*(resX/1600), this.batman.getPosY()-60*(resX/1600),estado[2],this.resX,tipo,au));
                    }
                }
                this.tecPresionadas[0] = false;
                estado[3] = false;
            }
             
            if (teclado.contains("UP")){         
                estado[7] = true;
                if(colision) movY = 160*(resX/1600);
            }
            
            if (teclado.contains("DOWN")) estado[8] = true;

            //movimiento vertical
            if(movY>0){
                movY -= (resX/200);
                this.batman.setPosY(this.batman.getPosY()-movY/10);
                batman.setEstado(5, true);
                estado[5] = true;
            } else {
                estado[5] = false;
                batman.setEstado(5, false);
                if(!colision && movY == 0){
                    caida += (resX/200);
                    batman.setPosY(batman.getPosY()+caida/10);
                    estado[6] = true;
                    batman.setEstado(6,true);
                } else {
                    estado[6] = false;
                    batman.setEstado(6, false);
                }
            }
            batman.cambiarEstado(estado);
            
            //puntaje
            lapiz.setFill(Color.WHITE);
            this.distMax  = max(this.distMax, this.distance);
            String points = (int)(this.distMax/100) + "";
            lapiz.fillText(points, this.resX/2, resX/24);
            
            //si se llega a puntaje alto se cambia de mapa y personaje
            if(this.distMax/100 == 100) { //reemplaze el termino antes de *100 por el puntaje que quiere
                lapiz.clearRect(0, 0, resX, resY);
                lapiz.setFill(Color.WHITE);
                //lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF", resX/10));
                lapiz.setTextAlign(TextAlignment.CENTER);
                lapiz.fillText("WOW!", resX/2, resY/2);
                this.distMax+=50;
                if(this.mapa.getMap() == 0) {
                    this.mapa = new Mapa(this.mapa.getKey(),1,this.mapa.getNumObs(),this.resX);
                }
                if(this.mapa.getMap() == 1) {
                    this.mapa = new Mapa(this.mapa.getKey(),0,this.mapa.getNumObs(),this.resX);
                }
                this.piso = mapa.getPiso();
                this.fondo = mapa.getFondo();
                this.batman.actualizarPuntosPoligonos();
            }
            
            //contador de monedas
            lapiz.drawImage(this.monedaC, 30, resY/37.5, this.monedaC.getWidth()*resY/375, this.monedaC.getHeight()*resY/375);
            lapiz.setTextAlign(TextAlignment.LEFT);
            lapiz.fillText("" + this.batman.getMonedas(), resX/16, resX/24);
            
        } else if (this.seleccion == 2){ //tienda
            
            if(teclado.contains("ESCAPE")){
                batman.actualizarIm();
                try {
                    saveInfo(playerInfo);
                } catch (FileNotFoundException ex){
                    System.out.println();
                }
                seleccionado = false;
            }
            boolean isGris = false;
            for(int i = 0; i < 3; i++){
                if(gris[i].equals("gris")){
                    isGris = true; 
                    break;
                }
            }            
            
            lapiz.setFill(Color.WHITE);
            lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/12));
            lapiz.setTextAlign(TextAlignment.CENTER);
            lapiz.fillText("TIENDA", resX/2, resY/10);
            
            if (teclado.contains("UP") && !pub) {
                this.tecPresionadas[1]=true;   
            }else if(this.tecPresionadas[1]){
                
                this.tecPresionadas[1]=false;
                this.tiendaSelec += 4;
                this.tiendaSelec %= 5;
                solicitudCompra = false;
            }
            if (teclado.contains("DOWN") && !pub) {
                this.tecPresionadas[2]=true;             
            }else if(this.tecPresionadas[2]){
                this.tecPresionadas[2]=false;
                this.tiendaSelec++;
                this.tiendaSelec %= 5;
                solicitudCompra = false;
            }
            
            if(this.tiendaSelec < 0) this.tiendaSelec = 6-1; //6 es el numero de opciones
            this.tiendaSelec %= 6;
            
            boolean presD = false; boolean presI = false;
            
            int part = (2-this.tiendaSelec + 3)%3;
            int per = curPer[part];
            
            boolean isPersonaje = this.tiendaSelec <= 2;
            
            if(teclado.contains("RIGHT") && !pub) {
                presD = true;
              this.tecPresionadas[3]=true;
            }else if(this.tecPresionadas[3] && this.tiendaSelec < 4){
                this.tecPresionadas[3] = false;
                if(isPersonaje){
                    per = (curPer[part] + 1)% batman.getPersonajes().length;
                    if(this.batman.getAdquisicion()[part][per] == 1){
                        batman.setPer(part, per);
                        gris[part] = "";
                    } else{
                        gris[part] = "gris";
                    }
                } else{
                    curAc = (curAc + 1) % 6;
                }
            }
            
            if(teclado.contains("LEFT") && !pub) {
               this.tecPresionadas[4]=true;
               presI = true;
            }else if(this.tecPresionadas[4] && this.tiendaSelec < 4){
                this.tecPresionadas[4]=false;
                
                if(this.batman.getAdquisicion()[part][per] == 1){
                    batman.setPer(part, per);
                    gris[part] = "";
                }
                else{
                    gris[part] = "gris";
                }
                
                if(isPersonaje){
                    per = (curPer[part] - 1 +  batman.getPersonajes().length)% batman.getPersonajes().length;
                    if(this.batman.getAdquisicion()[part][per] == 1){
                        batman.setPer(part, per);
                        gris[part] = "";
                    } else{
                        gris[part] = "gris";
                    }
                } else{
                    curAc = (curAc - 1 + 6) % 6;
                }
            }
            
            if(teclado.contains("ENTER") && !pub) {
               this.tecPresionadas[5]=true;
               presI = true;
               if(isGris &&  this.tiendaSelec < 3 && !solicitudCompra){
                   solicitudCompra = true;
               }
            } else if(this.tecPresionadas[5] && !pub){                
                if(this.tiendaSelec == 3){
                    if(!(curAc >= 4 && this.batman.getAccesorios()[curAc] == 1))solicitudCompra = true;
                }
                else if(this.tiendaSelec == 4) pub = true;
                this.tecPresionadas[5]=false;
            }
            curPer[part] = per;
  
            
            if(pub){
                //limpieza
                lapiz.clearRect(0, 0, resX, resY);
                lapiz.setFill(Color.WHITE);
                lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/20));
                lapiz.setTextAlign(TextAlignment.CENTER);
                lapiz.fillText("Desea   ver   un   anuncio   a   cambio   de   2   monedas", resX/2, 2*resY/5);
                if(this.YNpub == 1){
                    lapiz.setFill(Color.RED);
                    lapiz.fillRoundRect(resX/4.55, 3*resY/5 - 45, 90, 60, 4*resY/75,4*resY/75);
                    lapiz.setFill(Color.WHITE);
                }else if(this.YNpub == 0){
                    lapiz.setFill(Color.RED);
                    lapiz.fillRoundRect(resX/4.55 + resX/2, 3*resY/5 - 45, 90, 60, 4*resY/75,4*resY/75);
                    lapiz.setFill(Color.WHITE);
                }
                lapiz.fillText("Si", resX/4, 3*resY/5);
                lapiz.fillText("No", 3*resX/4, 3*resY/5);
                
                if(teclado.contains("ESCAPE")){
                    this.tecPresionadas[6] = true;
                } else if(this.tecPresionadas[6]){
                    pub = false;
                    this.tecPresionadas[6] = false;
                }
                if(teclado.contains("RIGHT")) {
                    presD = true;
                    this.tecPresionadas[3]=true;
                }else if(this.tecPresionadas[3]){
                    if(YNpub == 1) YNpub = 0;
                    else YNpub = 1;
                    this.tecPresionadas[3] = false;
                }
                if(teclado.contains("LEFT")) {
                   this.tecPresionadas[4]=true;
                   presI = true;
                }else if(this.tecPresionadas[4]){
                    if(YNpub == 1) YNpub = 0;
                    else YNpub = 1;
                    this.tecPresionadas[4] = false;
                }
                if(teclado.contains("ENTER")) {
                    this.tecPresionadas[5]=true;
                    presI = true;
                 }else if(this.tecPresionadas[5]){
                    if(YNpub == 1) publicidad();
                    else if(YNpub == 0) pub = false;
                    this.tecPresionadas[5] = false;
                }
            } else {
                lapiz.setFill(Color.rgb(34, 37, 42));//23, 26, 33 //69, 73, 85 //47, 69, 80 //64, 67, 78
                lapiz.fillRect(3*resX/5, resY/5 - (resY*0.65 - 3*resX/10)/2, resX/3, resY*0.65);
                
                //borde personaje derecha
                lapiz.setFill(Color.WHITE);
                lapiz.fillRect(3*resX/5, resY/5 - (resY*0.65 - 3*resX/10)/2, resX/3, 15);
                lapiz.fillRect(3*resX/5, resY/5 - (resY*0.65 - 3*resX/10)/2 + resY*0.65 - 15, resX/3, 15);
                lapiz.fillRect(3*resX/5, resY/5 - (resY*0.65 - 3*resX/10)/2, 15, resY*0.65);
                lapiz.fillRect(3*resX/5 + resX/3 - 15, resY/5 - (resY*0.65 - 3*resX/10)/2, 15, resY*0.65);

                Image imHead = new Image("image/" + batman.getPersonajes()[curPer[2]] + "/head/normalR.png");
                Image imTorso = new Image("image/" + batman.getPersonajes()[curPer[1]] + "/torso/normalR.png");
                Image imLeg = new Image("image/" + batman.getPersonajes()[curPer[0]] + "/leg/normalR.png");

                //personaje derecha grande
                lapiz.drawImage(imHead, 23*resX/30 - 3*imHead.getWidth()/2*resX/1600, resY/5, imHead.getWidth()*3*resX/1600, imHead.getHeight()*3*resX/1600);
                lapiz.drawImage(imTorso, 23*resX/30 - 3*imTorso.getWidth()/2*resX/1600, resY/5+imHead.getHeight()*3*resX/1600-imTorso.getHeight()*resX/1600, imTorso.getWidth()*3*resX/1600, imTorso.getHeight()*3*resX/1600);
                lapiz.drawImage(imLeg, 23*resX/30 - 3*imLeg.getWidth()/2*resX/1600, resY/5+(imHead.getHeight()*3*resX/1600-imTorso.getHeight()*resX/1600+imTorso.getHeight()*3*resX/1600), imLeg.getWidth()*3*resX/1600, imLeg.getHeight()*3*resX/1600);

                //interior
                //lapiz.strokeRect(resX/8, resY/5, imHead.getWidth()*1.5, imHead.getHeight()*1.5);
                //lapiz.strokeRect(resX/8, resY/5 + imHead.getHeight()*1.5 + resX/20, imTorso.getWidth()*1.5, imTorso.getHeight()*2/3*1.5);
                //lapiz.strokeRect(resX/8, resY/5 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 , imTorso.getWidth()*1.5, imTorso.getHeight()*1.5);

                lapiz.setStroke(Color.WHITE);

                //exterior
                lapiz.setFill(Color.rgb(34, 37, 42));
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64,imHead.getWidth()*1.5 + resX/32, imHead.getHeight()*1.5 + resX/32);
                lapiz.setFill(Color.WHITE);

                if(this.tiendaSelec == 0) lapiz.setFill(Color.RED);
                //rectangulos
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64,imHead.getWidth()*1.5 + resX/32, 10);
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64,10, imHead.getHeight()*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 - 10, resY/5 - resX/64, 10 , imHead.getHeight()*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/32 - 10 ,imHead.getWidth()*1.5 + resX/32, 10);
                lapiz.setFill(Color.WHITE);
                //triangulos
                if(presD && this.tiendaSelec == 0) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{(resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32) + 15, (resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32) + 15 + 15*(0.86602) , (resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32) + 15} ,
                                    new double[]{(resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 - 15*(0.5), (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2  , (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 + 15*(0.5)},3);
                lapiz.setFill(Color.WHITE);
                if(presI && this.tiendaSelec == 0) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/8 - resX/64 - 15, resX/8 - resX/64 - 15 - 15*(0.86602) , resX/8 - resX/64 - 15} ,
                                    new double[]{(resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 - 15*(0.5), (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2  , (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 + 15*(0.5)},3);
                lapiz.setFill(Color.WHITE);

                lapiz.setFill(Color.rgb(34, 37, 42));
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20, imTorso.getWidth()*1.5 + resX/32, imTorso.getHeight()*2/3*1.5 + resX/32);
                lapiz.setFill(Color.WHITE);

                if(this.tiendaSelec == 1) lapiz.setFill(Color.RED);
                //rectangulos
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20, imTorso.getWidth()*1.5 + resX/32, 10);
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20, 10, imTorso.getHeight()*2/3*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64 + imTorso.getWidth()*1.5 + resX/32 - 10, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20, 10, imTorso.getHeight()*2/3*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + imTorso.getHeight()*2/3*1.5 + resX/32 - 10, imTorso.getWidth()*1.5 + resX/32, 10);
                lapiz.setFill(Color.WHITE);
                //triangulos
                if(presD && this.tiendaSelec == 1) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 + 15, resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 + 15*(0.86602) + 15, resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 + 15},
                                  new double[]{resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + (imTorso.getHeight()*2/3*1.5 + resX/32)/2 - 15*(0.5), resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + (imTorso.getHeight()*2/3*1.5 + resX/32)/2, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + (imTorso.getHeight()*2/3*1.5 + resX/32)/2 + 15*(0.5)}, 3);
                lapiz.setFill(Color.WHITE);
                if(presI && this.tiendaSelec == 1) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/8 - resX/64 - 15, resX/8 - resX/64 - 15*(0.86602) - 15, resX/8 - resX/64 - 15},
                                  new double[]{resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + (imTorso.getHeight()*2/3*1.5 + resX/32)/2 - 15*(0.5), resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + (imTorso.getHeight()*2/3*1.5 + resX/32)/2, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/20 + (imTorso.getHeight()*2/3*1.5 + resX/32)/2 + 15*(0.5)}, 3);
                lapiz.setFill(Color.WHITE);

                lapiz.setFill(Color.rgb(34, 37, 42));
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 , imLeg.getWidth()*1.5 + resX/32, imLeg.getHeight()*1.5 + resX/32);
                lapiz.setFill(Color.WHITE);

                if(this.tiendaSelec == 2) lapiz.setFill(Color.RED);
                //rectangulos
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 , 10, imLeg.getHeight()*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 , imLeg.getWidth()*1.5 + resX/32, 10);
                lapiz.fillRect(resX/8 - resX/64 + imLeg.getWidth()*1.5 + resX/32 - 10, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 , 10, imLeg.getHeight()*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + imLeg.getHeight()*1.5 + resX/32 -10 , imLeg.getWidth()*1.5 + resX/32, 10);
                lapiz.setFill(Color.WHITE);
                //triangulos
                if(presD && this.tiendaSelec == 2) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 + 15, resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 + 15*(0.86602) + 15, resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 + 15},
                                  new double[]{resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + (imLeg.getHeight()*1.5 + resX/32)/2 - 15*(0.5), resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + (imLeg.getHeight()*1.5 + resX/32)/2, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + (imLeg.getHeight()*1.5 + resX/32)/2 + 15*(0.5)}, 3);
                lapiz.setFill(Color.WHITE);
                if(presI && this.tiendaSelec == 2) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/8 - resX/64 - 15, resX/8 - resX/64 - 15*(0.86602) - 15, resX/8 - resX/64 - 15},
                                  new double[]{resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + (imLeg.getHeight()*1.5 + resX/32)/2 - 15*(0.5), resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + (imLeg.getHeight()*1.5 + resX/32)/2, resY/5 - resX/64 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 + (imLeg.getHeight()*1.5 + resX/32)/2 + 15*(0.5)}, 3);
                lapiz.setFill(Color.WHITE);
                
                //accesorio
                double pp = imHead.getWidth()*1.5 + resX/32 + resX/16;
                this.batman.actualizarImAc(curAc);
                Image imAc = this.batman.getImageAccesorio();
                if(curAc < 4){
                    lapiz.drawImage(imHead, resX/8 + pp, resY/5, imHead.getWidth()*1.5, imHead.getHeight()*1.5);
                    lapiz.drawImage(imAc, 0,0, 130, 90, resX/8 + pp, resY/5, imHead.getWidth()*1.5, imHead.getHeight()*1.5); 
                }
                else{
                    double ancho = imHead.getWidth()*1.5 / 2;
                    double alto = imHead.getHeight()*1.5 / 2;
                    lapiz.drawImage(imAc, resX/8 + pp + (ancho)/2, resY/5 + (alto)/2, ancho, alto); 
                }
                    
                if(this.tiendaSelec == 3) lapiz.setFill(Color.RED);
                //rectangulos
                lapiz.fillRect(resX/8 - resX/64 + pp, resY/5 - resX/64,imHead.getWidth()*1.5 + resX/32, 10);
                lapiz.fillRect(resX/8 - resX/64 + pp , resY/5 - resX/64,10, imHead.getHeight()*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32 - 10 + pp, resY/5 - resX/64, 10 , imHead.getHeight()*1.5 + resX/32);
                lapiz.fillRect(resX/8 - resX/64 + pp, resY/5 - resX/64 + imHead.getHeight()*1.5 + resX/32 - 10 ,imHead.getWidth()*1.5 + resX/32, 10);
                lapiz.setFill(Color.WHITE);
                //triangulos
                if(presD && this.tiendaSelec == 0) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{(resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32) + 15 + pp, (resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32) + 15 + 15*(0.86602) + pp, (resX/8 - resX/64 + imHead.getWidth()*1.5 + resX/32) + 15 + pp} ,
                                    new double[]{(resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 - 15*(0.5) , (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2  , (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 + 15*(0.5)},3);
                lapiz.setFill(Color.WHITE);
                if(presI && this.tiendaSelec == 0) lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/8 - resX/64 - 15 + pp, resX/8 - resX/64 - 15 - 15*(0.86602) + pp , resX/8 - resX/64 - 15 + pp} ,
                                    new double[]{(resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 - 15*(0.5), (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2  , (resY/5 - resX/64) + (imHead.getHeight()*1.5 + resX/32)/2 + 15*(0.5)},3);
                lapiz.setFill(Color.WHITE);

                //personaje izquierda
                imHead = new Image("image/" + batman.getPersonajes()[curPer[2]] + "/head/normalR"+gris[2]+".png");
                imTorso = new Image("image/" + batman.getPersonajes()[curPer[1]] + "/torso/normalR"+gris[1]+".png");
                imLeg = new Image("image/" + batman.getPersonajes()[curPer[0]] + "/leg/normalR"+gris[0]+".png");
                lapiz.drawImage(imHead, resX/8, resY/5, imHead.getWidth()*1.5, imHead.getHeight()*1.5);
                lapiz.drawImage(imTorso, resX/8, resY/5 + imHead.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/20, imTorso.getWidth()*1.5, imTorso.getHeight()*1.5);
                lapiz.drawImage(imLeg, resX/8, resY/5 + imHead.getHeight()*1.5 + imTorso.getHeight()*1.5 - imTorso.getHeight()/3*1.5 + resX/10 , imLeg.getWidth()*1.5, imLeg.getHeight()*1.5);
                
                
                if(solicitudCompra){
                    try{
                        
                        if(isPersonaje){
                            int valor = this.batman.getValores()[part][per];
                            if(monedasG >= valor){
                                this.monedasG -= valor;
                                this.batman.restarMoneda(valor);
                                if(this.batman.getMonedas() < 0) this.batman.setMoneda(0);
                                this.batman.getAdquisicion()[part][per] = 1;
                                gris[part] = "";
                                this.batman.setPer(part, per);
                                saveInfo(playerInfo);
                                saveInfoCustom(customInfo);
                            }   
                        } else if (!isPersonaje) {
                            int valor = this.batman.getValoresAc()[curAc];
                            if(monedasG >= valor){
                                this.monedasG -= valor;
                                this.batman.restarMoneda(valor);
                                if(this.batman.getMonedas() < 0) this.batman.setMoneda(0);
                                this.batman.getAccesorios()[curAc]++;
                                this.batman.setPer(part, per);
                                saveInfo(playerInfo);
                                saveInfoCustom(customInfo);
                            }
                        }
                        this.tecPresionadas[5]=false;
                        solicitudCompra = false;                   
                    } catch (FileNotFoundException ex) {
                        System.out.println();
                    }
                }//corchete solicitud compra
                
                //BOTON COMPRAR
                if(isGris){
                    int valor = this.batman.getValores()[part][per];
                    pp = 130*1.5 + resX/32 + resX/16;
                    lapiz.setFill(Color.RED);
                    lapiz.fillRoundRect(resX/8 - resX/64 + pp, resY/5 - resX/64 + 90*1.5 + resX/15 , 130*1.5 + resX/32, (resX/40)*1.5, 4*resY/75,4*resY/75);
                    lapiz.setFill(Color.WHITE);
                    lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF", resY/25));
                    lapiz.setTextAlign(TextAlignment.CENTER);
                    lapiz.fillText("COMPRAR POR", resX/8 - resX/64 + pp + (130*1.5 + resX/32)/2, resY/5 - resX/64 + 90*1.5 + resX/15 + (resX/40)*0.75, 130*1.5);
                    lapiz.fillText(valor + " MONEDAS", resX/8 - resX/64 + pp + (130*1.5 + resX/32)/2, resY/5 - resX/64 + 90*1.5 + resX/15 + (resX/40)*1.5, 130*1.5);
                    if(monedasG < valor) lapiz.fillText("MONEDAS INSUFICIENTES", resX/8 - resX/64 + pp + (130*1.5 + resX/32)/2, resY/5 - resX/64 + 90*1.5 + resX/15 + (resX/40)*0.75 + (resX/40)*1.5, 130*1.5);
                    tecPresionadas[1] = false;
                    tecPresionadas[2] = false;
                }

                if(this.tiendaSelec ==  3){
                    int valor = this.batman.getValoresAc()[curAc];
                    pp = 130*1.5 + resX/32 + resX/16;
                    lapiz.setFill(Color.RED);
                    lapiz.fillRoundRect(resX/8 - resX/64 + pp, resY/5 - resX/64 + 90*1.5 + resX/15 , 130*1.5 + resX/32, (resX/40)*1.5, 4*resY/75,4*resY/75);
                    lapiz.setFill(Color.WHITE);
                    lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF", resY/25));
                    lapiz.setTextAlign(TextAlignment.CENTER);
                    lapiz.fillText("COMPRAR POR", resX/8 - resX/64 + pp + (130*1.5 + resX/32)/2, resY/5 - resX/64 + 90*1.5 + resX/15 + (resX/40)*0.75, 130*1.5);
                    lapiz.fillText(valor + " MONEDAS", resX/8 - resX/64 + pp + (130*1.5 + resX/32)/2, resY/5 - resX/64 + 90*1.5 + resX/15 + (resX/40)*1.5, 130*1.5);
                    if(monedasG < valor) lapiz.fillText("MONEDAS INSUFICIENTES", resX/8 - resX/64 + pp + (130*1.5 + resX/32)/2, resY/5 - resX/64 + 90*1.5 + resX/15 + (resX/40)*0.75 + (resX/40)*1.5, 130*1.5);
                }
                
                //contador de monedas
                lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/12));
                lapiz.drawImage(this.monedaC, 30, resY/37.5, this.monedaC.getWidth()*resY/375, this.monedaC.getHeight()*resY/375);
                lapiz.setTextAlign(TextAlignment.LEFT);
                lapiz.fillText("" + this.monedasG, resX/16, resX/24);               

                //monetizacion boton
                lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",43));
                lapiz.setTextAlign(TextAlignment.LEFT);
                lapiz.setFill(Color.RED);
                if(tiendaSelec == 4) lapiz.fillRoundRect(resX/8 - resX/16 - resY/32, resY-resY/10-43, "Conseguir monedas".length()*24.8, 43+15,4*resY/75,4*resY/75);
                lapiz.setFill(Color.WHITE);
                lapiz.fillText("Conseguir monedas", resX/8 - resX/16 - resY/64, resY-resY/10);
            }//corchete !pub
            
            
                
        } else if(this.seleccion == 3){ //opciones
            if(!seleccionado2){
                
            if(teclado.contains("ESCAPE")){
                    
                    if(resolucion.equals("1600")) resX=1600;
                    else resX=1200;
                    seleccionado= false;
                }
                
            if (teclado.contains("UP")){
                this.tecPresionadas[1] = true;
                
            }else if(this.tecPresionadas[1]){
                this.tecPresionadas[1] = false;
                this.seleccion2--;
                if(this.seleccion2==-1)this.seleccion2=1;
                
            }
            if (teclado.contains("DOWN")){
                this.tecPresionadas[2] = true;
            }else if(this.tecPresionadas[2]){
                this.tecPresionadas[2]=false;
                this.seleccion2++;
                if(this.seleccion2==2)this.seleccion2=0;
                
                
            }
            if (teclado.contains("ENTER")){
                this.tecPresionadas[5] = true;
                
            }else if(tecPresionadas[5]){
                this.seleccionado2 = true;  
                this.tecPresionadas[5] = false;
            }
            
            for(int i=0; i<opciones2.size(); i++){
                if (i==this.seleccion2){
                    lapiz.setFill(Color.RED);
                    lapiz.fillRoundRect(resX/2 - (opciones2.get(i).length()*0.6)*(resY/15)/2, resY/(opciones2.size()+1)*(i+1) - resY/15 + resY/75, (opciones2.get(i).length()*0.6)*(resY/15), resY/15,4*resY/75,4*resY/75);
                    lapiz.setFill(Color.WHITE);
                }
                lapiz.fillText(opciones2.get(i),resX/2, resY/(opciones2.size()+1)*(i+1));
            }
        }
            
            
            if(this.seleccion2==0 && seleccionado2){
                lapiz.setFill(Color.WHITE);
                lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/12));
                lapiz.setTextAlign(TextAlignment.CENTER);
                lapiz.fillText(resolucion,resX/2, resY/2);
                
                boolean der=false;
                boolean izq=false;
                
                 //eventos de creación de nuevo juego cn nueva resolución
                if(teclado.contains("ESCAPE")){
                    if(resolucion.equals("1600")){
                        resX=1600;
                        CVjuego1600 juego1600=new CVjuego1600();
                        juego1600.mostarVista();
                        
                    }
                    else{
                        resX=1200;
                        CVjuego1200 juego1200=new CVjuego1200();
                        juego1200.mostarVista();
                    }
                    seleccionado2= false;
                }
                
                if(teclado.contains("RIGHT")) {
                    der = true;
                    this.tecPresionadas[3]=true;
                }else if(this.tecPresionadas[3]){
                    resolucion="1200";
                    this.tecPresionadas[3] = false;
                }
                if(teclado.contains("LEFT")) {
                   this.tecPresionadas[4]=true;
                   izq = true;
                }else if(this.tecPresionadas[4]){
                    this.tecPresionadas[4]=false;
                    resolucion="1600";
                }
                
                if(der)lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/2 +70,resX/2 +70,resX/2 +90},new double[]{(resY-30)/2 +10,(resY-30)/2 -10,(resY-30)/2}, 3);
                lapiz.setFill(Color.WHITE);
                if(izq)lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/2 -70,resX/2 -70,resX/2 -90},new double[]{(resY-30)/2 +10,(resY-30)/2 -10,(resY-30)/2}, 3);
                lapiz.setFill(Color.WHITE);
                
            }else if(this.seleccion2==1 && seleccionado2){
                lapiz.setFill(Color.WHITE);
                lapiz.setFont(Font.loadFont("file:fonts/ARCADECLASSIC.TTF",resY/12));
                lapiz.setTextAlign(TextAlignment.CENTER);
                lapiz.fillText(fondoSelec,resX/2, resY/2);
                
                boolean der2=false;
                boolean izq2=false;
                
                if(teclado.contains("ESCAPE")){
                    
                    if(fondoSelec.equals("CIUDAD")){
                        this.mapa=new Mapa(1234, 0, 3, this.resX);
                        this.fondo=mapa.getFondo();
                    }
                    else{
                        this.mapa=new Mapa(1234, 1, 3, this.resX);
                        this.fondo=mapa.getFondo();
                    }
                    seleccionado2= false;
                }
                
                if(teclado.contains("RIGHT")) {
                    der2 = true;
                    this.tecPresionadas[3]=true;
                }else if(this.tecPresionadas[3]){
                    fondoSelec="SELVA";
                    this.tecPresionadas[3] = false;
                }
                if(teclado.contains("LEFT")) {
                   this.tecPresionadas[4]=true;
                   izq2 = true;
                }else if(this.tecPresionadas[4]){
                    this.tecPresionadas[4]=false;
                    fondoSelec="CIUDAD";
                }
                
                if(der2)lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/2 +100,resX/2 +100,resX/2 +120},new double[]{(resY-30)/2 +10,(resY-30)/2 -10,(resY-30)/2}, 3);
                lapiz.setFill(Color.WHITE);
                if(izq2)lapiz.setFill(Color.RED);
                lapiz.fillPolygon(new double[]{resX/2 -100,resX/2 -100,resX/2 -120},new double[]{(resY-30)/2 +10,(resY-30)/2 -10,(resY-30)/2}, 3);
                lapiz.setFill(Color.WHITE);
            }
            
        }else if (this.seleccion == 4){ //salir
            System.exit(0);
        }
    }
    
    public double posY(double posX){
        for(int i=0; i<resY-resY/15; i+=10){
            Shape s = new Rectangle(posX, i, 50, 10);
            for(int ob=0; ob<this.obstaculos.size(); ob++){
                for(int p=0; p<this.obstaculos.get(ob).getPoligonosPisos().size(); p++){
                    double area = SVGPath.intersect(s,obstaculos.get(ob).getPoligonosPisos().get(p)).getBoundsInLocal().getHeight(); 
                    if(area != -1) {
                        return i;
                    }
                }
            }
        }
        return resY-resY/15;
    }
    
    
    public void setHostServices(HostServicesDelegate hostServices){
        this.hostServices = hostServices;
    }
    
    public void openWeb(String url){
        this.hostServices.showDocument(url);
    }
    
    public void saveInfo(File f) throws FileNotFoundException{
        PrintStream pr = new PrintStream(f);
        pr.println(this.monedasG);
        for(int i = 0; i < this.batman.getPer().length; i++) pr.print(this.batman.getPer(i) + " ");
    }
    
    public void saveInfoCustom(File f) throws FileNotFoundException{
        PrintStream pr = new PrintStream(f);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < this.batman.getPersonajes().length; j++){
                pr.print(this.batman.getAdquisicion()[i][j] + " ");
            }
            pr.println();
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < this.batman.getPersonajes().length; j++){
                pr.print(this.batman.getValores()[i][j] + " ");
            }
            pr.println();
        }
        for(int i = 0; i < 6; i++){
            pr.print(this.batman.getAccesorios()[i] + " ");
        }
        pr.println();
        for(int i = 0; i < 6; i++){
            pr.print(this.batman.getValoresAc()[i] + " ");
        }
    }
    
    
    public void readInfo(){
        Scanner sc;
        try {
            sc = new Scanner(playerInfo);
            this.monedasG = sc.nextInt();
            for(int i = 0; i < 3; i++){
                int p = sc.nextInt();
                this.batman.setPer(i, p);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("");
        }
    }
    
    public void readInfoCustom(){
        Scanner sc;
        try {
            sc = new Scanner(customInfo);
            int[][] mat = new int[3][this.batman.getPersonajes().length];
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < this.batman.getPersonajes().length; j++){
                    mat[i][j] = sc.nextInt();
                }
            }
            this.batman.setAdquisicion(mat);
            mat = new int[3][this.batman.getPersonajes().length];
            for(int i = 0; i < 3; i++){
                for(int j = 0; j < this.batman.getPersonajes().length; j++){
                    mat[i][j] = sc.nextInt();
                }
            }
            this.batman.setValores(mat);
            int[] arr = new int[6];
            for(int i = 0; i < 6; i++){
                arr[i] = sc.nextInt();
            }
            this.batman.setAccesorios(arr);
            arr = new int[6];
            for(int i = 0; i < 6; i++){
                arr[i] = sc.nextInt();
            }
            this.batman.setValoresAc(arr);
        } catch (FileNotFoundException ex) {
            System.out.println();
        }
    }
    
    public void publicidad(){
        openWeb("https://ouo.io/43DNQs");
        this.monedasG +=2;
        this.pub = false;
    }
    
    
    public void reiniciarJuego(){
         lapiz.drawImage(fondo,movX,0,resX-movX,resY,0,0,resX-movX,resY);
         lapiz.drawImage(fondo,0,0,movX,resY,resX-movX,0,movX,resY);
         lapiz.drawImage(fondo,0,0,movX,resY,resX*2-movX,0,movX,resY);
         lapiz.drawImage(fondo,0,0,movX,resY,resX*3-movX,0,movX,resY); 
         
         for(int i=0;i<obstaculos.size();i++){
             this.obstaculos.remove(i--);
         }
         
         for(int j=0;j<monedas.size();j++){
             this.monedas.remove(j--);
         }
         
         for(int k=0;k<enemigos.size();k++){
             this.enemigos.remove(k--);
         }
         
        this.mapa = new Mapa(new Random().nextInt(),1,this.mapa.getNumObs(),this.resX);
         
        this.monedas.add(new Moneda(400, this.resY - this.resY/15 - 50*1600/this.resX, this.resX));
        this.enemigos.add(new Enemigo(0, resX*3/4 , resY  - resY/15 - 160*(resX/1600), 1, resX));
         
        try {
            this.obstaculos.add(new Obstaculo(1000,1,resX/3,resX));
        } catch (FileNotFoundException ex) {
            System.out.println();
        }
    }
    
    public int techo(int a, int b){
        if(a % b == 0) return a/b;
        else return a/b + 1;
    }
    
    
}


