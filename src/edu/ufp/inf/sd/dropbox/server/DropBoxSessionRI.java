package edu.ufp.inf.sd.dropbox.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;


public interface DropBoxSessionRI extends Remote {
    public void logout() throws RemoteException;
    public DropBoxSubjectRI getSubject(String pasta) throws RemoteException;
    public HashMap<String, DropBoxSubjectRI> listSubjects() throws RemoteException;

    public void addSubject(String user) throws RemoteException;
    public void shareWith(String user) throws RemoteException;
    //public DBMockup getDb();

}
