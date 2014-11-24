import models.Meta;
import models.dao.GenericDAO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;


public class Global extends GlobalSettings {

    private static GenericDAO dao = new GenericDAO();

    @Override
    public void onStart(Application app) {
        Logger.info("Aplicação inicializada...");

        JPA.withTransaction(new play.libs.F.Callback0() {
            @Override
            public void invoke() throws Throwable {
                Meta meta1 = new Meta("Aprender Play",1,2);
                Meta meta2 = new Meta("Aprender AJAX",1,1);
                Meta meta3 = new Meta("Aprender filosofia",2,1);
                Meta meta4 = new Meta("Aprender a nadar",3,3);
                Meta meta5 = new Meta("Festa",3,3);
                Meta meta6 = new Meta("Estudar linear",3,1);
                Meta meta7 = new Meta("Terminar de ler aquele livro",3,2);
                Meta meta8 = new Meta("Buscar a razão da existência",6,1);
                Meta meta9 = new Meta("Limpar a casa",6,2);
                Meta meta10 = new Meta("Dormir mais",5,1);
                
                dao.persist(meta1);
                dao.persist(meta2);
                dao.persist(meta3);
                dao.persist(meta4);
                dao.persist(meta5);
                dao.persist(meta6);
                dao.persist(meta7);
                dao.persist(meta8);
                dao.persist(meta9);
                dao.persist(meta10);
                
                dao.flush();                
            }});
    }
}
