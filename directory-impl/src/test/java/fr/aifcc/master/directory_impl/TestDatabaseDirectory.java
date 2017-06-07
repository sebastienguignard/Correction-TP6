/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_impl;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;

import fr.aifcc.master.directory_api.DirectoryException;
import fr.aifcc.master.directory_api.Person;
import fr.aifcc.master.directory_api.Relation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;

/**
 * Classe de tests unitaire de la gestion d'accès à la BDD.
 *
 * ATTENTION ! Cette classe ne DOIT PAS hériter de la classe « TestCase »
 * sinon certaines annotations ne sont pas disponible et le nom méthode
 * de test devrait toutes commencer par « test ».
 *
 * Si la classe hérite c'est junit 3 qui sera utilisé à la place de junit 4 !
 * @author PIVARD Julien
 */
public class TestDatabaseDirectory
{

    protected DatabaseDirectory database;
    protected static final String driver = "org.apache.derby.jdbc.ClientDriver";
    protected static final String url = "jdbc:derby://localhost:1527/BaseDeGens";
    protected static Connection connection;

    /**
     * Cette annotation n'est disponible que en junit 4
     *
     * Cette méthode sera exécutée une seule et unique fois AVANT
     * de commencer les tests unitaires.
     * */
    @BeforeClass
    public static void setUpClass()
        throws Exception
    {
        Class.forName( driver );
        connection = DriverManager.getConnection( url );
        insererPersonne( 1, "dupont", "durant" );
        insererPersonne( 2, "George", "Grement" );
        insererPersonne( 3, "Cabotin", "Botin" );
        insererPersonne( 4, "pedantic", "northcutt" );
        insererPersonne( 5, "gigantic", "dubinsky" );
        insererPersonne( 6, "elated", "brattain" );
        insererPersonne( 7, "desperate", "cori" );
        insererPersonne( 8, "stoic", "noether" );
        insererPersonne( 9, "condescending", "agnesi" );
        insererPersonne( 10, "pedantic", "perlman" );
        insererPersonne( 11, "elegant", "meninsky" );
        insererPersonne( 12, "jean", "yve" );

        insererRelation( 2, 4, "ami" );
        insererRelation( 4, 2, "ami" );
        insererRelation( 12, 11, "connaissance" );
        insererRelation( 1, 11, "connaissance" );
        insererRelation( 3, 8, "connaissance" );
        insererRelation( 9, 7, "connaissance" );
        insererRelation( 2, 7, "ronron" );
    }

    /**
     * Cette annotation n'est disponible que en junit 4
     *
     * Cette méthode sera exécutée une seule et unique fois APRÈS
     * que tous les tests unitaires aient été faits
     * */
    @AfterClass
    public static void tearDownClass()
        throws SQLException
    {

        supprimerRelation( 2, 4 );
        supprimerRelation( 4, 2 );
        supprimerRelation( 12, 11 );
        supprimerRelation( 1, 11 );
        supprimerRelation( 3, 8 );
        supprimerRelation( 9, 7 );
        supprimerRelation( 2, 7 );
        supprimerRelation( 5, 8 );

        supprimerPersonne( 1 );
        supprimerPersonne( 2 );
        supprimerPersonne( 3 );
        supprimerPersonne( 4 );
        supprimerPersonne( 5 );
        supprimerPersonne( 6 );
        supprimerPersonne( 7 );
        supprimerPersonne( 8 );
        supprimerPersonne( 9 );
        supprimerPersonne( 10 );
        supprimerPersonne( 11 );
        supprimerPersonne( 12 );

        connection.close();
    }

    /**
     * Cette méthode sera exécutée avant CHAQUE test unitaire.
     * */
    @Before
    public void setUp()
        throws DirectoryException
    {
        this.database = new DatabaseDirectory( this.driver, this.url );
    }

    /**
     * Cette méthode sera exécutée après CHAQUE test unitaire.
     * */
    @After
    public void tearDown()
        throws DirectoryException
    {
        database.dispose();
        database = null;
    }

