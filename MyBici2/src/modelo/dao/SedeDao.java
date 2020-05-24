package modelo.dao;

import conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.dto.EquipoOficina;
import modelo.dto.Inventario;
import modelo.dto.Mercancia;
import modelo.dto.Proveedor;
import modelo.dto.Sede;
import modelo.dto.Taller;
import modelo.dto.Telefono;

/**
 * Clase de acceso a datos de las sedes
 *
 * @author Santiago Pérez
 * @version 1.0
 * @since 2020-05-13
 */
public class SedeDao implements ISedeDao {

    /**
     *
     * @param sede
     * @return
     */
    @Override
    public boolean crear(Sede sede) {
        try {
            String sql = "call crearSede(?,?);";
            Connection conn = Conexion.conectado();
            CallableStatement call = conn.prepareCall(sql);
            call.setInt("idDireccion", sede.getDireccion().getIdDireccion());
            call.setString("nombreSede", sede.getNombreSede());
            boolean insert = call.execute();
            call.close();
            return insert;
        } catch (SQLException ex) {
            Logger.getLogger(SedeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @param clave
     * @return
     */
    @Override
    public Sede consultar(String clave) {
        Sede sede = null;
        try {
            /*select * from sede where idSede =1;
            select * from inventario where Sede_idSede=3;
            
            select * from objeto as o
            inner join mercancia as m on o.idObjeto = m.Objeto_idObjeto
            where m.Inventario_id_inventario=1;
            
            select * from objeto as o
            inner join equipooficina as e on o.idObjeto = e.Objeto_idObjeto
            where e.Sede_idSede = 3;
            
             */
            String sql = "select * from sede where idSede=" + clave;
            Connection conn = Conexion.conectado();
            PreparedStatement pat = conn.prepareStatement(sql);
            ResultSet rs = pat.executeQuery();
            if (rs.next()) {
                sede = new Sede();
                sede.setIdSede(rs.getInt("idSede"));
                int idDireccion = rs.getInt("idDireccion");
                sede.setNombreSede(rs.getString("nombreSede"));
                sede.setInventario(traerInventario(conn, sede.getIdSede()));
                sede.setDireccion(new DireccionDao().consultar(String.valueOf(idDireccion)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SedeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sede;
    }

    /**
     *
     * @param sede
     * @return
     */
    @Override
    public boolean actualizar(Sede sede) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param clave
     * @return
     */
    @Override
    public boolean eliminar(String clave) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public LinkedList<Sede> listar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param clave
     * @param telefono
     * @return
     */
    @Override
    public boolean agregarTelefono(String clave, Telefono telefono) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param equipoOficina
     * @return
     */
    @Override
    public boolean agregarEquipoOficina(EquipoOficina equipoOficina) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @param taller
     * @return
     */
    @Override
    public boolean agregarTaller(Taller taller) {
        try {
            String sql = "insert into taller (Sede_idSede,totalVentas) values(?,?)";
            Connection conn = Conexion.conectado();
            PreparedStatement pat = conn.prepareStatement(sql);
            pat.setInt(1, taller.getSede().getIdSede());
            pat.setDouble(2, 0);
            boolean insert = pat.execute();
            pat.close();
            return insert;
        } catch (SQLException ex) {
            Logger.getLogger(SedeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @param inventario
     * @param mercancia
     * @return
     */
    @Override
    public boolean agregarAInventario(Inventario inventario, Mercancia mercancia) {
        try {
            String sql = "call insertarMercanciaAInventario(?,?,?,?,?,?,?,?)";
            Connection conn = Conexion.conectado();
            CallableStatement call = conn.prepareCall(sql);
            call.setString("marca", mercancia.getMarca());
            call.setInt("años_garantia", mercancia.getAñosGarantia());
            call.setInt("idInventario", inventario.getIdInventario());
            call.setString("nombre", mercancia.getNombre());
            call.setInt("valor_adq", (int) mercancia.getValorAdquisicion());
            call.setInt("precio_venta", (int) mercancia.getPrecioVenta());
            call.setInt("idProveedor", mercancia.getProveedores().getFirst().getIdProveedor());
            boolean insert = call.execute();
            call.close();
            return insert;
        } catch (SQLException ex) {
            Logger.getLogger(SedeDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private Inventario traerInventario(Connection conn, int idSede) throws SQLException {
        Inventario inventario = null;
        String sql = "select * from inventario where Sede_idSede=" + idSede;
        PreparedStatement pat = conn.prepareStatement(sql);
        ResultSet rs = pat.executeQuery();
        if (rs.next()) {
            inventario = new Inventario();
            inventario.setIdInventario(rs.getInt("id_inventario"));
            pat.close();
            sql = "select * from objeto as o"
                    + "inner join mercancia as m on o.idObjeto = m.Objeto_idObjeto"
                    + "where m.Inventario_id_inventario=" + inventario.getIdInventario();
            pat = conn.prepareStatement(sql);
            ResultSet rs2 = pat.executeQuery();
            LinkedList<Mercancia> mercancias = new LinkedList();
            while (rs2.next()) {
                Mercancia mercancia = new Mercancia();
                mercancia.setIdObjeto(rs2.getInt("idObjeto"));
                mercancia.setMarca(rs2.getString("marca"));
                mercancia.setAñosGarantia(rs2.getInt("años_garantia"));
                mercancia.setNombre(rs2.getString("nombre"));
                mercancia.setValorAdquisicion(rs2.getDouble("valor_adq"));
                mercancia.setPrecioVenta(rs2.getDouble("precio_venta"));
                mercancia.setCantidad(rs.getInt("cantidad"));
                mercancia.setProveedores(buscarProveedores(conn, mercancia.getIdObjeto()));
                mercancias.add(mercancia);
            }
            pat.close();
            inventario.setMercancia(mercancias);
        }
        return inventario;
    }

    /**
     *
     * @param conn
     * @param idObjeto
     * @return
     */
    private LinkedList<Proveedor> buscarProveedores(Connection conn, int idObjeto) throws SQLException {
        LinkedList <Proveedor> proveedores= null;
        String sql = "select p.* from proveedor as p"
                + "inner join objeto_has_proveedor as op on op.Proveedor_idProveedor = p.idProveedor"
                + "inner join objeto as o on o.idObjeto = op.Objeto_idObjeto"
                + "where o.idObjeto ="+idObjeto;
        PreparedStatement pat = conn.prepareStatement(sql);
        ResultSet rs = pat.executeQuery();
        proveedores=new LinkedList();
        while(rs.next()){
            Proveedor proveedor = new Proveedor();
            proveedor.setIdProveedor(rs.getInt("idProveedor"));
            proveedor.setDireccion(new DireccionDao().consultar(String.valueOf(rs.getInt("Direccion_idDireccion"))));
            proveedor.setNombre(rs.getString("nombre"));
            proveedores.add(proveedor);
        }
        return proveedores;
    }

}
