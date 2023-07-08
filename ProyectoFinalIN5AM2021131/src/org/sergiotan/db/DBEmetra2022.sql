/*
	Nombre: Sergio Estuardo Tan Coroma
    Carné: 2021131
    Código Técnico: IN5AM
    Fecha de Creación: 26/09/2022
    Fechas de Modificación: 26/09/2022
*/

Drop database if exists DBEmetra2022;
Create database DBEmetra2022;
Use DBEmetra2022;

-- Entidad Vecinos
Create table Vecinos(
	NIT varchar(15) not null,
    DPI bigint(13) not null,
    nombres varchar(100) not null,
    apellidos varchar (100) not null,
    direccion varchar(200) not null,
    municipalidad varchar(45) not null,
    codigoPostal int not null,
    telefono varchar(8) not null,
    primary key (NIT)
);

-- Entidad Vehiculos
Create table Vehiculos(
	placa varchar(15) not null,
    marca varchar(45) not null,
    modelo varchar(45) not null,
    tipoDeVehiculo varchar(60) not null,
    Vecinos_NIT varchar(15) not null,
    primary key (placa),
    foreign key (Vecinos_NIT) references Vecinos(NIT)
);


-- Procedimientos Almacenados
-- Entidad Vecinos
-- Agregar Vecino
Delimiter //
	Create procedure sp_AgregarVecino(in NIT varchar(15), in DPI bigInt(13), in nombres varchar(100), in apellidos varchar(100), 
										in direccion varchar(200), in municipalidad varchar(45), in codigoPostal int, in telefono varchar(8))
		Begin
			Insert into Vecinos (NIT, DPI, nombres, apellidos, direccion, municipalidad, codigoPostal, telefono)
				values(NIT, DPI, nombres, apellidos, direccion, municipalidad, codigoPostal, telefono);
        End //
Delimiter ;

Call sp_AgregarVecino('39060926', 6548620321015, 'Sergio', 'Tan', 'Zona 1 Mixco','Mixco', '5', '50156315');
-- Call sp_AgregarVecino('39065012', 8620315462031, 'Luis', 'Ramirez', 'Zona 14 Guatemala','Guatemala', '1', '46895320');

-- Listar Vecinos
Delimiter //
	Create procedure sp_ListarVecinos()
		Begin
			select NIT, 
				DPI,
                nombres,
                apellidos,
                direccion,
                municipalidad,
                codigoPostal,
                telefono
			from Vecinos;
					
        End //
Delimiter ;

-- Call sp_ListarVecinos();

-- Buscar Vecino
Delimiter //
	Create procedure sp_BuscarVecino(in NitB varchar(15))
		Begin 
			Select NIT, 
				DPI,
                nombres,
                apellidos,
                direccion,
                municipalidad,
                codigoPostal,
                telefono
			from Vecinos where NitB = NIT; 
        End //
Delimiter ;

-- Call sp_BuscarVecino('39060926');

-- Eliminar Vecino
Delimiter //
	Create procedure sp_EliminarVecino(in NitB varchar(15))
		Begin
			Delete from Vecinos where NitB = NIT;
        End //
Delimiter ;

-- Call sp_EliminarVecino('39065012');

-- Editar Vecino
Delimiter //
	Create procedure sp_EditarVecino(in NitB varchar(15),in DPIB bigInt(13), in nombresB varchar(100), in apellidosB varchar(100), 
										in direccionB varchar(200), in municipalidadB varchar(45), in codigoPostalB int, in telefonoB varchar(8))
			Begin
				update Vecinos set
                    DPI = DPIB,
                    nombres = nombresB,
                    apellidos = apellidosB,
                    direccion = direccionB,
                    municipalidad = municipalidadB,
                    codigoPostal = codigoPostalB,
                    telefono = telefonoB
                    where NitB = NIT;
            End //
Delimiter ;

-- Call sp_EditarVecino('39060926', 1236547890258, 'Sergio Estuardo', 'Tan Coromac', 'Zona 1', 'Mixco', 3, '50170415');

-- Entidad Vehiculos
-- Crear Vehiculo
Delimiter //
	Create procedure sp_AgregarVehiculo(in placa varchar(15), in marca varchar(45), in modelo varchar(45), in tipoDeVehiculo varchar(60), in Vecinos_NIT varchar(15))
		Begin
			Insert into Vehiculos (placa, marca, modelo, tipoDeVehiculo, Vecinos_NIT)
				Values(placa, marca, modelo, tipoDeVehiculo, Vecinos_NIT) ;
        End //
Delimiter ;

Call sp_AgregarVehiculo('FR56203D4856921', 'Kia', 'Rio', 'Auto', '39060926');
Call sp_AgregarVehiculo('FR56203D4856777', 'Kia', 'Rdo', 'Camioneta', '39060926');

-- Listar Vehiculos
Delimiter //
	Create procedure sp_ListarVehiculos()
		Begin
			Select placa,
				marca,
                modelo, 
                tipoDeVehiculo,
                Vecinos_NIT
			from Vehiculos;
        End //
Delimiter ;

-- Call sp_ListarVehiculos();

-- Buscar Vehiculo 
Delimiter //
	Create procedure sp_BuscarVehiculo(in placaB varchar(15))
		Begin
			Select placa,
				marca,
                modelo, 
                tipoDeVehiculo,
                Vecinos_NIT
			from Vehiculos where placaB = placa;
        End //
Delimiter ;

-- Eliminar Veiculo
Delimiter //
	Create procedure sp_EliminarVehiculo(in placaB varchar(15))
    Begin 
		Delete from Vehiculos where placaB = placa;
    End //
Delimiter ;

-- Call sp_EliminarVehiculo('FR56203D4856777');

-- Editar Vehiculo
Delimiter //
	Create procedure sp_EditarVehiculo(in placaB varchar(15), in marcaB varchar(45), in modeloB varchar(45), in tipoDeVehiculoB varchar(60))
		Begin
			Update Vehiculos set
				marca = marcaB,
                modelo = modeloB,
                tipoDeVehiculo = tipoDeVehiculo
                where placaB = placa;
        End //
Delimiter ;

-- Call sp_EditarVehiculo('FR56203D4856777', 'Toyota', 'AAA', 'Auto');