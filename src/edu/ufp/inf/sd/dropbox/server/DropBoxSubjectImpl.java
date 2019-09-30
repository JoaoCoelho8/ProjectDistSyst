package edu.ufp.inf.sd.dropbox.server;

import edu.ufp.inf.sd.dropbox.client.ObserverRI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DropBoxSubjectImpl implements DropBoxSubjectRI {

    public DBMockup db;
    public ArrayList<ObserverRI> observers= new ArrayList<>();
    public String pasta;
    public SingletonFolderOperationsUser singletonStateFolder;
    public ObserverRI observerRI;


    public DropBoxSubjectImpl(String pasta) throws RemoteException {
        this.db= new DBMockup();
        this.pasta=pasta;
        this.singletonStateFolder= SingletonFolderOperationsUser.createSingletonFolderOperationsBooks(pasta);
        exportObject();
    }

    public String copyPasta() {
        return pasta;
    }

    public void setPasta(String pasta) {
        this.pasta = pasta;
        this.singletonStateFolder= SingletonFolderOperationsUser.createSingletonFolderOperationsBooks(pasta);
    }

    public void exportObject() throws RemoteException{
        UnicastRemoteObject.exportObject(this, 0);
    }

    public SingletonFolderOperationsUser getSingletonStateFolder(){
        return this.singletonStateFolder;
    }

    @Override
    public void attach(ObserverRI o) throws RemoteException {
        this.observers.add(o);
    }

    @Override
    public void dettach(ObserverRI o) throws RemoteException{
        this.observers.remove(o);
    }


    @Override
    public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException {
        return visitor.visitConcreteElementUsers(this);
    }

    /**
     * percorremos todos os obsrvers e fazemos o acceptvisitor
     * @param visitor
     */
    public void notifyAllObservers(VisitorFoldersOperationsI visitor) throws RemoteException {
        /*Thread*/
        DropBoxSubjectThread subjectThread = new DropBoxSubjectThread(this.observerRI, visitor);
        subjectThread.start();

        for (ObserverRI o : observers) {

            try {
                o.acceptVisitor(visitor);

            } catch (RemoteException e) {

                e.printStackTrace();

            }
        }
    }

    static void runReplyObserver(DropBoxSubjectObserverRI dropBoxSubjectObserverRI, VisitorFoldersOperationsI visitor) {

        Thread t = Thread.currentThread();
        try {
            // Create Random generator object
            Random generator = new Random();
            // Slowdown reply with a sleep time
            //long millisecs = Math.abs(generator.nextLong());
            //another way...
            long millisecs = (long) (Math.random() * 2000);
            Logger.getLogger(t.getName()).log(Level.INFO, " waiting " + millisecs + " millisec...");
            t.sleep(300);
            // Generate a probability of error between 1..100
            int playError = (100 - Math.abs(generator.nextInt(99) + 1));
            if (playError >= 5) {
                //Below 5 there is no reply error and CONTINUES playing
                Logger.getLogger(t.getName()).log(Level.INFO, "REPLY!! " +visitor);

                dropBoxSubjectObserverRI.acceptVisitor(visitor);

            } else {
                //Above 5 there is a reply error and STOPS playing
                Logger.getLogger(t.getName()).log(Level.INFO, " dropped  " +visitor);
            }
            Logger.getLogger(t.getName()).log(Level.INFO, " server thread end " + visitor);

        } catch (InterruptedException | RemoteException ie) {
            Logger.getLogger(t.getName()).log(Level.SEVERE, null, ie);
        }
    }
}
