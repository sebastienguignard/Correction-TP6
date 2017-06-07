/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_bean;

import java.util.Collection;
import java.util.Iterator;
import javax.faces.bean.*;

import fr.aifcc.master.directory_api.DirectoryException;
import fr.aifcc.master.directory_api.Directory;
import fr.aifcc.master.directory_api.Person;
import fr.aifcc.master.directory_api.Relation;

/**
 * Lien entre la partie métier et la page web.
 * @author PIVARD Julien
 */
@ManagedBean( name = "personBean" )
@ViewScoped
public class PersonBean
{

    /**
     * L'identifiant de la personne dont le détail sera affiché sur la page.
     * */
    private long id;

    /**
     * L'instance de directory bean qui est instancié par JSF
     * */
    @ManagedProperty( value = "#{directoryBean}" )
    private DirectoryBean directoryBean;

    /**
     * Une instance de personne vide
     * */
    private Person personne = new Person();

    /**
     * La chaine de la recherche faites par l'utilisateur.
     * On le garde en mémoire pour pouvoir l'afficher après le
     * rechargement de la page dans le champs de recherche.
     * */
    private String chaineRecherche = "";

    /**
     * Le résultat de la recherche.
     * */
    private Collection<Person> resultatRecherche = null;

    /**
     * Le résultats de la liste des relations de la personne.
     * */
    private Collection<Relation> listeDesRelations = null;

    /**
     * @return L'instance de directory bean
     * */
    public DirectoryBean getDirectoryBean()
    {
        return this.directoryBean;
    }

    /**
     * @param directoryBean
     * Une instance de directory bean
     * */
    public void setDirectoryBean( DirectoryBean directoryBean )
    {
        this.directoryBean = directoryBean;
    }

    /**
     * @return L'identifiant de la personne.
     * */
    public long getId()
    {
        return this.id;
    }

    /**
     * @param id
     * L'identifiant de la personne qui sera affiché sur la page.
     * Cette identifiant doit être donné avant le traitement de la page.
     * */
    public void setId( long id )
    {
        this.id = id;
    }

    /**
     * @return La recherche effectué par l'utilisateur.
     * */
    public String getChaineRecherche()
    {
        return this.chaineRecherche;
    }

    /**
     * @param chaine
     * La recherche que fait l'utilisateur.
     * */
    public void setChaineRecherche( String chaine )
    {
        this.chaineRecherche = chaine;
    }

    /**
     * @return La personne dont on veut afficher les détails.
     * @throws DirectoryException
     * Un problème c'est produit lors de l'accès aux données.
     * */
    public Person getPersonne()
        throws DirectoryException
    {
        if( this.personne.getId() == 0 )
        {
            Directory dir = this.directoryBean.getDirectory();
            this.personne = dir.getPerson( this.id );
        }
        return this.personne;
    }

    /**
     * La mise à jour de la personne se fait grâce à l'instance de Person
     * qui est stocké.
     * @throws DirectoryException
     * Un problème c'est produit lors de la mise à jours des données.
     * */
    public void majPerson()
        throws DirectoryException
    {
        Directory dir = this.directoryBean.getDirectory();
        dir.updatePerson( this.personne );
        this.personne = dir.getPerson( this.id );
    }

    /**
     * @return La liste des Relations de la Person stocké.
     * @throws DirectoryException
     * Un problème c'est produit lors de la récupération des données.
     * */
    public Collection<Relation> getRelations()
        throws DirectoryException
    {
        this.listeDesRelations = this.directoryBean.getDirectory().getPersonRelations( this.id );
        return this.listeDesRelations;
    }

    /**
     * @param id
     * L'identifiant de la personne dont on veut savoir si c'est une relation.
     * @return La personne est déjà une relation.
     * @throws DirectoryException
     * Un problème c'est produit lors de la récupération des données.
     * */
    public boolean estDejaUneRelation( long id )
        throws DirectoryException
    {
        if ( this.listeDesRelations == null )
        {
            this.listeDesRelations = this.directoryBean.getDirectory().getPersonRelations( this.id );
        }
        Iterator<Relation> it = this.listeDesRelations.iterator();
        while ( it.hasNext() )
        {
            Relation r = it.next();
            Person p = r.getPersonne();
            if ( p.getId() == id )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Permet d'exécuter la recherche demandé par l'utilisateur.
     * @throws DirectoryException
     * Un problème c'est produit lors de la récupération des données.
     * */
    public void faireRecherche()
        throws DirectoryException
    {
        this.resultatRecherche = this.directoryBean.getDirectory().getPersonRecherche( this.chaineRecherche );
    }

    /**
     * @return La liste des personnes qui correspondent aux résultats de recherche.
     * */
    public Collection<Person> getResultatRecherche()
    {
        // On retire la personne qui fait la recherche de la liste de résultats de recherche
        if ( this.resultatRecherche != null )
        {

            Collection<Person> listeResultat = this.resultatRecherche;
            Iterator<Person> it = listeResultat.iterator();
            while ( it.hasNext() )
            {
                Person p = it.next();
                if ( p.getId() == this.id )
                {
                    it.remove();
                }
            }

        }
        return this.resultatRecherche;
    }

    /**
     * @return Le nombre de résultats dans la recherche.
     * */
    public int getTailleResultat()
    {
        if ( this.resultatRecherche == null )
        {
            return -1;
        }
        return this.resultatRecherche.size();
    }

    /**
     * @param idRelation
     * Ajoute la personne dans ces relations.
     * */
    public void ajouterUneRelation( long idRelation )
        throws DirectoryException
    {
        if ( this.id == idRelation )
        {
            throw new DirectoryException();
        }
        this.directoryBean.getDirectory().ajouterRelation( this.id, idRelation, "connaissance" );
    }

}
