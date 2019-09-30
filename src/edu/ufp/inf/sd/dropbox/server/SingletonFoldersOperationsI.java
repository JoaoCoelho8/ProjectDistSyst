package edu.ufp.inf.sd.dropbox.server;

import java.io.File;

public interface SingletonFoldersOperationsI {
    public Boolean createFile(String fname);
    public Boolean deleteFile(String fname);
    public Boolean renameFile(String fname, String fnewname);
    public Boolean createFolder(String folderToCreate);
    public Boolean deleteFolder(String foldertodelete);
    public Boolean renameFolder(String folder, String newnamefolder);
    public Boolean move(String fname, String path);
    public Boolean listFolderContent(String path, String tabs);
}
