package cobweb3d.ui;

/**
 * The UIInterface is the "pipe" between the user interface and the simulation
 * classes; it is also the top-level interface to the simulation.
 * <p>
 * The Driver (main in an application, or the applet) should create a derivative
 * of UIInterface to initialize the system as appropriate for the context; a
 * LocalUIInterface exists for a single application simulation, and a
 * ClientInterface could be implemented to connect to a ServerInterface over
 * TCP/IP.
 * <p>
 * By forcing all communication between the UI and the simulation into the pipe
 * formed by this class, the separation between the two modules is made manifest
 * in the code, and practically speaking, the UIInterface can be implemented so
 * as to allow for a transparent TCP/IP layer, thereby allowing a client/server
 * variant of Cobweb to be developed trivially.
 * <p>
 * Note that UIInterface is only an Interface; all UI and simulation code should
 * be written in terms of this interface, and not a specific derivative.
 * However, the driver will need to create a subclass of UIInterface. The driver
 * should immediately forget the specific type of UIInterface after creation for
 * good form; a code snippet to do this is;
 * <code>UIInterface theUI = new SpecificUIType(); </code> Thus the
 * SpecificUIType is only used in the creation of the UIInterface, and is
 * immediately forgotten, treated simply as a UIInterface from then on.
 *
 * @see cobweb.LocalUIInterface
 */


/**
 * Public methods for interacting with simulation.
 * Used by UI, as well as simulation components.
 */
public interface SimulationInterface {
    /* returns the number of agents */
    int getAgentTypeCount();

    long getTime();

    void step();
}
