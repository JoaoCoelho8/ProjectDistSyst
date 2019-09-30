package edu.ufp.inf.sd.dropbox.server;


public class DropBoxSubjectRunnable implements Runnable {

    private DropBoxSubjectObserverRI dropBoxObserverRI;
    private VisitorFoldersOperationsI visitorFolderOperationI;

    public DropBoxSubjectRunnable(DropBoxSubjectObserverRI dropBoxObserverRI, VisitorFoldersOperationsI visitorFolderOperationI) {
        this.dropBoxObserverRI = dropBoxObserverRI;
        this.visitorFolderOperationI = visitorFolderOperationI;
    }

    @Override
    public void run() {

        DropBoxSubjectImpl.runReplyObserver(dropBoxObserverRI, visitorFolderOperationI);

    }
}
