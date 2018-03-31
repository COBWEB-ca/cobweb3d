package cobweb3d.impl.logging;

import cobweb3d.impl.Simulation;
import cobwebutil.FileUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AutoSavingLogManager extends LogManager {
    private ExecutorService threadProol = Executors.newSingleThreadExecutor();
    private Future<?> future;

    private SavingStrategy savingStrategy;
    private File file;
    private int autoSaveCounter = 100;

    public AutoSavingLogManager(@NotNull Simulation simulation, File file) {
        super(simulation);
        setAutoSaveFile(file);
    }

    public AutoSavingLogManager(@NotNull LogManager prev, File file) {
        super(prev);

        if (file == null || this.file == null) {
            setAutoSaveFile(file);
        } else {
            try {
                if (!file.getCanonicalPath().equals(this.file.getCanonicalPath())) {
                    setAutoSaveFile(file);
                }
            } catch (IOException ex) {
                setAutoSaveFile(file);
            }
        }
    }

    public void setAutoSaveFile(File file) {
        if (this.file != null) saveLog();
        this.file = file;
        this.savingStrategy = getSavingStrategyForExt(FileUtils.getFileExtension(file));
    }

    @Override
    public void update(boolean synchronous) {
        super.update(synchronous);
        autoSaveCounter--;
        if (autoSaveCounter <= 0) {
            if (future == null || future.isDone() || future.isCancelled()) {
                saveLog();
                autoSaveCounter = 100;
            }
        }
    }

    public void saveLog() {
        if (this.file == null) return;
        if (future != null) {
            future.cancel(true);
            if (future.isDone() || future.isCancelled())
                future = threadProol.submit(() -> saveLog(file, savingStrategy));
        } else future = threadProol.submit(() -> saveLog(file, savingStrategy));
    }

    @Override
    public void onStopped() {
        saveLog();
    }

    @Override
    public String getLoggingStatus() {
        return file != null ? "Logging to: " + file.getPath() : super.getLoggingStatus();
    }
}
