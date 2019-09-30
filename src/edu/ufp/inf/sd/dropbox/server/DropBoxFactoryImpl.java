package edu.ufp.inf.sd.dropbox.server;

import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;

public class DropBoxFactoryImpl extends UnicastRemoteObject implements DropBoxFactoryRI {

    private static final String path = "C:\\Users\\PC\\IdeaProjects\\ProjetoSD\\data\\Users";
    private static final String pathServer = "C:\\Users\\PC\\IdeaProjects\\ProjetoSD\\data\\Servers\\primary";
    public HashMap<String, DropBoxSessionRI> sessions = new HashMap<String, DropBoxSessionRI>();
    private DBMockup db = new DBMockup();
    //private ElementFolderRI elementFolderRI = (ConcreteElementFolderBooksImpl) new ConcreteElementFolderBooksImpl(path);

    // Uses RMI-default sockets-based transport
    // Runs forever (do not passivates) - Do not needs rmid (activation deamon)
    // Constructor must throw RemoteException due to export()
    public DropBoxFactoryImpl() throws RemoteException {
        // Invokes UnicastRemoteObject constructor which exports remote object
        super();
    }

    /**
     * recebe um user e uma password e faz o login
     * codificamos essa password e ao inserir a passeword, a mesma esta oculta logo nao a conseguimos ver ao inserir
     * vamos ver se esse user existe na base de dados, se existir criamos uma nova sessão
     * @param user
     * @param password
     * @return a sessão iniciada desse user
     * @throws RemoteException
     * @throws NoSuchProviderException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @Override
    public DropBoxSessionRI login(String user, String password) throws RemoteException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }

        String pass = sb.toString();

        if (db.exists(user, pass)) {

            DropBoxSessionRI s = new DropBoxSessionImpl(this, user, pass, db.subjects.get(user), db.subjectsServer.get(user));


            db.sessions.put(user, s);
            sessions.put(user, s);
//
//            for (String u:db.subjects.keySet()) {
//                System.out.println("todas "+u+ db.subjects.get(u));
//            }

//            for (String u:s.listSubjects().keySet()) {
//                System.out.println("Do ze "+((DropBoxSessionImpl) s).subjects.get(u));
//            }
            return s;
        } else {
            return null;
        }
    }

    /**
     * para fazer um registo recebemos um user e uma password
     * codificamos essa password e ao inserir a passeword, a mesma esta oculta logo nao a conseguimos ver ao inserir
     * vamos à base de dados e registamos esse user com o username password e o path da sua dopBox
     * criamos uma dropBox para esse user com uma pasta, com o seu nome, la dentro, atraves do visitor e vamos à base de dados á hashMap de subject e fazemos o put
     * fazemos a mesma coisa descrita em cima mas desta vez do lado do server
     * @param user
     * @param password
     * @return true se tudo descrito em cima aconteceu, se não, false
     * @throws RemoteException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     */
    @Override
    public Boolean register(String user, String password) throws RemoteException, NoSuchAlgorithmException, NoSuchProviderException {

        String pathuser = path.concat("\\DropBox"+user+"\\"+user);


        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        String pass = sb.toString();

        //System.out.println(((DropBoxSubjectImpl) subjectRI).getPasta());
        if (db.register(user, pass, pathuser)) {

            DropBoxSubjectRI subjectRI= new DropBoxSubjectImpl("C:\\Users\\PC\\IdeaProjects\\ProjetoSD\\data\\Users");
            VisitorFoldersOperationCreateFolder folder= new VisitorFoldersOperationCreateFolder("\\DropBox"+user);
            subjectRI.acceptVisitor(folder);

            String path1= "C:\\Users\\PC\\IdeaProjects\\ProjetoSD\\data\\Users\\DropBox"+user;
            DropBoxSubjectRI subjectRI2= new DropBoxSubjectImpl(path1);
            VisitorFoldersOperationCreateFolder folder2= new VisitorFoldersOperationCreateFolder(user);
            subjectRI2.acceptVisitor(folder2);

            String path2= "C:\\Users\\PC\\IdeaProjects\\ProjetoSD\\data\\Users\\DropBox"+user+"\\"+user;
            DropBoxSubjectRI subjectRIU= new DropBoxSubjectImpl(path2);
            db.subjects.put(user, subjectRIU);


            DropBoxSubjectImpl subjectServerRI= new DropBoxSubjectImpl("C:\\Users\\PC\\IdeaProjects\\ProjetoSD\\data\\Servers\\primary");
            VisitorFoldersOperationCreateFolder folder3= new VisitorFoldersOperationCreateFolder(user);
            subjectServerRI.acceptVisitor(folder3);
            subjectServerRI.setPasta(pathServer+"\\"+user);
            db.subjectsServer.put(user, subjectServerRI);

            subjectServerRI.notifyAllObservers(folder);
            subjectServerRI.notifyAllObservers(folder2);
            subjectServerRI.notifyAllObservers(folder3);


            return true;

        } else {
            return false;
        }
    }

    protected DBMockup getDb() {
        return db;
    }
}
