package edu.ufp.inf.sd.dropbox.server;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;


public interface DropBoxFactoryRI extends Remote {

    DropBoxSessionRI login(String user, String password) throws RemoteException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException;
    Boolean register(String user, String password) throws RemoteException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException;
}
