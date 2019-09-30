package edu.ufp.inf.sd.dropbox.server;

import edu.ufp.inf.sd.util.rmisetup.SetupContextRMI;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxServerBackup {

    /**
     * Context for running a RMI Servant on a host
     */
    private SetupContextRMI contextRMI;
    /**
     * Remote interface that will hold reference to the Servant impl
     */
    private DropBoxFactoryRI dropBoxFactoryRI;

    public static void main(String[] args) {
            System.out.println("BACKUP SERVER");
            if (args != null && args.length < 3) {
                System.err.println("usage: java [options] edu.ufp.sd.helloworld.server.HelloWorldServer <rmi_registry_ip> <rmi_registry_port> <service_name>");
                System.exit(-1);
            } else {
                //1. ============ Create Servant ============
                DropBoxServerBackup hws = new DropBoxServerBackup(args);
                //2. ============ Rebind servant on rmiregistry ============
                hws.rebindService();

            }
    }

    public DropBoxServerBackup(String args[]) {
        try {
            //============ List and Set args ============
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //============ Create a context for RMI setup ============
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
        }
    }

    private void rebindService() {
        try {
            //Get proxy to rmiregistry
            Registry registryBackup = contextRMI.getRegistry();
            //Bind service on rmiregistry and wait for calls
            if (registryBackup != null) {
                //============ Create Servant =============

                dropBoxFactoryRI= (DropBoxFactoryRI) new DropBoxFactoryImpl();

                //Get service url (including servicename)0
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to rebind service @ {0}", serviceUrl);

                //============ Rebind servant ============
                //Naming.bind(serviceUrl, helloWorldRI);
                registryBackup.rebind(serviceUrl, dropBoxFactoryRI);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "service bound and running. :)");
            } else {
                //System.out.println("VisitorServer - Constructor(): create registryBackup on port 1099");
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registryBackup not bound (check IPs). :(");
                //registryBackup = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            //System.out.println("HelloWorldServer - main(): args[" + i + "] = " + args[i]);
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }

    public SetupContextRMI getContextRMI() {
        return contextRMI;
    }

    public DropBoxFactoryRI getDropBoxFactoryRI() {
        return dropBoxFactoryRI;
    }
}