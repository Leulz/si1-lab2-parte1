package models;
//CLASSE INUTIL
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Entity(name = "Semana")
public class Semana {
	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private int semana;
	
	@OneToMany
	@JoinTable
	List<Meta> metas;
	
	public Semana(){
		this.metas = new ArrayList<Meta>();
	}
	
	public Semana(int semana){
		this.semana = semana;
	}

	public int getSemana() {
		return semana;
	}

	public void setSemana(int semana) {
		this.semana = semana;
	}

	public List<Meta> getMetas() {
		return metas;
	}

	public void addMeta(Meta meta) {
		metas.add(meta);
	}
}
