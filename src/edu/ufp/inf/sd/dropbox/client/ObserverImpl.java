package edu.ufp.inf.sd.dropbox.client;

import edu.ufp.inf.sd.dropbox.server.*;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ObserverImpl implements ObserverRI {
    public String username;
    public DropBoxSubjectRI subjectRI;
    public File folder;
    public SingletonFolderOperationsUser singletonStateFolder;
    public DBMockup db= new DBMockup();

    //public HashMap<String, DropBoxSessionRI> last = new HashMap<String, DropBoxSessionRI>();
    //public HashMap<String, DropBoxSessionRI> current = new HashMap<String, DropBoxSessionRI>();

    public ObserverImpl(String username, DropBoxSubjectRI subjectRI) throws RemoteException {
        this.username = username;
        SingletonFolderOperationsUser.createSingletonFolderOperationsBooks(subjectRI.copyPasta());
        folder= new File(subjectRI.copyPasta());
        export();
        this.subjectRI = subjectRI;
        //this.subjectRI.attach(this);
    }

    public void export() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    public SingletonFolderOperationsUser getSingletonStateFolder(){
        return this.singletonStateFolder;
    }

    @Override
    public void update() throws RemoteException { }

    /**
     * fazer o update
     * @param visitor
     * @return
     * @throws RemoteException
     */
    @Override
    public Object acceptVisitor(VisitorFoldersOperationsI visitor) throws RemoteException {
        return visitor.visitConcreteElementUsers(this.subjectRI);
    }

}
