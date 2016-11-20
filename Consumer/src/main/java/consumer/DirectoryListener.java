package consumer;

import converter.JsonConverter;
import data.Constants;
import messages.DirectoryMessage;
import sender.DatabaseSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * Created by nicob on 02.11.2016.
 * class for checking the directory for updates
 */

@SuppressWarnings({"StatementWithEmptyBody", "unchecked"})
public class DirectoryListener implements Runnable {

    //instance for the singleton pattern
    private static DirectoryListener instance;

    //filePath to watch
    private String filePath;

    private DirectoryListener(String filePath) {
        this.filePath = filePath;
    }

    /**
     *
     * @param filePath: directory to watch
     * @return the instance of the directory listener
     */
    public static DirectoryListener getDirectoryListener(String filePath){
        if (instance == null){
            instance = new DirectoryListener(filePath);
        }
        return instance;
    }

    private static void checkDirectory(Path path) {
        try {
            //check if given path is a folder
            boolean isFolder = (boolean) Files.getAttribute(path, "basic:isDirectory", LinkOption.NOFOLLOW_LINKS);

            if (!isFolder){
                throw new IllegalArgumentException("Path: " + path + " is not a folder!");
            }
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }

        FileSystem fileSystem = path.getFileSystem();

        try (WatchService watchService = fileSystem.newWatchService()){
            path.register(watchService, ENTRY_CREATE); //if other events (ENTRY_MODIFY/ENTRY_DELETE are desired, attach by comma

            WatchKey watchKey;

            while (true) {
                watchKey = watchService.take();

                WatchEvent.Kind kind;
                //all events in the given folder are noticed
                for (WatchEvent watchEvent : watchKey.pollEvents()) {
                    kind = watchEvent.kind();
                    if (kind == OVERFLOW) {
                        //empty if-block; only file-create is important
                    }
                    else if (kind == ENTRY_CREATE){
                        Path filePath = ((WatchEvent<Path>) watchEvent).context();
                        String file = path + fileSystem.getSeparator() + filePath.toString();
                        //buffered reader for the newly created file
                        BufferedReader data = new BufferedReader(new FileReader(file));
                        String jsonString = data.readLine();
                        while (jsonString != null){
                            //get the line and convert it to a directory message
                            DirectoryMessage message = JsonConverter.getInstance().getDirectoryMessage(jsonString);
                            message.setOrderNumber(AppConsumer.getCurrentOrderNumber());

                            if (!Constants.TESTING) {
                                //send message to the database
                                DatabaseSender.getDatabaseSender().insertMessage(message);
                            }
                            System.out.println(message);

                            //read next line
                            jsonString = data.readLine();
                        }
                        data.close();
                    }
                }

                if (!watchKey.reset()){
                    break;
                }
            }
        } catch (IOException | InterruptedException ioEx) {
            ioEx.printStackTrace();
        }
    }

    /**
     * start the activities of the thread
     */
    @Override
    public void run() {
        File directory = new File(filePath);
        checkDirectory(directory.toPath());
    }
}