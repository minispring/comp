package controllers;

import play.mvc.*;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        String result="";
//        DiscoveryVMWare vm = new DiscoveryVMWare();
//
//        DiscoveryVMWare.getHost();


        //DiscoverySMI cs = new DiscoverySMI("http","10.1.11.35","5998","root/lsissi11","admin","admin");
//        cs = new DiscoveryComputerSystem("http","10.1.11.35","5988","root/brocade1","admin","password");
        return ok(index.render(result));
    }

}
