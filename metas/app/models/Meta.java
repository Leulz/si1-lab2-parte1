package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity(name = "Meta")
public class Meta implements Comparable<Meta>{
	@Id
	@GeneratedValue
	private Long id;
	@Column
	private String descricao;
	@Column
	private int prioridade;
	@Column
	private int semana;
	
	public Meta(String descricao, int semana, int prioridade){
		this.descricao = descricao;
		this.semana = semana;
		this.prioridade = prioridade;
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

	public int getSemana() {
		return semana;
	}

	public void setSemana(int semana) {
		this.semana = semana;
	}
	
	@Override
	public int compareTo(Meta outraMeta) {
		int compareSemana=((Meta)outraMeta).getSemana();
		
        int value1 =  this.getSemana()-compareSemana;
        
        if (value1==0){
        	int comparePrioridade = ((Meta)outraMeta).getPrioridade();
        	int value2 = this.getPrioridade()-comparePrioridade;
        	return value2;
        }
        
        return value1;
	}
}
