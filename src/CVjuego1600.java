/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
public class CVjuego1600 {
    private Vjuego1600 juego;

    public CVjuego1600() {
        this.juego=new Vjuego1600();
        
    }
    
     public void mostarVista(){
        Singleton singleton=Singleton.getSingleton();
        this.juego.mostrar(singleton.getStage());
    }

    public Vjuego1600 getJuego() {
        return juego;
    }
     
     
    
}
