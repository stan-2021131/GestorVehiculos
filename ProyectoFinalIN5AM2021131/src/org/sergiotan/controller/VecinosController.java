/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.sergiotan.bean.Vecinos;
import org.sergiotan.db.Conexion;
import org.sergiotan.system.Principal;

/**
 *
 * @author informatica
 */
public class VecinosController implements Initializable{
    private enum operaciones {NUEVO , NINGUNO, GUARDAR, ACTUALIZAR}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Vecinos>listaVecinos;
    @FXML private TextField txtNIT;
    @FXML private TextField txtDPI;
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtMunicipalidad;
    @FXML private TextField txtCodPostal;
    @FXML private TextField txtTelefono;
    @FXML private TableView tblVecinos;
    @FXML private TableColumn colNIT;
    @FXML private TableColumn colDPI;
    @FXML private TableColumn colNombres;
    @FXML private TableColumn colApellidos;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colMunicipalidad;
    @FXML private TableColumn colCodPostal;
    @FXML private TableColumn colTelefono;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnCancelar;

    public void cargarDatos(){
        tblVecinos.setItems(getVecinos());
        colNIT.setCellValueFactory(new PropertyValueFactory<Vecinos, String> ("NIT"));
        colDPI.setCellValueFactory(new PropertyValueFactory <Vecinos, Integer>("DPI"));
        colNombres.setCellValueFactory(new PropertyValueFactory <Vecinos, String>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory <Vecinos, String>("apellidos"));
        colDireccion.setCellValueFactory(new PropertyValueFactory <Vecinos, String>("direccion"));
        colMunicipalidad.setCellValueFactory(new PropertyValueFactory <Vecinos, String>("municipalidad"));
        colCodPostal.setCellValueFactory(new PropertyValueFactory <Vecinos, Integer>("codigoPostal"));
        colTelefono.setCellValueFactory(new PropertyValueFactory <Vecinos, String>("telefono"));
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
    
    public void seleccionarElementos(){
        if(tblVecinos.getSelectionModel().getSelectedItem()==null){
            JOptionPane.showMessageDialog(null, "Seleccione un elemento");
        }else{
            txtNIT.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getNIT()));
            txtDPI.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getDPI()));
            txtNombre.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getNombres()));
            txtApellido.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getApellidos()));
            txtDireccion.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getDireccion()));
            txtMunicipalidad.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getMunicipalidad()));
            txtCodPostal.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getCodigoPostal()));
            txtTelefono.setText(String.valueOf(((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getTelefono()));
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
    }
//    
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnEditar.setDisable(true);
                btnEliminar.setDisable(true);
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
        if(tblVecinos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Seleccione un elemento");
        }else{
            int respuesta = JOptionPane.showConfirmDialog(null, "Desea Eliminar este elemento","ELiminar Registro", JOptionPane.YES_NO_OPTION);
            if(respuesta == JOptionPane.YES_OPTION){
                try {
                    PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarVecino(?)}");
                    procedimiento.setString(1, ((Vecinos)tblVecinos.getSelectionModel().getSelectedItem()).getNIT());
                    procedimiento.execute();
                    listaVecinos.remove(tblVecinos.getSelectionModel().getSelectedIndex());
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
                if(tblVecinos.getSelectionModel().getSelectedItem() == null){
                    JOptionPane.showMessageDialog(null, "Seleccione un registro");
                }else{
                    activarControles();
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnEditar.setText("Actualizar");
                    tipoDeOperacion = operaciones.ACTUALIZAR;
                }
                break;
            case ACTUALIZAR:
                    actualizar();
                    desactivarControles();
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnEditar.setText("Editar");
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                break;
        }
    }
    
    
    public void guardar(){
        Vecinos registro = new Vecinos();
        registro.setNIT(txtNIT.getText());
        registro.setDPI(Long.parseLong(txtDPI.getText()));
        registro.setNombres(txtNombre.getText());
        registro.setApellidos(txtApellido.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setMunicipalidad(txtMunicipalidad.getText());
        registro.setCodigoPostal(Integer.parseInt(txtCodPostal.getText()));
        registro.setTelefono(txtTelefono.getText());
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarVecino(?,?,?,?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNIT());
            procedimiento.setLong(2, registro.getDPI());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getMunicipalidad());
            procedimiento.setInt(7, registro.getCodigoPostal());
            procedimiento.setString(8, registro.getTelefono());
            ResultSet resultado = procedimiento.executeQuery();
            listaVecinos.add(registro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void actualizar(){
        try {
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarVecino(?,?,?,?,?,?,?,?)}");
            Vecinos registro = (Vecinos) tblVecinos.getSelectionModel().getSelectedItem();
            registro.setDPI(Long.parseLong(txtDPI.getText()));
            registro.setNombres(txtNombre.getText());
            registro.setApellidos(txtApellido.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setMunicipalidad(txtMunicipalidad.getText());
            registro.setCodigoPostal(Integer.parseInt(txtCodPostal.getText()));
            registro.setTelefono(txtTelefono.getText());
            procedimiento.setString(1, registro.getNIT());
            procedimiento.setLong(2, registro.getDPI());
            procedimiento.setString(3, registro.getNombres());
            procedimiento.setString(4, registro.getApellidos());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getMunicipalidad());
            procedimiento.setInt(7, registro.getCodigoPostal());
            procedimiento.setString(8, registro.getTelefono());
            procedimiento.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void cancelar(){
        desactivarControles();
        limpiarControles();
    }
    
    public void activarControles(){
        txtNIT.setEditable(true);
        txtDPI.setEditable(true);
        txtNombre.setEditable(true);
        txtApellido.setEditable(true);
        txtDireccion.setEditable(true);
        txtMunicipalidad.setEditable(true);
        txtCodPostal.setEditable(true);
        txtTelefono.setEditable(true);
        btnCancelar.setDisable(false);
    }
    
    public void desactivarControles(){
        txtNIT.setEditable(false);
        txtDPI.setEditable(false);
        txtNombre.setEditable(false);
        txtApellido.setEditable(false);
        txtDireccion.setEditable(false);
        txtMunicipalidad.setEditable(false);
        txtCodPostal.setEditable(false);
        txtTelefono.setEditable(false);
        btnCancelar.setDisable(true);
    }
    
    public void limpiarControles(){
        txtNIT.setText(null);
        txtDPI.setText(null);
        txtNombre.setText(null);
        txtApellido.setText(null);
        txtDireccion.setText(null);
        txtMunicipalidad.setText(null);
        txtCodPostal.setText(null);
        txtTelefono.setText(null);
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
}
