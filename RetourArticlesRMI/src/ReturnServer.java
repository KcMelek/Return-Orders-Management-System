

import com.retourarticles.services.ReturnService;
import com.retourarticles.services.ReturnServiceImpl;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ReturnServer {
    public static void main(String[] args) {
        try {
            ReturnService returnService = new ReturnServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("ReturnService", returnService);
            System.out.println("Return Server is running...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}