package cobweb3d.ui;


/**
 * UI component that can be updated
 */
public interface UpdatableUI {

    /**
     * Updates UI.
     *
     * @param synchronous whether to wait for component to complete update before returning
     */
    default void update(boolean synchronous) {
    }

    /**
     * Checks if the UI is free to update right now.
     *
     * @return true when ready
     */
    default boolean isReadyToUpdate() {
        return false;
    }

    default void onStopped() {
    }

    default void onStarted() {
    }

    interface UpdateableLoggingUI extends UpdatableUI {
        void onLogStarted();

        void onLogStopped();

        @Override
        default void update(boolean synchronous) {
        }

        @Override
        default void onStopped() {
        }

        @Override
        default void onStarted() {
        }
    }
}
