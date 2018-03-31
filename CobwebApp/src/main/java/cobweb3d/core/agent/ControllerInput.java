package cobweb3d.core.agent;

/**
 * Input state for a Controller
 */
public interface ControllerInput {

    /**
     * Modifies Controller's output for this input
     *
     * @param adjustmentStrength controller-defined strength of modification to apply
     */
    void mutate(float adjustmentStrength);

}
