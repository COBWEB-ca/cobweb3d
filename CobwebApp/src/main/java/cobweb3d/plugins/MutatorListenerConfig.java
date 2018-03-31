package cobweb3d.plugins;

import cobweb3d.plugins.mutators.AgentMutator;
import cobwebutil.io.ConfMap;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MutatorListenerConfig implements ParameterSerializable {

    @ConfXMLTag("pluginEntries")
    @ConfMap(keyName = "Mutator", entryName = "Enabled", valueClass = Boolean.class)
    public HashMap<String, Boolean> pluginEntryMap = new HashMap<>();

    public Set<String> editableEntries = new HashSet<>();

    public MutatorListenerConfig() {
        load(PluginProvider.getConfiguratedPlugins());
    }

    public void load(Set<Class<? extends AgentMutator>> mutators) {
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
