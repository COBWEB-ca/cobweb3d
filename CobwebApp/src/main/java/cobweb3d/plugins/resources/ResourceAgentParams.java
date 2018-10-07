package cobweb3d.plugins.resources;

import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

public class ResourceAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 12L;

    /**
     * Initial amount of the food
     */
    @ConfDisplayName("Initial amount")
    @ConfXMLTag("Food")
    public int initialFood = 20;

    /**
     * How many squares of food to randomly drop on the grid at each time step.
     * Note, 1.5 drop rate means drop 1 square and have 50% chance of dropping a second.
     */
    @ConfDisplayName("Spawn Rate")
    @ConfXMLTag("FoodRate")
    public float dropRate = 0.0f;

    /**
     * Rate at which food grows around existing food.
     * The chance food will grow at a specific cell, given there are N cells
     * with food touching this cell from top/bottom/left/right is:
     * N * growRate / 1000
     */
    @ConfDisplayName("Growth Rate")
    @ConfXMLTag("FoodGrow")
    public int growRate = 20;

    /**
     * In food growth, the probability that the most common food will be selected
     * to have a chance at growing.
     * i.e. If LikeFoodGrowth = 0.4, then there is a 40% chance that the food type will be selected.
     * Otherwise, then all food types have an equal chance of being chosen, including this food type as well.
     */
    @ConfDisplayName("Like Food Growth Probability")
    @ConfXMLTag("LikeFoodGrowth")
    public float likeFoodGrowth = 0.2f;

    /**
     * Fraction of the food that will disappear when the deplete time comes.
     */
    @ConfDisplayName("Depletion Rate")
    @ConfXMLTag("FoodDeplete")
    public float depleteRate = 0.6f;

    /**
     * Food will deplete every depleteTime time steps.
     */
    @ConfDisplayName("Depletion time")
    @ConfXMLTag("DepleteTimeSteps")
    public int depleteTime = 40;

    /**
     * How much the agent that prefers this food will gain in energy
     * upon eating the food
     */
    @ConfDisplayName("Energy gain from food")
    @ConfXMLTag("EnergyGain")
    public int energyGain = 100;
}
