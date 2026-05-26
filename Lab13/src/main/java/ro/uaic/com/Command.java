package ro.uaic.com;

import ro.uaic.app.LocaleExplore;

/**
 * Command
 */
public interface Command {
    public String execute(LocaleExplore app, String[] args);
}
