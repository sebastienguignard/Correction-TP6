/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_bean;

import javax.faces.bean.*;

import fr.aifcc.master.directory_api.Directory;
import fr.aifcc.master.directory_impl.*;

/**
 * Cette classe ne sera jamais instancié manuellement.
 * Toutes ces instanciations seront faites par JSF
 * @author PIVARD Julien
 */
@ManagedBean( name = "directoryBean" )
@ApplicationScoped          // L'application est instancié dés que l'app est lancée
public class DirectoryBean
{

    private final Directory directory;

    /**
     * Une instance géré par JSF qui donne accès à la BDD.
     * */
    public DirectoryBean()
        throws Exception
    {
        this.directory = new DatabaseDirectory( "org.apache.derby.jdbc.ClientDriver", "jdbc:derby://localhost:1527/BaseDeGens" );
    }

    /**
     * @return L'instance de directory.
     * */
    public Directory getDirectory()
    {
        return this.directory;
    }

}
