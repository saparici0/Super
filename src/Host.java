
import com.sun.deploy.uitoolkit.impl.fx.HostServicesFactory;
import com.sun.javafx.application.HostServicesDelegate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ADMIN
 */
public class Host {
    private HostServicesDelegate hostService;
    private static Host host=null;

    public static Host getHost() {
        if(host==null){
            host= new Host();
        }
        return host;
    }

    public HostServicesDelegate getHostService() {
        return hostService;
    }

    public void setHostService(HostServicesDelegate hostService) {
        this.hostService = hostService;
    }
    
    
    
    
    
    
}
