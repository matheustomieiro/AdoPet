package backend;

/*
 * Essa classe representa um usu�rio do programa;
 * */
public class Usuario {
	
	private long id;
	private String userName;
	private String senha;
	private String nome;
	private String cpf;
	private String cidade;
	private String endereco;
	private String cep;

	/**
	 * Construtor da classe
	 * @param id - long que representa o id �nico do usu�rio;
	 * @param userName - String que representa o nome de login do usu�rio;
	 * @param senha - String que representa a senha do usu�rio;
	 * @param nome - String que representa o nome do usu�rio;
	 * @param idade - int que representa a idade do usu�rio;
	 * @param cpf - String que representa o Cadastro de Pessoa F�sica;
	 * @param cidade - String que representa a cidade do usu�rio;
	 * @param endereco - String que representa o endere�o do usu�rio;
	 * @param cep - String que representa o CEP do usu�rio;
	 */
	public Usuario(long id, String userName, String senha, String nome, int idade, String cpf, String cidade, String endereco, String cep) {
		this.id = id;
		this.userName= userName;
		this.senha = senha;
		this.nome = nome;
		this.cpf = cpf;
		this.cidade = cidade;
		this.endereco = endereco;
		this.cep = cep;
	}
	
	/**
	 * Construtor vazio
	 */
	public Usuario() {
		;
	}

	/**
	 * Getter para o atributo de id;
	 * @return - long com o id do usu�rio;
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Setter para o atributo de id;
	 * @param id - long com o id do usu�rio;
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Getter para o atributo de nome de login;
	 * @return - String com o nome de login do usu�rio;
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Setter para o atributo de nome de login;
	 * @param userName - String com o nome de login do usu�rio;
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Getter para o atributo de senha;
	 * @return - String com a senha do usu�rio;
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * Setter para o atributo de senha;
	 * @param senha - String com a senha do usu�rio;
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	/**
	 * Getter para o atributo de nome;
	 * @return - String com o nome do usu�rio;
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Setter para o atributo de nome;
	 * @param nome - String com o nome do usu�rio;
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Getter para o atributo de CPF;
	 * @return - String com o CPF do usu�rio;
	 */
	public String getCpf() {
		return cpf;
	}
	
	/**
	 * Setter para o atributo de CPF;
	 * @param cpf - String com o CPF do usu�rio;
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	/**
	 * Getter para o atributo de cidade;
	 * @return - String com a cidade do usu�rio;
	 */
	public String getCidade() {
		return cidade;
	}
	
	/**
	 * Setter para o atributo de cidade;
	 * @param cidade - String com a cidade do usu�rio;
	 */
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
	/**
	 * Getter para o atributo de endereço;
	 * @return - String com o endereço do usu�rio;
	 */
	public String getEndereco() {
		return endereco;
	}

	/**
	 * Setter para o atributo de endereço;
	 * @param endereco - String com o endere�o do usu�rio;
	 */
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	/**
	 * Getter para o atributo de CEP;
	 * @return - String com o CEP do usu�rio;
	 */
	public String getCep() {
		return cep;
	}
	
	/**
	 * Setter para o atributo de CEP;
	 * @param cep - String com o CEP do usu�rio;
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	
}
