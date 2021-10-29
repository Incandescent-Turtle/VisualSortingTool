package util;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import javafx.stage.DirectoryChooser;

public class SynchronousJFXDirectoryChooser
{
    private final Supplier<DirectoryChooser> directoryChooserSupplier;
    
    public SynchronousJFXDirectoryChooser(Supplier<DirectoryChooser> directoryChooserSupplier)
	{
    	this.directoryChooserSupplier = directoryChooserSupplier;
	}
    
    public File showDialog()
    {
        SynchronousJFXCaller<File> caller = new SynchronousJFXCaller<>(() -> directoryChooserSupplier.get().showDialog(null));
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