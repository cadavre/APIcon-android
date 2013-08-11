package com.cadavre.APIcon;

import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class provides utils to cache any object to file in package's /cache directory.
 *
 * @author Seweryn Zeman
 * @version 1
 */
public final class ObjectCacher {

    /**
     * Put object into cache.
     *
     * @param object
     *
     * @return true if cached successfully, false otherwise
     */
    public static boolean cacheObject(Object object) {

        StringBuilder fileName = new StringBuilder();
        fileName.append("api_");
        fileName.append(object.getClass().getSimpleName());
        fileName.append("_");
        fileName.append(object.hashCode());
        fileName.append("@");
        fileName.append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        fileName.append(".xml");

        XStream xstream = new XStream();
        String objXml = xstream.toXML(object);
        if (objXml == null || objXml.isEmpty()) {
            return false;
        }

        File cacheDir = APIcon.getInstance().getCacheDir();
        File cacheFile = new File(cacheDir, fileName.toString());
        try {
            Writer writer = new BufferedWriter(new FileWriter(cacheFile));
            writer.write(objXml);
            writer.close();

            return true;
        } catch (IOException e) {
            Logger.dummyException(e);
        }

        return false;
    }

    /**
     * Get object from cache.
     *
     * @param file
     *
     * @return Object
     */
    public static Object getObjectFromCache(File file) {

        XStream xstream = new XStream();

        return xstream.fromXML(file);
    }

    /**
     * Get list of all Files that are APIcon cached objects.
     *
     * @return File[]
     */
    public static File[] getCachedObjectFiles() {

        File cacheDir = APIcon.getInstance().getCacheDir();

        return cacheDir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String filename) {

                return filename.startsWith("api_") && filename.endsWith(".xml");
            }
        });
    }

    /**
     * Get cached files size in bytes
     *
     * @return long
     */
    public static long getCachedFilesSize() {

        long cacheSize = 0;
        for (File file : getCachedObjectFiles()) {
            cacheSize += file.length();
        }

        return cacheSize;
    }
}
