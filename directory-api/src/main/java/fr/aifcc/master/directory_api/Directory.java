/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_api;

import java.util.Collection;

/**
 * Interface de gestion des accès aux données.
 * Permet d'abstraire la couche d'accès aux données pour ne pas dépendre
 * d'un type de stockage en particulier.
 * @author PIVARD Julien
 */
public interface Directory
{

    /**
     * Permet de récupérer un ensemble de personnes dans un ordre arbitraire.
     * Le sous-ensemble des personnes à récupérer est donné par les paramètres offset et limit.
     * L'ordre est arbitraire mais il est garanti qu'il sera le même pour tous les appels.
     * @param offset
     * Index de la première personne à récupérer
     * @param limit
     * Nombre maximum de personnes à récupérer
     * @return La collection de personnes trouvé en base de donnée
     * */
    public Collection<Person> getPersons( long offset, long limit ) throws DirectoryException;

    /**
     * Retourne une personne donnée par son identifiant (id)
     * @param id
     * L'identifiant de la personne à trouver dans la base de données
     * @return La personne trouvée, ou null si l'identifiant n'existe pas
     * */
    public Person getPerson( long id ) throws DirectoryException;

    /**
     * Met à jour les informations de la personne dans la base de données
     * @param personne
     * La personne modifié.
     * */
    public void updatePerson( Person personne ) throws DirectoryException;

    /**
     * Permet de récupérer la liste des relations d'une personne
     * @param id
     * L'identifiant de la personne dont on veut connaitre les relations
     * @return La liste des relation de la personne
     * */
    public Collection<Relation> getPersonRelations( long personId ) throws DirectoryException;

    /**
     * Permet de récupérer la liste des personnes qui répondent au critère de recherche
     * @param critere
     * Le critère de recherche des personnes.
     * @return La liste des personnes qui correspondent à la recherche
     * */
    public Collection<Person> getPersonRecherche( String critere ) throws DirectoryException;

    /**
     * Permet d'ajouter une relation entre deux personnes
     * @param id
     * L'identifiant de la personne
     * @param autreId
     * L'identifiant de l'autre personne
     * @param label
     * Le type de la relation
     * */
    public void ajouterRelation( long id, long autreId, String label ) throws DirectoryException;

    /**
     * Libère les ressources occupées par cette instance d'annuaire
     * */
    public void dispose() throws DirectoryException;

}
