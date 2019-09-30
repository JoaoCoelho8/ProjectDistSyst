package edu.ufp.inf.sd.dropbox.client;

import edu.ufp.inf.sd.dropbox.server.DropBoxSubjectObserverRI;
import edu.ufp.inf.sd.dropbox.server.DropBoxSubjectRI;
import edu.ufp.inf.sd.dropbox.server.VisitorFoldersOperationsI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ObserverRI extends DropBoxSubjectObserverRI {
    public void update() throws RemoteException;
    public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException;

}
