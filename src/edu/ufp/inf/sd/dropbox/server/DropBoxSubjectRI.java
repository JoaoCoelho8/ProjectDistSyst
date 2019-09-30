package edu.ufp.inf.sd.dropbox.server;

import edu.ufp.inf.sd.dropbox.client.ObserverRI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxSubjectRI extends DropBoxSubjectObserverRI, Remote {
    public void attach(ObserverRI o) throws RemoteException;
    public void dettach(ObserverRI o) throws RemoteException;
    public String copyPasta() throws RemoteException;
    //public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException;
}
