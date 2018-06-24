package cobweb3d.core.entity;

/**
 * This interface intends to illustrate the reason why an agent's energy is changed.
 * However, it seems that this interface hasn't been used in current version (0.1.4)
 * Anyway I think this interface is useful, since it enables researchers to figure out
 * why an agent dies.
 *
 * Document Author: Zewen Shen
 * Reminder: I'm not the author of this class, so my documentation may be inaccurate.
 */
public interface Cause {
    String getName();
}
