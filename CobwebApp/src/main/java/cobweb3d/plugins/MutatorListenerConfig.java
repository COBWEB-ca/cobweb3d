package cobweb3d.plugins;

import cobweb3d.plugins.mutators.AgentMutator;
import cobwebutil.io.ConfMap;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MutatorListenerConfig implements ParameterSerializable {

    // This HashMap maps from the class name of the mutator to a boolean,
    // which represents whether or not one mutator is enabled.
    @ConfXMLTag("pluginEntries")
    @ConfMap(keyName = "Mutator", entryName = "Enabled", valueClass = Boolean.class)
    public HashMap<String, Boolean> pluginEntryMap = new HashMap<>();

    public Set<String> editableEntries = new HashSet<>();

    public MutatorListenerConfig() {
        load(PluginProvider.getConfiguratedPlugins());
    }

    /**
     * Update the pluginEntryMap based on the given set mutators.
     *
     * @param mutators all the mutators that are going to be added in the simulation.
     */
    public void load(Set<Class<? extends AgentMutator>> mutators) {
        // First loop through the pluginEntryMap to see whether or not its element is in the given set mutators
        // If the string element in pluginEntryMap.keySet() doesn't exist in the set mutators,
        // it will be removed from pluginEntryMap.
        for (String key : pluginEntryMap.keySet()) {
            boolean flag = false;
            for (Class<? extends AgentMutator> mutator : mutators) {
                if (mutator.getCanonicalName().equals(key)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) pluginEntryMap.remove(key);
        }
        // Then loop through the set mutators to see if there are some missing mutators.
        // If exists, add that into the hash table pluginEntryMap.
        for (Class<? extends AgentMutator> mutator : mutators) {
            String key = mutator.getCanonicalName();
            editableEntries.add(key);
            if (!pluginEntryMap.containsKey(key)) {
                pluginEntryMap.put(key, true);
            }
        }
    }

    public void disableMutator(String mutator) {
        pluginEntryMap.put(mutator, false);
    }

    public void enableMutator(String mutator) {
        pluginEntryMap.put(mutator, true);
    }

    public boolean enabled(String mutator) {
        return pluginEntryMap.getOrDefault(mutator, true);
    }
}
