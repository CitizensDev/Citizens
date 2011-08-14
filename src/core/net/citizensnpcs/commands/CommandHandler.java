package net.citizensnpcs.commands;

public interface CommandHandler {

	/**
	 * Add permissions to be registered by superperms, register permissions like
	 * so: PermissionManager.addPermission(perm) Note: perm cannot contain
	 * "citizens.". Also, you must register non-command permissions in this
	 * method...ex) 'wizard.use.interact'
	 * 
	 * @return
	 */
	public void addPermissions();
}