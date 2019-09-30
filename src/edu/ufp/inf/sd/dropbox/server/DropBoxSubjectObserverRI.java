package edu.ufp.inf.sd.dropbox.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DropBoxSubjectObserverRI extends Remote {
    public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException;
}
