package bd;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.imageio.ImageIO;
import backend.Usuario;
import frontend.Gui;
import javafx.embed.swing.SwingFXUtils;
import backend.Pair;
import backend.Pet;
/**
 * Essa classe representa todas as funcoes que s�o recorrentes e que gerenciam o Banco de Dados
 * 
 * @authors  Mateus Prado, Mateus Tomieiro, Victor Reis, Matheus Rigato
 *
 */

public class BDConexaoClass{
    
    DriverManager driver;
    
    /*
     * BDConexao - Faz conexao com o Banco de Dados
     * @return - Objeto Connection para executar as Queries
     */
    
    public static Connection BDConexao(){
        
       String con = "jdbc:mysql://127.0.0.1:3306/adopet";
       String server_user = "adopet";
       String server_pass = "@adopet33";
       
       Connection connect = null;
       try {
    	   connect = DriverManager.getConnection(con,server_user,server_pass);
       } catch (SQLException e) {
    	   System.out.println("Erro ao conectar com o BD");
       }
       return connect;
        
    }
    
    /*
     * getIdAnun - funcao que retorna o id do usuario que tem determinado nome de usuario
     * @param username - Recebe o nome de usuario
     * @return id do usuario
     * 
     */
    public static int getIdAnun(String username) {
    	Connection con = BDConexao();
    	String select = "SELECT cliente_id FROM clientes WHERE username=?";
    	PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro na preparacao da Query getIdAnun");
		}
		try {
			ps.setString(1, username);
		} catch (SQLException e) {
			System.out.println("Erro ao setar na Query getIdAnun");
		}
		
