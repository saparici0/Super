
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
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
public class Vjuego1600 {
    //private HostServicesDelegate hostServices;
    private Pane layout;
    private Canvas canvas;
    private Scene escena;
    private GraphicsContext lapiz;
    private Juego juego;
    

    public Vjuego1600() {
        //this.hostServices=HostServicesFactory.getInstance(this);
        this.layout=new Pane();
        this.canvas=new Canvas(1600,900);
        this.layout.getChildren().add(canvas);
        this.escena=new Scene(layout,1600,900,Color.BLACK);
        this.lapiz=this.canvas.getGraphicsContext2D();
        this.juego=new Juego(this.escena,this.lapiz,1600,900);
        
    }
    
    public void mostrar(Stage stage){
        Host host = Host.getHost();
        this.juego.setHostServices(host.getHostService());
        this.juego.start();
        stage.setScene(this.escena);
        stage.show();
        
    }

    public Juego getJuego() {
        return juego;
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }
    
    
    
    
}
