package edu.ufp.inf.sd.dropbox.server;

import edu.ufp.inf.sd.dropbox.client.ObserverRI;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DropBoxSubjectThread extends Thread {

    DropBoxSubjectObserverRI dropBoxObserverRI;

    private VisitorFoldersOperationsI visitorFolderOperation;


    public DropBoxSubjectThread(DropBoxSubjectObserverRI observer, VisitorFoldersOperationsI singletonFolderState) {

        Logger.getLogger(this.getClass().getName()).log(Level.INFO, " Received Reply: = " + singletonFolderState);
        this.dropBoxObserverRI = observer;
        this.visitorFolderOperation = singletonFolderState;
    }

    @Override
    public void run() {

        DropBoxSubjectImpl.runReplyObserver(dropBoxObserverRI, visitorFolderOperation);
    }

}