    @Test
    public void testPerson1()
        throws DirectoryException
    {

        final long id = 2;
        final String prenomAttendu = "George";
        final String nomAttendu = "Grement";
        Person p = database.getPerson( id );
        assertEquals(
                "Erreur pour l'identifiant : ",
                id, p.getId()
                );
        assertEquals(
                "Erreur pour le prénom : ",
                prenomAttendu, p.getFirstName()
                );
        assertEquals(
                "Erreur pour le nom : ",
                nomAttendu, p.getLastName()
                );

    }

    @Test
    public void testPerson2()
        throws DirectoryException
    {

        final long id = 5;
        final String prenomAttendu = "gigantic";
        final String nomAttendu = "dubinsky";
        Person p = database.getPerson( id );
        assertEquals(
                "Erreur pour l'identifiant : ",
                id, p.getId()
                );
        assertEquals(
                "Erreur pour le prénom : ",
                prenomAttendu, p.getFirstName()
                );
        assertEquals(
                "Erreur pour le nom : ",
                nomAttendu, p.getLastName()
                );

    }

    @Test( expected = DirectoryException.class )
    public void testPerson3()
        throws DirectoryException
    {

        final long id = 51;
        Person p = database.getPerson( id );

    }

    @Test
    public void testGroupePersonnes1()
        throws DirectoryException
    {

        final long taille = 4;
        Collection<Person> col = database.getPersons( 3, taille );
        assertEquals(
                "La taille ne correspond pas : ",
                taille, col.size()
                );

        Iterator<Person> it = col.iterator();
        final int[] tabId = { 3, 4, 5, 6 };
        final String[] tabPrenom = { "Cabotin", "pedantic", "gigantic", "elated" };
        final String[] tabNom = { "Botin", "northcutt", "dubinsky", "brattain" };

        int i = 0;
        while ( it.hasNext() )
        {
            Person p = it.next();
            assertEquals(
                    "Erreur pour l'identifiant : ",
                    tabId[i], p.getId()
                    );
            assertEquals(
                    "Erreur pour le prénom : ",
                    tabPrenom[i], p.getFirstName()
                    );
            assertEquals(
                    "Erreur pour le nom : ",
                    tabNom[i], p.getLastName()
                    );
            i++;
        }

    }

    @Test
    public void modifierPersonne1()
        throws DirectoryException
    {

        final long id = 5;
        final String prenomAttendu = "Abrabadou";
        final String nomAttendu = "Mamabouloud";
        Person p = database.getPerson( id );

        p.setFirstName( prenomAttendu );
        p.setLastName( nomAttendu );

        database.updatePerson( p );

        p = database.getPerson( id );
        assertEquals(
                "Erreur pour l'identifiant : ",
                id, p.getId()
                );
        assertEquals(
                "Erreur pour le prénom : ",
                prenomAttendu, p.getFirstName()
                );
        assertEquals(
                "Erreur pour le nom : ",
                nomAttendu, p.getLastName()
                );

    }

    @Test
    public void voirRelation1()
        throws DirectoryException
    {

        final long id = 2;
        final long taille = 2;
        Collection<Relation> col = database.getPersonRelations( id );
        assertEquals(
                "La taille ne correspond pas : ",
                taille, col.size()
                );

        Iterator<Relation> it = col.iterator();
        final int[] tabId = { 4, 7 };
        final String[] tabPrenom = { "pedantic", "desperate" };
        final String[] tabNom = { "northcutt", "cori" };
        final String[] tabRelations = { "ami", "ronron" };

        int i = 0;
        while ( it.hasNext() )
        {
            Relation r = it.next();
            Person p = r.getPersonne();
            assertEquals(
                    "Erreur pour l'identifiant : ",
                    tabId[i], p.getId()
                    );
            assertEquals(
                    "Erreur pour le prénom : ",
                    tabPrenom[i], p.getFirstName()
                    );
            assertEquals(
                    "Erreur pour le nom : ",
                    tabNom[i], p.getLastName()
                    );
            assertEquals(
                    "Erreur pour le label de relation : ",
                    tabRelations[i], r.getLabel()
                    );
            i++;
        }

    }

