package org.velas.scoreboard.api;

import org.jetbrains.annotations.NotNull;

/**
 * Main entry point of the Velas Scoreboard API.
 *
 * <p>This abstract class acts as a singleton access point for the entire API system.
 * It provides global access to version information and the main {@link ScoreboardHandler}.</p>
 *
 * <p>Only one instance of this API can exist at runtime. Attempting to initialize
 * it more than once will throw an exception.</p>
 *
 * <p>This class is typically initialized internally
 * and should NOT be manually instantiated by API users.</p>
 */
public abstract class ScoreboardAPI {

    private static ScoreboardAPI instance;

    /**
     * Initializes the API instance.
     *
     * <p>Only one instance is allowed. If an instance already exists,
     * an exception will be thrown.</p>
     *
     * @throws IllegalStateException if API is already initialized
     */
    protected ScoreboardAPI(){
        if(instance!=null)
            throw new IllegalStateException("ScoreboardAPI already initialized");
        instance=this;
    }

    /**
     * Checks whether the API has been initialized.
     *
     * @return true if the API is ready to use
     */
    public static boolean isInitialized(){
        return instance!=null;
    }

    /**
     * Gets the Minecraft version of the server.
     *
     * <p>Example: {@code 1.20}</p>
     *
     * @return Minecraft version string
     * @throws IllegalStateException if API is not initialized
     */
    public static @NotNull String getMinecraftVersion(){
        return instance.getMinecraftVersionA();
    }

    /**
     * Gets the main scoreboard handler responsible for managing:
     * <ul>
     *     <li>Teams</li>
     *     <li>Sidebars</li>
     *     <li>Creation of scoreboards</li>
     *     <li>Retrieving scoreboard instances</li>
     *     <li>Player scoreboard state</li>
     * </ul>
     *
     * @return scoreboard handler instance
     * @throws IllegalStateException if API is not initialized
     */
    public static @NotNull ScoreboardHandler getHandler(){
        return instance.getHandlerA();
    }

    @NotNull
    protected abstract String getMinecraftVersionA();


    @NotNull
    protected abstract ScoreboardHandler getHandlerA();
}
