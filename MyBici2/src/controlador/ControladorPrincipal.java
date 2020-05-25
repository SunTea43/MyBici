package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;
import modelo.dao.EmpleadosDao;
import modelo.dao.RolesDao;
import modelo.dao.TrabajosDAO;
import modelo.dao.UsuariosDao;
import modelo.dto.Usuario;
import vista.VistaSede;
import vista.VistaLogin;
import vista.VistaPrincipal;
import vista.VistaTrabajos;
import vista.VistaUsuarios;

/**
 * Clase controlador principal. Aquí se lleva a cabo el control de la
 * aplicación. Se habilitan o se deshabilitan vistas de acuerdo a los permisos
 * asignados a un usuario
 *
 * @author Santiago Pérez, Andrés Camilo López, Carlos Antonio Plaza
 * @version 1.0
 * @since 2020-05-10
 */
public class ControladorPrincipal implements ActionListener {

    /**
     *
     */
    private VistaPrincipal vista;
    /**
     *
     */
    private final VistaLogin vistaAnterior;
    /**
     * 
     */
    private final UsuariosDao usuariosDao;
    private VistaSede vistaInv;
    private Usuario usuario;

    /**
     * Constructor de la clase
     *
     * @param vista que es la vista que usa este controlador
     * @param vistaAnterior que es la vista login
     * @param usuariosDao que es el objeto de acceso a datos del controlador
     * @param usuario
     */
    public ControladorPrincipal(VistaPrincipal vista, VistaLogin vistaAnterior, UsuariosDao usuariosDao, Usuario usuario) {
        this.vista = vista;
        this.usuario = usuario;
        this.vista.ItemTrabajos.addActionListener(this);
        this.vista.ItemTMantenimiento.addActionListener(this);
        this.vista.ItemContabilidad.addActionListener(this);
        this.vista.ItemPeticiones.addActionListener(this);
        this.vista.ItemProveedores.addActionListener(this);
        this.vista.ItemRoles.addActionListener(this);
        this.vista.ItemSede.addActionListener(this);
        this.vista.itemEmpleados.addActionListener(this);
        this.vista.itemUsuarios.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);

        this.vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir(); //To change body of generated methods, choose Tools | Templates.
            }

        });
        this.vistaAnterior = vistaAnterior;
        this.vistaAnterior.setVisible(false);
        this.usuariosDao = usuariosDao;
        this.vista.setVisible(true);
        this.vista.setLocationRelativeTo(null);

    }
    /**
     * 
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.vista.itemUsuarios)) {
            ControladorUsuarios controladorUsuarios;
            controladorUsuarios = new ControladorUsuarios(this.vista, new VistaUsuarios(), this.usuariosDao, new RolesDao(), new EmpleadosDao());

        } else if (e.getSource().equals(this.vista.ItemTrabajos)) {
            ControladorTrabajos controladorTrabajos;
            controladorTrabajos = new ControladorTrabajos(this.vista, new VistaTrabajos(), this.usuariosDao, this.usuario, new TrabajosDAO(), new EmpleadosDao());
        } else if (e.getSource().equals(this.vista.ItemContabilidad)) {
            ControladorContabilidad controladorContabilidad;
            controladorContabilidad = new ControladorContabilidad();
        }else if(e.getSource().equals(this.vista.ItemPeticiones)){
            
        } 
        else if(e.getSource().equals(this.vista.ItemProveedores)){
            
        }
        else if(e.getSource().equals(this.vista.ItemRoles)){
            
        }
        else if(e.getSource().equals(this.vista.ItemSede)){
            
        }
        else if(e.getSource().equals(this.vista.itemEmpleados)){
            
        }
        else if(e.getSource().equals(this.vista.ItemTMantenimiento)){
            
        }
        else if (e.getSource().equals(this.vista.btnSalir)) {
            salir();
        }

    }

    public void salir() {
        int opc = JOptionPane.showConfirmDialog(null, "¿Está seguro que desea salir?");
        if (opc == JOptionPane.YES_OPTION) {
            this.vista.dispose();
            this.vistaAnterior.setVisible(true);
        }
    }

}
