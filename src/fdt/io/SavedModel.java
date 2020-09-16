
package fdt.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * To Save and Load the models.
 * 
 * @author Mohammed Jabreel
 */
public class SavedModel {

    /**
     *
     * @param model
     * @param fileName
     */
    public static void save(Serializable model, String fileName) {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(fileName))) {
            writer.writeObject(model);
            System.out.println("Saved!");
        } catch (IOException ex) {
            Logger.getLogger(SavedModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static Serializable load(String fileName) {
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(fileName))) {
            Serializable model = (Serializable) reader.readObject();
            return model;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SavedModel.class.getName()).log(Level.SEVERE, null, ex);
            return null;

        }

    }
}
