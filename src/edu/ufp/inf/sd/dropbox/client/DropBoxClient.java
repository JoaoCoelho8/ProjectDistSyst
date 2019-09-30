package edu.ufp.inf.sd.dropbox.client;


import edu.ufp.inf.sd.dropbox.server.*;

import edu.ufp.inf.sd.util.rmisetup.SetupContextRMI;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.rmi.registry.Registry;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DropBoxClient {

    /**
     * Context for connecting a RMI client to a RMI Servant
     */
    private SetupContextRMI contextRMI;
    private String username;
    private String algo = "Blowfish";

    /**
     * Remote interface that will hold the Servant proxy
     */
    private DropBoxFactoryRI dropBoxFactoryRI;

    public static void main(String[] args) {
        if (args != null && args.length < 2) {
            System.err.println("usage: java [options] edu.ufp.sd.helloworld.server.HelloWorldClient <rmi_registry_ip> <rmi_registry_port> <service_name>");
            System.exit(-1);
        } else {
            //1. ============ Setup client RMI context ============
            DropBoxClient hwc = new DropBoxClient(args);
            //2. ============ Lookup service ============
            hwc.lookupService();
            //3. ============ Play with service ============
            hwc.playService();
        }
    }

    public DropBoxClient(String args[]) {
        try {
            //List ans set args
            printArgs(args);
            String registryIP = args[0];
            String registryPort = args[1];
            String serviceName = args[2];
            //Create a context for RMI setup
            contextRMI = new SetupContextRMI(this.getClass(), registryIP, registryPort, new String[]{serviceName});
        } catch (RemoteException e) {
            Logger.getLogger(DropBoxClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private DropBoxFactoryRI lookupService() {
        try {
            //Get proxy to rmiregistry
            Registry registry = contextRMI.getRegistry();
            //Lookup service on rmiregistry and wait for calls
            if (registry != null) {
                //Get service url (including servicename)
                String serviceUrl = contextRMI.getServicesUrl(0);
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "going to lookup service @ {0}", serviceUrl);

                //============ Get proxy to HelloWorld service ============
                dropBoxFactoryRI = (DropBoxFactoryRI) registry.lookup(serviceUrl);

            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, "registry not bound (check IPs). :(");
                //registry = LocateRegistry.createRegistry(1099);
            }
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return dropBoxFactoryRI;
    }

    private void playService() {
        String username;
        DropBoxSessionRI session = null;
        while (true) {

            switch (menu()) {

                case 1:
                    if (register()) {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Successfully Registered");
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Register Error!");
                    }
                    session = login();
                    if (session != null) {

                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Welcome");
                        while (true){

                        }

                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Login Error!");
                    }
                    break;

                case 2:
                    if (register()) {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Successfully Registered");
                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Register Error!");
                    }
                    session = login();
                    if (session != null) {

                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Welcome");
                        try {
                            sessionCreateFolder(session);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Login Error!");
                    }
                    break;

                case 0:
                    Logger.getLogger(this.getClass().getName()).log(Level.INFO, "goint to finish, bye. ;)");
                    System.exit(0);
                    break;

            }
        }


    }

    private void printArgs(String args[]) {
        for (int i = 0; args != null && i < args.length; i++) {
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "args[{0}] = {1}", new Object[]{i, args[i]});
        }
    }

    /**
     * encriptar
     * @param file
     */
    public void encriptar( String file)
    {
        try {
            byte[] fileinbytes = file.getBytes("ISO-8859-1");
            Key chave = new SecretKeySpec(fileinbytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.ENCRYPT_MODE, chave);
        }
        catch (Exception e) {
            System.out.println("Erro quando tentava encriptar!!");
        }
    }

    /**
     * desencriptar
     * @param file
     */
    public void desencriptar(String file)
    {
        try {
            byte[] fileinbytes = file.getBytes("ISO-8859-1");
            Key clef = new SecretKeySpec(fileinbytes, algo);
            Cipher cipher = Cipher.getInstance(algo);
            cipher.init(Cipher.DECRYPT_MODE, clef);
        }
        catch (Exception e) {
            System.out.println("Erro quando tentava desencriptar!!");
        }
    }

    private void sessionCreateFolder(DropBoxSessionRI session) throws RemoteException {
        session.shareWith("Andre");
        HashMap<String, DropBoxSubjectRI> subjects = session.listSubjects();
        ArrayList<ObserverRI> observers = new ArrayList<>();
        String pasta = "P1";
        for (String user: subjects.keySet())
        {

            encriptar(pasta);
            System.out.println("Encriptação:"+pasta);

            DropBoxSubjectRI sub= subjects.get(user);
            System.out.println(sub);
            ObserverImpl o = new ObserverImpl(username, sub);
            sub.attach(o);
            observers.add(o);
            VisitorFoldersOperationCreateFolder visitorCreateFolder = new VisitorFoldersOperationCreateFolder(pasta);
            subjects.get(user).acceptVisitor(visitorCreateFolder);

            desencriptar(pasta);
            System.out.println("Desincriptação:"+pasta);
        }

    }

    private DropBoxSessionRI login() {
        System.out.print("Username: ");
        String user = System.console().readLine();
        username= user;
        System.out.print("Password: ");
        //String pass = System.console().readLine();
        char[] password = System.console().readPassword();
        String pass = new String(password);

        try {
            return dropBoxFactoryRI.login(user, pass);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Boolean register() {
        System.out.print("Username: ");
        String user = System.console().readLine();

        System.out.print("Password: ");
        char[] password = System.console().readPassword();
        String pass = new String(password);
        //String pass = System.console().readLine();


        try {
            return dropBoxFactoryRI.register(user, pass);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int menu() {
        System.out.println("1 - Case1\n2 - Case2\n0 - Exit");
        return Integer.parseInt(System.console().readLine());
    }
}

