package com.ash.util.files;

/**
 * ConfigException.
 */
@Deprecated
public class ConfigException extends Exception
{
	public ConfigException(Throwable t)
	{
		super(t);
	}

	public ConfigException(String msg)
	{
		super(msg);
	}
}
