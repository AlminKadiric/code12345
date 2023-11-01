package ab3;

import java.util.List;

/**
 * Schnittstelle für die Implementierung eines B-Baums als Map
 *
 */
public interface BTreeMap {
    /**
     * Setzt den Minimalgrad des Baumes. Diese Methode muss als erstes und darf kein
     * weiteres Mal aufgerufen werden, solange clear nicht aufgerufen wird.
     *
     * @param t
     *            Minimalgrad
     * @throws IllegalStateException
     *             falls die Methode ein zweites Mal aufgerufen wird, ohne dass
     *             clear aufgerufen wurde.
     * @throws IllegalArgumentException
     *             Falls t ungültigen ist (t<2)
     */
    public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException;

    /**
     * Fügt ein Werte-Paar in den Baum ein. Duplikate (keys) sind im Baum nicht erlaubt.
     *
     * @param key
     *            einzufügender Schlüssel
     * @param value
     *            einzufügender Wert
     * @return false, wenn der Schlüssel bereits vorhanden war, andernfalls true
     * @throws IllegalStateException
     *             Falls setMinDegree noch nicht aufgerufen wurde
     */
    public boolean put(int key, String value) throws IllegalStateException, IllegalArgumentException;

    /**
     * Löscht einen Wert aus dem Baum
     *
     * @param key
     *            zu löschender Schlüssel
     * @return true, falls der Schlüssel gelöscht wurde. Sonst false.
     * @throws IllegalStateException
     *             Falls setMinDegree noch nicht aufgerufen wurde
     */
    public boolean delete(int key) throws IllegalStateException, IllegalArgumentException;

    /**
     * Prüft, ob ein Schlüsse bereits im Baum vorhanden ist und gibt den dazugehörigen Wert zurück
     *
     * @param key
     * @return der gespeicherte Wert, wenn der Schlüssel vorhanden ist. Sonst null
     * @throws IllegalStateException
     *             Falls setMinDegree noch nicht aufgerufen wurde
     */
    public String contains(int key) throws IllegalStateException;

    /**
     * Liefert den Root-Knoten des Baumes.
     *
     * @return Root-Knoten
     * @throws IllegalStateException
     *             Falls setMinDegree noch nicht aufgerufen wurde
     */
    public BTreeNode getRoot() throws IllegalStateException;

    /**
     * Gibt die Schlüssel des Baumes (sortiert) als Liste zurück
     *
     * @return sortierte Liste der Schlüssel
     * @throws IllegalStateException
     *             Falls setMinDegree noch nicht aufgerufen wurde
     */
    public List<Integer> getKeys() throws IllegalStateException;

    /**
     * Gibt die Werte des Baumes (sortiert nach den Keys) als Liste zurück
     *
     * @return Liste der Werte
     * @throws IllegalStateException
     *             Falls setMinDegree noch nicht aufgerufen wurde
     */
    public List<String> getValues() throws IllegalStateException;

    /**
     * Setzt den Baum zurück. setMinDegree muss wieder aufgerufen werden
     */
    public void clear();

    /**
     * Liefert die Anzahl der gespeicherten Schlüssel-Wert-Paare
     */
    public int size();
}