    @Test
    public void rechercherPerson1()
        throws DirectoryException
    {
        final long taille = 3;
        Collection<Person> col = database.getPersonRecherche( "d%" );
        assertEquals(
                "La taille ne correspond pas : ",
                taille, col.size()
                );

        Iterator<Person> it = col.iterator();
        final int[] tabId = { 1, 5, 7 };
        final String[] tabPrenom = { "dupont", "gigantic", "desperate" };
        final String[] tabNom = { "durant", "dubinsky", "cori" };

        int i = 0;
        while ( it.hasNext() )
        {
            Person p = it.next();
            assertEquals(
                    "Erreur pour l'identifiant : ",
                    tabId[i], p.getId()
                    );
            assertEquals(
                    "Erreur pour le prénom : ",
                    tabPrenom[i], p.getFirstName()
                    );
            assertEquals(
                    "Erreur pour le nom : ",
                    tabNom[i], p.getLastName()
                    );
            i++;
        }

    }

    @Test
    public void insertionRelation1()
        throws DirectoryException
    {

        final long idAttendu = 5;
        final long autreIdAttendu = 8;
        final String relationAttendu = "ami";

        database.ajouterRelation( idAttendu, autreIdAttendu, relationAttendu );
        final long taille = 1;
        Collection<Relation> col = database.getPersonRelations( idAttendu );
        assertEquals(
                "La taille ne correspond pas : ",
                taille, col.size()
                );

        Iterator<Relation> it = col.iterator();
        final int[] tabId = { 8 };
        final String[] tabPrenom = { "stoic" };
        final String[] tabNom = { "noether" };
        final String[] tabRelations = { relationAttendu };

        int i = 0;
        while ( it.hasNext() )
        {
            Relation r = it.next();
            Person p = r.getPersonne();
            assertEquals(
                    "Erreur pour l'identifiant : ",
                    tabId[i], p.getId()
                    );
            assertEquals(
                    "Erreur pour le prénom : ",
                    tabPrenom[i], p.getFirstName()
                    );
            assertEquals(
                    "Erreur pour le nom : ",
                    tabNom[i], p.getLastName()
                    );
            assertEquals(
                    "Erreur pour le label de relation : ",
                    tabRelations[i], r.getLabel()
                    );
            i++;
        }

    }

    public static void insererRelation( long id, long autreId, String relation )
        throws SQLException
    {
        System.out.println( ">>>>> Ajout d'une relation <<<<<" );
        String requete = "INSERT INTO Relations ( personId, otherPersonID, label ) VALUES ( ?, ?, ? )";
        PreparedStatement requetePrepare = connection.prepareStatement( requete );

        requetePrepare.setLong( 1, id );
        requetePrepare.setLong( 2, autreId );
        requetePrepare.setString( 3, relation );

        requetePrepare.executeUpdate();
    }

    public static void supprimerRelation( long id, long autreId )
        throws SQLException
    {
        System.out.println( ">>>>> Suppression d'une relation <<<<<" );
        String requete = "DELETE FROM Relations WHERE personID=? AND otherPersonID=?";
        PreparedStatement requetePrepare = connection.prepareStatement( requete );

        requetePrepare.setLong( 1, id );
        requetePrepare.setLong( 2, autreId );

        requetePrepare.executeUpdate();
    }

    public static void insererPersonne( long id, String prenom, String nom )
        throws SQLException
    {
        System.out.println( ">>>>>>>>>>>>>> INSERTION d'une entrée dans la table Person <<<<<<<<<<<<<" );
        String requete = "INSERT INTO Person ( id, firstName, lastName ) VALUES ( ?, ?, ? )";
        PreparedStatement insert = connection.prepareStatement( requete );
        insert.setLong( 1, id );
        insert.setString( 2, prenom );
        insert.setString( 3, nom );
        insert.executeUpdate();
    }

    public static void supprimerPersonne( long id )
        throws SQLException
    {
        System.out.println( ">>>>>>>>>>>>>> SUPPRESSION d'une entrée dans la table Person <<<<<<<<<<<<<" );
        String requete = "DELETE FROM Person WHERE id=?";
        PreparedStatement insert = connection.prepareStatement( requete );
        insert.setLong( 1, id );
        insert.executeUpdate();
    }

}
