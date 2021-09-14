package main.interfaces;

import main.ui.RoryFrame;

/**
 * for classes that need to implement some behavior when the JFrame is closed
 * to be added to the listener via {@link RoryFrame#addClosable(Closable)}
 */
public interface Closable
{	
	void close();
}