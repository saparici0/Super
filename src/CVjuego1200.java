
import javafx.application.HostServices;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
public class CVjuego1200 {
    public Vjuego1200 juego1200;

    public CVjuego1200() {
        this.juego1200=new Vjuego1200();
    }
    
    public void mostarVista(){
        Singleton singleton = Singleton.getSingleton();
        this.juego1200.mostrar(singleton.getStage());
    }
    
}
