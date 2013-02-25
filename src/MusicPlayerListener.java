/**
 * Ein MusicPlayerListener ist ein Objekt, das über Veränderungen eines
 * MusicPlayers - wie die aktuelle Position - benachrichtigt werden möchte.
 * @author Thomas Karp
 */
public interface MusicPlayerListener {
    /**
     * Wird vom MusicPlayer aufgerufen, wenn sich die Position im aktuellen Song ändert.
     * Dazu muss man sich beim MusicPlayer als MusicPlayerListener anmelden.
     * @param newPosition Neue Position in Prozent.
     */
    void positionChanged(int newPosition);
    void lengthChanged(int length);
    void statusChanged(int status);
}
