package frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Control;


public class DisponiveisManager {
	
	@FXML protected void abaPorqueAdotar(ActionEvent event) {
		Gui.telaPorqueAdotar();
	}
	
	@FXML protected void abaChat(ActionEvent event) {
		Gui.telaChat();
	}
	
	@FXML protected void avancaPag(ActionEvent event) {
		Gui.avancaPag();
	}
	
	@FXML protected void voltaPag(ActionEvent event) {
		Gui.voltaPag();
	}
	
	@FXML protected void clicaPet(ActionEvent event) {
		Gui.index = ((int)(((Control)event.getSource()).getId().charAt(3))) - 1;
		Gui.telaInfoPet();
	}
	
	@FXML protected void clicaAnunciar(ActionEvent event) {
		Gui.telaAnunciar();
	}
	
	//Dentro da tela de informacoes do animal
	
	@FXML protected void voltarDisponiveis(ActionEvent event) {
		Gui.telaDisponiveis(Gui.paginaatual);
	}
	
	@FXML protected void mensagemDono(ActionEvent event) {
		Gui.iniciarChat(Gui.pet[Gui.index].getAnunciante());
	}
	
	
}
