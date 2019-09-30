package edu.ufp.inf.sd.dropbox.server;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class simulates a DBMockup for managing users and books.
 *
 * @author rmoreira
 *
 */
public class DBMockup {

    public ArrayList<User> users;
    public HashMap<String, DropBoxSessionRI> sessions = new HashMap<String, DropBoxSessionRI>();
    public HashMap<String, DropBoxSubjectRI> subjects = new HashMap<String, DropBoxSubjectRI>();
    public HashMap<String, DropBoxSubjectRI> subjectsServer = new HashMap<String, DropBoxSubjectRI>();

    //private ArrayList<User> 1;
    //private ArrayList<User> 2;

    /**
     * This constructor inits the database with some users.
     */
    public DBMockup() {

        users = new ArrayList();

        users.add(new User("Joao", "3be7d1b2096d2ca466f57938ef9d0bb9", "C:\\Users\\pacm1\\IdeaProjects\\ProjetoSD\\data\\Users\\Joao"));
        users.add(new User("Pedro", "3be7d1b2096d2ca466f57938ef9d0bb9", "C:\\Users\\pacm1\\IdeaProjects\\ProjetoSD\\data\\Users\\Pedro"));
    }


    /**
     * Registers a new user.
     *
     * @param u username
     * @param p passwd
     */
    public boolean register(String u, String p, String path) throws NoSuchProviderException, NoSuchAlgorithmException {
        if (!exists(u, p)) {

            users.add(new User(u, p, path));
            return true;
        }
        return false;
    }




    /**
     * Checks the credentials of an user.
     *
     * @param u username
     * @param p passwd
     * @return true se existir e false se não existir
     */
    public boolean exists(String u, String p) throws NoSuchProviderException, NoSuchAlgorithmException {
        for (User usr : this.users) {

            if (usr.getUname().compareTo(u) == 0 && usr.getPword().compareTo(p) == 0) {
                return true;
            }
        }
        return false;
        //return ((u.equalsIgnoreCase("guest") && p.equalsIgnoreCase("ufp")) ? true : false);
    }

}


