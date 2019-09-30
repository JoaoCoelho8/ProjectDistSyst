package edu.ufp.inf.sd.dropbox.server;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;


public class DropBoxSessionImpl implements DropBoxSessionRI, Runnable {

    private DBMockup db;
    private DropBoxFactoryImpl factory;
    private String user, pass;
    private boolean keepRunning = false;
    public HashMap<String, DropBoxSubjectRI> subjects = new HashMap<String, DropBoxSubjectRI>();

    public DropBoxSessionImpl(DropBoxFactoryImpl factory, String user, String pass, DropBoxSubjectRI dropBoxSubjectRI, DropBoxSubjectRI dropBoxSubjectServerRI) throws RemoteException {
        this.factory = factory;
        this.user = user;
        this.pass = pass;
        subjects.put(user, dropBoxSubjectRI);
        subjects.put(user+"Server", dropBoxSubjectServerRI);
        this.db= factory.getDb();
        exportObject();
    }

    public void exportObject() throws RemoteException{
        UnicastRemoteObject.exportObject(this, 0);
    }



    public void setDb(DBMockup db) {
        this.db = db;
    }

    public DropBoxFactoryImpl getFactory() {
        return factory;
    }

    public void setFactory(DropBoxFactoryImpl factory) {
        this.factory = factory;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }

    /**
     * fazer o logout do user
     */
    @Override
    public void logout() {

        factory.sessions.remove(user);
        this.keepRunning = false;
    }

    /**
     * get subject
     * @param pasta
     * @return subejects
     * @throws RemoteException
     */
    @Override
    public DropBoxSubjectRI getSubject(String pasta) throws RemoteException {
        return subjects.get(pasta);
    }

    /**
     * listar subjects
     * @return subjects
     * @throws RemoteException
     */
    @Override
    public HashMap<String, DropBoxSubjectRI> listSubjects() throws RemoteException {
        return subjects;
    }

    /**
     * vamos à HashMap de subjects e add o user
     * @param user
     * @throws RemoteException
     */
    public void addSubject(String user) throws RemoteException {
        subjects.put(user, db.subjects.get(user));
    }

    /**
     * função para fazer a partilha de conteudos com um determiado user
     * se o user existir nos subjects e nos subjects do server adicionamos a ambos e fazemos a partilha
     * @param user
     * @throws RemoteException
     */
    @Override
    public void shareWith(String user) throws RemoteException {

        if(db.subjects.get(user)!=null && db.subjectsServer.get(user)!=null)
        {
            subjects.put(user, db.subjects.get(user));
            subjects.put(user+"Server", db.subjectsServer.get(user));
        }
        else {
            System.out.println("Erro a partilhar "+user);
        }
    }

    @Override
    public void run() {
        while (keepRunning) {
        }
    }
}
