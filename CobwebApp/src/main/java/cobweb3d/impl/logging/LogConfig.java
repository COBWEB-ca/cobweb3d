package cobweb3d.impl.logging;

import cobweb3d.plugins.MutatorListener;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobwebutil.io.ConfDisplayName;
import cobwebutil.io.ConfMap;
import cobwebutil.io.ConfXMLTag;
import cobwebutil.io.ParameterSerializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LogConfig implements ParameterSerializable {

    @ConfDisplayName("Log Core Data?")
    @ConfXMLTag("logCore")
    public boolean logCore = true;

    @ConfXMLTag("pluginEntries")
    @ConfMap(keyName = "Mutator", entryName = "Enabled", valueClass = Boolean.class)
    public HashMap<String, Boolean> pluginEntryMap = new HashMap<>();

    public Set<String> editableEntries = new HashSet<>();

    public LogConfig() {
    }

    public void load(MutatorListener mutatorListener) {
        Set<DataLoggingMutator> mutators = mutatorListener.getDataLoggingMutators();
        for (String key : pluginEntryMap.keySet()) {
            boolean flag = false;
            for (DataLoggingMutator mutator : mutatorListener.getDataLoggingMutators()) {
                if (mutator.getName().equals(key)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) pluginEntryMap.remove(key);
        }
        for (DataLoggingMutator mutator : mutators) {
            String key = mutator.getName();
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
