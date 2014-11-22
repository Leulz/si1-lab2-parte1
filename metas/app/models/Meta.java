package models;

import javax.persistence.*;

@Entity(name = "Meta")
public class Meta{
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String descricao;
	@Column
	private int prioridade;
	
	public Meta(String descricao){
		this.descricao = descricao;
	}
	
	public Meta(){}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	

	public int getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(int prioridade) {
		this.prioridade = prioridade;
	}
}
