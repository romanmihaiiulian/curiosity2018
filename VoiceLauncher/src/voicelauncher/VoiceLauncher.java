/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voicelauncher;

/**
 *
 * @author adrian
 * @url https://procurity.wordpress.com/2016/09/10/make-your-own-voice-command-app-using-java-and-sphinx4/
 */
//Imports
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.io.IOException;

/**
 *
 * @author adphilip
 */
public class VoiceLauncher {

    public static void main(String[] args) throws IOException {
        // Configuration Object
        Configuration configuration = new Configuration();

        // Set path to the acoustic model.
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        // Set path to the dictionary.
        configuration.setDictionaryPath("src/config/0507.dic");
        // Set path to the language model.
        configuration.setLanguageModelPath("src/config/0507.lm");

        //Recognizer Object, Pass the Configuration object
        LiveSpeechRecognizer recognize = new LiveSpeechRecognizer(configuration);

        //Start Recognition Process (The bool parameter clears the previous cache if true)
        recognize.startRecognition(true);
        
        //Create SpeechResult Object
        SpeechResult result;
 
        //Checking if recognizer has recognized the speech
        while ((result = recognize.getResult()) != null) {
            //Get the recognize speech
            String command = result.getHypothesis();
            
             //Match recognized speech with our commands
            if(command.equalsIgnoreCase("open file manager")) {
                System.out.println("File Manager Opened!");
            } else if (command.equalsIgnoreCase("close file manager")) {
                System.out.println("File Manager Closed!");
            } else if (command.equalsIgnoreCase("open browser")) {
                System.out.println("Browser Opened!");
            } else if (command.equalsIgnoreCase("close browser")) {
                System.out.println("Browser Closed!");
            }
            
           // System.out.println("Command out : " + command);

        }
        

    }
}
