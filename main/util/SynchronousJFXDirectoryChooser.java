package main.util;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javafx.stage.DirectoryChooser;

public class SynchronousJFXDirectoryChooser
{
    private final Supplier<DirectoryChooser> directoryChooserFactory;
    
    public SynchronousJFXDirectoryChooser(Supplier<DirectoryChooser> directoryChooserFactory)
	{
    	this.directoryChooserFactory = directoryChooserFactory;
	}
    
    public File showDialog()
    {
    	Callable<File> task = () -> {
            return directoryChooserFactory.get().showDialog(null);
        };
        SynchronousJFXCaller<File> caller = new SynchronousJFXCaller<>(task);
        try {
            return caller.call(1, TimeUnit.SECONDS);
        } catch (RuntimeException | Error ex) {
            throw ex;
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            return null;
        } catch (Exception ex) {
            throw new AssertionError("Got unexpected checked exception from"
                    + " SynchronousJFXCaller.call()", ex);
        }
    }
}