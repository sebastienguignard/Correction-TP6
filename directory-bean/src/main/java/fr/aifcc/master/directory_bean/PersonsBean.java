/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_bean;

import java.util.Collection;
import javax.faces.bean.*;

import fr.aifcc.master.directory_api.DirectoryException;
import fr.aifcc.master.directory_api.Directory;
import fr.aifcc.master.directory_api.Person;

/**
 * Lien entre la partie métier et la page web.
 * @author PIVARD Julien
 */
@ManagedBean( name = "personsBean" )
@ViewScoped         // Tant qu'on reste sur la même page cette instance n'est pas déchargé de la mémoire
public class PersonsBean
{

    /**
     * L'instance d'application directory bean sera injecté directement par JSF
     * */
    @ManagedProperty( value = "#{directoryBean}" )
    private DirectoryBean directoryBean;

    /**
     * Le nombre de résultats par page.
     * */
    private long limite = 7;

    /**
     * La première personne des résultats
     * */
    private long position = 1;

    /**
     * Une page suivante est disponible.
     * */
    private boolean pageSuivante = false;

    /**
     * Constructeur sans arguments conforme à la norme java bean
     * */
    public PersonsBean()
    {}

    /**
     * @return L'instance de directory bean
     * */
    public DirectoryBean getDirectoryBean()
    {
        return this.directoryBean;
    }

    /**
     * @param directoryBean
     * L'instance de la directory bean
     * */
    public void setDirectoryBean( DirectoryBean directoryBean )
    {
        this.directoryBean = directoryBean;
    }

    /**
     * Permet de passer à la page suivante de la liste des personnes.
     * La page sur laquelle on se trouve est stocké en interne.
     * */
    public void goToNextPage()
    {
        this.position = this.position + this.limite;
    }

    /**
     * Permet de passer à la page précédente de la liste des personnes.
     * */
    public void goToPreviousPage()
    {
        this.position = this.position - this.limite;
        if ( this.position <= 0 )
        {
            this.position = 1;
        }
    }

    /**
     * @return Il y a une page précédente.
     * */
    public boolean hasPreviousPage()
    {
        return this.position > 1;
    }

    /**
     * @return Il y a une page suivante.
     * */
    public boolean hasNextPage()
    {
        return this.pageSuivante;
    }

    /**
     * Le nombre de personnes maximum qui sera retourné dépend de la limite de
     * résultats par pages.
     * @return La liste des personnes
     * @throws DirectoryException
     * Une erreur à été levé lors de l'accès aux données.
     * */
    public Collection<Person> getPersons()
        throws DirectoryException
    {
        Directory dir = this.directoryBean.getDirectory();
        Collection<Person> it = dir.getPersons( this.position, this.limite );
        this.pageSuivante = it.size() == this.limite;
        return it;
    }

}
