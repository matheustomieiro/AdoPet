
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import backend.Usuario;
import backend.Pet;

class BDConexao{
    
    
    DriverManager driver;
    
    public static Connection BDConexao() throws SQLException{
        
       String con="jdbc:mysql://localhost:3306/poo_db";
       String server_user = "root";
       String server_pass = "rootpass";
       
       Connection connect = null;
       connect = DriverManager.getConnection(con,server_user,server_pass);
       
       return connect;
        
    }
    
    public void cadastroPet(Pet p) throws SQLException{
        
        Connection con = BDConexao();
        String insert = "INSERT INTO pets values(?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insert);
        ps.setLong(1, p.getPetID());
        ps.setString(2, p.getEspecie());
        ps.setString(3, p.getNome());
        ps.setString(4, p.getSexo());
        ps.setString(5, p.getDetalhes());
        ps.setString(6, p.getAnunciantes().getNome());
        
        
        ResultSet rs = ps.executeQuery();
        
    }
    // Fun��o que cadastra o usu�rio no Banco de Dados
    public void cadastroUser(Usuario user) throws SQLException{
        
        Connection con = BDConexao();
        String insert = "INSERT INTO clientes values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(insert);
        ps.setLong(1, user.getId());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getSenha());
        ps.setString(4, user.getNome());
        ps.setInt(5, user.getIdade());
        ps.setString(6, user.getCpf());
        ps.setString(7, user.getCidade());
        ps.setString(8, user.getEndereco());
        ps.setString(9, user.getCep());
        
        ResultSet rs = ps.executeQuery();
    }
    
    public void loginUser(Usuario user) throws SQLException{
        
        Connection con = BDConexao();
        String select = "SELECT * FROM clientes where nomelogin=?";
        PreparedStatement ps = con.prepareStatement(select);
        ps.setString(1, user.getUserName());
        
        ResultSet rs = ps.executeQuery();
        
        if(rs.wasNull()){
            //Mensagem de erro, pois n�o existe ningu�m com o nome colocado no campo
            return;
        }
        
        while(rs.next()){
            String  getpass = rs.getString(3);
            if(!user.getSenha().equals(getpass)){
                //Mensagem de erro, pois as  senhas s�o diferentes
                return;
            }
            
            else{
                //Abre a JFrame com as informa��es do usuario
            }
            
        }
        
        
        
    }
    
    
}