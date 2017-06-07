/*
 * Dernière modification : Mercredi 07 juin[06] 2017
 * */
package fr.aifcc.master.directory_api;

/**
 * @author PIVARD Julien
 */
public class DirectoryException extends Exception
{

    /**
     * Message par défaut de l'exception directory.
     * */
    public DirectoryException()
    {
        super( "Erreur dans la gestion des personnes." );
    }

    /**
     * @param s
     * Le message personnalisé de l'exception.
     * */
    public DirectoryException( String s )
    {
        super( s );
    }

    /**
     * @param t
     * L'exception levé à encapsuler à l'intérieur.
     * */
    public DirectoryException( Throwable t )
    {
        super( t );
    }

    /**
     * @param s
     * Le message personnalisé de l'exception.
     * @param t
     * Une exception déjà levée.
     * */
    public DirectoryException( String s, Throwable t )
    {
        super( s, t );
    }

}
