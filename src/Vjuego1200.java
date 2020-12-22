
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
public class Vjuego1200 {
     //private HostServicesDelegate hostServices;
    private Pane layout;
    private Canvas canvas;
    private Scene escena;
    private GraphicsContext lapiz;
    private Juego juego;
    

    public Vjuego1200() {
       //this.hostServices=HostServicesFactory.getInstance(this);
        this.layout=new Pane();
        this.canvas=new Canvas(1200,675);
        this.layout.getChildren().add(canvas);
        this.escena=new Scene(layout,1200,675,Color.BLACK);
        this.lapiz=this.canvas.getGraphicsContext2D();
        this.juego=new Juego(this.escena,this.lapiz,1200,675);
        Host host = new Host();
        this.juego.setHostServices(host.getHostService());
    }
    
    public void mostrar(Stage stage){
        Host host = Host.getHost();
        this.juego.setHostServices(host.getHostService());
        stage.setScene(this.escena);
        this.juego.start();
        stage.show();
    }
}
