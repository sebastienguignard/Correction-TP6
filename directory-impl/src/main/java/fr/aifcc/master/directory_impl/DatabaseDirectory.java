/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_impl;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

import fr.aifcc.master.directory_api.Directory;
import fr.aifcc.master.directory_api.Person;
import fr.aifcc.master.directory_api.Relation;
import fr.aifcc.master.directory_api.DirectoryException;

/**
 * Implémentation concrète de la classe d'accès aux données.
 * La technologie de stockage choisie est une base de données classique.
 * @author PIVARD Julien
 */
public class DatabaseDirectory implements Directory
{

    /**
     * L'objet de connections à la base de données.
     * */
    private final Connection connection;

    /**
     * Le nom des différentes table de la base de données.
     * */
    private final String nomTablePersonne = "Person";
    private final String nomTableRelations = "Relations";

    /**
     * Les noms des différents champs de la base de données.
     * */
    private final String nomId = "id";
    private final String nomPrenom = "firstName";
    private final String nomNom = "lastName";
    private final String nomIdRelation = "personID";
    private final String nomIdAutreRelation = "otherPersonID";
    private final String nomTypeRelation = "label";

    /**
     * Initialise une connexion à la base de données
     * @param driverClass
     * le nom du driver de la BDD.
     * @param databaseURL
     * l'URL vers la base de données.
     * */
    public DatabaseDirectory( String driverClass, String databaseURL )
        throws DirectoryException
    {
        try
        {
            Class.forName( driverClass );
            this.connection = DriverManager.getConnection( databaseURL );
        }
        catch ( ClassNotFoundException e )
        {
            throw new DirectoryException( "Le driver de connexion n'existe pas" );
        }
        catch ( SQLException e )
        {
            throw new DirectoryException( e );
        }
    }

    @Override
    public synchronized Collection<Person> getPersons ( long offset, long limit )
        throws DirectoryException
    {
        try
        {

            ResultSet resultats;

            String requete = "SELECT * FROM " + this.nomTablePersonne
                + " WHERE " + this.nomId + ">=? FETCH NEXT ? ROWS ONLY";
            // Une requête doit TOUJOURS être préparé pour éviter les injections SQL.
            PreparedStatement personne = this.connection.prepareStatement( requete );
            personne.setLong( 1, offset );
            personne.setLong( 2, limit );

            resultats = personne.executeQuery();

            if ( ! resultats.next() )
            {
                resultats.close();
                throw new DirectoryException( "Aucune personne n'a été récupéré"
                        + " dans la base." );
            }

            Collection<Person> col = new LinkedList<>();

            do
            {
                Person personneRecherche = new Person();

                personneRecherche.setId( resultats.getLong( 1 ) );
                personneRecherche.setFirstName( resultats.getString( 2 ) );
                personneRecherche.setLastName( resultats.getString( 3 ) );

                col.add( personneRecherche );
            }
            while ( resultats.next() );

            resultats.close();

            return col;

        }
        catch ( SQLException e )
        {
            throw new DirectoryException( "La requête SQL est mal écrite ou"
                    + " à subit une erreur \n===> " + e.getMessage() );
        }

    }

    @Override
    public synchronized Person getPerson( long id )
        throws DirectoryException
    {

        try
        {

            ResultSet resultat;

            String requete = "SELECT * FROM " + this.nomTablePersonne
                + " WHERE " + this.nomId + " = ?";
            // Une requête doit TOUJOURS être préparé pour éviter les injections SQL.
            PreparedStatement personne = this.connection.prepareStatement( requete );
            personne.setLong( 1, id );

            resultat = personne.executeQuery();

            int taille = resultat.getFetchSize();
            if ( ! resultat.next() )
            {
                resultat.close();
                throw new DirectoryException( "Aucune occurrence de la "
                        + "personne dans la base." );
            }
            else if ( taille != 0 )
            {
                resultat.close();
                throw new DirectoryException( "L'identifiant n'est pas unique"
                        + " dans la base ! Il y a " + taille
                        + " identifiants dans la base." );
            }

            Person personneRecherche = new Person();

            personneRecherche.setId( resultat.getLong( 1 ) );
            personneRecherche.setFirstName( resultat.getString( 2 ) );
            personneRecherche.setLastName( resultat.getString( 3 ) );

            resultat.close();

            return personneRecherche;

        }
        catch ( SQLException e )
        {
            throw new DirectoryException( "La requête SQL est mal écrite ou"
                    + " à subit une erreur \n===> " + e.getMessage() );
        }

    }

