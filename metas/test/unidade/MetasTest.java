package unidade;

import static org.junit.Assert.*;

import java.util.List;

import models.Meta;
import models.dao.GenericDAO;

import org.junit.Test;

import play.libs.F.Callback;
import play.test.TestBrowser;
import base.AbstractTest;
import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public class MetasTest extends AbstractTest{
	Meta meta = new Meta("Aprender Play");
    GenericDAO dao = new GenericDAO();
    List<Meta> metas;
	
    @Test
	public void deveIniciarSemMeta() {
		metas = dao.findAllByClass(Meta.class);
		assertThat(metas.size()).isEqualTo(0);
	}
    
    @Test
    public void deveAdicionarMeta(){
    	dao.persist(meta);
    	metas = dao.findAllByClass(Meta.class);
    	assertThat(metas.size()).isEqualTo(1);
    	assertThat(metas.get(0).getDescricao()).isEqualTo("Aprender Play");
    }
}
