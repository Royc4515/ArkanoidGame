package listeners;

/**
 * The {@code HitNotifier} interface should be implemented by objects that can be hit
 * and want to notify registered {@link HitListener}s when such a hit occurs.
 */
public interface HitNotifier {

    /**
     * Adds a {@link HitListener} to the list of listeners to be notified on hit events.
     *
     * @param hl the listener to add
     */
    void addHitListener(HitListener hl);

    /**
     * Removes a {@link HitListener} from the list of listeners to be notified on hit events.
     *
     * @param hl the listener to remove
     */
    void removeHitListener(HitListener hl);
}
