package interfaces;

import ui.BetterFrame;

/**
 * for classes that need to implement some behavior when the JFrame is closed
 * to be added to the listener via {@link BetterFrame#addClosable(Closable)}
 */
@FunctionalInterface public interface Closable
{	
	void close();
}