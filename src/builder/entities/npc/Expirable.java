package builder.entities.npc;

import engine.timing.FixedTimer;

/**
 * Indicates the entity or other object that is implementing is set to expire over a specific set of
 * time.
 */
public interface Expirable {
    /**
     * Sets the lifespan timer for this entity.
     *
     * @param lifespan The timer to set
     */
    public void setLifespan(FixedTimer lifespan);

    /**
     * Gets the lifespan timer for this entity.
     *
     * @return The lifespan timer
     */
    public FixedTimer getLifespan();
}
