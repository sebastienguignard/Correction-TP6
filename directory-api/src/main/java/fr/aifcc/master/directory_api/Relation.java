/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_api;

/**
 * Représente la relation unilatérale entre deux personnes dans la base de données.
 * @author PIVARD Julien
 * */
public class Relation extends DirectoryObject
{

    /**
     * L'identifiant unique de la personne dont part la relation.
     * */
    protected long id;

    /**
     * La personne avec qui la relation est établie
     * */
    private Person personne = null;

    /**
     * Le type de relation entre les deux personnes.
     * */
    private String label = "";

    /**
     * Une relation par défaut.
     * */
    public Relation()
    {
        this.id = 0;
        this.personne = null;
        this.label = "";
    }

    /**
     * @return La personne dont part la relation.
     * */
    public long getId()
    {
        return this.id;
    }

    /**
     * @param id
     * L'identifiant de la personne dont la relation part.
     * */
    public void setId( long id )
    {
        this.id = id;
    }

    /**
     * @return La personne avec qui est établi la relation.
     * */
    public Person getPersonne()
    {
        return this.personne;
    }

    /**
     * @param personne
     * La personne avec qui la relation va être établie.
     * */
    public void setPersonne( Person personne )
    {
        this.personne = personne;
    }

    /**
     * @return Le type de relation entre les personnes.
     * */
    public String getLabel()
    {
        return this.label;
    }

    /**
     * @param label
     * Le type de relation entre les personnes.
     * */
    public void setLabel( String label )
    {
        this.label = label;
    }

}
