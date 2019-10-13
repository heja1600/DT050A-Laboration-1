import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import se.miun.distsys.messages.ChatMessage;
import se.miun.distsys.messages.LoginMessage;
import se.miun.distsys.messages.LogoutMessage;
import se.miun.distsys.messages.Message;
import se.miun.models.User;

public class Logger {
    File file;

    Logger() {
        System.out.println("Creating file");
        file = new File("logger.txt");
        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
            writer.print("");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public <T extends Message> void log(T message)
    {
        String messageType = "";
        if(message instanceof ChatMessage) {
            messageType = "ChatMessage";
        }
        // else if(message instanceof LoginMessage)
        // {
        //     messageType = "LoginMessage";
        // }
        // else if(message instanceof LogoutMessage) 
        // {
        //     messageType = "LogoutMessage";
        // }
        // else if(message instanceof SendLoginMessage)
        // {
        //     messageType = "SendLoginMessage";
        // }
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String string = contentBuilder.toString() + "messageType: "+ messageType + " \n vectorOrder -> " + ""+ "\n\n";
        try (
            BufferedReader reader = new BufferedReader(new StringReader(string));
            PrintWriter writer = new PrintWriter(new FileWriter(file));
        ) {
            reader.lines().forEach(line -> writer.println(line));
        } catch (Exception e) {
            System.out.println("error logging" + e.getStackTrace());
        }
    }
}