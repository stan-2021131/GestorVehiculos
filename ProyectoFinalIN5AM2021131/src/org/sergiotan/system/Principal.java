/*
Nombre Sergio Estuardo Tan Coromac 2021131 IN5AM 
Fecha Creacion = 26/09/2022
Fechas de Modificacion: 26/09/2022
                        27/09/2022
 */
package org.sergiotan.system;

import java.io.InputStream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.sergiotan.controller.MenuPrincipalController;
import org.sergiotan.controller.VecinosController;
import org.sergiotan.controller.VehiculosController;

/**
 *
 * @author informatica
 */
public class Principal extends Application {
    private final String PAQUETE_VISTA = "/org/sergiotan/view/";
    private Scene escena; 
    private Stage escenarioPrincipal;
    @Override
    public void start(Stage escenarioPrincipal)throws Exception {
        this.escenarioPrincipal = escenarioPrincipal;
        menuPrincipal();
        escenarioPrincipal.show();
    }

    public void menuPrincipal(){
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController) cambiarEscena("MenuPrincipalView.fxml", 492, 400);
            menuPrincipal.setEscenarioPrincipal(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void ventanaVecinos(){
        try{
            VecinosController ventanaVecinos = (VecinosController) cambiarEscena("VecinosView.fxml", 1009, 400);
            ventanaVecinos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaVehiculos(){
        try{
            VehiculosController ventanaVehiculos = (VehiculosController) cambiarEscena("VehiculoView.fxml", 830, 400);
            ventanaVehiculos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();        
        return resultado;
    }
    
}
