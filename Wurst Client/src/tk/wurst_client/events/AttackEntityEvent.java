package tk.wurst_client.events;

import net.minecraft.entity.Entity;

public class AttackEntityEvent extends CancellableEvent 
{
    private final Entity entity;
    private boolean cancelled;

    public AttackEntityEvent(final Entity entity) {
    	
        this.entity = entity;
    }

    public Entity getEntity() {
    	
        return entity;
    }

    public boolean isCancelled() {
    	
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
    	
        this.cancelled = cancelled;
    }

	@Override
	public String getAction()
	{
		return "attacking entity";
	}
}