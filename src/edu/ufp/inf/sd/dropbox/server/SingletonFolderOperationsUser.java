package edu.ufp.inf.sd.dropbox.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SingletonFolderOperationsUser implements SingletonFoldersOperationsI {

    private final File folderUser;

    /** private - Avoid direct instantiation */
    private SingletonFolderOperationsUser(String folder) {
        folderUser = new File(folder);
    }
    
    public synchronized static SingletonFolderOperationsUser createSingletonFolderOperationsBooks(String folder){
        //if (singletonFolderOperationsBooks==null){
        SingletonFolderOperationsUser singletonFolderOperationsBooks = new SingletonFolderOperationsUser(folder);
        //}
        return singletonFolderOperationsBooks;
    }

    /**
     * criar um ficheiro
     * @param fname
     * @return ficheiro criado ou false se nao for possivel criar
     */
    @Override
    public Boolean createFile(String fname) {
        try {
            File newFile = new File(this.folderUser.getAbsolutePath() + "/" + fname);
            return newFile.createNewFile();
        } catch (IOException ex) {
           // Logger.getLogger(SingletonFolderOperationsMagazines.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * eliminar um ficheiro se ele exitir
     * @param fname
     * @return ficheiro eliminado ou false se nao for possivel eliminar
     */
    @Override
    public Boolean deleteFile(String fname) {
            File existingFile = new File(this.folderUser.getAbsolutePath() + "/" + fname);
            if(existingFile.isFile()){
                return existingFile.delete();
            }
            else {
                System.err.println("There is no file @ given path");
                return false;
            }

    }

    /**
     * mudar o nome de um ficheiro de fname para fnewname se ele existir
     * @param fname
     * @param fnewname
     * @return ficheiro com novo nome ou false se nao for possivel
     */
    public Boolean renameFile(String fname, String fnewname) {

        File existingFile = new File(this.folderUser.getAbsolutePath() + "/" + fname);
        File newFile = new File(this.folderUser.getAbsolutePath() + "/" + fnewname);
        if(existingFile.isFile()){
            return existingFile.renameTo(newFile);
        }
        else{
            System.err.println("There is no file @ given path");
            return false;
        }
    }

    /**
     * criar uma pasta se ela ainda não existir com aquele nome
     * @param folderToCreate
     * @return false se não conseguirmos criar a pasta
     */
    public Boolean createFolder(String folderToCreate) {
        System.out.println("Caminho: "+ folderUser.getAbsolutePath());
        File newfolder= new File(this.folderUser.getAbsolutePath()+"/"+ folderToCreate);
        System.out.println("folder");
        if (!newfolder.exists()) {
            System.out.println("creating directory: " + newfolder.getName());
            boolean result = false;

            try{
                newfolder.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created "+ newfolder.getPath());
            }
        }
        return false;
    }

    /**
     * eliminar uma pasta
     * @param foldertodelete
     * @return false se não conseguirmos eliminar a pasta
     */
    public Boolean deleteFolder(String foldertodelete) {
        File folder= new File(this.folderUser.getAbsolutePath()+"/"+ foldertodelete);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println("deleting directory: " + folder.getName());
            boolean result = false;

            try{
                folder.delete();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR deleted");
            }
        }
        return false;
    }

    /**
     * mudar o nome a uma pasta se ela existir
     * @param folder
     * @param newnamefolder
     * @return nome da nova pasta ou entao false se nao for possivel
     */
    public Boolean renameFolder(String folder, String newnamefolder) {
        File existingfolder= new File(this.folderUser.getAbsolutePath()+"/"+ folder);
        File newfolder= new File(this.folderUser.getAbsolutePath()+"/"+ newnamefolder);
        if(existingfolder.isDirectory()){
            return existingfolder.renameTo(newfolder);
        }
        else {
            System.err.println("There is no directory @ given path");
            return false;
        }
    }

    /**
     * mover de pasta ou ficheiro, recebemos o nome dessa pasta ou ficheiro e o destino (path)
     * @param fname
     * @param path
     * @return flase se nao conserguirmos mover
     */
    public Boolean move(String fname, String path) {
        File sourceFile = new File(this.folderUser.getAbsolutePath() + "/" + fname);
        File destFile = new File(path);

        try {
            Files.move(Paths.get(sourceFile.getPath()), Paths.get(destFile.getPath()), REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * listar todos os ficheiros dentro de uma pasta
     * @param path
     * @param tabs
     * @return a lista de todas as pastas e o seu conteudo ou false se nao for possivel
     */
    public Boolean listFolderContent(String path, String tabs) {
        File f= new File(path);
        if(f.exists()){
            System.out.println(tabs + f.getName());
            File[] files = f.listFiles();
            for (File fl : files){
                if(fl.isDirectory()){
                    listFolderContent(fl.getPath(), tabs+"\t");
                }
                else {
                    System.out.println(tabs+"\t" +fl.getName());
                }
            }
            return true;
        }
        else{
            return false;
        }
    }
}