		ResultSet rs = null;
		
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query getIdAnun - ");
		}
		try {
			rs.last();
		} catch (SQLException e1) {
			System.out.println("Erro ao indexar first - getAnun");
		}
		try {
			return rs.getInt(1);
		} catch (SQLException e) {
			return 0;
		}
		
    }
    /*
     * cadastroPet - funcao que adiciona pet no Banco de Dados
     * @param p - recebe um Objeto Pet 
     */
    public static void cadastroPet(Pet p){
        Connection con = BDConexao();
        String insert = "INSERT INTO pets(species,nome,sexo,detalhes,id_doador,imagem) values(?,?,?,?,?,?)";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
		} catch (SQLException e) {
			System.out.println("Erro na preparacao da Query");
		}
        try {
			ps.setString(1, p.getEspecie());
			ps.setString(2, p.getNome());
	        ps.setString(3, p.getSexo());
	        ps.setString(4, p.getDetalhes());
	        int idAnun = getIdAnun(Gui.User.getUserName());
	        ps.setInt(5,idAnun);
	        BufferedImage image = SwingFXUtils.fromFXImage(p.getIcone(), null);
	        ByteArrayOutputStream A = new ByteArrayOutputStream();
	        try {
				ImageIO.write(image, "jpg", A);
			} catch (IOException e) {
				System.out.println("Erro ao escrever a imagem!");
			}
	        InputStream is = new ByteArrayInputStream(A.toByteArray());
	        ps.setBlob(6, is);
		} catch (SQLException e) {
			System.out.println("Erro ao setar o statament");
		}
        try {
			ps.execute();
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query Cadastro");
		}
    }
    
    /*
     * cadastroUser - Adiciona um usuario no Banco de Dados
     * @param user - Recebe um Objeto Usuario
     */
    public static void cadastroUser(Usuario user){
        Connection con = BDConexao();
        String insert = "INSERT INTO clientes(username,senha,nome,cpf,cidade,endereco,cep) values(?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(insert);
		} catch (SQLException e) {
			System.out.println("Erro na preparacao da Query");
		}
        try {
			ps.setString(1, user.getUserName());
	        ps.setString(2, user.getSenha());
	        ps.setString(3, user.getNome());
	        ps.setString(4, user.getCpf());
	        ps.setString(5, user.getCidade());
	        ps.setString(6, user.getEndereco());
	        ps.setString(7, user.getCep());
		} catch (SQLException e) {
			System.out.println("Erro ao setar valores");
		}
        
        try {
			ps.execute();
		} catch (SQLException e) {
			System.out.println("Erro na execucao da Query");
		}
    }
    /**
     * loginUser - Faz a funcao para verificar os dados inseridos em uma row especifica
     * @param user - Recebe objeto Usuario
     * @return retorna true se a verificacao deu certo 
     */
    public static boolean loginUser(Usuario user){
        Connection con = BDConexao();
        String select = "SELECT * FROM clientes where username=?";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar statament");
		}
        try {
			ps.setString(1, user.getUserName());
		} catch (SQLException e) {
			System.out.println("Erro ao setar nome");
		}
        
        ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao exucutar a query antes do while");
		}
		
		
		
        if(rs == null){
		    //Nega login, pois n�o existe ningu�m com o nome colocado no campo
		    return false;
		}
        
        try {
			while(rs.next()){
			    String  getpass = rs.getString(3);
			    if(user.getSenha().equals(getpass)){
			    	//Autoriza login, pois o usuario esta no BD;
			        return true;
			    }
			}
		} catch (SQLException e) {
			System.out.println("Erro ao acessar o bd para verificar se \"next\"");
		}
        //Se nao encontrar nenhuma senha igual, nega login!
        return false;
    }
    /*
     * getPetsFromChat - pega todos os chats iniciados com um determinado usuario
     * @param user1 - Recebe o primeiro usuario
     * @param user2 - Recebe o segundo usuario
     * @return - retorna um Vetor de Pets
     */
    public static Vector<Pet> getPetsFromChat(Usuario User1, Usuario User2){
    	Vector<Pet> pets = new Vector<Pet>();
    	
    	Connection con = BDConexao();
        String select = "SELECT pet_id FROM chat WHERE (user1_id = ? AND user2_id = ?)";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar statament");
		}
        try {
			ps.setInt(1, BDConexaoClass.getIdAnun(User1.getUserName()));
			ps.setInt(2, BDConexaoClass.getIdAnun(User2.getUserName()));

        } catch (SQLException e) {
			System.out.println("Erro ao setar nome");
		}
        
        ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao exucutar a query antes do while");
		}
		try {
			while(rs.next()){
			    pets.add(BDConexaoClass.getPet(rs.getInt(1)));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao setar return getPet");
		}
		
    	return pets;
    }
    /*
     * getPet - Retorna um objeto Pet
     * @param id - Recebe um id de um pet especifico
     * @return - Retorna um  objeto Pet
     */
    public static Pet getPet(int id){
		Connection con = BDConexao();
		
		Pet p = new Pet();

		String select = "SELECT * FROM pets WHERE pet_id=?";

		PreparedStatement ps = null;

		try{
			ps = con.prepareStatement(select);
		}catch (SQLException e) {
			System.out.println("Erro ao preparar a Query");
		}	
		try{
			
			ps.setInt(1, id);
		} catch(SQLException e){
			System.out.println("Erro ao setar statement");
		}

		ResultSet rs = null;

		try{
			rs = ps.executeQuery();
		}catch(SQLException e){
			System.out.println("Erro ao executar a query");
		}

		try {
			while(rs.next()){
			    p.setPetID(rs.getInt(1));
			    p.setEspecie(rs.getString(2));
			    p.setNome(rs.getString(3));
			    p.setSexo(rs.getString(4));
			    p.setDetalhes(rs.getString(5));
			    p.setAnuncianteID(rs.getInt(6));
			    Usuario anunciante = new Usuario();
			    anunciante = retornaUsuario((int)p.getAnuncianteID());
			    p.setAnunciante(anunciante);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao setar return getPet");
		}
        
        try {
        	rs.last();
            InputStream in = rs.getBlob(7).getBinaryStream();
            BufferedImage image = ImageIO.read(in);
			p.setIcone(SwingFXUtils.toFXImage(image, null));
		} catch (SQLException | IOException e) {
			return null;
		}
        
        return p;

	}
    
    
    /*
     * usuarioAceitou - Verifica se o usuario clicou no botao Finalizar
     * @param user1 - Recebe o Objeto Usuario
     * @param p - Recebe o Objeto Pet
     * @return - Retorna se a verificacao deu certo ou errado
     */
    public static boolean UsuarioAceitou(Usuario user1, Pet p) {
    	Connection con = BDConexao();
    	String select = "SELECT confirma_user1 FROM chat WHERE pet_id=? AND user1_id=?";
    	PreparedStatement pss = null;
    	
    	try {
    		pss = con.prepareStatement(select);
    	}catch(SQLException e) {
    		System.out.println("Erro ao preparar Statement");
    	}
    	
    	try {
    		pss.setInt(1,(int)p.getPetID());
			pss.setInt(2,BDConexaoClass.getIdAnun(user1.getUserName()));
			} catch (SQLException e1) {
			System.out.println("Erro ao setar ID's no prepared statament! - Chat");
		}
    	
    	ResultSet rss = null;
    	
    	try {
    		rss = pss.executeQuery();
    	}catch(SQLException e) {
    		System.out.println("Erro ao executar a Query");
    	}
    	
    	try {
			rss.first();
		} catch (SQLException e) {
			System.out.println("Erro ao receber o primeiro - userAceitou");
		}
    	
    	try {
			if(rss.getBoolean(1)) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao receber retorno - userAceitou");
			return false;
		}
    	
    	return false;
    }
    
   /**
    * setaFinalizado - Seta no Banco de Dados que o Usuario clicou no botao Finalizar
    * @param user1 - Recebe o primeiro Usuario
    * @param user2 - Recebe o segundo Usuario
    * @param p - Recebe o Objeto Pet
    */
    public static void setaFinalizado(Usuario user1, Usuario user2, Pet p) {
    	Connection con = BDConexao();
    	String up1= "UPDATE chat SET confirma_user1=true where (user1_id=? AND user2_id=? AND pet_id=?)";
    	
    	PreparedStatement psu1 = null;
    	try {
    		psu1 = con.prepareStatement(up1);
    	}catch(SQLException e) {
    		System.out.println("Erro ao preparar Statement"); 
        }
    	try {
    		psu1.setInt(1,BDConexaoClass.getIdAnun(user1.getUserName()));
    		psu1.setInt(2,BDConexaoClass.getIdAnun(user2.getUserName()));
    		psu1.setInt(3, (int)p.getPetID());
    	} catch (SQLException e1) {
    		System.out.println("Erro ao setar args statement - finaliza");
    	}
    		
    	try {
    		psu1.execute();
    	}catch(SQLException e) {
    		System.out.println("Erro ao executar a Query - finaliza");
    		return;
    	}
    	
    }
    
    /**
     * excluirChat - Exclui as mensagens de um determinado chat
     * @param user1 - Recebe o primeiro Usuario
     * @param user2 - Recebe o segundo Usuario
     */
    
    public static void excluirChat(Usuario user1, Usuario user2, Pet p){ 
    	 
		Connection con = BDConexao(); 
 
		String excluirMensagens = "DELETE FROM mensagens WHERE id_chat=?"; 
		String selectChat1 = "SELECT chat_id FROM chat WHERE (user1_id=? AND user2_id=? AND pet_id=?)";
		String deletarChat1 = "DELETE FROM chat WHERE chat_id=?";
 
		PreparedStatement ps = null; 
 
		try{ 
			ps = con.prepareStatement(selectChat1); 
		}catch (SQLException e) { 
			System.out.println("Erro ao preparar a statament excluirMensagens"); 
		} 
 
		try{ 
			ps.setInt(1, BDConexaoClass.getIdAnun(user1.getUserName()));
			ps.setInt(2, BDConexaoClass.getIdAnun(user2.getUserName()));
			ps.setInt(3, (int)p.getPetID());
		}catch (SQLException e) { 
			System.out.println("Erro ao setar a statement excluirMensagens"); 
		} 
 
		ResultSet chat_id = null;
		
		try{ 
			chat_id = ps.executeQuery(); 
		} catch (SQLException e) { 
			System.out.println("Erro ao executar a Query excluirMensagens C"); 
		} 
		int ChatId = 0;
		try {
			chat_id.first();
			ChatId = chat_id.getInt(1);
		} catch (SQLException e1) {
			System.out.println("Erro");
		}
		
		PreparedStatement psd = null;   
		 
		try{ 
			psd = con.prepareStatement(excluirMensagens); 
		}catch (SQLException e) { 
			System.out.println("Erro ao preparar a statament deletarChat"); 
		} 
 
		try{ 
			psd.setInt(1, ChatId);
		}catch (SQLException e) { 
			System.out.println("Erro ao setar a statement deletarChat"); 
		} 
 
		try{ 
			psd.execute(); 
		}catch (SQLException e) { 
			System.out.println("Erro ao executar a Query excluirMensagens A "); 
		} 
		
		
		
		try{ 
			ps = con.prepareStatement(deletarChat1); 
		}catch (SQLException e) { 
			System.out.println("Erro ao preparar a statament excluirMensagens"); 
		} 
 
		try{ 
			ps.setInt(1, ChatId);
		}catch (SQLException e) { 
			System.out.println("Erro ao setar a statement excluirMensagens"); 
		} 
 
		try{ 
			ps.execute(); 
		} catch (SQLException e) { 
			System.out.println("Erro ao executar a Query excluirMensagens B"); 
		} 
		
		try{ 
			ps = con.prepareStatement(deletarChat1); 
		}catch (SQLException e) { 
			System.out.println("Erro ao preparar a statament excluirMensagens"); 
		} 
 
		try{ 
			ps.setInt(1, (ChatId+1));
		}catch (SQLException e) { 
			System.out.println("Erro ao setar a statement excluirMensagens"); 
		} 
 
		try{ 
			ps.execute(); 
		} catch (SQLException e) { 
			System.out.println("Erro ao executar a Query excluirMensagens B"); 
		} 
 
	} 
    
    /*
     * excluirTodosChats - Exclui todos os chat que tem um determinado petId
     * @param id - Recebe o id do Pet
     * 
     */
    public static void excluirTodosChats(int id){

		Connection con = BDConexao();

		String select = "SELECT chat_id FROM chat WHERE pet_id=?";

		PreparedStatement ps = null;

		try{
			ps = con.prepareStatement(select);
		}catch (SQLException e) {
			System.out.println("Erro ao preparar statament");
		}

		try{
			ps.setInt(1,id);
		}catch (SQLException e) {
			System.out.println("Erro ao receber ID");
		}

		ResultSet rs = null;

		try{
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar statament");
		}

		String delMensagens = "DELETE FROM mensagens WHERE id_chat=?";
		PreparedStatement psdm = null;

		try {
			while(rs.next()){

				try{
					psdm = con.prepareStatement(delMensagens);
				}catch (SQLException e) {
					System.out.println("Erro ao preparar statament");
				}

				try{
					psdm.setInt(1,rs.getInt(1));
				}catch (SQLException e) {
					System.out.println("Erro ao receber ID");
				}

				try{
					psdm.execute();
				}catch(SQLException e){
					System.out.println("Erro ao executar a Query");
				}

			}
		} catch (SQLException e1) {
			System.out.println("Erro next - excluit chat");
		}

		String delChats = "DELETE FROM chat WHERE pet_id=?";

		PreparedStatement psdc = null;

		try{
			psdc = con.prepareStatement(delChats);
		}catch (SQLException e) {
			System.out.println("Erro ao preparar statament");
		}
		
		try{
			psdc.setInt(1,id);
		}catch (SQLException e) {
			System.out.println("Erro ao receber ID");
		}

		try{
			psdc.execute();
		}catch(SQLException e){
			System.out.println("Erro ao executar a Query");
		}
	}
    /**
     * adotarPet - Deleta o pet do Banco de Dados
     * @param user1 - Recebe o primeiro Usuario
     * @param user2 - Recebe o segundo Usuario
     * @param petId - Recebe o id do Pet
     */
    public static void adotarPet(Usuario user1, Usuario user2, int petId){

    	//BDConexaoClass.getPetId(user1,user2);
    	
    	excluirTodosChats(petId);
    	
        Connection con = BDConexao();
        String del = "Delete FROM pets WHERE pet_id = ?";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(del);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar statament");
		}
        try {
			ps.setInt(1,petId);
		} catch (SQLException e) {
			System.out.println("Erro ao receber nome");
		}
        try {
			ps.execute();
		} catch (SQLException e) {
			System.out.println("Erro ao executar statament");
		}

		String up = "ALTER TABLE pets AUTO_INCREMENT=?";
		String pegarId = "Select pet_id FROM pets";

		PreparedStatement pss = null;
		try{
			pss = con.prepareStatement(pegarId);
		}catch(SQLException e){
			System.out.println("Erro ao preparar statement");
		}

		ResultSet rss = null;
		try{
			rss = pss.executeQuery();
		}catch(SQLException e){
			System.out.println("Erro ao executar query");
		}

		try {
			rss.last();
		} catch (SQLException e1) {
			System.out.println("Erro ao receber ultima row - adotarPet");
		}
		int id = 0;
		try {
			id = rss.getInt(1);
		} catch (SQLException e1) {
			return;
		}

		PreparedStatement psu = null;
		try{
			psu = con.prepareStatement(up);
		}catch(SQLException e){
			System.out.println("Erro ao preparar statement");
		}

		try {
			psu.setInt(1,id-1);
		} catch (SQLException e1) {
			System.out.println("Erro ao setar index autoIncrem - adotarPet");
		}

		try{
			psu.execute();
		}catch(SQLException e){
			return;
		}

    }
    /*
     * Verifica se existe algum chat criado entre dois usuarios
     * @param user1 - Recebe o primeiro usuario
     * @param user2 - Recebe o segundo usuario
     * @return Retorna um boolean que demonstra a exist�ncia do chat(ou n�o) no Banco de Dados
     * 
     */

    public static boolean existeChat(Usuario user1,Usuario user2){
        Connection con = BDConexao();
        String select = "SELECT * FROM chat WHERE ((user1_id=? AND user2_id=?) OR (user1_id=? AND user2_id=?)) AND pet_id=?";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro na statement existeChat");
		}
        try {
			ps.setInt(1, BDConexaoClass.getIdAnun(user1.getUserName()));
			ps.setInt(2, BDConexaoClass.getIdAnun(user2.getUserName()));
			ps.setInt(3, BDConexaoClass.getIdAnun(user2.getUserName()));
			ps.setInt(4, BDConexaoClass.getIdAnun(user1.getUserName()));
			ps.setInt(5, (int)Gui.petCorrente.getPetID());
		} catch (SQLException e) {
			System.out.println("Erro ao setar inteiros Statement - existeChar");
		}
        
        ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar Query - exiteChat");
		}

        try {
			if(rs.next()){
			    return true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao getchar size - Existe chat");
		}
        
        return false;
    }

    /*
     * cria uma query que adiciona um novo chat no Banco de Dados
     * @param user1 - Primeiro usuario participante do chat
     * @param user2 - Segundo usuario participante do chat
     * 
     */
    
    public static void comecarChat(Usuario user1,Usuario user2){
        
        Connection con = BDConexao();
        String insert = "INSERT INTO chat(user1_id,user2_id,confirma_user1,confirma_user2,pet_id) Values(?,?,?,?,?)";
        String insert2 = "INSERT INTO chat(user1_id,user2_id,confirma_user1,confirma_user2,pet_id) Values(?,?,?,?,?)";
        PreparedStatement psi = null, psi2 = null;
		try {
			psi = con.prepareStatement(insert);
			psi2 = con.prepareStatement(insert2);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar o statement! - Comeca chat");
		}
        try {
			psi.setInt(1, BDConexaoClass.getIdAnun((user1.getUserName())));
			psi.setInt(2, BDConexaoClass.getIdAnun((user2.getUserName())));
			psi.setBoolean(3, false);
			psi.setBoolean(4, false);
			psi.setInt(5, (int)Gui.petCorrente.getPetID());
			psi2.setInt(1, BDConexaoClass.getIdAnun((user2.getUserName())));
			psi2.setInt(2, BDConexaoClass.getIdAnun((user1.getUserName())));
			psi2.setBoolean(3, false);
			psi2.setBoolean(4, false);
			psi2.setInt(5, (int)Gui.petCorrente.getPetID());
			
		} catch (SQLException e) {
			System.out.println("Erro ao setar valores do statement! - comeChat");
		}
		try {
			psi.execute();
			psi2.execute();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o prepered statement! - Comc. Chat");
		}
    }

    /*
     * Retorna todas as mensagens que dois usuarios j� tiveram entre si
     * @param user1 - Primeiro usuario participante do chat
     * @param user2 - Segundo usuario participante do chat
     * @return Um vector que contem os ids dos usuarios e as suas respectivas mensagens
     * 
     */
    public static Vector<Pair<Integer,String>> getMensagensAntigas(Usuario user1, Usuario user2){
        Connection con = BDConexao();
        String select = "SELECT chat_id FROM chat WHERE ((user1_id=? AND user2_id=?) OR (user2_id=? AND user1_id=?)) AND pet_id=?";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro PS - getMensagens");
		}
        try {
			ps.setInt(1, BDConexaoClass.getIdAnun(user1.getUserName()));
			ps.setInt(2, BDConexaoClass.getIdAnun(user2.getUserName()));
			ps.setInt(3, BDConexaoClass.getIdAnun(user1.getUserName()));
			ps.setInt(4, BDConexaoClass.getIdAnun(user2.getUserName()));
			ps.setInt(5, (int)Gui.petCorrente.getPetID());
        } catch (SQLException e) {
			System.out.println("Erro PS setInt - getMensagens");
		}
        
        ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro execucao Query - getMensagens");
		}
		int id = 0;
		try {
			rs.first();
			id = rs.getInt(1);
		} catch (SQLException e1) {
			System.out.println("Erro no get da Query - getMensagens");
		}
		
        //MENSAGENS
		
        String puxarMensagens = "Select mensagem,id_remetente FROM mensagens INNER JOIN chat ON chat.chat_id = mensagens.id_chat WHERE chat.chat_id = ?";
        PreparedStatement psp = null;
		try {
			psp = con.prepareStatement(puxarMensagens);
		} catch (SQLException e) {
			System.out.println("Erro na PS2 - getMensagens");
		}
        try {
			psp.setInt(1,id);
		} catch (SQLException e) {
			System.out.println("Erro setId da Query - getMensagens");
		}

        ResultSet rsp = null;
		try {
			rsp = psp.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro na exec Query2 - getMensagens");
		}

		int i=0;
		try {
			while(rsp.next()){
			    i++;
			}
		} catch (SQLException e) {
			System.out.println("Erro nas mensagens - getMensagens");
		}
		
		try {
			rsp.beforeFirst();
		} catch (SQLException e1) {
			System.out.println("Primeiro do result");
		}
		
		Vector<Pair<Integer,String>> pares = new Vector<Pair<Integer, String>>(i);
		i=0;
        try {
			while(rsp.next()){
				String msg = rsp.getString(1);
				int sent = rsp.getInt(2);
			    pares.add(new Pair<Integer,String>(sent,msg));
			}
		} catch (SQLException e) {
			System.out.println("Erro nas mensagens - getMensagens");
		}

        return pares;
    }

    /*
     * Cria uma query que coloca uma mensagem de acordo com o chat de dois usuarios
     * @param user1 - Primeiro participante do chat
     * @param user2 - Segundo participante do chat
     * @param mensagem - Mensagem que foi enviada
     * 
     */
    
    public static void criarMensagem(Usuario user1, Usuario user2,String mensagem){
    	Connection con = BDConexao();
        String pegarID = "Select chat_id FROM chat WHERE ((user1_id = ? AND user2_id = ?) OR (user1_id = ? AND user2_id = ?)) AND pet_id=?";
        PreparedStatement psc = null;
		try {
			psc = con.prepareStatement(pegarID);
		} catch (SQLException e) {
			System.out.println("Erro PS - criaMensagem");
		}
		
        try {
			psc.setInt(1,BDConexaoClass.getIdAnun(user1.getUserName()));
			psc.setInt(2,BDConexaoClass.getIdAnun(user2.getUserName()));
			psc.setInt(3,BDConexaoClass.getIdAnun(user2.getUserName()));
			psc.setInt(4,BDConexaoClass.getIdAnun(user1.getUserName()));
			psc.setInt(5,(int)Gui.petCorrente.getPetID());
		} catch (SQLException e) {
			System.out.println("Erro ao integers - criaMensagens");
		}
        

        ResultSet rsc = null;
		
        try {
			rsc = psc.executeQuery();
		}catch (SQLException e) {
			System.out.println("Erro ao integers - criaMensagens");
		}
        try {
			if(!rsc.next())
					return;
		} catch (SQLException e1) {
			System.out.println("Erro fetch size - criaMensagens");
		}
        
        int chat = 0;
        
        try {
			rsc.first();
			chat = rsc.getInt(1);
		} catch (SQLException e1) {
			System.out.println("Erro get integers - criaMensagens");
		}
        


        String inserirMensagem = "Insert INTO mensagens(id_chat,id_remetente,mensagem) VALUES(?,?,?)";
        PreparedStatement psi = null;
		try {
			psi = con.prepareStatement(inserirMensagem);
		} catch (SQLException e1) {
			System.out.println("Erro psi - criaMensagens");
		}
        try {
			psi.setInt(1,chat);
			psi.setInt(2,BDConexaoClass.getIdAnun(user2.getUserName()));
	        psi.setString(3,mensagem);     
		} catch (SQLException e1) {
			System.out.println("Erro psi setting - criaMensagens");
		}
        
        try {
			psi.execute();
		}catch (SQLException e) {
			System.out.println("Erro ao integers - criaMensagens");
		}
    }
    
    /*
     * getUserFromId - retorna o objeto User
     * @param id - recebe o id ddo usuario
     * @return - retorna o Objeto Usuario
     * 
     */
    
    private static Usuario getUserFromId(int id) {
    	Usuario aux = new Usuario();
    	Connection con = BDConexao();
    	String cont = "SELECT * FROM clientes WHERE cliente_id = ?";
    	PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(cont);
		} catch (SQLException e) {
			System.out.println("Erro preparacao getUserFromId");
		}
    	try {
			ps.setInt(1,id);
		} catch (SQLException e) {
			System.out.println("Erro setting int getUserFromId");
		}
    	ResultSet rs = null;
    	try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query getUserFromId");
		}
    	try {
			while(rs.next()){
				aux.setId(id);
				aux.setUserName(rs.getString(2));
				aux.setSenha(rs.getString(3));
				aux.setNome(rs.getString(4));
				aux.setCpf(rs.getString(5));
				aux.setCidade(rs.getString(6));
				aux.setEndereco(rs.getString(7));
				aux.setCep(rs.getString(8));
				
			}
		} catch (SQLException e) {
			System.out.println("Erro ao receber retornos getUserFromId");
		}
    	
		return aux;
	}
    /**
     * getSizeUser - Retorna o total de todos os contatos que determinado usuario conversou
     * @param user1 - Recebe o usuario
     * @return - retorna o total de contatos
     */
    
    public static int getSizeUser(Usuario user1){
    	Connection con = BDConexao();
    	String cont = "SELECT DISTINCTROW(user2_id) FROM chat WHERE user1_id = ?";
    	PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(cont);
		} catch (SQLException e) {
			System.out.println("Erro preparacao getSizeUser");
		}
    	try {
			ps.setInt(1,BDConexaoClass.getIdAnun(user1.getUserName()));
		} catch (SQLException e) {
			System.out.println("Erro setting int getSizeUser");
		}
    	ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query getSizeUser");
		}
		int aux = 0;
		try {
			while(rs.next()) {
				aux++;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao receber retorno getSizeUser");
		}
    	return aux;
    	
    }

    /**
     * getSizePets - Retorna o total de pets no Banco de Dados
     * @return - retorna o total de pets no Banco de Dados
     */
       
    public static int getSizePets(){
    	
    	Connection con = BDConexao();
    	String cont = "SELECT COUNT(*) FROM pets";
    	PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(cont);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar a Query");
		}
    	
    	ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar o Query RS");
		}
    	
    	try {
    		rs.last();
			return rs.getInt(1);
		} catch (SQLException e) {
			return 0;
		}
    }


    /**
     * listaContatos - Lista os contatos de um determinado usuario
     * @param user1 - Recebe o Objeto Usuario
     * @return - Retorna um vetor de Usuario
     */
    public static Usuario[] listaContatos(Usuario user1){
    	
    	Connection con = BDConexao();
    	String select = "SELECT DISTINCTROW(user2_id) FROM chat WHERE user1_id=?";
    	
    	PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar statement - listaContatos");
		}
		
    	try {
			ps.setInt(1,BDConexaoClass.getIdAnun(user1.getUserName()));
		} catch (SQLException e) {
			System.out.println("Erro ao setar Query - listaContatos");
		}

    	ResultSet rs = null;
    	int len = 0;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query - listaContatos");
		}
		
		len = getSizeUser(user1);
		
    	Usuario[] pessoa = new Usuario[len];
    	
    	int i = 0;
    	
    	try {
			while(rs.next()){
				pessoa[i] = getUserFromId(rs.getInt(1));
				i++;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao receber usuarios - listaContatos");
		}
    	
    	return pessoa;
    }

    
	public static Usuario retornaUsuario(int id) throws SQLException{

        Usuario user = new Usuario();
        Connection con = BDConexao();
        String select = "SELECT * FROM clientes WHERE cliente_id = ?";
        PreparedStatement ps = con.prepareStatement(select);
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            user.setId(rs.getInt(1));
            user.setUserName(rs.getString(2));
            user.setSenha(rs.getString(3));
            user.setNome(rs.getString(4));
            user.setCpf(rs.getString(5));
            user.setCidade(rs.getString(6));
            user.setEndereco(rs.getString(7));
            user.setCep(rs.getString(8));
        }

        return user;

    }
	/**
	 * retornaPet - Retorna o  Objeto pet 
	 * @param index - Recebe o index do Pet no Banco de Dados 
	 * @return - Retorna o Objeto Pet
	 */
    public static Pet retornaPet(int index){

        Connection con = BDConexao();
        String select = "SELECT * FROM pets LIMIT ?,1";
        PreparedStatement ps = null;
		try {
			ps = con.prepareStatement(select);
		} catch (SQLException e) {
			System.out.println("Erro ao preparar a statament retornaPet");
		}

		try {
			ps.setInt(1, index);
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query retornaPet");
		}
		
		
        ResultSet rs = null;
		try {
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println("Erro ao executar a Query retornaPet");
		}

        Pet p = new Pet();
        
        try {
			while(rs.next()){
			    p.setPetID(rs.getInt(1));
			    p.setEspecie(rs.getString(2));
			    p.setNome(rs.getString(3));
			    p.setSexo(rs.getString(4));
			    p.setDetalhes(rs.getString(5));
			    p.setAnuncianteID(rs.getInt(6));
			    Usuario anunciante = new Usuario();
			    anunciante = retornaUsuario((int)p.getAnuncianteID());
			    p.setAnunciante(anunciante);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao setar return retornaPet");
		}
        
        try {
        	rs.last();
            InputStream in = rs.getBlob(7).getBinaryStream();
            BufferedImage image = ImageIO.read(in);
			p.setIcone(SwingFXUtils.toFXImage(image, null));
		} catch (SQLException | IOException e) {
			return null;
		}
        
        return p;

    }

}