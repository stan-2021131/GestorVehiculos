
package org.sergiotan.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Vecinos;
import org.sergiotan.bean.Vehiculos;
import org.sergiotan.db.Conexion;
import org.sergiotan.system.Principal;

public class VehiculosController implements Initializable{
    private Principal escenarioPrincipal;
     private enum operaciones {NUEVO, ELIMINAR, GUARDAR, ACTUALIZAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Vehiculos>listaVehiculos;
    private ObservableList<Vecinos>listaVecinos;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtMarca;
    @FXML private TextField txtModelo;
    @FXML private TextField txtTipoVehiculo;
    @FXML private ComboBox cmbNIT;
    @FXML private TableView tblVehiculos;
    @FXML private TableColumn colPlaca;
    @FXML private TableColumn colMarca;
    @FXML private TableColumn colModelo;
    @FXML private TableColumn colTipoVehiculo;
    @FXML private TableColumn colNit;
    @FXML private Button btnAgregar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbNIT.setItems(getVecinos());
    }

    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnEliminar.setDisable(true);
                btnEditar.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                btnEditar.setDisable(false);
                btnEliminar.setDisable(false);
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void eliminar(){
        if(tblVehiculos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null , "Seleccione un elemento");
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "Desea eliminar este elemenot? " , "Eliminar registro", JOptionPane.YES_NO_OPTION);
            if(respuesta  == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarVehiculo(?)}");
                    procedimiento.setString(1, ((Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem()).getPlaca());
                    procedimiento.execute();
                    listaVehiculos.remove(tblVehiculos.getSelectionModel().getSelectedIndex());
                    limpiarControles();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void editar(){
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblVehiculos.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "Seleccione un elemento");
                }
                else{
                activarControles();
                btnAgregar.setDisable(true);
                btnEliminar.setDisable(true);
                btnEditar.setText("Actualizar");
                tipoDeOperacion = operaciones.ACTUALIZAR;
                cmbNIT.setDisable(true);
                }
                break;
            case ACTUALIZAR:
                desactivarControles();
                actualizar();                
                cargarDatos();
                limpiarControles();
                btnAgregar.setDisable(false);
                btnEliminar.setDisable(false);
                break;
        }
    }
    
    public void guardar(){
        Vehiculos registro = new Vehiculos();
        registro.setPlaca(txtPlaca.getText());
        registro.setMarca(txtMarca.getText());
        registro.setModelo(txtModelo.getText());
        registro.setTipoDeVehiculo(txtTipoVehiculo.getText());
        registro.setVecinos_NIT(((Vecinos)cmbNIT.getSelectionModel().getSelectedItem()).getNIT());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarVehiculo(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getPlaca());
            procedimiento.setString(2, registro.getMarca());
            procedimiento.setString(3, registro.getModelo());
            procedimiento.setString(4, registro.getTipoDeVehiculo());
            procedimiento.setString(5, registro.getVecinos_NIT());
            ResultSet resultado = procedimiento.executeQuery();
            listaVehiculos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarVehiculo(?,?,?,?)}");
            Vehiculos registro = (Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem();
            registro.setMarca(txtMarca.getText());
            registro.setModelo(txtModelo.getText());
            registro.setTipoDeVehiculo(txtTipoVehiculo.getText());
            procedimiento.setString(1, registro.getPlaca());
            procedimiento.setString(2, registro.getMarca());
            procedimiento.setString(3, registro.getModelo());
            procedimiento.setString(4, registro.getTipoDeVehiculo());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void seleccionarElementos(){
        if(tblVehiculos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Seleccione un elemento");
        }else{
            txtPlaca.setText(String.valueOf(((Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem()).getPlaca()));
            txtMarca.setText(String.valueOf(((Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem()).getMarca()));
            txtModelo.setText(String.valueOf(((Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem()).getModelo()));
            txtTipoVehiculo.setText(String.valueOf(((Vehiculos)tblVehiculos.getSelectionModel().getSelectedItem()).getTipoDeVehiculo()));
        }
    }
    
    public void cancelar(){
        desactivarControles();
        limpiarControles();
    }
    
    public void desactivarControles(){
        txtPlaca.setEditable(false);
        txtMarca.setEditable(false);
        txtModelo.setEditable(false);
        txtTipoVehiculo.setEditable(false);
        cmbNIT.setDisable(true);
        btnCancelar.setDisable(true);
    }
    public void activarControles(){
        txtPlaca.setEditable(true);
        txtMarca.setEditable(true);
        txtModelo.setEditable(true);
        txtTipoVehiculo.setEditable(true);
        cmbNIT.setDisable(false);
        btnCancelar.setDisable(false);
    }
    public void limpiarControles(){
        txtPlaca.setText(null);
        txtMarca.setText(null);
        txtModelo.setText(null);
        txtTipoVehiculo.setText(null);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
        public void cargarDatos(){
            tblVehiculos.setItems(getVehiculos());
            colPlaca.setCellValueFactory(new PropertyValueFactory<Vehiculos, String> ("placa"));
            colMarca.setCellValueFactory(new PropertyValueFactory<Vehiculos, String> ("marca"));
            colModelo.setCellValueFactory(new PropertyValueFactory<Vehiculos, String> ("modelo"));
            colTipoVehiculo.setCellValueFactory(new PropertyValueFactory<Vehiculos, String> ("tipoDeVehiculo"));
            colNit.setCellValueFactory(new PropertyValueFactory<Vehiculos, String> ("Vecinos_NIT"));
        }
    
        public ObservableList<Vehiculos> getVehiculos(){
        ArrayList<Vehiculos> lista = new ArrayList<Vehiculos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarVehiculos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Vehiculos(
                        resultado.getString("placa"),
                        resultado.getString("marca"),
                        resultado.getString("modelo"),
                        resultado.getString("tipoDeVehiculo"),
                        resultado.getString("Vecinos_NIT")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaVehiculos = FXCollections.observableArrayList(lista);
    }
        
        public ObservableList<Vecinos>getVecinos(){
        ArrayList<Vecinos> lista = new ArrayList<Vecinos>();
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarVecinos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Vecinos(
                        resultado.getString("NIT"),
                        resultado.getLong("DPI"),
                        resultado.getString("nombres"),
                        resultado.getString("apellidos"),
                        resultado.getString("Direccion"),
                        resultado.getString("Municipalidad"),
                        resultado.getInt("codigoPostal"),
                        resultado.getString("telefono")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaVecinos = FXCollections.observableArrayList(lista);
    }
}
