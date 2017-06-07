/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_api;

/**
 * Représente une personne stocké dans la base de données
 * @author PIVARD Julien
 */
public class Person extends DirectoryObject
{

    /**
     * Initialisation d'une personne par défaut.
     * */
    public Person()
    {
        this.id = 0;
        this.firstName = "";
        this.lastName = "";
    }

    /**
     * @return Le prénom de la personne
     * */
    public String getFirstName()
    {
        return this.firstName;
    }

    /**
     * @param firstName
     * Le prénom de la personne
     * */
    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    /**
     * @return Le nom de la personne
     * */
    public String getLastName()
    {
        return this.lastName;
    }

    /**
     * @param lastName
     * Le nom de la personne
     * */
    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    /**
     * Le prénom
     * */
    protected String firstName;

    /**
     * Le nom
     * */
    protected String lastName;

}
