/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Estudiante
 */
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
public class EscenarioInteractivo extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        /*System.out.println("Ingrese la resolucíon: ");
        Scanner lectura = new Scanner(System.in);*/
        Singleton singleton=Singleton.getSingleton();
        singleton.setStage(primaryStage);
        Host host = Host.getHost();
        host.setHostService(HostServicesFactory.getInstance(this));
        //HostServicesDelegate hostServices = HostServicesFactory.getInstance(this);
        double resX = /*lectura.nextDouble()*/1200;
        double resY = resX*9/16;
        Pane layout  = new Pane();
        Canvas canvas = new Canvas(resX, resY);
        layout.getChildren().add(canvas);
        Scene escena = new Scene(layout,resX,resY, Color.BLACK);
        
        GraphicsContext lapiz = canvas.getGraphicsContext2D();
        Juego juego = new Juego (escena,lapiz,resX,resY);
        juego.setHostServices(host.getHostService());                
        CVjuego1200 juego1200 = new CVjuego1200();
        juego1200.mostarVista();
    }
    
    public static void main(String[] args) {
        Application.launch(args);
        
    }
}
