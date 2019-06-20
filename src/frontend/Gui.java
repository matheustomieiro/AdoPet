package frontend;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import backend.Pet;
import backend.Usuario;
import bd.BDConexaoClass;
import backend.Pair;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class Gui extends Application {
	static Parent root;
	static Stage Stg;
	public static Usuario User;
	static boolean finalizaCadastro;
	static Usuario contatos[];
	static Button botaoContato[];
	static Usuario contato;
	
	//Uso na DisponiveisManager
	public static Pet pet[];
	public static int index;
	static int numeropaginas;
	static int paginaatual;
	static Image fotopet;
	
	
	public static Object getComp(String str) {
		return (Object)Gui.root.lookup("#" + str);
	}
	
	public static Usuario setCadastroUser() {
		Gui.User.setNome(((TextField)Gui.getComp("name")).getText());
		Gui.User.setUserName(((TextField)Gui.getComp("user")).getText());
		Gui.User.setSenha(((PasswordField)Gui.getComp("password")).getText());
		Gui.User.setCpf(((TextField)Gui.getComp("cpf")).getText());
		Gui.User.setCidade(((TextField)Gui.getComp("cidade")).getText());
		Gui.User.setCep(((TextField)Gui.getComp("cep")).getText());
		Gui.User.setEndereco(((TextField)Gui.getComp("endereco")).getText());
		return User;
	}
	
	public static void telaInicial() throws IOException {
		FXMLLoader loader = new FXMLLoader(new File("src/frontend/inicial.fxml").toURI().toURL());
		Gui.root = loader.load();
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet");
        Gui.Stg.show();
	}
	
	public static void login() {
		Gui.User.setUserName(((TextField)getComp("user")).getText());
		Gui.User.setSenha(((PasswordField)getComp("password")).getText());
		if(BDConexaoClass.loginUser(Gui.User)){
			Gui.telaDisponiveis();
		}
	} 
	
	public static void telaCadastro() throws IOException {
		FXMLLoader loader = new FXMLLoader(new File("src/frontend/cadastro.fxml").toURI().toURL());
		Gui.root = loader.load();
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet - Cadastro");
        Gui.Stg.show();
	}
	
	private static boolean validarCpf(String CPF) {
		return true;
	}
	
	public static void finalizaCadastro() {
		boolean nomeValido = ((TextField) getComp("user")).getLength() != 0;
		boolean senhaValida = ((TextField) getComp("password")).getLength() != 0;
		boolean cpfValido = validarCpf(((TextField)getComp("cpf")).getText());
		boolean validaTermos = ((CheckBox)getComp("aceita")).isSelected();
		if(nomeValido && cpfValido && senhaValida && validaTermos){
			BDConexaoClass.cadastroUser(setCadastroUser());	
			Gui.telaDisponiveis();
		}
	}

	public static void telaDisponiveis(){
		if(BDConexaoClass.getSizePets() < 4) {
			Gui.numeropaginas = 1;
		}
		Gui.numeropaginas = BDConexaoClass.getSizePets() / 4;
		
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(new File("src/frontend/disponiveis.fxml").toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println("Erro na URL do FXML");
		}
		try {
			Gui.root = loader.load();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
		
		((Text)Gui.getComp("npag")).setText("Pagina " + (Gui.paginaatual+1));
		
		//PEGA OBJETO PET DO BD E SETAR NO GUI.PET[].
		for(int i=0; i<4; i++) {
			try {
				Gui.pet[i] = BDConexaoClass.retornaPet(((Gui.paginaatual)*4)+(i+1)-1);
			} catch (NumberFormatException e) {
				System.out.println("Erro no retorno do Pet do BD");
			}
		}
		
		 //SETANDO NOMES E IMAGENS DOS PETS AQUI
		for(int i=0; i<4; i++) {
			if(Gui.pet[i] == null){
				((Button)Gui.getComp("nome_pet" + (i+1))).setVisible(false);
			}
			else {
				((Button)Gui.getComp("nome_pet" + (i+1))).setVisible(true);
				((ImageView)Gui.getComp("image" +(i+1))).setImage(pet[i].getIcone());
				((Button)Gui.getComp("nome_pet" + (i+1))).setText(pet[i].getNome());
			}
		}
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet");
        Gui.Stg.show();
	}
	
	
	public static void telaInfoPet() {
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(new File("src/frontend/infopet.fxml").toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println("Erro na URL do FXML");
		}
		try {
			Gui.root = loader.load();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
		
		//Setando infos do pet
		((ImageView)(Gui.getComp("icone"))).setImage(Gui.pet[Gui.index].getIcone());
		((Text)(Gui.getComp("nome"))).setText((Gui.pet[Gui.index].getNome()));
		((TextField)(Gui.getComp("especie"))).setText((Gui.pet[Gui.index].getEspecie()));
		((TextField)(Gui.getComp("sexo"))).setText((Gui.pet[Gui.index].getSexo()));
		((TextField)(Gui.getComp("dono"))).setText((Gui.pet[Gui.index].getAnunciante().getNome()));
		((TextArea)(Gui.getComp("descricao"))).setText((Gui.pet[Gui.index].getDetalhes()));
	
		if(BDConexaoClass.getIdAnun(Gui.User.getUserName()) == Gui.pet[Gui.index].getAnuncianteID()) {
			((Button)(Gui.getComp("mensagem"))).setVisible(false);
		}else {
			((Button)(Gui.getComp("mensagem"))).setVisible(true);
		}
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet - Info");
        Gui.Stg.show();
	}
	
	public static void telaAnunciar() {
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(new File("src/frontend/anunciar.fxml").toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println("Erro na URL do FXML");
		}
		try {
			Gui.root = loader.load();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet - Info");
        Gui.Stg.show();
	}
	
	public static void escolheFoto() {
		FileChooser fc = new FileChooser();
		fc.setTitle("Selecione a foto do pet!");
		fc.getExtensionFilters().addAll(new ExtensionFilter("Image(.jpg .jpeg)","*.jpg","*.jpeg"));
		File foto = fc.showOpenDialog(Gui.Stg);
		if (Gui.fotopet == null) {
        	Gui.telaDisponiveis();
        	return;
        }
		Gui.fotopet= new Image(foto.toURI().toString());
        ((ImageView)Gui.getComp("imagem_anuncio")).setImage(Gui.fotopet);
	}
	
	public static void confirmarAnuncio(Pet pet) {
		BDConexaoClass.cadastroPet(pet);
		Gui.paginaatual = 0;
		Gui.telaDisponiveis();
	}
	
	
	public static void telaChat() {
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(new File("src/frontend/chat.fxml").toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println("Erro na URL do FXML");
		}
		try {
			Gui.root = loader.load();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
		Gui.contatos = BDConexaoClass.listaContatos(Gui.User);
		
		if(Gui.contatos.length != 0) {
			Gui.botaoContato = new Button[Gui.contatos.length];
			VBox painel = ((VBox)((ScrollPane)Gui.getComp("spane")).getContent().lookup("#vpane"));
			((Label)getComp("texto1")).setVisible(true);
			((Label)getComp("texto2")).setVisible(false);
			for(int i=0; i<contatos.length; i++) {
				Gui.botaoContato[i] = new Button();
				Gui.botaoContato[i].setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent event) {
				       ChatManager.apertou(event);
				    }
				});
				Gui.botaoContato[i].setId(i + "b");
				Gui.botaoContato[i].setText(Gui.contatos[i].getNome());
				Gui.botaoContato[i].setVisible(true);
				Gui.botaoContato[i].setMinSize(320, 30);
				Gui.botaoContato[i].setAlignment(Pos.CENTER);
				painel.getChildren().add(Gui.botaoContato[i]);
				((ScrollPane)Gui.getComp("spane")).setVisible(true);
			}
		}else {
			((Label)getComp("texto2")).setVisible(true);
			((Label)getComp("texto1")).setVisible(false);
		}
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet");
        Gui.Stg.show();
	}
	
	
	public static void iniciarChat(){
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(new File("src/frontend/conversa.fxml").toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println("Erro na URL do FXML");
		}
		try {
			Gui.root = loader.load();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
		
		if(!BDConexaoClass.existeChat(Gui.User, Gui.contato)){
			BDConexaoClass.comecarChat(Gui.User, Gui.contato);
		}else{
			Gui.mostrarMensagensAntigas();
		}
		
		if(BDConexaoClass.UsuarioAceitou(Gui.User, Gui.contato)) {
			((Button)Gui.getComp("finalizar")).setText("Esperando");
		}
		((Label)Gui.getComp("titulo")).setText(((Label)getComp("titulo")).getText() + Gui.contato.getUserName());
		Scene S = new Scene(Gui.root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet");
        Gui.Stg.show();
	}
	
	public static void mostrarMensagensAntigas() {
		Vector<Pair<Integer, String>> mensagens = null;
		mensagens = BDConexaoClass.getMensagensAntigas(Gui.User, contato);
		VBox box = (VBox)((ScrollPane)Gui.getComp("pbox")).getContent().lookup("#box");
		for(int i=0; i<mensagens.size(); i++) {
			Label texto = new Label();
			if(mensagens.get(i).getId() == BDConexaoClass.getIdAnun((Gui.User.getUserName()))) {
				texto.setText("[" + Gui.contato.getUserName() + "]: " + mensagens.get(i).getMensagem());
				texto.setMinWidth(470);
				texto.setMinHeight(30);
				texto.setAlignment(Pos.CENTER_LEFT);
			}else{
				texto.setText("[voce]: " + mensagens.get(i).getMensagem());
				texto.setMinWidth(470);
				texto.setMinHeight(30);
				texto.setAlignment(Pos.CENTER_RIGHT);
			}
			box.getChildren().add(texto);
		}
	}
	
	public static void mostrarNovaMensagem(String mensagem) {
		VBox box = (VBox)((ScrollPane)Gui.getComp("pbox")).getContent().lookup("#box");
		Label nova = new Label();
		nova.setText("[voce]: " + mensagem);
		nova.setMinWidth(470);
		nova.setMinHeight(30);
		nova.setAlignment(Pos.TOP_RIGHT);
		box.getChildren().add(nova);
	}
	
	public static void finalizaAdocao() {
		/*BDConexaoClass.excluirChat(Gui.User, Gui.contato);
		BDConexaoClass.adotaPet(Gui.User, Gui.contato);*/
		Gui.telaDisponiveis();
	}
	
	public static void telaPorqueAdotar() {
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(new File("src/frontend/porqueAdotar.fxml").toURI().toURL());
		} catch (MalformedURLException e) {
			System.out.println("Erro na URL do FXML");
		}
		try {
			Gui.root = loader.load();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
		Scene S = new Scene(root);
		Gui.Stg.setScene(S);
        Gui.Stg.setTitle("AdoPet");
        Gui.Stg.show();
	}
	
	
	@Override
    public void start (Stage stage){
        Gui.Stg = stage;
        Gui.Stg.setResizable(false);
        try {
			Gui.telaInicial();
		} catch (IOException e) {
			System.out.println("Erro no carregamento do FXML");
		}
    }

	public static void avancaPag() {
		if(!(Gui.paginaatual == (BDConexaoClass.getSizePets()/4))) {
			Gui.paginaatual++;
			Gui.telaDisponiveis();
		}
		
	}

	public static void voltaPag() {
		if(!(Gui.paginaatual == 0)) {
			Gui.paginaatual--;
			Gui.telaDisponiveis();
		}
		
	}
	
	public static void run(String[] args) {
    	Gui.User = new Usuario();
    	Gui.pet = new Pet[4];
    	Gui.index = 0;
    	Gui.numeropaginas = 1;
    	if(BDConexaoClass.getSizePets() < 4) {
			Gui.numeropaginas = 1;
		}
		Gui.numeropaginas = (BDConexaoClass.getSizePets() / 4);
    	Gui.launch(args); //Requisitando inicializacao da Gui
    }
    
}