    @Override
    public void updatePerson( Person personneModifie )
        throws DirectoryException
    {
        try
        {
            String requete = "UPDATE " + this.nomTablePersonne + " SET "
                + this.nomPrenom + " = ?, " + this.nomNom + " = ? WHERE "
                + this.nomId + " = ?";
            // Une requête doit TOUJOURS être préparé pour éviter les injections SQL.
            PreparedStatement requetePersonne = this.connection.prepareStatement( requete );

            requetePersonne.setString( 1, personneModifie.getFirstName() );
            requetePersonne.setString( 2, personneModifie.getLastName() );
            requetePersonne.setLong( 3, personneModifie.getId() );

            requetePersonne.executeUpdate();
        }
        catch ( SQLException e )
        {
            throw new DirectoryException( "La mise à jour via la requête SQL à "
                    + "échoué ou à subit une erreur \n===> " + e.getMessage() );
        }
    }

    @Override
    public Collection<Relation> getPersonRelations( long personId )
        throws DirectoryException
    {

        try
        {

            ResultSet resultats;

            String requete = "SELECT * FROM " + this.nomTableRelations
                + " WHERE " + this.nomIdRelation + " = ?";
            // Une requête doit TOUJOURS être préparé pour éviter les injections SQL.
            PreparedStatement requetePrepare = this.connection.prepareStatement( requete );

            requetePrepare.setLong( 1, personId );

            resultats = requetePrepare.executeQuery();

            Collection<Relation> listeRelations = new LinkedList<>();
            while ( resultats.next() )
            {
                Relation r = new Relation();
                r.setPersonne( getPerson( resultats.getLong( 2 ) ) );
                r.setLabel( resultats.getString( 3 ) );
                listeRelations.add( r );
            }
            resultats.close();

            return listeRelations;

        }
        catch ( SQLException e )
        {
            throw new DirectoryException( "La récupération des relations de la "
                    + "personne à échoué.\n" + e.getMessage() );
        }

    }

    @Override
    public Collection<Person> getPersonRecherche( String critere )
        throws DirectoryException
    {

        try
        {

            ResultSet resultats;

            String requete = "SELECT * FROM " + this.nomTablePersonne
                + " WHERE " + this.nomPrenom + " LIKE ? OR "
                + this.nomNom + " LIKE ? ";
            // Une requête doit TOUJOURS être préparé pour éviter les injections SQL.
            PreparedStatement requetePrepare = this.connection.prepareStatement( requete );

            requetePrepare.setString( 1, critere );
            requetePrepare.setString( 2, critere );

            resultats = requetePrepare.executeQuery();

            Collection<Person> listeRelations = new LinkedList<>();

            while ( resultats.next() )
            {
                listeRelations.add( getPerson( resultats.getLong( 1 ) ) );
            }
            resultats.close();

            return listeRelations;

        }
        catch ( SQLException e )
        {
            throw new DirectoryException( "La récupération du résultat de la"
                    + " recherche des personnes à échoué.\n" + e.getMessage() );
        }

    }

    @Override
    public void ajouterRelation( long id, long autreId, String label )
        throws DirectoryException
    {

        try
        {
            String requete = "INSERT INTO " + this.nomTableRelations + " ( "
                + this.nomIdRelation + ", " + this.nomIdAutreRelation + ", "
                + this.nomTypeRelation + " ) VALUES ( ?, ?, ? )";
            // Une requête doit TOUJOURS être préparé pour éviter les injections SQL.
            PreparedStatement requetePersonne = this.connection.prepareStatement( requete );

            requetePersonne.setLong( 1, id );
            requetePersonne.setLong( 2, autreId );
            requetePersonne.setString( 3, label );

            requetePersonne.executeUpdate();
        }
        catch ( SQLException e )
        {
            throw new DirectoryException( "Echec SQL mise à jour des"
                    + " relations\n" + e.getMessage() );
        }

    }

    @Override
    public synchronized void dispose()
        throws DirectoryException
    {
        try
        {
            this.connection.close();
        }
        catch ( SQLException e )
        {
            throw new DirectoryException( e );
        }
    }

}
