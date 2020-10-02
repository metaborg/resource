package mb.resource.classloader;

import mb.resource.fs.FSResource;

import java.util.ArrayList;

public class ClassloaderResourceLocations {
    public final ArrayList<FSResource> directories;
    public final ArrayList<JarFileWithPath> jarFiles;

    public ClassloaderResourceLocations(ArrayList<FSResource> directories, ArrayList<JarFileWithPath> jarFiles) {
        this.directories = directories;
        this.jarFiles = jarFiles;
    }
}
